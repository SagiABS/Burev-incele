package com.burevers.bur.temkul;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemkuliznRepository - TemkuliznEntity için veri erişim arayüzü.
 * Ne işe yarar: kullanıcı izni kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemkuliznRepository extends JpaRepository<TemkuliznEntity, Long> {

    /** Bir kaynağın (menü adımı ya da tablo) aktif izinlerini getirir. */
    List<TemkuliznEntity> findByKaynakTuruAndKaynakKoduAndPasifFalse(String kaynakTuru, String kaynakKodu);
}
