package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifVrl;
import com.burevers.bur.mhttem.MhttemrolVrl;
import com.burevers.bur.temorg.TemorgkurVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevidmVrl - mhtlevidm tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: holding içi (intercompany) kuruluşlar arası muhatap-rol ilişkisi; iki tarafın karşılıklı tanımını tek kayıtta tutar.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevidm")
public class MhtlevidmVrl extends TemortPasifVrl {

    /** Kaynak kuruluş. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurVrl kurulus;

    /** Kaynak taraftaki muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufVrl muhatap;

    /** Kaynağın bu muhataba biçtiği rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private MhttemrolVrl rol;

    /** Karşı kuruluşa kaydın otomatik eklenmesi istenir mi? */
    @Column(name = "kkuroto", nullable = false)
    private boolean karsiKurulusOto;

    /** Karşı kuruluş. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kkurvid", nullable = false)
    private TemorgkurVrl karsiKurulus;

    /** Karşı taraftaki muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kmuhatapvid", nullable = false)
    private MhtlevmufVrl karsiMuhatap;

    /** Karşı rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "krolvid", nullable = false)
    private MhttemrolVrl karsiRol;

    /** Kaynak kuruluş. */
    public TemorgkurVrl getKurulus() {
        return kurulus;
    }

    /** Kaynak kuruluş. */
    public void setKurulus(TemorgkurVrl kurulus) {
        this.kurulus = kurulus;
    }

    /** Kaynak taraftaki muhatap. */
    public MhtlevmufVrl getMuhatap() {
        return muhatap;
    }

    /** Kaynak taraftaki muhatap. */
    public void setMuhatap(MhtlevmufVrl muhatap) {
        this.muhatap = muhatap;
    }

    /** Kaynağın bu muhataba biçtiği rol. */
    public MhttemrolVrl getRol() {
        return rol;
    }

    /** Kaynağın bu muhataba biçtiği rol. */
    public void setRol(MhttemrolVrl rol) {
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
    public TemorgkurVrl getKarsiKurulus() {
        return karsiKurulus;
    }

    /** Karşı kuruluş. */
    public void setKarsiKurulus(TemorgkurVrl karsiKurulus) {
        this.karsiKurulus = karsiKurulus;
    }

    /** Karşı taraftaki muhatap. */
    public MhtlevmufVrl getKarsiMuhatap() {
        return karsiMuhatap;
    }

    /** Karşı taraftaki muhatap. */
    public void setKarsiMuhatap(MhtlevmufVrl karsiMuhatap) {
        this.karsiMuhatap = karsiMuhatap;
    }

    /** Karşı rol. */
    public MhttemrolVrl getKarsiRol() {
        return karsiRol;
    }

    /** Karşı rol. */
    public void setKarsiRol(MhttemrolVrl karsiRol) {
        this.karsiRol = karsiRol;
    }
}
