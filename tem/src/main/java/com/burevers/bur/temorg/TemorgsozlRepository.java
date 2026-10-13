package com.burevers.bur.temorg;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemorgsozlRepository - TemorgsozlEntity için veri erişim arayüzü.
 * Ne işe yarar: sözlük kayıtlarını bulur, ekler, günceller; terime göre sorgular.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgsozlRepository extends JpaRepository<TemorgsozlEntity, Long> {

    /** Kaydı terimine göre bulur (örn. "tem"). */
    Optional<TemorgsozlEntity> findByTerim(String terim);

    /** Aktif sözlük kayıtlarını terime göre sıralı getirir. */
    List<TemorgsozlEntity> findByPasifFalseOrderByTerim();
}
