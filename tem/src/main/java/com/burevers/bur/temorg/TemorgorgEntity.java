package com.burevers.bur.temorg;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.burevers.bur.temort.TemortPasifEntity;
import java.time.LocalDate;
/**
 * TemorgorgEntity - temorgorg tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: organizasyon (holding) kaydı; kiracı birimidir.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgorg")
public class TemorgorgEntity extends TemortPasifEntity {

    /** Kod (3 karakter, benzersiz). */
    @Column(name = "kodu", length = 3, nullable = false)
    private String kodu;

    /** Ad. */
    @Column(name = "adi", length = 200, nullable = false)
    private String adi;

    /** Açılış tarihi. */
    @Column(name = "acilistarihi", nullable = false)
    private LocalDate acilisTarihi;

    /** Kod (3 karakter, benzersiz). */
    public String getKodu() {
        return kodu;
    }

    /** Kod (3 karakter, benzersiz). */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Ad. */
    public String getAdi() {
        return adi;
    }

    /** Ad. */
    public void setAdi(String adi) {
        this.adi = adi;
    }

    /** Açılış tarihi. */
    public LocalDate getAcilisTarihi() {
        return acilisTarihi;
    }

    /** Açılış tarihi. */
    public void setAcilisTarihi(LocalDate acilisTarihi) {
        this.acilisTarihi = acilisTarihi;
    }
}
