package com.burevers.bur.temorg;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemorgmimrRepository - TemorgmimrEntity için veri erişim arayüzü.
 * Ne işe yarar: mimari kayıtlarını bulur, ekler, günceller; sistematik koda ve üst kayda göre sorgular.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgmimrRepository extends JpaRepository<TemorgmimrEntity, Long> {

    /** Kaydı sistematik koduna (siskod) göre bulur. */
    Optional<TemorgmimrEntity> findBySiskod(String siskod);

    /** Bir kaydın doğrudan alt kayıtlarını sıraya göre getirir (ağaç gezinmesi). */
    List<TemorgmimrEntity> findByUstVidOrderBySira(Long ustVid);

    /** Belirli türdeki aktif kayıtları sıraya göre getirir (örn. menü için VIEW kayıtları). */
    List<TemorgmimrEntity> findByTerimAndPasifFalseOrderBySiskod(String terim);
}
