package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevadrRepository - MhtlevadrEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif adres bağlantılarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevadrRepository extends JpaRepository<MhtlevadrEntity, Long> {

    /** Bir muhatabın aktif adres bağlantılarını getirir. */
    List<MhtlevadrEntity> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
