package com.burevers.bur.temort;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortblokRepository - TemortblokEntity için veri erişim arayüzü.
 * Ne işe yarar: bloke kayıtlarını bulur, ekler, günceller; bir karta ait blokeleri getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortblokRepository extends JpaRepository<TemortblokEntity, Long> {

    /** Bir kartın (tablo kodu + kayıt vid) bütün bloke kayıtlarını getirir. */
    List<TemortblokEntity> findByTabloKoduAndKayitVid(String tabloKodu, Long kayitVid);
}
