package com.burevers.bur.mhttem;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhttemrolRep - MhttemrolVrl için veri erişim arayüzü.
 * Ne işe yarar: rol tanımlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhttemrolRep extends JpaRepository<MhttemrolVrl, Long> {

    /** Rolü kısa koduna göre bulur. */
    Optional<MhttemrolVrl> findByRol(String rol);
}
