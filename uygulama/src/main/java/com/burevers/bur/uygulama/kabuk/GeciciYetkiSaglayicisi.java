package com.burevers.bur.uygulama.kabuk;

import org.springframework.stereotype.Component;

/**
 * GEÇİCİ yer tutucu: her izni verir. Gerçek yetki (temkul tabloları) bağlanınca silinir.
 * Üretimde kullanılmamalıdır; yalnızca iskeletin görsel denemesi içindir.
 */
@Component
public class GeciciYetkiSaglayicisi implements YetkiSaglayicisi {

    /** Her isteğe "izin var" der. */
    @Override
    public boolean izinVarMi(String siskod, String izin) {
        return true;
    }
}
