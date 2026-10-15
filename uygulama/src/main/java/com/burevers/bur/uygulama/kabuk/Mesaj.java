package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

/**
 * Kullanıcıya kısa bildirim gösteren yardımcı sınıf.
 *
 * <p>Neden var: Mesaj renkleri tek yerde belirlenir: bilgi yeşil, uyarı turuncu, hata kırmızı.
 * Renk, Vaadin tema adlarıyla verilir; tema değişse de anlam korunur.
 */
public final class Mesaj {

    private static final int SURE_MS = 4000;

    private Mesaj() {
        // Yardımcı sınıf: örneği oluşturulamaz
    }

    /** Yeşil bilgi mesajı. */
    public static void bilgi(String metin) {
        goster(metin, "success");
    }

    /** Turuncu uyarı mesajı. */
    public static void uyari(String metin) {
        goster(metin, "warning");
    }

    /** Kırmızı hata mesajı. Hata mesajı daha uzun süre ekranda kalır. */
    public static void hata(String metin) {
        Notification bildirim = Notification.show(metin, SURE_MS * 2, Position.BOTTOM_START);
        bildirim.addThemeNames("error");
    }

    private static void goster(String metin, String tema) {
        Notification bildirim = Notification.show(metin, SURE_MS, Position.BOTTOM_START);
        bildirim.addThemeNames(tema);
    }
}
