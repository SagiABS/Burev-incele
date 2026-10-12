package com.burevers.bur.temort;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * TemortetkRep - TemortetkVrl için veri erişim arayüzü.
 * Ne işe yarar: etiket kayıtlarını bulur, ekler, günceller; tabloya göre etiket listesi sorgular.
 * Neden var: Spring Data bu arayüzden gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortetkRep extends JpaRepository<TemortetkVrl, Long> {

    /** Bir tablonun etiket kodundan etiketini bulur (örn. kuruluş formundaki etiket seçimi doğrulaması). */
    Optional<TemortetkVrl> findByDbTablosuAndEtiketKodu(String dbTablosu, String etiketKodu);
}
