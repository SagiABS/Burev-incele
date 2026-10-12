package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.mhttem.MhttemrolEntity;
import com.burevers.bur.temorg.TemorgkurEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevidmEntity - mhtlevidm tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: holding içi (intercompany) kuruluşlar arası muhatap-rol ilişkisi; iki tarafın karşılıklı tanımını tek kayıtta tutar.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevidm")
public class MhtlevidmEntity extends TemortPasifEntity {

    /** Kaynak kuruluş. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurEntity kurulus;

    /** Kaynak taraftaki muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** Kaynağın bu muhataba biçtiği rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private MhttemrolEntity rol;

    /** Karşı kuruluşa kaydın otomatik eklenmesi istenir mi? */
    @Column(name = "kkuroto", nullable = false)
    private boolean karsiKurulusOto;

    /** Karşı kuruluş. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kkurvid", nullable = false)
    private TemorgkurEntity karsiKurulus;

    /** Karşı taraftaki muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kmuhatapvid", nullable = false)
    private MhtlevmufEntity karsiMuhatap;

    /** Karşı rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "krolvid", nullable = false)
    private MhttemrolEntity karsiRol;

    /** Kaynak kuruluş. */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Kaynak kuruluş. */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Kaynak taraftaki muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** Kaynak taraftaki muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }

    /** Kaynağın bu muhataba biçtiği rol. */
    public MhttemrolEntity getRol() {
        return rol;
    }

    /** Kaynağın bu muhataba biçtiği rol. */
    public void setRol(MhttemrolEntity rol) {
        this.rol = rol;
    }

    /** Karşı kuruluşa kaydın otomatik eklenmesi istenir mi? */
    public boolean isKarsiKurulusOto() {
        return karsiKurulusOto;
    }

    /** Karşı kuruluşa kaydın otomatik eklenmesi istenir mi? */
    public void setKarsiKurulusOto(boolean karsiKurulusOto) {
        this.karsiKurulusOto = karsiKurulusOto;
    }

    /** Karşı kuruluş. */
    public TemorgkurEntity getKarsiKurulus() {
        return karsiKurulus;
    }

    /** Karşı kuruluş. */
    public void setKarsiKurulus(TemorgkurEntity karsiKurulus) {
        this.karsiKurulus = karsiKurulus;
    }

    /** Karşı taraftaki muhatap. */
    public MhtlevmufEntity getKarsiMuhatap() {
        return karsiMuhatap;
    }

    /** Karşı taraftaki muhatap. */
    public void setKarsiMuhatap(MhtlevmufEntity karsiMuhatap) {
        this.karsiMuhatap = karsiMuhatap;
    }

    /** Karşı rol. */
    public MhttemrolEntity getKarsiRol() {
        return karsiRol;
    }

    /** Karşı rol. */
    public void setKarsiRol(MhttemrolEntity karsiRol) {
        this.karsiRol = karsiRol;
    }
}
