package com.burevers.bur.temorg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * TemorgmdeRepository - TemorgmdeEntity için veri erişim arayüzü.
 * Ne işe yarar: ek alan tanımlarını bulur, ekler, günceller; bir tablonun tanımlarını getirir.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL/JDBC kodu yazılmaz.
 */
public interface TemorgmdeRepository extends JpaRepository<TemorgmdeEntity, Long> {

    /** Bir tablonun aktif ek alan tanımlarını getirir (ekran bu listeden dinamik form üretir). */
    List<TemorgmdeEntity> findByTabloKoduAndPasifFalse(String tabloKodu);

    /**
     * Bir tablo için, verilen konumda (organizasyon / kuruluş / tesis) geçerli aktif tanımları getirir.
     * Kapsam aşağı doğru kalıtılır: organizasyon düzeyindeki tanım bütün kuruluş ve tesisleri, kuruluş düzeyindeki
     * tanım o kuruluşun bütün tesislerini kapsar. Kuruluş veya tesis boş verilirse (null) yalnızca o düzeyin
     * üstündeki tanımlar eşleşir. Ekran tesis seçtiğinde org ve kuruluşu yapı ağacından bulup üçünü de verir.
     */
    @Query("select m from TemorgmdeEntity m where m.tabloKodu = :tabloKodu and m.pasif = false"
            + " and m.organizasyon.vid = :orgVid"
            + " and (m.kurulus is null or m.kurulus.vid = :kurVid)"
            + " and (m.tesis is null or m.tesis.vid = :tesVid)")
    List<TemorgmdeEntity> findUygulananlar(@Param("tabloKodu") String tabloKodu,
            @Param("orgVid") Long orgVid, @Param("kurVid") Long kurVid, @Param("tesVid") Long tesVid);
}
