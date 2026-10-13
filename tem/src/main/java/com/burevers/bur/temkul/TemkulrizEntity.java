package com.burevers.bur.temkul;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemkulrizEntity - temkulriz tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: bir role hangi iznin verildiğini ya da yasaklandığını tutar (Rol - izin ilişkisi).
 * Neden var: varsayılan yasaktır; yasak işaretli kayıt her zaman kazanır.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulriz")
public class TemkulrizEntity extends TemortPasifEntity {

    /** Rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private TemkulrolEntity rol;

    /** İzin. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "izinvid", nullable = false)
    private TemkuliznEntity izin;

    /** Doğruysa izin açıkça yasaklanır. */
    @Column(name = "yasak", nullable = false)
    private boolean yasak = false;

    /** Rol. */
    public TemkulrolEntity getRol() {
        return rol;
    }

    /** Rol. */
    public void setRol(TemkulrolEntity rol) {
        this.rol = rol;
    }

    /** İzin. */
    public TemkuliznEntity getIzin() {
        return izin;
    }

    /** İzin. */
    public void setIzin(TemkuliznEntity izin) {
        this.izin = izin;
    }

    /** Doğruysa izin açıkça yasaklanır. */
    public boolean isYasak() {
        return yasak;
    }

    /** Doğruysa izin açıkça yasaklanır. */
    public void setYasak(boolean yasak) {
        this.yasak = yasak;
    }
}
