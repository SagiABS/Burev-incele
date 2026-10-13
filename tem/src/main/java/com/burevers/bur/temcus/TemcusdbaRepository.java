package com.burevers.bur.temcus;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemcusdbaRepository - TemcusdbaEntity için veri erişim arayüzü.
 * Ne işe yarar: tablo ve alan sözlüğünü sorgular; eşleme ekranlarına alan listesi verir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemcusdbaRepository extends JpaRepository<TemcusdbaEntity, Long> {

    /** Bir tablonun aktif alanlarını alan adına göre sıralı getirir. */
    List<TemcusdbaEntity> findByTabloKoduAndPasifFalseOrderByAlanAdi(String tabloKodu);

    /** Bir tablodaki sistem (göçle açılan) alanı adıyla bulur. */
    Optional<TemcusdbaEntity> findByTabloKoduAndAlanAdiAndKaynak(String tabloKodu, String alanAdi, String kaynak);
}
