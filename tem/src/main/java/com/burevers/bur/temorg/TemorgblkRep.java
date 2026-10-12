package com.burevers.bur.temorg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemorgblkRep - TemorgblkVrl için veri erişim arayüzü.
 * Ne işe yarar: bloke kayıtlarını bulur, ekler, günceller; bir karta ait blokeleri getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgblkRep extends JpaRepository<TemorgblkVrl, Long> {

    /** Bir kartın (tablo kodu + kayıt vid) bütün bloke kayıtlarını getirir. */
    List<TemorgblkVrl> findByTabloKoduAndKayitVid(String tabloKodu, Long kayitVid);
}
