package com.burevers.bur.temort;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortgeoRep - TemortgeoVrl için veri erişim arayüzü.
 * Ne işe yarar: lokasyonları bulur, ekler, günceller; üst lokasyona göre alt listeyi getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortgeoRep extends JpaRepository<TemortgeoVrl, Long> {

    /** Bir lokasyonun doğrudan alt lokasyonlarını (örn. şehrin ilçeleri) adına göre sıralı getirir. */
    List<TemortgeoVrl> findByUstVidOrderByLokasyonAdi(Long ustVid);

    /** Verilen kademedeki (örn. ULKE) aktif lokasyonları adına göre sıralı getirir. */
    List<TemortgeoVrl> findByLokasyonTipiAndPasifFalseOrderByLokasyonAdi(String lokasyonTipi);
}
