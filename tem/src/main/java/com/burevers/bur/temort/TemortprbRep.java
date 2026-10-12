package com.burevers.bur.temort;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemortprbRep - TemortprbVrl için veri erişim arayüzü.
 * Ne işe yarar: para birimi kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortprbRep extends JpaRepository<TemortprbVrl, Long> {

    /** Para birimini koduna göre bulur. */
    Optional<TemortprbVrl> findByPbKodu(String pbKodu);
}
