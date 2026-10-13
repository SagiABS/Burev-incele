package com.burevers.bur.temkul;

import com.burevers.bur.temcus.TemcusdbaEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemkulalyEntity - temkulaly tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: bir rolün bir alanı göremeyeceğini, değiştiremeyeceğini ya da maskeli göreceğini tutar (Rol bazlı alan yetkisi).
 * Neden var: yalnızca kısıtlama kaydedilir; kayıt yoksa alan düzenlenebilirdir; kısıt sunucu tarafında uygulanır.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulaly")
public class TemkulalyEntity extends TemortPasifEntity {

    /** Rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private TemkulrolEntity rol;

    /** Kısıtlanan alan (temcusdba). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dbavid", nullable = false)
    private TemcusdbaEntity alan;

    /** Erişim: GIZLI, SALTOKUNUR veya MASKELI. */
    @Column(name = "erisim", length = 10, nullable = false)
    private String erisim;

    /** MASKELI için gösterim deseni (örn. SON4). */
    @Column(name = "maske", length = 30)
    private String maske;

    /** Rol. */
    public TemkulrolEntity getRol() {
        return rol;
    }

    /** Rol. */
    public void setRol(TemkulrolEntity rol) {
        this.rol = rol;
    }

    /** Kısıtlanan alan (temcusdba). */
    public TemcusdbaEntity getAlan() {
        return alan;
    }

    /** Kısıtlanan alan (temcusdba). */
    public void setAlan(TemcusdbaEntity alan) {
        this.alan = alan;
    }

    /** Erişim: GIZLI, SALTOKUNUR veya MASKELI. */
    public String getErisim() {
        return erisim;
    }

    /** Erişim: GIZLI, SALTOKUNUR veya MASKELI. */
    public void setErisim(String erisim) {
        this.erisim = erisim;
    }

    /** MASKELI için gösterim deseni (örn. SON4). */
    public String getMaske() {
        return maske;
    }

    /** MASKELI için gösterim deseni (örn. SON4). */
    public void setMaske(String maske) {
        this.maske = maske;
    }
}
