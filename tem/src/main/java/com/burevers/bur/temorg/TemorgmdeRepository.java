package com.burevers.bur.temorg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemorgmdeRepository - TemorgmdeEntity için veri erişim arayüzü.
 * Ne işe yarar: ek alan tanımlarını bulur, ekler, günceller; bir tablonun tanımlarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgmdeRepository extends JpaRepository<TemorgmdeEntity, Long> {

    /** Bir tablonun aktif ek alan tanımlarını getirir (ekran bu listeden dinamik form üretir). */
    List<TemorgmdeEntity> findByTabloKoduAndPasifFalse(String tabloKodu);
}
