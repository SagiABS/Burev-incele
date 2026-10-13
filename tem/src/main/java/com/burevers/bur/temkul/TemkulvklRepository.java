package com.burevers.bur.temkul;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulvklRepository - TemkulvklEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı vekâleti kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulvklRepository extends JpaRepository<TemkulvklEntity, Long> {

    /**
     * Bir vekâlet verenin verilen konumda (organizasyon / kuruluş / tesis) ve tarihte geçerli aktif vekillerini,
     * sıra numarasına göre getirir. Tutar limitine göre hangi vekilin onaylayacağı servis katmanında seçilir.
     */
    @Query("select v from TemkulvklEntity v where v.veren.vid = :verenVid and v.pasif = false"
            + " and v.organizasyon.vid = :orgVid"
            + " and (v.kurulus is null or v.kurulus.vid = :kurVid)"
            + " and (v.tesis is null or v.tesis.vid = :tesVid)"
            + " and v.gecerliBaslangic <= :tarih and v.gecerliBitis >= :tarih"
            + " order by v.sira")
    List<TemkulvklEntity> findGecerliVekiller(@Param("verenVid") Long verenVid, @Param("orgVid") Long orgVid,
            @Param("kurVid") Long kurVid, @Param("tesVid") Long tesVid, @Param("tarih") LocalDate tarih);
}
