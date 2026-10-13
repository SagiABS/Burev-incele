package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevgrpRepository - MhtlevgrpEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif grup üyeliklerini getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevgrpRepository extends JpaRepository<MhtlevgrpEntity, Long> {

    /** Bir muhatabın aktif grup üyeliklerini getirir. */
    List<MhtlevgrpEntity> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
