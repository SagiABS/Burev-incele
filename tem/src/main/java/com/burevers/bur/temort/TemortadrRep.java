package com.burevers.bur.temort;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemortadrRep - TemortadrVrl için veri erişim arayüzü.
 * Ne işe yarar: adres kayıtlarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemortadrRep extends JpaRepository<TemortadrVrl, Long> {
}
