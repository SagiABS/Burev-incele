package com.burevers.bur.temkul;

import com.burevers.bur.temorg.TemorgkurEntity;
import com.burevers.bur.temorg.TemorgorgEntity;
import com.burevers.bur.temorg.TemorgtesEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import com.burevers.bur.temort.TemortprbEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * TemkulvklEntity - temkulvkl tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: onay yetkisinin tarih aralığıyla, sıra ve tutar limitine göre bir ya da birkaç kişiye devrini tutar (Kullanıcı vekâleti).
 * Neden var: izinli ya da yokken onay süreçlerinin durmaması için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulvkl")
public class TemkulvklEntity extends TemortPasifEntity {

    /** Vekâlet veren kullanıcı. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "verenvid", nullable = false)
    private TemkultanEntity veren;

    /** Vekil kullanıcı. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vekilvid", nullable = false)
    private TemkultanEntity vekil;

    /** Organizasyon (zorunlu). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgvid", nullable = false)
    private TemorgorgEntity organizasyon;

    /** Kuruluş (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurEntity kurulus;

    /** Tesis (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesEntity tesis;

    /** Vekil sırası (1 = ilk başvurulan). */
    @Column(name = "sira", nullable = false)
    private Short sira;

    /** Vekilin onaylayabileceği en yüksek tutar (boşsa limitsiz). */
    @Column(name = "tutarlimiti", precision = 19, scale = 4)
    private BigDecimal tutarLimiti;

    /** Limitin para birimi (limit doluysa zorunlu). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prbvid")
    private TemortprbEntity paraBirimi;

    /** Geçerlilik başlangıcı. */
    @Column(name = "gecerlibas", nullable = false)
    private LocalDate gecerliBaslangic;

    /** Geçerlilik bitişi. */
    @Column(name = "gecerlibit", nullable = false)
    private LocalDate gecerliBitis;

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Vekâlet veren kullanıcı. */
    public TemkultanEntity getVeren() {
        return veren;
    }

    /** Vekâlet veren kullanıcı. */
    public void setVeren(TemkultanEntity veren) {
        this.veren = veren;
    }

    /** Vekil kullanıcı. */
    public TemkultanEntity getVekil() {
        return vekil;
    }

    /** Vekil kullanıcı. */
    public void setVekil(TemkultanEntity vekil) {
        this.vekil = vekil;
    }

    /** Organizasyon (zorunlu). */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Organizasyon (zorunlu). */
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

    /** Tesis (boş olabilir). */
    public TemorgtesEntity getTesis() {
        return tesis;
    }

    /** Tesis (boş olabilir). */
    public void setTesis(TemorgtesEntity tesis) {
        this.tesis = tesis;
    }

    /** Vekil sırası (1 = ilk başvurulan). */
    public Short getSira() {
        return sira;
    }

    /** Vekil sırası (1 = ilk başvurulan). */
    public void setSira(Short sira) {
        this.sira = sira;
    }

    /** Vekilin onaylayabileceği en yüksek tutar (boşsa limitsiz). */
    public BigDecimal getTutarLimiti() {
        return tutarLimiti;
    }

    /** Vekilin onaylayabileceği en yüksek tutar (boşsa limitsiz). */
    public void setTutarLimiti(BigDecimal tutarLimiti) {
        this.tutarLimiti = tutarLimiti;
    }

    /** Limitin para birimi (limit doluysa zorunlu). */
    public TemortprbEntity getParaBirimi() {
        return paraBirimi;
    }

    /** Limitin para birimi (limit doluysa zorunlu). */
    public void setParaBirimi(TemortprbEntity paraBirimi) {
        this.paraBirimi = paraBirimi;
    }

    /** Geçerlilik başlangıcı. */
    public LocalDate getGecerliBaslangic() {
        return gecerliBaslangic;
    }

    /** Geçerlilik başlangıcı. */
    public void setGecerliBaslangic(LocalDate gecerliBaslangic) {
        this.gecerliBaslangic = gecerliBaslangic;
    }

    /** Geçerlilik bitişi. */
    public LocalDate getGecerliBitis() {
        return gecerliBitis;
    }

    /** Geçerlilik bitişi. */
    public void setGecerliBitis(LocalDate gecerliBitis) {
        this.gecerliBitis = gecerliBitis;
    }

    /** Açıklama. */
    public String getAciklama() {
        return aciklama;
    }

    /** Açıklama. */
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
