package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.temorg.TemorgkurEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevkurEntity - mhtlevkur tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap - kuruluş ataması; muhatabın hangi kuruluşlarda kullanılabildiğini belirler.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevkur")
public class MhtlevkurEntity extends TemortPasifEntity {

    /** Muhatabın atandığı kuruluş (temorgkur). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurEntity kurulus;

    /** Kuruluşa atanan muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** Muhatabın atandığı kuruluş (temorgkur). */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Muhatabın atandığı kuruluş (temorgkur). */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Kuruluşa atanan muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** Kuruluşa atanan muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }
}
