package com.burevers.bur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kanıt çalışması (PrimeFaces adayı) giriş sınıfı.
 *
 * <p>Neden bu pakette: tem modülündeki JPA sınıfları ve servisler {@code com.burevers.bur} altında olduğundan
 * otomatik tarama için giriş sınıfı kök pakette durmalıdır (ana uygulamadaki BurUygulamasi gibi).
 */
@SpringBootApplication
public class PocPrimefacesUygulamasi {

    public static void main(String[] args) {
        SpringApplication.run(PocPrimefacesUygulamasi.class, args);
    }
}
