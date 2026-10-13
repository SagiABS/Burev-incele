package com.burevers.bur.temcus;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemcusidmRepository - TemcusidmEntity için veri erişim arayüzü.
 * Ne işe yarar: bir kaynak alan için, verilen konumda ve tarihte geçerli eşlemeleri getirir.
 * Neden var: kayıt atılırken eşleme kuralları buradan okunur; sonuç servis katmanında önbelleğe alınacaktır.
 */
public interface TemcusidmRepository extends JpaRepository<TemcusidmEntity, Long> {

    /**
     * Bir kaynak alan için, verilen konumda (organizasyon / kuruluş / tesis) ve tarihte geçerli aktif eşlemeleri
     * çalışma sırasına göre getirir. Kapsam aşağı doğru kalıtılır (temcusmde ile aynı kural).
     */
    @Query("select m from TemcusidmEntity m where m.kaynakAlan.vid = :kaynakVid and m.pasif = false"
            + " and m.organizasyon.vid = :orgVid"
            + " and (m.kurulus is null or m.kurulus.vid = :kurVid)"
            + " and (m.tesis is null or m.tesis.vid = :tesVid)"
            + " and m.gecerliBaslangic <= :tarih and (m.gecerliBitis is null or m.gecerliBitis >= :tarih)"
            + " order by m.sira")
    List<TemcusidmEntity> findUygulananlar(@Param("kaynakVid") Long kaynakVid,
            @Param("orgVid") Long orgVid, @Param("kurVid") Long kurVid, @Param("tesVid") Long tesVid,
            @Param("tarih") LocalDate tarih);
}
