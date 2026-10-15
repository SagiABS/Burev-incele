package com.burevers.bur.uygulama.kabuk;

/**
 * Arayüzdeki düğme ve ekranların yetkisini sorar.
 *
 * <p>Neden var: Düğmelerin (örn. dışa aktar) görünürlüğü yetkiye bağlıdır; ama temkul yetki
 * hizmeti arayüze bağlanana kadar bir yer tutucu gerekir. Arayüz yalnızca bu arayüze bağlıdır.
 * Not: arayüzdeki gizleme yalnızca kolaylıktır; asıl denetim sunucu hizmetlerinde yapılır.
 */
public interface YetkiSaglayicisi {

    /**
     * @param siskod ekranın sistem kodu
     * @param izin   izin kodu (örn. {@code DISARIAKTAR})
     * @return kullanıcının bu izni var mı
     */
    boolean izinVarMi(String siskod, String izin);
}
