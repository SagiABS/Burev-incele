package com.burevers.bur.temkul;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemkultanRepository - TemkultanEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı tanımı kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkultanRepository extends JpaRepository<TemkultanEntity, Long> {

    /** Kullanıcıyı koduna göre bulur (büyük/küçük harf duyarsız). */
    Optional<TemkultanEntity> findByKoduIgnoreCase(String kodu);

    /** Kullanıcıyı e-postasına göre bulur (büyük/küçük harf duyarsız). */
    Optional<TemkultanEntity> findByEpostasiIgnoreCase(String epostasi);
}
