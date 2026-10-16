package com.burevers.bur.temdil;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Çeviri metinlerini (temdilcvr) ve proje dillerini (temdiltur) okuyan servis.
 *
 * <p>Neden var: Arayüzdeki her metin (alan etiketi, düğme, menü, mesaj) koda gömülü olmasın; dile ve kapsama göre
 * veritabanından gelsin (ADR-11). Servis, bir dilin tüm metinlerini kapsam kalıtımıyla birleştirip (sistem metni,
 * üzerine organizasyon, kuruluş, tesis ezmesi) küçük bir {@link Cozucu} nesnesi olarak verir; arayüz her metin için
 * veritabanına gitmez. Sonuç kısa süre önbellekte tutulur; metin değişince {@link #onbellegiTemizle()} çağrılır.
 *
 * <p>Metin bulunamazsa sırayla: istenen dilde sıradaki anahtar, varsayılan dildeki anahtar, çağıranın verdiği yedek metin.
 */
@Service
public class TemdilcvrService {

    /** Önbellekte bir dil/kapsam metin haritasının geçerlilik süresi. */
    private static final Duration ONBELLEK_OMRU = Duration.ofSeconds(60);

    /** Bir proje dilinin arayüz için gereken bilgileri (değişmez kopya). */
    public record Dil(String kodu, String adi, String yerelAd, String yon, boolean varsayilan) {

        /** Dil sağdan sola mı yazılıyor (Arapça gibi)? */
        public boolean sagdanSola() {
            return "RTL".equals(yon);
        }
    }

    /**
     * Bir dil ve kapsam için hazırlanmış metin çözücü. Değişmezdir; önbellekte paylaşılır.
     */
    public static final class Cozucu {
        private final Map<String, String> metinler;
        private final Map<String, String> yedekMetinler;

        Cozucu(Map<String, String> metinler, Map<String, String> yedekMetinler) {
            this.metinler = metinler;
            this.yedekMetinler = yedekMetinler;
        }

        /**
         * İlk bulunan anahtarın metnini döndürür.
         *
         * @param yedek     hiçbir anahtar bulunamazsa döndürülecek metin
         * @param anahtarlar özelden genele sıralı anahtarlar (örn. alan.temorgorg.kodu, alan.kodu)
         */
        public String metin(String yedek, String... anahtarlar) {
            for (String anahtar : anahtarlar) {
                String m = metinler.get(anahtar);
                if (m != null) {
                    return m;
                }
            }
            // İstenen dilde yoksa varsayılan dile bakılır (eksik çeviri ekranı boş bırakmasın)
            for (String anahtar : anahtarlar) {
                String m = yedekMetinler.get(anahtar);
                if (m != null) {
                    return m;
                }
            }
            return yedek;
        }

        /** Tek anahtar için metin; yoksa yedek. {0}, {1} gibi yer tutucular değerlerle doldurulur. */
        public String bicimli(String anahtar, String yedek, Object... degerler) {
            String sonuc = metin(yedek, anahtar);
            for (int i = 0; i < degerler.length; i++) {
                sonuc = sonuc.replace("{" + i + "}", String.valueOf(degerler[i]));
            }
            return sonuc;
        }
    }

    private record Girdi<T>(T deger, Instant zaman) {
        boolean tazeMi() {
            return zaman.plus(ONBELLEK_OMRU).isAfter(Instant.now());
        }
    }

    private final TemdilturRepository diller;
    private final TemdilcvrRepository cevirler;
    private final ConcurrentHashMap<String, Girdi<Map<String, String>>> haritaOnbellegi = new ConcurrentHashMap<>();

    /** Bağımlılıklar kurucudan alınır (alan enjeksiyonu yasaktır). */
    public TemdilcvrService(TemdilturRepository diller, TemdilcvrRepository cevirler) {
        this.diller = diller;
        this.cevirler = cevirler;
    }

    /** Dil seçici için aktif dilleri sırayla döndürür. */
    @Transactional(readOnly = true)
    public List<Dil> aktifDiller() {
        return diller.findByPasifFalseOrderBySira().stream()
                .map(d -> new Dil(d.getKodu(), d.getAdi(), d.getYerelAd(), d.getYon(), d.isVarsayilan()))
                .toList();
    }

    /** Varsayılan dilin kodu; hiç varsayılan yoksa (kurulum hatası) "tr". */
    @Transactional(readOnly = true)
    public String varsayilanDilKodu() {
        return aktifDiller().stream().filter(Dil::varsayilan).map(Dil::kodu).findFirst().orElse("tr");
    }

    /**
     * Dil ve kapsam için metin çözücüyü verir. İstenen dil aktif değilse varsayılan dil kullanılır.
     *
     * @param dilKodu  istenen dil (örn. ar)
     * @param orgVid   çalışılan organizasyon (boş olabilir)
     * @param kurVid   çalışılan kuruluş (boş olabilir)
     * @param tesVid   çalışılan tesis (boş olabilir)
     */
    @Transactional(readOnly = true)
    public Cozucu cozucuGetir(String dilKodu, Long orgVid, Long kurVid, Long tesVid) {
        String varsayilan = varsayilanDilKodu();
        boolean gecerli = aktifDiller().stream().anyMatch(d -> d.kodu().equals(dilKodu));
        String kullanilacak = gecerli ? dilKodu : varsayilan;
        Map<String, String> asil = metinHaritasi(kullanilacak, orgVid, kurVid, tesVid);
        Map<String, String> yedek = kullanilacak.equals(varsayilan)
                ? Map.of() : metinHaritasi(varsayilan, orgVid, kurVid, tesVid);
        return new Cozucu(asil, yedek);
    }

    /** Metin önbelleğini boşaltır; bir çeviri eklendiğinde ya da değiştirildiğinde çağrılır. */
    public void onbellegiTemizle() {
        haritaOnbellegi.clear();
    }

    /** Bir dilin metinlerini kapsam kalıtımıyla birleştirir: sistem < organizasyon < kuruluş < tesis. */
    private Map<String, String> metinHaritasi(String dilKodu, Long orgVid, Long kurVid, Long tesVid) {
        String anahtar = dilKodu + "|" + orgVid + "|" + kurVid + "|" + tesVid;
        Girdi<Map<String, String>> girdi = haritaOnbellegi.get(anahtar);
        if (girdi != null && girdi.tazeMi()) {
            return girdi.deger();
        }
        Map<String, String> sonuc = new HashMap<>();
        Map<String, Integer> puanlar = new HashMap<>();
        for (TemdilcvrEntity c : cevirler.findAktifByDil(dilKodu)) {
            int puan = puan(c, orgVid, kurVid, tesVid);
            if (puan < 0) {
                continue; // başka kiracıya ait metin
            }
            Integer onceki = puanlar.get(c.getAnahtar());
            if (onceki == null || puan > onceki) {
                puanlar.put(c.getAnahtar(), puan);
                sonuc.put(c.getAnahtar(), c.getMetin());
            }
        }
        Map<String, String> degismez = Map.copyOf(sonuc);
        haritaOnbellegi.put(anahtar, new Girdi<>(degismez, Instant.now()));
        return degismez;
    }

    /**
     * Metnin kapsama ne kadar uyduğu: -1 uymaz, 0 sistem, 1 organizasyon, 2 kuruluş, 3 tesis.
     * Büyük puan daha özeldir ve kazanır (ekran düzeniyle aynı kalıtım kuralı).
     */
    private static int puan(TemdilcvrEntity c, Long orgVid, Long kurVid, Long tesVid) {
        if (c.getTesisVid() != null) {
            return c.getTesisVid().equals(tesVid) ? 3 : -1;
        }
        if (c.getKurulusVid() != null) {
            return c.getKurulusVid().equals(kurVid) ? 2 : -1;
        }
        if (c.getOrganizasyonVid() != null) {
            return c.getOrganizasyonVid().equals(orgVid) ? 1 : -1;
        }
        return 0;
    }
}
