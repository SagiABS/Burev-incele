package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemortktrEntity - temortktr tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: şirket (muhatap) türü: kısa kod, ad ve vergi no zorunluluğu.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortktr")
public class TemortktrEntity extends TemortPasifEntity {

    /** Tür kısa kodu (en çok 5 karakter, benzersiz). */
    @Column(name = "turkodu", length = 5, nullable = false)
    private String turKodu;

    /** Tür adı. */
    @Column(name = "turadi", length = 100, nullable = false)
    private String turAdi;

    /** Vergi no girilmesi zorunlu mu? Muhatap formundaki doğrulamayı yönlendirir. */
    @Column(name = "vknzorunlu", nullable = false)
    private boolean vknZorunlu;

    /** Tür kısa kodu (en çok 5 karakter, benzersiz). */
    public String getTurKodu() {
        return turKodu;
    }

    /** Tür kısa kodu (en çok 5 karakter, benzersiz). */
    public void setTurKodu(String turKodu) {
        this.turKodu = turKodu;
    }

    /** Tür adı. */
    public String getTurAdi() {
        return turAdi;
    }

    /** Tür adı. */
    public void setTurAdi(String turAdi) {
        this.turAdi = turAdi;
    }

    /** Vergi no girilmesi zorunlu mu? Muhatap formundaki doğrulamayı yönlendirir. */
    public boolean isVknZorunlu() {
        return vknZorunlu;
    }

    /** Vergi no girilmesi zorunlu mu? Muhatap formundaki doğrulamayı yönlendirir. */
    public void setVknZorunlu(boolean vknZorunlu) {
        this.vknZorunlu = vknZorunlu;
    }
}
