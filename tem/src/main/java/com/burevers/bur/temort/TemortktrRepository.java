package com.burevers.bur.temort;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortktrRepository - TemortktrEntity için veri erişim arayüzü.
 * Ne işe yarar: şirket türü kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortktrRepository extends JpaRepository<TemortktrEntity, Long> {

    /** Türü koduna göre bulur. */
    Optional<TemortktrEntity> findByTurKodu(String turKodu);
}
