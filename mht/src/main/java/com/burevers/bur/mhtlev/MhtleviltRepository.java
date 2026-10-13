package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtleviltRepository - MhtleviltEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif iletişim kayıtlarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtleviltRepository extends JpaRepository<MhtleviltEntity, Long> {

    /** Bir muhatabın aktif iletişim kayıtlarını getirir. */
    List<MhtleviltEntity> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
