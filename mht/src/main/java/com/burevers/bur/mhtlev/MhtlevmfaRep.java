package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevmfaRep - MhtlevmfaVrl için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif adres bağlantılarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevmfaRep extends JpaRepository<MhtlevmfaVrl, Long> {

    /** Bir muhatabın aktif adres bağlantılarını getirir. */
    List<MhtlevmfaVrl> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
