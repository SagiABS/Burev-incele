package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifVrl;
import com.burevers.bur.temort.TemortadrVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevmfaVrl - mhtlevmfa tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - adres ilişkisi; bir adres birden çok muhatapça ortak kullanılabilir.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevmfa")
public class MhtlevmfaVrl extends TemortPasifVrl {

    /** Adresin bağlandığı muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufVrl muhatap;

    /** Muhataba atanan adres (temortadr). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adresvid", nullable = false)
    private TemortadrVrl adres;

    /** Adresin bağlandığı muhatap. */
    public MhtlevmufVrl getMuhatap() {
        return muhatap;
    }

    /** Adresin bağlandığı muhatap. */
    public void setMuhatap(MhtlevmufVrl muhatap) {
        this.muhatap = muhatap;
    }

    /** Muhataba atanan adres (temortadr). */
    public TemortadrVrl getAdres() {
        return adres;
    }

    /** Muhataba atanan adres (temortadr). */
    public void setAdres(TemortadrVrl adres) {
        this.adres = adres;
    }
}
