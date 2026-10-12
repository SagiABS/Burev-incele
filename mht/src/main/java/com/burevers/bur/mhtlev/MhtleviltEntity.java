package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtleviltEntity - mhtlevilt tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap iletişim bilgileri (telefon, e-posta, KEP, web) ve ilgili kişi.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevilt")
public class MhtleviltEntity extends TemortPasifEntity {

    /** İletişimin ait olduğu muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufEntity muhatap;

    /** İletişim kişisi (ad alanı dolu muhatap); boş olabilir. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iletisimmhtvid")
    private MhtlevmufEntity iletisimMuhatap;

    /** İletişim türü: TELEFON, EPOSTA, KEP, WEB. */
    @Column(name = "iletisimtipi", length = 20, nullable = false)
    private String iletisimTipi;

    /** İletişim verisi (numara, adres...). */
    @Column(name = "iletisimbilgisi", length = 150, nullable = false)
    private String iletisimBilgisi;

    /** İlgili departman / yetkili adı (serbest metin). */
    @Column(name = "ilgilikisi", length = 100)
    private String ilgiliKisi;

    /** İletişimin ait olduğu muhatap. */
    public MhtlevmufEntity getMuhatap() {
        return muhatap;
    }

    /** İletişimin ait olduğu muhatap. */
    public void setMuhatap(MhtlevmufEntity muhatap) {
        this.muhatap = muhatap;
    }

    /** İletişim kişisi (ad alanı dolu muhatap); boş olabilir. */
    public MhtlevmufEntity getIletisimMuhatap() {
        return iletisimMuhatap;
    }

    /** İletişim kişisi (ad alanı dolu muhatap); boş olabilir. */
    public void setIletisimMuhatap(MhtlevmufEntity iletisimMuhatap) {
        this.iletisimMuhatap = iletisimMuhatap;
    }

    /** İletişim türü: TELEFON, EPOSTA, KEP, WEB. */
    public String getIletisimTipi() {
        return iletisimTipi;
    }

    /** İletişim türü: TELEFON, EPOSTA, KEP, WEB. */
    public void setIletisimTipi(String iletisimTipi) {
        this.iletisimTipi = iletisimTipi;
    }

    /** İletişim verisi (numara, adres...). */
    public String getIletisimBilgisi() {
        return iletisimBilgisi;
    }

    /** İletişim verisi (numara, adres...). */
    public void setIletisimBilgisi(String iletisimBilgisi) {
        this.iletisimBilgisi = iletisimBilgisi;
    }

    /** İlgili departman / yetkili adı (serbest metin). */
    public String getIlgiliKisi() {
        return ilgiliKisi;
    }

    /** İlgili departman / yetkili adı (serbest metin). */
    public void setIlgiliKisi(String ilgiliKisi) {
        this.ilgiliKisi = ilgiliKisi;
    }
}
