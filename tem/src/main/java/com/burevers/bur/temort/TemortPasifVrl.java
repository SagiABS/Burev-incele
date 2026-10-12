package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

/**
 * TemortPasifVrl - pasif yapılabilen tabloların ortak üst sınıfı.
 * Ne işe yarar: pasif, pasif tarihi ve pasif nedeni alanlarını taşır.
 * Neden var: kayıtlar silinmez, pasif yapılır; veritabanındaki tutarlılık kuralı
 * (pasifse tarih ve neden dolu, aktifse ikisi de boş) burada metotlarla korunur.
 */
@MappedSuperclass
public abstract class TemortPasifVrl extends TemortTemelVrl {

    /** Kayıt pasif mi? */
    @Column(name = "pasif", nullable = false)
    private boolean pasif;

    /** Pasif yapıldığı tarih (aktifse boş). */
    @Column(name = "pasiftarihi")
    private LocalDate pasifTarihi;

    /** Pasif nedeni kodu (aktifse boş). Neden tablosu sonra eklenecek. */
    @Column(name = "pasifnedeni", length = 10)
    private String pasifNedeni;

    /** Kaydı pasif yapar; üç alanı birlikte doldurur (veritabanı kuralı ile uyumlu). */
    public void pasifYap(LocalDate tarih, String neden) {
        if (tarih == null || neden == null || neden.isBlank()) {
            throw new IllegalArgumentException("Pasif yapmak için tarih ve neden zorunludur");
        }
        this.pasif = true;
        this.pasifTarihi = tarih;
        this.pasifNedeni = neden;
    }

    /** Kaydı yeniden aktif yapar; pasif tarihi ve nedeni temizlenir. */
    public void aktifYap() {
        this.pasif = false;
        this.pasifTarihi = null;
        this.pasifNedeni = null;
    }

    /** Kayıt pasif mi? */
    public boolean isPasif() {
        return pasif;
    }

    /** Pasif yapıldığı tarih. */
    public LocalDate getPasifTarihi() {
        return pasifTarihi;
    }

    /** Pasif nedeni kodu. */
    public String getPasifNedeni() {
        return pasifNedeni;
    }
}
