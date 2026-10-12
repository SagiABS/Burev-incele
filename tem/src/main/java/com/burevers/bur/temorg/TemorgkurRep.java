package com.burevers.bur.temorg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemorgkurRep - TemorgkurVrl için veri erişim arayüzü.
 * Ne işe yarar: kuruluş kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgkurRep extends JpaRepository<TemorgkurVrl, Long> {

    /** Kuruluşu koduna göre bulur. */
    Optional<TemorgkurVrl> findByKodu(String kodu);
}
