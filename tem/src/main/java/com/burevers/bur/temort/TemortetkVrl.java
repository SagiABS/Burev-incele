package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
/**
 * TemortetkVrl - temortetk tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: etiket tanımları (hangi tablo için hangi etiket kodu/adı).
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortetk")
public class TemortetkVrl extends TemortPasifVrl {

    /** Etiketin ait olduğu tablo kodu (sistem referansı). */
    @Column(name = "dbtablosu", length = 9, nullable = false)
    private String dbTablosu;

    /** Etiket kodu (3 karakter). */
    @Column(name = "etiketkodu", length = 3, nullable = false)
    private String etiketKodu;

    /** Etiket adı. */
    @Column(name = "etiketadi", length = 50, nullable = false)
    private String etiketAdi;

    /** Etiket grubu. */
    @Column(name = "etkgrubu", length = 10)
    private String etkGrubu;

    /** Alfabetik tanım. */
    @Column(name = "alftanimi", length = 100)
    private String alfTanimi;

    /** Etiket notu. */
    @Column(name = "etiketnot", length = 200)
    private String etiketNot;

    /** Açılış tarihi. */
    @Column(name = "acilistarihi", nullable = false)
    private LocalDate acilisTarihi;

    /** Etiketin ait olduğu tablo kodu (sistem referansı). */
    public String getDbTablosu() {
        return dbTablosu;
    }

    /** Etiketin ait olduğu tablo kodu (sistem referansı). */
    public void setDbTablosu(String dbTablosu) {
        this.dbTablosu = dbTablosu;
    }

    /** Etiket kodu (3 karakter). */
    public String getEtiketKodu() {
        return etiketKodu;
    }

    /** Etiket kodu (3 karakter). */
    public void setEtiketKodu(String etiketKodu) {
        this.etiketKodu = etiketKodu;
    }

    /** Etiket adı. */
    public String getEtiketAdi() {
        return etiketAdi;
    }

    /** Etiket adı. */
    public void setEtiketAdi(String etiketAdi) {
        this.etiketAdi = etiketAdi;
    }

    /** Etiket grubu. */
    public String getEtkGrubu() {
        return etkGrubu;
    }

    /** Etiket grubu. */
    public void setEtkGrubu(String etkGrubu) {
        this.etkGrubu = etkGrubu;
    }

    /** Alfabetik tanım. */
    public String getAlfTanimi() {
        return alfTanimi;
    }

    /** Alfabetik tanım. */
    public void setAlfTanimi(String alfTanimi) {
        this.alfTanimi = alfTanimi;
    }

    /** Etiket notu. */
    public String getEtiketNot() {
        return etiketNot;
    }

    /** Etiket notu. */
    public void setEtiketNot(String etiketNot) {
        this.etiketNot = etiketNot;
    }

    /** Açılış tarihi. */
    public LocalDate getAcilisTarihi() {
        return acilisTarihi;
    }

    /** Açılış tarihi. */
    public void setAcilisTarihi(LocalDate acilisTarihi) {
        this.acilisTarihi = acilisTarihi;
    }
}
