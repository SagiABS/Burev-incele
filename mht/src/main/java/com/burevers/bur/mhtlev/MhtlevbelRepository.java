package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevbelRepository - MhtlevbelEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın belgelerini getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevbelRepository extends JpaRepository<MhtlevbelEntity, Long> {

    /** Bir muhatabın belgelerini getirir. */
    List<MhtlevbelEntity> findByMuhatapVid(Long muhatapVid);
}
