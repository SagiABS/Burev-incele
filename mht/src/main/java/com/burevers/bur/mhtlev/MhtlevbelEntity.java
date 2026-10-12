package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * MhtlevbelEntity - mhtlevbel tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap resmi belgeleri (vergi levhası, imza sirküleri...); dosyanın yalnızca adresi tutulur.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevbel")
public class MhtlevbelEntity extends TemortPasifEntity {

    /** Belgenin ait olduğu muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** Belge türü. */
    @Column(name = "belgetipi", length = 50, nullable = false)
    private String belgeTipi;

    /** Belgenin depolama adresi/yolu. */
    @Column(name = "documenturl", length = 500, nullable = false)
    private String documentUrl;

    /** Yasal son geçerlilik tarihi (varsa). */
    @Column(name = "songecerliliktarihi")
    private LocalDate sonGecerlilikTarihi;

    /** Belgenin ait olduğu muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** Belgenin ait olduğu muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }

    /** Belge türü. */
    public String getBelgeTipi() {
        return belgeTipi;
    }

    /** Belge türü. */
    public void setBelgeTipi(String belgeTipi) {
        this.belgeTipi = belgeTipi;
    }

    /** Belgenin depolama adresi/yolu. */
    public String getDocumentUrl() {
        return documentUrl;
    }

    /** Belgenin depolama adresi/yolu. */
    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    /** Yasal son geçerlilik tarihi (varsa). */
    public LocalDate getSonGecerlilikTarihi() {
        return sonGecerlilikTarihi;
    }

    /** Yasal son geçerlilik tarihi (varsa). */
    public void setSonGecerlilikTarihi(LocalDate sonGecerlilikTarihi) {
        this.sonGecerlilikTarihi = sonGecerlilikTarihi;
    }
}
