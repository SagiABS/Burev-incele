package com.burevers.bur.temort;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortktrRep - TemortktrVrl için veri erişim arayüzü.
 * Ne işe yarar: şirket türü kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortktrRep extends JpaRepository<TemortktrVrl, Long> {

    /** Türü koduna göre bulur. */
    Optional<TemortktrVrl> findByTurKodu(String turKodu);
}
