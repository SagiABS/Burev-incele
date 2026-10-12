package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevkurRep - MhtlevkurVrl için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir kuruluşa atanmış aktif muhatap kayıtlarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevkurRep extends JpaRepository<MhtlevkurVrl, Long> {

    /** Bir kuruluşa atanmış aktif muhatap kayıtlarını getirir. */
    List<MhtlevkurVrl> findByKurulusVidAndPasifFalse(Long kurulusVid);
}
