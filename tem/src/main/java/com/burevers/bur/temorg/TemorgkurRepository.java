package com.burevers.bur.temorg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemorgkurRepository - TemorgkurEntity için veri erişim arayüzü.
 * Ne işe yarar: kuruluş kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgkurRepository extends JpaRepository<TemorgkurEntity, Long> {

    /** Kuruluşu koduna göre bulur. */
    Optional<TemorgkurEntity> findByKodu(String kodu);
}
