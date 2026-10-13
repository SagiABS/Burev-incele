package com.burevers.bur.temkul;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulalyRepository - TemkulalyEntity için veri erişim arayüzü.
 * Ne işe yarar: rol bazlı alan yetkisi kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulalyRepository extends JpaRepository<TemkulalyEntity, Long> {

    /** Bir rolün aktif alan kısıtlarını getirir. */
    @Query("select y from TemkulalyEntity y where y.rol.vid = :rolVid and y.pasif = false")
    List<TemkulalyEntity> findAktifByRol(@Param("rolVid") Long rolVid);
}
