package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.temort.TemortadrEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevadrEntity - mhtlevadr tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - adres ilişkisi; bir adres birden çok muhatapça ortak kullanılabilir.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevadr")
public class MhtlevadrEntity extends TemortPasifEntity {

    /** Adresin bağlandığı muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** Muhataba atanan adres (temortadr). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adresvid", nullable = false)
    private TemortadrEntity adres;

    /** Adresin bağlandığı muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** Adresin bağlandığı muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }

    /** Muhataba atanan adres (temortadr). */
    public TemortadrEntity getAdres() {
        return adres;
    }

    /** Muhataba atanan adres (temortadr). */
    public void setAdres(TemortadrEntity adres) {
        this.adres = adres;
    }
}
