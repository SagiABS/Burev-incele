package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * TemortadrEntity - temortadr tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: ortak adres havuzu; muhatapla bağı mht modülündeki ilişki tablosunda kurulur.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortadr")
public class TemortadrEntity extends TemortPasifEntity {

    /** Hızlı seçim için adres başlığı. */
    @Column(name = "adresbasligi", length = 50, nullable = false)
    private String adresBasligi;

    /** Ülke (temortgeo). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ulkevid", nullable = false)
    private TemortgeoEntity ulke;

    /** Şehir (temortgeo). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sehirvid", nullable = false)
    private TemortgeoEntity sehir;

    /** Bölge/ilçe (temortgeo). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bolgevid", nullable = false)
    private TemortgeoEntity bolge;

    /** Mahalle; listeden seçilirse dolar. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mahallevid")
    private TemortgeoEntity mahalle;

    /** Mahalle listede yoksa elle yazılan ad. */
    @Column(name = "mahalletext", length = 100)
    private String mahalleText;

    /** Cadde, sokak ve açık adres. */
    @Column(name = "adrestext", length = 500, nullable = false)
    private String adresText;

    /** Bina no. */
    @Column(name = "binano", length = 10)
    private String binaNo;

    /** Kat no. */
    @Column(name = "katno", length = 5)
    private String katNo;

    /** Daire no. */
    @Column(name = "daireno", length = 10)
    private String daireNo;

    /** Posta kodu. */
    @Column(name = "postakodu", length = 10)
    private String postaKodu;

    /** Harita/konum bağlantısı. */
    @Column(name = "cografiurl", length = 500)
    private String cografiUrl;

    /** Enlem (-90..90, 8 ondalık). */
    @Column(name = "enlem", precision = 11, scale = 8)
    private BigDecimal enlem;

    /** Boylam (-180..180, 8 ondalık). */
    @Column(name = "boylam", precision = 11, scale = 8)
    private BigDecimal boylam;

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ekbilgiler")
    private Map<String, Object> ekBilgiler;

    /** Bilgileri kopyalanan başvuru adresi. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refadresvid")
    private TemortadrEntity refAdres;

    /** Hızlı seçim için adres başlığı. */
    public String getAdresBasligi() {
        return adresBasligi;
    }

    /** Hızlı seçim için adres başlığı. */
    public void setAdresBasligi(String adresBasligi) {
        this.adresBasligi = adresBasligi;
    }

    /** Ülke (temortgeo). */
    public TemortgeoEntity getUlke() {
        return ulke;
    }

    /** Ülke (temortgeo). */
    public void setUlke(TemortgeoEntity ulke) {
        this.ulke = ulke;
    }

    /** Şehir (temortgeo). */
    public TemortgeoEntity getSehir() {
        return sehir;
    }

    /** Şehir (temortgeo). */
    public void setSehir(TemortgeoEntity sehir) {
        this.sehir = sehir;
    }

    /** Bölge/ilçe (temortgeo). */
    public TemortgeoEntity getBolge() {
        return bolge;
    }

    /** Bölge/ilçe (temortgeo). */
    public void setBolge(TemortgeoEntity bolge) {
        this.bolge = bolge;
    }

    /** Mahalle; listeden seçilirse dolar. */
    public TemortgeoEntity getMahalle() {
        return mahalle;
    }

    /** Mahalle; listeden seçilirse dolar. */
    public void setMahalle(TemortgeoEntity mahalle) {
        this.mahalle = mahalle;
    }

    /** Mahalle listede yoksa elle yazılan ad. */
    public String getMahalleText() {
        return mahalleText;
    }

    /** Mahalle listede yoksa elle yazılan ad. */
    public void setMahalleText(String mahalleText) {
        this.mahalleText = mahalleText;
    }

    /** Cadde, sokak ve açık adres. */
    public String getAdresText() {
        return adresText;
    }

    /** Cadde, sokak ve açık adres. */
    public void setAdresText(String adresText) {
        this.adresText = adresText;
    }

    /** Bina no. */
    public String getBinaNo() {
        return binaNo;
    }

    /** Bina no. */
    public void setBinaNo(String binaNo) {
        this.binaNo = binaNo;
    }

    /** Kat no. */
    public String getKatNo() {
        return katNo;
    }

    /** Kat no. */
    public void setKatNo(String katNo) {
        this.katNo = katNo;
    }

    /** Daire no. */
    public String getDaireNo() {
        return daireNo;
    }

    /** Daire no. */
    public void setDaireNo(String daireNo) {
        this.daireNo = daireNo;
    }

    /** Posta kodu. */
    public String getPostaKodu() {
        return postaKodu;
    }

    /** Posta kodu. */
    public void setPostaKodu(String postaKodu) {
        this.postaKodu = postaKodu;
    }

    /** Harita/konum bağlantısı. */
    public String getCografiUrl() {
        return cografiUrl;
    }

    /** Harita/konum bağlantısı. */
    public void setCografiUrl(String cografiUrl) {
        this.cografiUrl = cografiUrl;
    }

    /** Enlem (-90..90, 8 ondalık). */
    public BigDecimal getEnlem() {
        return enlem;
    }

    /** Enlem (-90..90, 8 ondalık). */
    public void setEnlem(BigDecimal enlem) {
        this.enlem = enlem;
    }

    /** Boylam (-180..180, 8 ondalık). */
    public BigDecimal getBoylam() {
        return boylam;
    }

    /** Boylam (-180..180, 8 ondalık). */
    public void setBoylam(BigDecimal boylam) {
        this.boylam = boylam;
    }

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    public Map<String, Object> getEkBilgiler() {
        return ekBilgiler;
    }

    /** Yönetici tanımlı ek alanlar (JSONB); anahtarlar temorgmde.alanadi. */
    public void setEkBilgiler(Map<String, Object> ekBilgiler) {
        this.ekBilgiler = ekBilgiler;
    }

    /** Bilgileri kopyalanan başvuru adresi. */
    public TemortadrEntity getRefAdres() {
        return refAdres;
    }

    /** Bilgileri kopyalanan başvuru adresi. */
    public void setRefAdres(TemortadrEntity refAdres) {
        this.refAdres = refAdres;
    }
}
