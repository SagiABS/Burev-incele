package com.burevers.bur.temort;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemortprbRepository - TemortprbEntity için veri erişim arayüzü.
 * Ne işe yarar: para birimi kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortprbRepository extends JpaRepository<TemortprbEntity, Long> {

    /** Para birimini koduna göre bulur. */
    Optional<TemortprbEntity> findByPbKodu(String pbKodu);
}
