package com.burevers.bur.temekr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

/**
 * Liste ekranlarının satırlarını sunucu tarafında sayfalayarak okur.
 *
 * <p>Neden var: Ekran düzeni tabloyu ve kolonları veriyle belirler; her tablo için ayrı sorgu kodu yazmak
 * yerine bu servis düzenden SELECT üretir. Bu yüzden SQL güvenliği kritiktir: tablo ve kolon adları
 * kullanıcıdan değil yalnızca doğrulanmış düzenden (veri sözlüğü) gelir ve ayrıca sıkı bir kalıpla
 * denetlenir; değerler (sayfa, sıra) her zaman parametredir.
 *
 * <p>Henüz YOK (bilinçli, sonraki adım): kiracı (organizasyon/kuruluş/tesis) süzgeci ve alan yetkisi
 * (temkulaly). Bunlar oturum ve yetki bağlanınca eklenecek; o zamana kadar yalnızca geliştirme verisi
 * görüntülenmelidir. Yalnızca gerçek sütunlar (kaynak SISTEM) okunur; dinamik (ek) alanlar sonra gelecek.
 */
@Service
public class TemekrveriService {

    /** Tablo kodu kalıbı: paket kodu (6) + ad (3-4), yalnızca küçük ASCII harf. */
    private static final Pattern TABLO_DESENI = Pattern.compile("(tem|bel|muh|mht)[a-z]{3}[a-z]{3,4}");
    /** Alan adı kalıbı: küçük harfle başlar; küçük harf, rakam ve alt çizgi. */
    private static final Pattern ALAN_DESENI = Pattern.compile("[a-z][a-z0-9_]{0,49}");

    /** Bir sayfa sonucu: satırlar ve süzgece uyan toplam kayıt sayısı. */
    public record Sayfa(List<Map<String, Object>> satirlar, long toplam) {
    }

    private final JdbcClient jdbc;

    /** Bağımlılık kurucudan alınır (alan enjeksiyonu yasaktır). */
    public TemekrveriService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Düzendeki tablo bölümünün satırlarını bir sayfa olarak okur.
     *
     * @param duzen      çözülmüş ekran düzeni (tablo kodunu ve geçerli kolonları buradan alırız)
     * @param bolumKodu  okunacak GRID bölümünün kodu
     * @param siralaAlan sıralanacak alan adı; boşsa vid'e göre. Yalnızca düzendeki sıralanabilir kolon olabilir
     * @param artan      artan mı azalan mı sıralama
     * @param baslangic  atlanacak satır sayısı
     * @param adet       en çok kaç satır (1-500 arası kırpılır)
     */
    public Sayfa sayfaGetir(TemekrduzenDto duzen, String bolumKodu, String siralaAlan, boolean artan,
                            int baslangic, int adet) {
        String tablo = tabloAdi(duzen);
        List<String> alanlar = okunacakAlanlar(duzen, bolumKodu);
        if (alanlar.isEmpty()) {
            return new Sayfa(List.of(), 0);
        }
        String sira = "vid";
        if (siralaAlan != null && siralanabilirMi(duzen, bolumKodu, siralaAlan)) {
            sira = siralaAlan;
        }
        int limit = Math.max(1, Math.min(adet, 500));
        int atla = Math.max(0, baslangic);

        // Tablo ve alan adları yukarıda kalıpla doğrulandı; değerler parametre olarak verilir.
        // vid her zaman seçilir (satır kimliği için); aynı alan iki kez yazılmasın diye kontrol edilir.
        List<String> secilecek = new ArrayList<>(alanlar);
        if (!secilecek.contains("vid")) {
            secilecek.add(0, "vid");
        }
        // Alan adları çift tırnakla yazılır: kalıpla doğrulanmışlardır, ayrıca ayrılmış sözcüklerle çakışmaz
        String sql = "SELECT \"" + String.join("\", \"", secilecek) + "\" FROM " + tablo
                + " WHERE pasif = FALSE ORDER BY \"" + sira + "\"" + (artan ? " ASC" : " DESC") + ", vid ASC"
                + " LIMIT :limit OFFSET :atla";
        List<Map<String, Object>> satirlar = jdbc.sql(sql).param("limit", limit).param("atla", atla)
                .query().listOfRows();
        return new Sayfa(satirlar, toplamKayit(tablo));
    }

    /** Süzgece uyan toplam kayıt sayısı (şimdilik yalnızca pasif olmayanlar). */
    public long toplamKayit(TemekrduzenDto duzen) {
        return toplamKayit(tabloAdi(duzen));
    }

    private long toplamKayit(String tablo) {
        Long adet = jdbc.sql("SELECT count(*) FROM " + tablo + " WHERE pasif = FALSE")
                .query(Long.class).single();
        return adet == null ? 0 : adet;
    }

    /** Düzenin ana tablo kodunu doğrular ve döndürür; kalıba uymazsa durdurur. */
    private static String tabloAdi(TemekrduzenDto duzen) {
        String tablo = duzen.tabloKodu();
        if (tablo == null || !TABLO_DESENI.matcher(tablo).matches()) {
            throw new IllegalStateException("Geçersiz tablo kodu: " + tablo);
        }
        return tablo;
    }

    /** Bölümdeki görünür, gerçek (SISTEM) kolonların alan adları; her biri kalıpla doğrulanır. */
    private static List<String> okunacakAlanlar(TemekrduzenDto duzen, String bolumKodu) {
        List<String> alanlar = new ArrayList<>();
        for (TemekrduzenDto.Bolum b : duzen.bolumler()) {
            if (!b.kodu().equals(bolumKodu)) {
                continue;
            }
            for (TemekrduzenDto.Kolon k : b.kolonlar()) {
                if (k.gorunur() && "SISTEM".equals(k.kaynak())) {
                    if (!ALAN_DESENI.matcher(k.alanAdi()).matches()) {
                        throw new IllegalStateException("Geçersiz alan adı: " + k.alanAdi());
                    }
                    alanlar.add(k.alanAdi());
                }
            }
        }
        return alanlar;
    }

    private static boolean siralanabilirMi(TemekrduzenDto duzen, String bolumKodu, String alan) {
        return duzen.bolumler().stream()
                .filter(b -> b.kodu().equals(bolumKodu))
                .flatMap(b -> b.kolonlar().stream())
                .anyMatch(k -> k.alanAdi().equals(alan) && k.siralanabilir() && k.gorunur()
                        && "SISTEM".equals(k.kaynak()));
    }
}
