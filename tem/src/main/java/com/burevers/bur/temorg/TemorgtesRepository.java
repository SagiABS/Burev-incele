package com.burevers.bur.temorg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemorgtesRepository - TemorgtesEntity için veri erişim arayüzü.
 * Ne işe yarar: tesis kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgtesRepository extends JpaRepository<TemorgtesEntity, Long> {

    /** Tesisi koduna göre bulur. */
    Optional<TemorgtesEntity> findByKodu(String kodu);
}
