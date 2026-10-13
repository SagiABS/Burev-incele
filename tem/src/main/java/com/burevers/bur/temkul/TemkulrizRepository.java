package com.burevers.bur.temkul;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulrizRepository - TemkulrizEntity için veri erişim arayüzü.
 * Ne işe yarar: rol - izin ilişkisi kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulrizRepository extends JpaRepository<TemkulrizEntity, Long> {

    /** Bir rolün aktif izin kayıtlarını (verilen ve yasaklanan) getirir. */
    @Query("select z from TemkulrizEntity z where z.rol.vid = :rolVid and z.pasif = false")
    List<TemkulrizEntity> findAktifByRol(@Param("rolVid") Long rolVid);
}
