package com.burevers.bur.temekr;

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

/**
 * TemekrtanEntity - temekrtan tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: bir ekranın siskod ve varyantını, şablonunu, ana tablosunu, kapsamını ve sürümünü tutar (Ekran tanımı).
 * Neden var: genel ekran iskeleti ekranları veriyle tanımlanır (esnek tasarım).
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temekrtan")
public class TemekrtanEntity extends TemortPasifEntity {

    /** Ekranı açan menü adımının siskod değeri. */
    @Column(name = "siskod", length = 20, nullable = false)
    private String siskod;

    /** Varyant kodu (örn. ANA, GIRIS2); aynı siskod için birden çok ekran olabilir. */
    @Column(name = "varyant", length = 10, nullable = false)
    private String varyant = "ANA";

    /** Ekran başlığı. */
    @Column(name = "baslik", length = 100, nullable = false)
    private String baslik;

    /** Şablon: LISTE (liste ekranı) veya BELGE (belge giriş ekranı). */
    @Column(name = "sablon", length = 10, nullable = false)
    private String sablon;

    /** Ekranın ana tablosu. */
    @Column(name = "tablokodu", length = 10, nullable = false)
    private String tabloKodu;

    /** Varyant seçim koşulu: alan (örn. belgeturu). */
    @Column(name = "secimalani", length = 50)
    private String secimAlani;

    /** Varyant seçim koşulu: değer (örn. FATURA). */
    @Column(name = "secimdegeri", length = 30)
    private String secimDegeri;

    /** Düzenin geçerli olduğu organizasyon (boşsa sistem varsayılanı). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgvid")
    private TemorgorgEntity organizasyon;

    /** Kuruluş (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurEntity kurulus;

    /** Tesis (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesEntity tesis;

    /** Düzen sürümü. */
    @Column(name = "surum", nullable = false)
    private Integer surum = 1;

    /** Durum: TASLAK veya YAYIN (kapsam başına tek yayın). */
    @Column(name = "durum", length = 10, nullable = false)
    private String durum = "TASLAK";

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Ekranı açan menü adımının siskod değeri. */
    public String getSiskod() {
        return siskod;
    }

    /** Ekranı açan menü adımının siskod değeri. */
    public void setSiskod(String siskod) {
        this.siskod = siskod;
    }

    /** Varyant kodu (örn. ANA, GIRIS2); aynı siskod için birden çok ekran olabilir. */
    public String getVaryant() {
        return varyant;
    }

    /** Varyant kodu (örn. ANA, GIRIS2); aynı siskod için birden çok ekran olabilir. */
    public void setVaryant(String varyant) {
        this.varyant = varyant;
    }

    /** Ekran başlığı. */
    public String getBaslik() {
        return baslik;
    }

    /** Ekran başlığı. */
    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    /** Şablon: LISTE (liste ekranı) veya BELGE (belge giriş ekranı). */
    public String getSablon() {
        return sablon;
    }

    /** Şablon: LISTE (liste ekranı) veya BELGE (belge giriş ekranı). */
    public void setSablon(String sablon) {
        this.sablon = sablon;
    }

    /** Ekranın ana tablosu. */
    public String getTabloKodu() {
        return tabloKodu;
    }

    /** Ekranın ana tablosu. */
    public void setTabloKodu(String tabloKodu) {
        this.tabloKodu = tabloKodu;
    }

    /** Varyant seçim koşulu: alan (örn. belgeturu). */
    public String getSecimAlani() {
        return secimAlani;
    }

    /** Varyant seçim koşulu: alan (örn. belgeturu). */
    public void setSecimAlani(String secimAlani) {
        this.secimAlani = secimAlani;
    }

    /** Varyant seçim koşulu: değer (örn. FATURA). */
    public String getSecimDegeri() {
        return secimDegeri;
    }

    /** Varyant seçim koşulu: değer (örn. FATURA). */
    public void setSecimDegeri(String secimDegeri) {
        this.secimDegeri = secimDegeri;
    }

    /** Düzenin geçerli olduğu organizasyon (boşsa sistem varsayılanı). */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Düzenin geçerli olduğu organizasyon (boşsa sistem varsayılanı). */
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

    /** Düzen sürümü. */
    public Integer getSurum() {
        return surum;
    }

    /** Düzen sürümü. */
    public void setSurum(Integer surum) {
        this.surum = surum;
    }

    /** Durum: TASLAK veya YAYIN (kapsam başına tek yayın). */
    public String getDurum() {
        return durum;
    }

    /** Durum: TASLAK veya YAYIN (kapsam başına tek yayın). */
    public void setDurum(String durum) {
        this.durum = durum;
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
