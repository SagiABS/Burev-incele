package com.burevers.bur.uygulama;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Uygulamaya özgü ayarlar ("burever." önekiyle application*.yml dosyalarından okunur).
 *
 * <p>Neden var? Dağıtım biçimine göre davranışı tek yerden okumak için. İleride lisans, kiracı
 * stratejisi gibi ayarlar da buraya eklenecek. Ayar sınıfı değişmezdir (record).
 *
 * @param dagitimProfili "onprem" (şirket içi, tek kiracı) veya "saas" (ortak, çok kiracı)
 */
@ConfigurationProperties(prefix = "burever")
public record BurOzellikleri(String dagitimProfili) {

    /** Ayar verilmemişse güvenli varsayılan: şirket içi kurulum. */
    public BurOzellikleri {
        if (dagitimProfili == null || dagitimProfili.isBlank()) {
            dagitimProfili = "onprem";
        }
    }
}
