package com.burevers.bur.temekr;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemekrtanRepository - TemekrtanEntity için veri erişim arayüzü.
 * Ne işe yarar: ekran tanımı kayıtlarını bulur, ekler, günceller; yetki hesabında gereken sorguları sağlar.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemekrtanRepository extends JpaRepository<TemekrtanEntity, Long> {

    /** Bir ekranın (siskod + varyant) yayındaki düzenlerini getirir; kapsam seçimi servis katmanında yapılır (tesis > kuruluş > organizasyon > sistem). */
    @Query("select e from TemekrtanEntity e where e.siskod = :siskod and e.varyant = :varyant and e.durum = 'YAYIN' and e.pasif = false")
    List<TemekrtanEntity> findYayindakiler(@Param("siskod") String siskod, @Param("varyant") String varyant);

    /** Bir siskod için yayındaki tüm varyantları getirir (varyant seçim kuralı için). */
    @Query("select e from TemekrtanEntity e where e.siskod = :siskod and e.durum = 'YAYIN' and e.pasif = false order by e.varyant")
    List<TemekrtanEntity> findYayindakiVaryantlar(@Param("siskod") String siskod);
}
