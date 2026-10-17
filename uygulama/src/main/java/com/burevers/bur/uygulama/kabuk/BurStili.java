package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.UI;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Kabuk stilini (bur/kabuk.css) sayfaya bir kez ekler.
 *
 * <p>Neden var: Menü grupları, araç çubuğu düğmeleri ve paneller taslaktaki görünümü için özel sınıflara ihtiyaç duyar.
 * Stil dosyası sınıf yolundan okunur ve sayfaya {@code <style>} olarak eklenir; böylece Vaadin tema yapılandırmasına
 * dokunmadan çalışır. Aynı sayfaya ikinci kez eklenmez.
 */
public final class BurStili {

    private static final String KAYNAK = "/bur/kabuk.css";
    /** Stil metni uygulama açılırken bir kez okunur ve paylaşılır. */
    private static final String CSS = oku();

    private BurStili() {
        // Yardımcı sınıf: örneği oluşturulamaz
    }

    /** Stili verilen arayüze ekler (zaten ekliyse bir şey yapmaz). */
    public static void uygula(UI ui) {
        ui.getPage().executeJs(
                "if (!document.getElementById('bur-stil')) {"
                        + " const s = document.createElement('style'); s.id = 'bur-stil';"
                        + " s.textContent = $0; document.head.appendChild(s); }",
                CSS);
    }

    private static String oku() {
        try (InputStream girdi = BurStili.class.getResourceAsStream(KAYNAK)) {
            if (girdi == null) {
                throw new IllegalStateException("Stil dosyası bulunamadı: " + KAYNAK);
            }
            return new String(girdi.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException hata) {
            throw new UncheckedIOException(hata);
        }
    }
}
