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

    /** Sıralama kodu (örn. 201010). */
    @Column(name = "sira", nullable = false)
    private Integer sira;

    /** Ana bölüm: modül kodu (örn. mht). */
    @Column(name = "ana", length = 3)
    private String ana;

    /** Tali bölüm: paket kodunun grup bölümü (örn. lev). */
    @Column(name = "tali", length = 3)
    private String tali;

    /** Detay bölüm: tablo adı bölümü (örn. muf, blok). */
    @Column(name = "detay", length = 4)
    private String detay;

    /** Teknik ad (örn. temorgtes, TemorgtesView). */
    @Column(name = "adi", length = 60, nullable = false)
    private String adi;

    /** Türkçe tanım (örn. Tesis Tanımla). */
    @Column(name = "tanimi", length = 200)
    private String tanimi;

    /** Kaydın ne işe yaradığı ve hakkında önemli bilgi açıklamaları. */
    @Column(name = "islevi", length = 1000)
    private String islevi;

    /** Kaydın türü: MODUL, PAKET, KLASOR, TABLO, ENTITY, REPOSITORY, SERVICE, VIEW, DIALOG, CONTROLLER, DTO, VALIDATOR, EVENT, TEST. */
    @Column(name = "terim", length = 20, nullable = false)
    private String terim;

    /** Sistematik kod: modül_ebced_sıra (örn. tem_893_001); menü/arama kodudur, benzersizdir; modül ve paket kayıtlarında boştur, tablo ve sınıf kayıtlarında zorunludur. */
    @Column(name = "siskod", length = 20)
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

    /** Ana bölüm (modül kodu). */
    public String getAna() {
        return ana;
    }

    /** Ana bölüm (modül kodu). */
    public void setAna(String ana) {
        this.ana = ana;
    }

    /** Tali bölüm (paket grubu). */
    public String getTali() {
        return tali;
    }

    /** Tali bölüm (paket grubu). */
    public void setTali(String tali) {
        this.tali = tali;
    }

    /** Detay bölüm (tablo adı bölümü). */
    public String getDetay() {
        return detay;
    }

    /** Detay bölüm (tablo adı bölümü). */
    public void setDetay(String detay) {
        this.detay = detay;
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

    /** İşlev ve önemli bilgi açıklamaları. */
    public String getIslevi() {
        return islevi;
    }

    /** İşlev ve önemli bilgi açıklamaları. */
    public void setIslevi(String islevi) {
        this.islevi = islevi;
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
