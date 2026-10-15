package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Listelerde kayıt durumunu gösteren simgeler.
 *
 * <p>Neden var: Durum yalnızca renkle değil ŞEKİLLE de belli edilir (renk körlüğü için); her durumun
 * farklı bir şekli vardır. Renkler ileride kullanıcı tercihinden okunacak şekilde tek yerde tutulur.
 */
public enum DurumSimgesi {

    /** Etkin kayıt: yeşil, içi dolu daire ve tik. */
    AKTIF(VaadinIcon.CHECK_CIRCLE, "var(--lumo-success-color, #2e7d32)", "Aktif"),
    /** Pasif kayıt: gri, eksi işaretli daire. */
    PASIF(VaadinIcon.MINUS_CIRCLE, "var(--lumo-contrast-50pct, #757575)", "Pasif"),
    /** Dikkat gerektiren kayıt: turuncu üçgen. */
    UYARI(VaadinIcon.WARNING, "var(--lumo-warning-color, #ef6c00)", "Uyarı"),
    /** Hatalı kayıt: kırmızı, çarpı işaretli daire. */
    HATA(VaadinIcon.CLOSE_CIRCLE, "var(--lumo-error-color, #c62828)", "Hata");

    private final VaadinIcon sekil;
    private final String renk;
    private final String ad;

    DurumSimgesi(VaadinIcon sekil, String renk, String ad) {
        this.sekil = sekil;
        this.renk = renk;
        this.ad = ad;
    }

    /** Simgeyi üretir; fare üstüne gelince ve ekran okuyucuya durum adını verir. */
    public Icon olustur() {
        Icon simge = sekil.create();
        simge.setColor(renk);
        simge.getElement().setAttribute("title", ad);
        simge.getElement().setAttribute("aria-label", ad);
        return simge;
    }
}
