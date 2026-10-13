package com.burevers.bur.temkul;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemkulparRepository - TemkulparEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı giriş bilgisi kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkulparRepository extends JpaRepository<TemkulparEntity, Long> {

    /** Kullanıcının aktif giriş bilgisini getirir. */
    @Query("select p from TemkulparEntity p where p.kullanici.vid = :kullaniciVid and p.pasif = false")
    Optional<TemkulparEntity> findAktifByKullanici(@Param("kullaniciVid") Long kullaniciVid);

    /** Dış kimlik (SSO) bilgisine göre aktif giriş kaydını getirir. */
    @Query("select p from TemkulparEntity p where p.ssoSaglayici = :saglayici and p.ssoKimlik = :kimlik and p.pasif = false")
    Optional<TemkulparEntity> findAktifBySso(@Param("saglayici") String saglayici, @Param("kimlik") String kimlik);
}
