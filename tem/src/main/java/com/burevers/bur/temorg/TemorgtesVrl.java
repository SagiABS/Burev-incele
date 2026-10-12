package com.burevers.bur.temorg;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.burevers.bur.temort.TemortPasifVrl;
import java.time.LocalDate;
/**
 * TemorgtesVrl - temorgtes tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: tesis (şube) kaydı.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgtes")
public class TemorgtesVrl extends TemortPasifVrl {

    /** Kod (3 karakter, benzersiz). */
    @Column(name = "kodu", length = 3, nullable = false)
    private String kodu;

    /** Ad. */
    @Column(name = "adi", length = 200, nullable = false)
    private String adi;

    /** Kısa ad. */
    @Column(name = "adikisa", length = 50)
    private String adiKisa;

    /** Vergi numarası. */
    @Column(name = "vergino", length = 30)
    private String vergiNo;

    /** Dönem başlangıç ayı (1-12). */
    @Column(name = "donemi")
    private Short donemi;

    /** Etiket kodu (temortetk); doğrulama servis katmanında. */
    @Column(name = "etiket", length = 4)
    private String etiket;

    /** Açılış tarihi. */
    @Column(name = "acilistarihi", nullable = false)
    private LocalDate acilisTarihi;

    /** Kod (3 karakter, benzersiz). */
    public String getKodu() {
        return kodu;
    }

    /** Kod (3 karakter, benzersiz). */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Ad. */
    public String getAdi() {
        return adi;
    }

    /** Ad. */
    public void setAdi(String adi) {
        this.adi = adi;
    }

    /** Kısa ad. */
    public String getAdiKisa() {
        return adiKisa;
    }

    /** Kısa ad. */
    public void setAdiKisa(String adiKisa) {
        this.adiKisa = adiKisa;
    }

    /** Vergi numarası. */
    public String getVergiNo() {
        return vergiNo;
    }

    /** Vergi numarası. */
    public void setVergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
    }

    /** Dönem başlangıç ayı (1-12). */
    public Short getDonemi() {
        return donemi;
    }

    /** Dönem başlangıç ayı (1-12). */
    public void setDonemi(Short donemi) {
        this.donemi = donemi;
    }

    /** Etiket kodu (temortetk); doğrulama servis katmanında. */
    public String getEtiket() {
        return etiket;
    }

    /** Etiket kodu (temortetk); doğrulama servis katmanında. */
    public void setEtiket(String etiket) {
        this.etiket = etiket;
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
