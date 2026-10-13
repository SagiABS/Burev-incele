package com.burevers.bur.temkul;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemkultanEntity - temkultan tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: uygulamayı kullanan kişi ya da servis hesabının kimlik bilgisini (kod, ad, unvan, e-posta, tip, dil) tutar (Kullanıcı tanımı).
 * Neden var: kullanıcı kimliği yetkiden ayrı tutulur; kullanıcı kiracıdan bağımsızdır.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkultan")
public class TemkultanEntity extends TemortPasifEntity {

    /** Kullanıcı kodu (giriş adı); büyük/küçük harf duyarsız benzersizdir. */
    @Column(name = "kodu", length = 30, nullable = false)
    private String kodu;

    /** Ad soyad. */
    @Column(name = "adi", length = 100, nullable = false)
    private String adi;

    /** Unvan. */
    @Column(name = "unvani", length = 100)
    private String unvani;

    /** E-posta adresi (doluysa benzersizdir). */
    @Column(name = "epostasi", length = 150)
    private String epostasi;

    /** Tip: IC (çalışan), DIS (dış kullanıcı), SERVIS (servis hesabı). */
    @Column(name = "tipi", length = 10, nullable = false)
    private String tipi = "IC";

    /** Arayüz dili (örn. tr, en). */
    @Column(name = "dili", length = 5)
    private String dili;

    /** Kullanıcı kodu (giriş adı); büyük/küçük harf duyarsız benzersizdir. */
    public String getKodu() {
        return kodu;
    }

    /** Kullanıcı kodu (giriş adı); büyük/küçük harf duyarsız benzersizdir. */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Ad soyad. */
    public String getAdi() {
        return adi;
    }

    /** Ad soyad. */
    public void setAdi(String adi) {
        this.adi = adi;
    }

    /** Unvan. */
    public String getUnvani() {
        return unvani;
    }

    /** Unvan. */
    public void setUnvani(String unvani) {
        this.unvani = unvani;
    }

    /** E-posta adresi (doluysa benzersizdir). */
    public String getEpostasi() {
        return epostasi;
    }

    /** E-posta adresi (doluysa benzersizdir). */
    public void setEpostasi(String epostasi) {
        this.epostasi = epostasi;
    }

    /** Tip: IC (çalışan), DIS (dış kullanıcı), SERVIS (servis hesabı). */
    public String getTipi() {
        return tipi;
    }

    /** Tip: IC (çalışan), DIS (dış kullanıcı), SERVIS (servis hesabı). */
    public void setTipi(String tipi) {
        this.tipi = tipi;
    }

    /** Arayüz dili (örn. tr, en). */
    public String getDili() {
        return dili;
    }

    /** Arayüz dili (örn. tr, en). */
    public void setDili(String dili) {
        this.dili = dili;
    }
}
