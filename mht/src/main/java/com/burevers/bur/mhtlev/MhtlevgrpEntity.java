package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.temort.TemortetkEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevgrpEntity - mhtlevgrp tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - grup ilişkisi; gruplar temortetk tablosundadır.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevgrp")
public class MhtlevgrpEntity extends TemortPasifEntity {

    /** Gruba atanan muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** Muhatabın dahil olduğu grup (temortetk). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupvid", nullable = false)
    private TemortetkEntity grup;

    /** Gruba atanan muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** Gruba atanan muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }

    /** Muhatabın dahil olduğu grup (temortetk). */
    public TemortetkEntity getGrup() {
        return grup;
    }

    /** Muhatabın dahil olduğu grup (temortetk). */
    public void setGrup(TemortetkEntity grup) {
        this.grup = grup;
    }
}
