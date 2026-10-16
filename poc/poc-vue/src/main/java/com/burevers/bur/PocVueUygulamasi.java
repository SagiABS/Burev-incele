package com.burevers.bur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kanıt çalışması (Vue adayı) giriş sınıfı.
 *
 * <p>Neden bu pakette: tem modülündeki JPA sınıfları ve servisler {@code com.burevers.bur} altında olduğundan,
 * otomatik tarama için giriş sınıfı bu kök pakette durmalıdır (ana uygulamadaki BurUygulamasi ile aynı yaklaşım).
 */
@SpringBootApplication
public class PocVueUygulamasi {

    public static void main(String[] args) {
        SpringApplication.run(PocVueUygulamasi.class, args);
    }
}
