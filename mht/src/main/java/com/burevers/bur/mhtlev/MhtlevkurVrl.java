package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifVrl;
import com.burevers.bur.temorg.TemorgkurVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevkurVrl - mhtlevkur tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - kuruluş ataması; muhatabın hangi kuruluşlarda kullanılabildiğini belirler.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevkur")
public class MhtlevkurVrl extends TemortPasifVrl {

    /** Muhatabın atandığı kuruluş (temorgkur). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurVrl kurulus;

    /** Kuruluşa atanan muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufVrl muhatap;

    /** Muhatabın atandığı kuruluş (temorgkur). */
    public TemorgkurVrl getKurulus() {
        return kurulus;
    }

    /** Muhatabın atandığı kuruluş (temorgkur). */
    public void setKurulus(TemorgkurVrl kurulus) {
        this.kurulus = kurulus;
    }

    /** Kuruluşa atanan muhatap. */
    public MhtlevmufVrl getMuhatap() {
        return muhatap;
    }

    /** Kuruluşa atanan muhatap. */
    public void setMuhatap(MhtlevmufVrl muhatap) {
        this.muhatap = muhatap;
    }
}
