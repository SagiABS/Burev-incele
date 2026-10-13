package com.burevers.bur.temkul;

import com.burevers.bur.temorg.TemorgorgEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemkulrolEntity - temkulrol tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: izinlerin verildiği yetki grubunu; üst rol ile kalıtım yapılabilir tutar (Kullanıcı rolü).
 * Neden var: izinler kullanıcıya değil role verilir.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulrol")
public class TemkulrolEntity extends TemortPasifEntity {

    /** Rolün özel olduğu organizasyon (boşsa sistem rolü). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgvid")
    private TemorgorgEntity organizasyon;

    /** Üst rol (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ustvid")
    private TemkulrolEntity ust;

    /** Rol kodu. */
    @Column(name = "kodu", length = 20, nullable = false)
    private String kodu;

    /** Rol adı. */
    @Column(name = "adi", length = 100, nullable = false)
    private String adi;

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Rolün özel olduğu organizasyon (boşsa sistem rolü). */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Rolün özel olduğu organizasyon (boşsa sistem rolü). */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Üst rol (boş olabilir). */
    public TemkulrolEntity getUst() {
        return ust;
    }

    /** Üst rol (boş olabilir). */
    public void setUst(TemkulrolEntity ust) {
        this.ust = ust;
    }

    /** Rol kodu. */
    public String getKodu() {
        return kodu;
    }

    /** Rol kodu. */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Rol adı. */
    public String getAdi() {
        return adi;
    }

    /** Rol adı. */
    public void setAdi(String adi) {
        this.adi = adi;
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
