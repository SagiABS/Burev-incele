package com.burevers.bur.temekr;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemekryerRepository - TemekryerEntity için veri erişim arayüzü.
 * Ne işe yarar: alan yerleşimi kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemekryerRepository extends JpaRepository<TemekryerEntity, Long> {

    /** Bir bölümün aktif alan yerleşimlerini satır ve sütuna göre getirir. */
    @Query("select y from TemekryerEntity y where y.bolum.vid = :bolumVid and y.pasif = false order by y.satir, y.sutun")
    List<TemekryerEntity> findAktifByBolum(@Param("bolumVid") Long bolumVid);
}
