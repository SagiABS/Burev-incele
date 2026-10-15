package com.burevers.bur.uygulama.kabuk;

import com.burevers.bur.temekr.TemekrduzenDto;
import com.burevers.bur.temekr.TemekrduzenService;
import com.burevers.bur.temekr.TemekrkapsamDto;
import com.burevers.bur.temekr.TemekrveriService;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bir siskod için doğru ekranı üretir: düzeni veritabanından okur ve şablona göre ekranı kurar.
 *
 * <p>Neden var: Menü, sekme yöneticisi ve ekran şablonları birbirini bilmesin; "şu siskodun ekranını ver"
 * isteği tek yerde çözülsün. Düzen tanımı yoksa kullanıcıya açık bir uyarı gösterilir (boş ekran değil).
 * Kapsam şimdilik yalnızca sistem varsayılanıdır; oturum (kullanıcının çalıştığı organizasyon/kuruluş/tesis)
 * bağlanınca buradan geçirilecektir.
 */
@org.springframework.stereotype.Component
public class EkranFabrikasi {

    private static final Logger GUNLUK = LoggerFactory.getLogger(EkranFabrikasi.class);
    private static final String VARSAYILAN_VARYANT = "ANA";

    private final TemekrduzenService duzenler;
    private final TemekrveriService veriler;
    private final YetkiSaglayicisi yetki;

    /** Bağımlılıklar kurucudan alınır (alan enjeksiyonu yasaktır). */
    public EkranFabrikasi(TemekrduzenService duzenler, TemekrveriService veriler, YetkiSaglayicisi yetki) {
        this.duzenler = duzenler;
        this.veriler = veriler;
        this.yetki = yetki;
    }

    /**
     * Ekranı üretir.
     *
     * @param siskod ekranın sistem kodu
     * @param baslik menüde görünen ad (düzen bulunamazsa uyarıda kullanılır)
     */
    public com.vaadin.flow.component.Component olustur(String siskod, String baslik) {
        Optional<TemekrduzenDto> duzen;
        try {
            duzen = duzenler.duzenGetir(siskod, VARSAYILAN_VARYANT, TemekrkapsamDto.sistem());
        } catch (RuntimeException hata) {
            // Veritabanı hatası ayrıntısı kullanıcıya gösterilmez; sunucu günlüğüne yazılır
            GUNLUK.error("Ekran düzeni okunamadı: {}", siskod, hata);
            return mesajEkrani("'" + baslik + "' ekranı açılamadı. Ayrıntı sunucu günlüğündedir.");
        }
        if (duzen.isEmpty()) {
            return mesajEkrani("'" + baslik + "' (" + siskod + ") için yayınlanmış bir ekran düzeni yok.");
        }
        TemekrduzenDto d = duzen.get();
        if ("LISTE".equals(d.sablon())) {
            return new ListeSablonu(d, veriler, yetki);
        }
        return mesajEkrani("'" + d.baslik() + "' için " + d.sablon() + " şablonu henüz hazır değil.");
    }

    private static com.vaadin.flow.component.Component mesajEkrani(String metin) {
        VerticalLayout kutu = new VerticalLayout(new Paragraph(metin));
        kutu.setSizeFull();
        return kutu;
    }
}
