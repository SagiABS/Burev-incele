package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.temort.TemortktrEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * MhtlevmufEntity - mhtlevmuf tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap (cari) ana kartı; şahıs, kurum ve iletişim kişileri bu tabloda tutulur.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevmuf")
public class MhtlevmufEntity extends TemortPasifEntity {

    /** Muhatap (cari) kodu; benzersiz, şimdilik elle girilir. */
    @Column(name = "muhatapkodu", length = 15, nullable = false)
    private String muhatapKodu;

    /** Resmi ticari ünvan. */
    @Column(name = "muhatapadi", length = 250, nullable = false)
    private String muhatapAdi;

    /** Şahıs muhatap adı. */
    @Column(name = "ad", length = 100)
    private String ad;

    /** Şahıs muhatap soyadı. */
    @Column(name = "soyad", length = 100)
    private String soyad;

    /** Kısa ad veya marka. */
    @Column(name = "muhatapkisaadi", length = 100)
    private String muhatapKisaAdi;

    /** Yasal şirket türü (temortktr). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurturvid", nullable = false)
    private TemortktrEntity kurTur;

    /** Vergi/ulusal kimlik numarası (ülkeye göre VKN, TCKN, SSN, EIN). */
    @Column(name = "vergino", length = 30, nullable = false)
    private String vergiNo;

    /** Logo bağlantısı (bulut depolama). */
    @Column(name = "logourl", length = 500)
    private String logoUrl;

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ekbilgiler")
    private Map<String, Object> ekBilgiler;

    /** Muhatap (cari) kodu; benzersiz, şimdilik elle girilir. */
    public String getMuhatapKodu() {
        return muhatapKodu;
    }

    /** Muhatap (cari) kodu; benzersiz, şimdilik elle girilir. */
    public void setMuhatapKodu(String muhatapKodu) {
        this.muhatapKodu = muhatapKodu;
    }

    /** Resmi ticari ünvan. */
    public String getMuhatapAdi() {
        return muhatapAdi;
    }

    /** Resmi ticari ünvan. */
    public void setMuhatapAdi(String muhatapAdi) {
        this.muhatapAdi = muhatapAdi;
    }

    /** Şahıs muhatap adı. */
    public String getAd() {
        return ad;
    }

    /** Şahıs muhatap adı. */
    public void setAd(String ad) {
        this.ad = ad;
    }

    /** Şahıs muhatap soyadı. */
    public String getSoyad() {
        return soyad;
    }

    /** Şahıs muhatap soyadı. */
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    /** Kısa ad veya marka. */
    public String getMuhatapKisaAdi() {
        return muhatapKisaAdi;
    }

    /** Kısa ad veya marka. */
    public void setMuhatapKisaAdi(String muhatapKisaAdi) {
        this.muhatapKisaAdi = muhatapKisaAdi;
    }

    /** Yasal şirket türü (temortktr). */
    public TemortktrEntity getKurTur() {
        return kurTur;
    }

    /** Yasal şirket türü (temortktr). */
    public void setKurTur(TemortktrEntity kurTur) {
        this.kurTur = kurTur;
    }

    /** Vergi/ulusal kimlik numarası (ülkeye göre VKN, TCKN, SSN, EIN). */
    public String getVergiNo() {
        return vergiNo;
    }

    /** Vergi/ulusal kimlik numarası (ülkeye göre VKN, TCKN, SSN, EIN). */
    public void setVergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
    }

    /** Logo bağlantısı (bulut depolama). */
    public String getLogoUrl() {
        return logoUrl;
    }

    /** Logo bağlantısı (bulut depolama). */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    public Map<String, Object> getEkBilgiler() {
        return ekBilgiler;
    }

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    public void setEkBilgiler(Map<String, Object> ekBilgiler) {
        this.ekBilgiler = ekBilgiler;
    }
}
