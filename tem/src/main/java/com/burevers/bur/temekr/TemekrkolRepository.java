package com.burevers.bur.temekr;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemekrkolRepository - TemekrkolEntity için veri erişim arayüzü.
 * Ne işe yarar: kolon tanımı kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemekrkolRepository extends JpaRepository<TemekrkolEntity, Long> {

    /** Bir tablo bölümünün aktif kolonlarını sıraya göre getirir. */
    @Query("select k from TemekrkolEntity k where k.bolum.vid = :bolumVid and k.pasif = false order by k.sira")
    List<TemekrkolEntity> findAktifByBolum(@Param("bolumVid") Long bolumVid);
}
