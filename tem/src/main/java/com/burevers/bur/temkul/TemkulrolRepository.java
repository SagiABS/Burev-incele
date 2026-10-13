package com.burevers.bur.temkul;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulrolRepository - TemkulrolEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı rolü kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulrolRepository extends JpaRepository<TemkulrolEntity, Long> {

    /** Bir organizasyonda kullanılabilen aktif roller: sistem rolleri (organizasyonsuz) ve o organizasyonun özel rolleri. */
    @Query("select r from TemkulrolEntity r where r.pasif = false and (r.organizasyon is null or r.organizasyon.vid = :orgVid) order by r.kodu")
    List<TemkulrolEntity> findKullanilabilirler(@Param("orgVid") Long orgVid);
}
