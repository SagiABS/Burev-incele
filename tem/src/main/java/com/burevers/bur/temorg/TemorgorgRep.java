package com.burevers.bur.temorg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemorgorgRep - TemorgorgVrl için veri erişim arayüzü.
 * Ne işe yarar: organizasyon kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgorgRep extends JpaRepository<TemorgorgVrl, Long> {

    /** Organizasyonu koduna göre bulur. */
    Optional<TemorgorgVrl> findByKodu(String kodu);
}
