package com.burevers.bur.temdil;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemdilcvrEntity - temdilcvr tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: bir anahtarın bir dildeki çevrilmiş metnini tutar (Proje dil çevirisi). Kapsam (organizasyon,
 * kuruluş, tesis) boşsa sistem metnidir; doluysa o kapsama özel ezme metnidir.
 * Neden var: alan etiketi, düğme, menü ve mesaj metinleri koda gömülü olmasın, dil ve kiracı bazında değiştirilebilsin.
 * Not: kapsam alanları düz Long tutulur (tembel ilişki yüklemesi olmadan hızlı süzülsün diye).
 */
@Entity
@Table(name = "temdilcvr")
public class TemdilcvrEntity extends TemortPasifEntity {

    /** Metin anahtarı, örn. alan.temorgorg.kodu, dugme.kaydet. */
    @Column(name = "anahtar", length = 100, nullable = false)
    private String anahtar;

    /** Metnin dili. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dilvid", nullable = false)
    private TemdilturEntity dil;

    /** Ezmenin geçerli olduğu organizasyon (boşsa sistem metni). */
    @Column(name = "orgvid")
    private Long organizasyonVid;

    /** Kuruluş (boş olabilir). */
    @Column(name = "kurvid")
    private Long kurulusVid;

    /** Tesis (boş olabilir). */
    @Column(name = "tesvid")
    private Long tesisVid;

    /** Çevrilmiş metin. */
    @Column(name = "metin", length = 500, nullable = false)
    private String metin;

    /** Çevirmen notu. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Metin anahtarı. */
    public String getAnahtar() {
        return anahtar;
    }

    /** Metin anahtarı. */
    public void setAnahtar(String anahtar) {
        this.anahtar = anahtar;
    }

    /** Metnin dili. */
    public TemdilturEntity getDil() {
        return dil;
    }

    /** Metnin dili. */
    public void setDil(TemdilturEntity dil) {
        this.dil = dil;
    }

    /** Organizasyon vid'i (boşsa sistem metni). */
    public Long getOrganizasyonVid() {
        return organizasyonVid;
    }

    /** Organizasyon vid'i. */
    public void setOrganizasyonVid(Long organizasyonVid) {
        this.organizasyonVid = organizasyonVid;
    }

    /** Kuruluş vid'i. */
    public Long getKurulusVid() {
        return kurulusVid;
    }

    /** Kuruluş vid'i. */
    public void setKurulusVid(Long kurulusVid) {
        this.kurulusVid = kurulusVid;
    }

    /** Tesis vid'i. */
    public Long getTesisVid() {
        return tesisVid;
    }

    /** Tesis vid'i. */
    public void setTesisVid(Long tesisVid) {
        this.tesisVid = tesisVid;
    }

    /** Çevrilmiş metin. */
    public String getMetin() {
        return metin;
    }

    /** Çevrilmiş metin. */
    public void setMetin(String metin) {
        this.metin = metin;
    }

    /** Çevirmen notu. */
    public String getAciklama() {
        return aciklama;
    }

    /** Çevirmen notu. */
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
