package com.burevers.bur.temdil;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemdilturRepository - TemdilturEntity için veri erişim arayüzü.
 * Ne işe yarar: proje dillerini bulur, ekler, günceller; dil seçici için aktif dilleri sıralı verir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemdilturRepository extends JpaRepository<TemdilturEntity, Long> {

    /** Aktif dilleri dil seçicideki sırayla getirir. */
    List<TemdilturEntity> findByPasifFalseOrderBySira();
}
