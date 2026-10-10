package com.burevers.bur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Burever ana sınıfı: uygulamanın tek giriş noktasıdır.
 *
 * <p>Bu sınıf kök pakette ({@code com.burevers.bur}) durur; Spring, bu paketin altındaki tüm
 * paketleri (tem*, bel*, muh*, mht*, cekirdek, uygulama) otomatik tarar.
 *
 * <p>Çalışma biçimi {@code onprem} veya {@code saas} Spring profiliyle seçilir (ADR-17).
 * Varsayılan profil {@code onprem}'dir; çünkü ilk hedef şirket içi kurulumdur.
 */
@SpringBootApplication
@ConfigurationPropertiesScan // @ConfigurationProperties sınıflarını (ör. BurOzellikleri) otomatik kaydeder
public class BurUygulamasi {

    /** Uygulamayı başlatır. */
    public static void main(String[] args) {
        SpringApplication.run(BurUygulamasi.class, args);
    }
}
