package com.burevers.bur.temekr;

import com.burevers.bur.temcus.TemcusdbaEntity;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ekran düzenini veritabanından (temekrtan / temekrbol / temekrkol) okuyup değişmez bir {@link TemekrduzenDto}'ya çevirir.
 *
 * <p>Neden var: Ekranlar kodla değil veriyle tanımlanır (belge 11, esnek tasarım). Arayüz bu servise
 * "şu siskod ve varyantın düzenini ver" der; servis kapsam kalıtımını uygular
 * (tesis &gt; kuruluş &gt; organizasyon &gt; sistem) ve yalnızca YAYIN durumundaki, pasif olmayan tanımı döndürür.
 * Sonuç kısa süreli önbelleğe alınır; düzen yayınlanınca {@link #onbellegiTemizle()} çağrılır.
 *
 * <p>Güvenlik notu: Düzen yalnızca "ekranda ne olacağını" söyler; alan yetkisi (temkulaly) ve veri yetkisi
 * ayrıca sunucuda uygulanmalıdır, düzen yetkiyi aşamaz (henüz bağlanmadı, bkz. belge 11).
 */
@Service
public class TemekrduzenService {

    /** Önbellekte bir düzenin ne kadar süre geçerli sayılacağı. Düzen sık değişmediği için kısa tutulur. */
    private static final Duration ONBELLEK_OMRU = Duration.ofSeconds(60);

    private final TemekrtanRepository ekranlar;
    private final TemekrbolRepository bolumler;
    private final TemekrkolRepository kolonlar;

    /** Önbellek girdisi: düzen (bulunamadıysa boş) ve üretildiği an. */
    private record Girdi(Optional<TemekrduzenDto> duzen, Instant zaman) {
    }

    private final ConcurrentHashMap<String, Girdi> onbellek = new ConcurrentHashMap<>();

    /** Bağımlılıklar kurucudan alınır (alan enjeksiyonu yasaktır). */
    public TemekrduzenService(TemekrtanRepository ekranlar, TemekrbolRepository bolumler,
                              TemekrkolRepository kolonlar) {
        this.ekranlar = ekranlar;
        this.bolumler = bolumler;
        this.kolonlar = kolonlar;
    }

    /**
     * Ekranın bu kapsam için geçerli düzenini döndürür.
     *
     * @param siskod  ekranın sistem kodu
     * @param varyant varyant adı (örn. ANA)
     * @param kapsam  çalışılan organizasyon/kuruluş/tesis
     * @return en özel uyan yayındaki düzen; tanım yoksa boş
     */
    @Transactional(readOnly = true)
    public Optional<TemekrduzenDto> duzenGetir(String siskod, String varyant, TemekrkapsamDto kapsam) {
        String anahtar = siskod + "|" + varyant + "|" + kapsam.organizasyonVid() + "|"
                + kapsam.kurulusVid() + "|" + kapsam.tesisVid();
        Girdi girdi = onbellek.get(anahtar);
        if (girdi != null && girdi.zaman().plus(ONBELLEK_OMRU).isAfter(Instant.now())) {
            return girdi.duzen();
        }
        Optional<TemekrduzenDto> yeni = oku(siskod, varyant, kapsam);
        onbellek.put(anahtar, new Girdi(yeni, Instant.now()));
        return yeni;
    }

    /** Önbelleği boşaltır; bir düzen yayınlandığında ya da değiştirildiğinde çağrılır. */
    public void onbellegiTemizle() {
        onbellek.clear();
    }

    /**
     * Veritabanından en özel uyan tanımı bulur ve DTO'ya çevirir. İşlem, çağıran {@link #duzenGetir}
     * metodunda açıktır (bu metot aynı sınıftan çağrıldığı için kendi başına @Transactional olamaz);
     * tembel yüklenen ilişkiler bu sayede güvenle okunur.
     */
    private Optional<TemekrduzenDto> oku(String siskod, String varyant, TemekrkapsamDto kapsam) {
        TemekrtanEntity enOzel = null;
        int enYuksek = -1;
        for (TemekrtanEntity aday : ekranlar.findYayindakiler(siskod, varyant)) {
            int p = puan(aday, kapsam);
            if (p > enYuksek) {
                enYuksek = p;
                enOzel = aday;
            }
        }
        return enOzel == null ? Optional.empty() : Optional.of(donustur(enOzel));
    }

    private static int puan(TemekrtanEntity e, TemekrkapsamDto kapsam) {
        Long org = e.getOrganizasyon() == null ? null : e.getOrganizasyon().getVid();
        Long kur = e.getKurulus() == null ? null : e.getKurulus().getVid();
        Long tes = e.getTesis() == null ? null : e.getTesis().getVid();
        return kapsam.puan(org, kur, tes);
    }

    private TemekrduzenDto donustur(TemekrtanEntity e) {
        List<TemekrduzenDto.Bolum> sonuc = new ArrayList<>();
        for (TemekrbolEntity b : bolumler.findAktifByEkran(e.getVid())) {
            List<TemekrduzenDto.Kolon> kolonListesi = new ArrayList<>();
            if ("GRID".equals(b.getTuru())) {
                for (TemekrkolEntity k : kolonlar.findAktifByBolum(b.getVid())) {
                    TemcusdbaEntity alan = k.getAlan();
                    // Etiket verilmemişse alan adı gösterilir; çok dilli etiket sözlükten (temorgsozl) sonra gelecek
                    String etiket = k.getEtiket() != null ? k.getEtiket() : alan.getAlanAdi();
                    kolonListesi.add(new TemekrduzenDto.Kolon(alan.getAlanAdi(), alan.getVeriTipi(),
                            alan.getKaynak(), etiket, k.getGenislik() == null ? null : k.getGenislik().intValue(),
                            k.getBicim(), k.getToplam(), k.isSiralanabilir(), k.isFiltrelenebilir(), k.isGorunur()));
                }
            }
            String ustKodu = b.getUst() == null ? null : b.getUst().getKodu();
            sonuc.add(new TemekrduzenDto.Bolum(b.getKodu(), ustKodu, b.getTuru(), b.getYuva(), b.getBaslik(),
                    b.getSira(), b.isSurukle(), b.isAcik(), List.copyOf(kolonListesi)));
        }
        return new TemekrduzenDto(e.getVid(), e.getSiskod(), e.getVaryant(), e.getBaslik(), e.getSablon(),
                e.getTabloKodu(), e.getSurum(), List.copyOf(sonuc));
    }
}
