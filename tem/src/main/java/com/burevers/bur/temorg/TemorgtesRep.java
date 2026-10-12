package com.burevers.bur.temorg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemorgtesRep - TemorgtesVrl için veri erişim arayüzü.
 * Ne işe yarar: tesis kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgtesRep extends JpaRepository<TemorgtesVrl, Long> {

    /** Tesisi koduna göre bulur. */
    Optional<TemorgtesVrl> findByKodu(String kodu);
}
