package com.burevers.bur.temekr;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemekrbolRepository - TemekrbolEntity için veri erişim arayüzü.
 * Ne işe yarar: ekran bölümü kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemekrbolRepository extends JpaRepository<TemekrbolEntity, Long> {

    /** Bir ekranın aktif bölümlerini yuva ve sıraya göre getirir. */
    @Query("select b from TemekrbolEntity b where b.ekran.vid = :ekranVid and b.pasif = false order by b.yuva, b.sira")
    List<TemekrbolEntity> findAktifByEkran(@Param("ekranVid") Long ekranVid);
}
