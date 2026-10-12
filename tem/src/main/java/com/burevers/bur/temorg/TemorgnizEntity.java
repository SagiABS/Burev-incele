package com.burevers.bur.temorg;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * TemorgnizEntity - temorgniz tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: organizasyon - kuruluş - tesis bağlantısını tutar (yapı ağacı).
 * Neden var: hangi tesisin hangi kuruluşa, hangi kuruluşun hangi organizasyona bağlı olduğunu bilmek için.
 * Kurallar veritabanında da korunur: aktif bir tesis tek kuruluşa, aktif bir kuruluş tek organizasyona bağlıdır.
 * İlişkiler LAZY: ihtiyaç olunca yüklenir (gereksiz sorgu atılmaz).
 */
@Entity
@Table(name = "temorgniz")
public class TemorgnizEntity extends TemortPasifEntity {

    /** Bağlı organizasyon (orgvid). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgvid", nullable = false)
    private TemorgorgEntity organizasyon;

    /** Bağlı kuruluş (kurvid). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurEntity kurulus;

    /** Bağlı tesis (tesvid). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tesvid", nullable = false)
    private TemorgtesEntity tesis;

    /** Etiket kodu (temortetk); doğrulama servis katmanında. */
    @Column(name = "etiket", length = 4)
    private String etiket;

    /** Açılış tarihi. */
    @Column(name = "acilistarihi", nullable = false)
    private LocalDate acilisTarihi;

    /** Bağlı organizasyon. */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Bağlı organizasyon. */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Bağlı kuruluş. */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Bağlı kuruluş. */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Bağlı tesis. */
    public TemorgtesEntity getTesis() {
        return tesis;
    }

    /** Bağlı tesis. */
    public void setTesis(TemorgtesEntity tesis) {
        this.tesis = tesis;
    }

    /** Etiket kodu. */
    public String getEtiket() {
        return etiket;
    }

    /** Etiket kodu. */
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
