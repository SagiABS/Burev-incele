package com.burevers.bur.mhtlev;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevmufRepository - MhtlevmufEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Muhatabı koduna göre bulur.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevmufRepository extends JpaRepository<MhtlevmufEntity, Long> {

    /** Muhatabı koduna göre bulur. */
    Optional<MhtlevmufEntity> findByMuhatapKodu(String muhatapKodu);
}
