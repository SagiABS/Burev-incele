package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifVrl;
import com.burevers.bur.temort.TemortetkVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevgrpVrl - mhtlevgrp tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - grup ilişkisi; gruplar temortetk tablosundadır.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevgrp")
public class MhtlevgrpVrl extends TemortPasifVrl {

    /** Gruba atanan muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufVrl muhatap;

    /** Muhatabın dahil olduğu grup (temortetk). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupvid", nullable = false)
    private TemortetkVrl grup;

    /** Gruba atanan muhatap. */
    public MhtlevmufVrl getMuhatap() {
        return muhatap;
    }

    /** Gruba atanan muhatap. */
    public void setMuhatap(MhtlevmufVrl muhatap) {
        this.muhatap = muhatap;
    }

    /** Muhatabın dahil olduğu grup (temortetk). */
    public TemortetkVrl getGrup() {
        return grup;
    }

    /** Muhatabın dahil olduğu grup (temortetk). */
    public void setGrup(TemortetkVrl grup) {
        this.grup = grup;
    }
}
