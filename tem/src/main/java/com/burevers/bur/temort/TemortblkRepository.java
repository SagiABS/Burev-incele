package com.burevers.bur.temort;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortblkRepository - TemortblkEntity için veri erişim arayüzü.
 * Ne işe yarar: bloke kayıtlarını bulur, ekler, günceller; bir karta ait blokeleri getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortblkRepository extends JpaRepository<TemortblkEntity, Long> {

    /** Bir kartın (tablo kodu + kayıt vid) bütün bloke kayıtlarını getirir. */
    List<TemortblkEntity> findByTabloKoduAndKayitVid(String tabloKodu, Long kayitVid);
}
