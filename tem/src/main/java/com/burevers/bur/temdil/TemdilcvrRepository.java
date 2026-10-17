package com.burevers.bur.temdil;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemdilcvrRepository - TemdilcvrEntity için veri erişim arayüzü.
 * Ne işe yarar: çeviri metinlerini bulur, ekler, günceller; bir dilin tüm aktif metinlerini tek sorguda verir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; metinler dil bazında topluca okunup önbelleğe alınır.
 */
public interface TemdilcvrRepository extends JpaRepository<TemdilcvrEntity, Long> {

    /** Bir dilin tüm aktif metinlerini getirir; kapsam süzgeci servis katmanında uygulanır. */
    @Query("select c from TemdilcvrEntity c where c.dil.kodu = :dilKodu and c.pasif = false")
    List<TemdilcvrEntity> findAktifByDil(@Param("dilKodu") String dilKodu);
}
