package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemortprbEntity - temortprb tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: para birimi tanımları (kod, ad, ondalık basamak sayısı).
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortprb")
public class TemortprbEntity extends TemortPasifEntity {

    /** Para birimi kodu (3 karakter). */
    @Column(name = "pbkodu", length = 3, nullable = false)
    private String pbKodu;

    /** Para birimi adı. */
    @Column(name = "pbadi", length = 50, nullable = false)
    private String pbAdi;

    /** Ondalık basamak sayısı (0-4). */
    @Column(name = "decimal", nullable = false)
    private Short ondalik;

    /** Ondalık kısmın adı (örn. kuruş). */
    @Column(name = "decadi", length = 50)
    private String ondalikAdi;

    /** Standart ad (örn. TRY). */
    @Column(name = "stdadi", length = 30)
    private String standartAdi;

    /** Örnek gösterim. */
    @Column(name = "ornek", length = 50)
    private String ornek;

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Para birimi kodu (3 karakter). */
    public String getPbKodu() {
        return pbKodu;
    }

    /** Para birimi kodu (3 karakter). */
    public void setPbKodu(String pbKodu) {
        this.pbKodu = pbKodu;
    }

    /** Para birimi adı. */
    public String getPbAdi() {
        return pbAdi;
    }

    /** Para birimi adı. */
    public void setPbAdi(String pbAdi) {
        this.pbAdi = pbAdi;
    }

    /** Ondalık basamak sayısı (0-4). */
    public Short getOndalik() {
        return ondalik;
    }

    /** Ondalık basamak sayısı (0-4). */
    public void setOndalik(Short ondalik) {
        this.ondalik = ondalik;
    }

    /** Ondalık kısmın adı (örn. kuruş). */
    public String getOndalikAdi() {
        return ondalikAdi;
    }

    /** Ondalık kısmın adı (örn. kuruş). */
    public void setOndalikAdi(String ondalikAdi) {
        this.ondalikAdi = ondalikAdi;
    }

    /** Standart ad (örn. TRY). */
    public String getStandartAdi() {
        return standartAdi;
    }

    /** Standart ad (örn. TRY). */
    public void setStandartAdi(String standartAdi) {
        this.standartAdi = standartAdi;
    }

    /** Örnek gösterim. */
    public String getOrnek() {
        return ornek;
    }

    /** Örnek gösterim. */
    public void setOrnek(String ornek) {
        this.ornek = ornek;
    }

    /** Açıklama. */
    public String getAciklama() {
        return aciklama;
    }

    /** Açıklama. */
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
