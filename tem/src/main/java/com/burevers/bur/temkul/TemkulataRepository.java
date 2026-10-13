package com.burevers.bur.temkul;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulataRepository - TemkulataEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı - rol ataması kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulataRepository extends JpaRepository<TemkulataEntity, Long> {

    /**
     * Bir kullanıcının verilen tarihte geçerli aktif atamalarını getirir (yetki matrisi bundan hesaplanır).
     * Kapsam süzmesi servis katmanında yapılır: tesis seçili ise org, kuruluş ve tesis düzeyindeki atamalar birlikte geçerlidir.
     */
    @Query("select a from TemkulataEntity a where a.kullanici.vid = :kullaniciVid and a.pasif = false"
            + " and a.gecerliBaslangic <= :tarih and (a.gecerliBitis is null or a.gecerliBitis >= :tarih)")
    List<TemkulataEntity> findGecerliler(@Param("kullaniciVid") Long kullaniciVid, @Param("tarih") LocalDate tarih);
}
