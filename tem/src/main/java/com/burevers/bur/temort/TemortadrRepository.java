package com.burevers.bur.temort;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortadrRepository - TemortadrEntity için veri erişim arayüzü.
 * Ne işe yarar: adres kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortadrRepository extends JpaRepository<TemortadrEntity, Long> {
}
