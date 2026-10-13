package com.burevers.bur.mhttem;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhttemrolRepository - MhttemrolEntity için veri erişim arayüzü.
 * Ne işe yarar: rol tanımlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhttemrolRepository extends JpaRepository<MhttemrolEntity, Long> {

    /** Rolü kısa koduna göre bulur. */
    Optional<MhttemrolEntity> findByRol(String rol);
}
