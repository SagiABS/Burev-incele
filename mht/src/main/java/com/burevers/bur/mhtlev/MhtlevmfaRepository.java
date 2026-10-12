package com.burevers.bur.mhtlev;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MhtlevmfaRepository - MhtlevmfaEntity için veri erişim arayüzü.
 * Ne işe yarar: kayıtları bulur, ekler, günceller. Bir muhatabın aktif adres bağlantılarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface MhtlevmfaRepository extends JpaRepository<MhtlevmfaEntity, Long> {

    /** Bir muhatabın aktif adres bağlantılarını getirir. */
    List<MhtlevmfaEntity> findByMuhatapVidAndPasifFalse(Long muhatapVid);
}
