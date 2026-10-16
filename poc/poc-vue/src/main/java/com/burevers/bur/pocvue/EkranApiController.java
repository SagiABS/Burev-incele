package com.burevers.bur.pocvue;

import com.burevers.bur.temdil.TemdilcvrService;
import com.burevers.bur.temdil.TemdilcvrService.Cozucu;
import com.burevers.bur.temekr.TemekrduzenDto;
import com.burevers.bur.temekr.TemekrduzenService;
import com.burevers.bur.temekr.TemekrkapsamDto;
import com.burevers.bur.temekr.TemekrveriService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kanıt çalışması: Vue ön yüzünün kullandığı salt-okunur REST uçları.
 *
 * <p>Neden var: "ekran düzenini veritabanından oku, tarayıcıda çiz" yaklaşımının sunucu tarafı yarısıdır.
 * Hiçbir oturum durumu tutulmaz; her istek kendi kendine yeter (durumsuz). Etiketler sunucuda seçilen dile göre
 * çözülür, böylece tarayıcı yalnızca hazır metin alır. Kiracı süzgeci ve yetki bilerek YOKTUR (kanıt kapsamı dışı).
 */
@RestController
@RequestMapping("/api")
public class EkranApiController {

    /** Ekranın arayüz metinleri; anahtar -> yedek metin. Vue bu sözlüğü olduğu gibi kullanır. */
    private static final Map<String, String> ARAYUZ_ANAHTARLARI = Map.of(
            "liste.toplamkayit", "Toplam kayıt",
            "dugme.altpanel", "Alt panel",
            "dugme.yenile", "Yenile",
            "dil.secim", "Dil");

    private final TemekrduzenService duzenler;
    private final TemekrveriService veriler;
    private final TemdilcvrService diller;

    /** Bağımlılıklar kurucudan enjekte edilir (alan enjeksiyonu yasak). */
    public EkranApiController(TemekrduzenService duzenler, TemekrveriService veriler, TemdilcvrService diller) {
        this.duzenler = duzenler;
        this.veriler = veriler;
        this.diller = diller;
    }

    /** Etkin diller: seçici için kod, ad ve yazı yönü. */
    @GetMapping("/diller")
    public List<TemdilcvrService.Dil> dilleriGetir() {
        return diller.aktifDiller();
    }

    /**
     * Ekran düzeni; etiketler istenen dile çözülmüş olarak döner.
     *
     * @param siskod ekranın sistem kodu (örn. tem_893_000)
     * @param dil    dil kodu; boşsa varsayılan dil
     */
    @GetMapping("/ekran/{siskod}")
    public ResponseEntity<Map<String, Object>> ekranGetir(@PathVariable String siskod,
                                                          @RequestParam(required = false) String dil) {
        var duzen = duzenler.duzenGetir(siskod, "ANA", TemekrkapsamDto.sistem());
        if (duzen.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TemekrduzenDto d = duzen.get();
        String dilKodu = dil == null || dil.isBlank() ? diller.varsayilanDilKodu() : dil;
        Cozucu cozucu = diller.cozucuGetir(dilKodu, null, null, null);

        Map<String, Object> cevap = new LinkedHashMap<>();
        cevap.put("siskod", d.siskod());
        cevap.put("baslik", d.baslik());
        cevap.put("tabloKodu", d.tabloKodu());
        cevap.put("yon", diller.aktifDiller().stream().filter(x -> x.kodu().equals(dilKodu))
                .findFirst().map(x -> x.sagdanSola() ? "rtl" : "ltr").orElse("ltr"));
        Map<String, String> metin = new LinkedHashMap<>();
        ARAYUZ_ANAHTARLARI.forEach((anahtar, yedek) -> metin.put(anahtar, cozucu.metin(yedek, anahtar)));
        cevap.put("metin", metin);
        cevap.put("bolumler", d.bolumler().stream().map(b -> bolumDonustur(d, b, cozucu)).toList());
        return ResponseEntity.ok(cevap);
    }

    /** Bir tablo bölümünün satırları; sıralama ve sayfalama sunucuda yapılır. */
    @GetMapping("/veri/{siskod}/{bolum}")
    public ResponseEntity<Map<String, Object>> veriGetir(@PathVariable String siskod, @PathVariable String bolum,
                                                         @RequestParam(required = false) String sirala,
                                                         @RequestParam(defaultValue = "true") boolean artan,
                                                         @RequestParam(defaultValue = "0") int baslangic,
                                                         @RequestParam(defaultValue = "50") int adet) {
        var duzen = duzenler.duzenGetir(siskod, "ANA", TemekrkapsamDto.sistem());
        if (duzen.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var sayfa = veriler.sayfaGetir(duzen.get(), bolum, sirala, artan, baslangic, adet);
        return ResponseEntity.ok(Map.of("satirlar", sayfa.satirlar(), "toplam", sayfa.toplam()));
    }

    /** Bölümü, kolon etiketleri çözülmüş sade bir haritaya çevirir. */
    private static Map<String, Object> bolumDonustur(TemekrduzenDto d, TemekrduzenDto.Bolum b, Cozucu cozucu) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("kodu", b.kodu());
        m.put("turu", b.turu());
        m.put("yuva", b.yuva());
        m.put("baslik", cozucu.metin(b.baslik(), "panel." + b.kodu().toLowerCase()));
        m.put("surukle", b.surukle());
        m.put("acik", b.acik());
        m.put("kolonlar", b.kolonlar().stream().filter(TemekrduzenDto.Kolon::gorunur).map(k -> {
            Map<String, Object> km = new LinkedHashMap<>();
            km.put("alan", k.alanAdi());
            // Etiket seçim sırası: ekrana özel etiket -> alan.<tablo>.<alan> -> alan.<alan> -> alan adı
            km.put("etiket", k.etiket() != null ? k.etiket()
                    : cozucu.metin(k.alanAdi(), "alan." + d.tabloKodu() + "." + k.alanAdi(), "alan." + k.alanAdi()));
            km.put("siralanabilir", k.siralanabilir());
            km.put("genislik", k.genislik());
            return km;
        }).toList());
        return m;
    }
}
