package com.burevers.bur.temorg;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemorgsozlEntity - temorgsozl (Temel Sözlük) tablosunun JPA sınıfı.
 * Ne işe yarar: sistemde kullanılan özel kod, terim ve tanımları (örn. tem = Temel) tutar.
 * Neden var: kısa kodların anlamı tek yerde, uygulama içinde okunabilsin diye.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgsozl")
public class TemorgsozlEntity extends TemortPasifEntity {

    /** Kod ya da terim (örn. tem, siskod, Entity); benzersizdir. */
    @Column(name = "terim", length = 30, nullable = false)
    private String terim;

    /** Kısa tanım (örn. Temel). */
    @Column(name = "tanimi", length = 100, nullable = false)
    private String tanimi;

    /** Ayrıntılı açıklama. */
    @Column(name = "aciklamasi", length = 1000)
    private String aciklamasi;

    /** Kod ya da terim. */
    public String getTerim() {
        return terim;
    }

    /** Kod ya da terim. */
    public void setTerim(String terim) {
        this.terim = terim;
    }

    /** Kısa tanım. */
    public String getTanimi() {
        return tanimi;
    }

    /** Kısa tanım. */
    public void setTanimi(String tanimi) {
        this.tanimi = tanimi;
    }

    /** Ayrıntılı açıklama. */
    public String getAciklamasi() {
        return aciklamasi;
    }

    /** Ayrıntılı açıklama. */
    public void setAciklamasi(String aciklamasi) {
        this.aciklamasi = aciklamasi;
    }
}
