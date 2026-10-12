package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtleviltRep - MhtleviltVrl için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif iletişim kayıtlarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtleviltRep extends JpaRepository<MhtleviltVrl, Long> {

    /** Bir muhatabın aktif iletişim kayıtlarını getirir. */
    List<MhtleviltVrl> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
