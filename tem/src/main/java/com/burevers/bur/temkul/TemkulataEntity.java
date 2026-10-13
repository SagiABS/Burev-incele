package com.burevers.bur.temkul;

import com.burevers.bur.temorg.TemorgkurEntity;
import com.burevers.bur.temorg.TemorgorgEntity;
import com.burevers.bur.temorg.TemorgtesEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * TemkulataEntity - temkulata tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: kullanıcının hangi rolü hangi kapsamda (organizasyon / kuruluş / tesis) ve hangi tarihlerde kullandığını tutar (Kullanıcı - rol ataması).
 * Neden var: aynı kullanıcı farklı kapsamda farklı rollere sahip olabilir.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulata")
public class TemkulataEntity extends TemortPasifEntity {

    /** Kullanıcı. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kullanicivid", nullable = false)
    private TemkultanEntity kullanici;

    /** Atanan rol. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private TemkulrolEntity rol;

    /** Organizasyon (zorunlu); tek başına seçilirse tüm kuruluş ve tesislerde geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgvid", nullable = false)
    private TemorgorgEntity organizasyon;

    /** Kuruluş (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurEntity kurulus;

    /** Tesis (boş olabilir; doluysa kuruluş da dolu olmalı). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesEntity tesis;

    /** Geçerlilik başlangıcı. */
    @Column(name = "gecerlibas", nullable = false)
    private LocalDate gecerliBaslangic;

    /** Geçerlilik bitişi (boşsa süresiz). */
    @Column(name = "gecerlibit")
    private LocalDate gecerliBitis;

    /** Kullanıcı. */
    public TemkultanEntity getKullanici() {
        return kullanici;
    }

    /** Kullanıcı. */
    public void setKullanici(TemkultanEntity kullanici) {
        this.kullanici = kullanici;
    }

    /** Atanan rol. */
    public TemkulrolEntity getRol() {
        return rol;
    }

    /** Atanan rol. */
    public void setRol(TemkulrolEntity rol) {
        this.rol = rol;
    }

    /** Organizasyon (zorunlu); tek başına seçilirse tüm kuruluş ve tesislerde geçerlidir. */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Organizasyon (zorunlu); tek başına seçilirse tüm kuruluş ve tesislerde geçerlidir. */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Kuruluş (boş olabilir). */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Kuruluş (boş olabilir). */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Tesis (boş olabilir; doluysa kuruluş da dolu olmalı). */
    public TemorgtesEntity getTesis() {
        return tesis;
    }

    /** Tesis (boş olabilir; doluysa kuruluş da dolu olmalı). */
    public void setTesis(TemorgtesEntity tesis) {
        this.tesis = tesis;
    }

    /** Geçerlilik başlangıcı. */
    public LocalDate getGecerliBaslangic() {
        return gecerliBaslangic;
    }

    /** Geçerlilik başlangıcı. */
    public void setGecerliBaslangic(LocalDate gecerliBaslangic) {
        this.gecerliBaslangic = gecerliBaslangic;
    }

    /** Geçerlilik bitişi (boşsa süresiz). */
    public LocalDate getGecerliBitis() {
        return gecerliBitis;
    }

    /** Geçerlilik bitişi (boşsa süresiz). */
    public void setGecerliBitis(LocalDate gecerliBitis) {
        this.gecerliBitis = gecerliBitis;
    }
}
