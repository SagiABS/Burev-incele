package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevrolRepository - MhtlevrolEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif rollerini getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevrolRepository extends JpaRepository<MhtlevrolEntity, Long> {

    /** Bir muhatabın aktif rollerini getirir. */
    List<MhtlevrolEntity> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
