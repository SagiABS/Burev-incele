package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevidmRepository - MhtlevidmEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir kuruluşun kaynak olduğu aktif ilişkileri getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevidmRepository extends JpaRepository<MhtlevidmEntity, Long> {

    /** Bir kuruluşun kaynak olduğu aktif ilişkileri getirir. */
    List<MhtlevidmEntity> findByKurulusVidAndPasifFalse(Long kurulusVid);
}
