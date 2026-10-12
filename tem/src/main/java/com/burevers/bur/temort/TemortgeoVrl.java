package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemortgeoVrl - temortgeo tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: coğrafi lokasyon ağacı (ülke, bölge, şehir, ilçe, mahalle); üst lokasyona kendi tablosundan bağlanır.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortgeo")
public class TemortgeoVrl extends TemortPasifVrl {

    /** Bir üst lokasyon (ülkede boş). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentvid")
    private TemortgeoVrl ust;

    /** Kademe: ULKE, BOLGE, SEHIR, ILCE, MAHALLE. */
    @Column(name = "lokasyontipi", length = 20, nullable = false)
    private String lokasyonTipi;

    /** Lokasyon adı. */
    @Column(name = "lokasyonadi", length = 150, nullable = false)
    private String lokasyonAdi;

    /** ISO 3166 kodu (örn. TR, TR-34); varsa benzersiz. */
    @Column(name = "isokodu", length = 10)
    private String isoKodu;

    /** Bir üst lokasyon (ülkede boş). */
    public TemortgeoVrl getUst() {
        return ust;
    }

    /** Bir üst lokasyon (ülkede boş). */
    public void setUst(TemortgeoVrl ust) {
        this.ust = ust;
    }

    /** Kademe: ULKE, BOLGE, SEHIR, ILCE, MAHALLE. */
    public String getLokasyonTipi() {
        return lokasyonTipi;
    }

    /** Kademe: ULKE, BOLGE, SEHIR, ILCE, MAHALLE. */
    public void setLokasyonTipi(String lokasyonTipi) {
        this.lokasyonTipi = lokasyonTipi;
    }

    /** Lokasyon adı. */
    public String getLokasyonAdi() {
        return lokasyonAdi;
    }

    /** Lokasyon adı. */
    public void setLokasyonAdi(String lokasyonAdi) {
        this.lokasyonAdi = lokasyonAdi;
    }

    /** ISO 3166 kodu (örn. TR, TR-34); varsa benzersiz. */
    public String getIsoKodu() {
        return isoKodu;
    }

    /** ISO 3166 kodu (örn. TR, TR-34); varsa benzersiz. */
    public void setIsoKodu(String isoKodu) {
        this.isoKodu = isoKodu;
    }
}
