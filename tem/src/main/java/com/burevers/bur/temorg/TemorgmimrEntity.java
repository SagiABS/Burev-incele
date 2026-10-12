package com.burevers.bur.temorg;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemorgmimrEntity - temorgmimr tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: Burevers mimari kaydı; modül, paket, klasör, tablo ve sınıfları hiyerarşik tutar (ustvid).
 * Neden var: menü kısa kodunun (siskod) ve uygulama içi yapı belgelemesinin tek kaynağı olması için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgmimr")
public class TemorgmimrEntity extends TemortPasifEntity {

    /** Üst kayıt (modülde boş). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ustvid")
    private TemorgmimrEntity ust;

    /** Aynı üst kayıt altındaki sıralama. */
    @Column(name = "sira", nullable = false)
    private Integer sira;

    /** Teknik ad (örn. temorgtes, TemorgtesView). */
    @Column(name = "adi", length = 60, nullable = false)
    private String adi;

    /** Türkçe tanım (örn. Tesis Tanımla). */
    @Column(name = "tanimi", length = 200)
    private String tanimi;

    /** Kaydın türü: MODUL, PAKET, KLASOR, TABLO, ENTITY, REPOSITORY, SERVICE, VIEW, DIALOG, CONTROLLER, DTO, VALIDATOR, EVENT, TEST. */
    @Column(name = "terim", length = 20, nullable = false)
    private String terim;

    /** Sistematik kod: modül_ebced_sıra (örn. tem_893_000); menü/arama kodudur, benzersizdir. */
    @Column(name = "siskod", length = 20, nullable = false)
    private String siskod;

    /** Üst kayıt. */
    public TemorgmimrEntity getUst() {
        return ust;
    }

    /** Üst kayıt. */
    public void setUst(TemorgmimrEntity ust) {
        this.ust = ust;
    }

    /** Sıra. */
    public Integer getSira() {
        return sira;
    }

    /** Sıra. */
    public void setSira(Integer sira) {
        this.sira = sira;
    }

    /** Teknik ad. */
    public String getAdi() {
        return adi;
    }

    /** Teknik ad. */
    public void setAdi(String adi) {
        this.adi = adi;
    }

    /** Türkçe tanım. */
    public String getTanimi() {
        return tanimi;
    }

    /** Türkçe tanım. */
    public void setTanimi(String tanimi) {
        this.tanimi = tanimi;
    }

    /** Kaydın türü. */
    public String getTerim() {
        return terim;
    }

    /** Kaydın türü. */
    public void setTerim(String terim) {
        this.terim = terim;
    }

    /** Sistematik kod. */
    public String getSiskod() {
        return siskod;
    }

    /** Sistematik kod. */
    public void setSiskod(String siskod) {
        this.siskod = siskod;
    }
}
