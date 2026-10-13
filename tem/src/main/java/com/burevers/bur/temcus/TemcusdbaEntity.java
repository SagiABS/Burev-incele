package com.burevers.bur.temcus;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemcusdbaEntity - temcusdba (Tablolar ve Alanları / veri sözlüğü) tablosunun JPA sınıfı.
 * Ne işe yarar: veritabanındaki tabloların alanlarını, veri türünü, işlevini ve kimin açtığını tutar;
 * temcusmde ile eklenen dinamik alanlar da burada yer alır.
 * Neden var: temcusidm eşlemelerinin kaynak ve hedef alanı bu listeden seçilebilsin diye.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir; alanı açan kişi c_kullanici'dır.
 */
@Entity
@Table(name = "temcusdba")
public class TemcusdbaEntity extends TemortPasifEntity {

    /** Tablo kodu (örn. mhtlevmuf). */
    @Column(name = "tablokodu", length = 10, nullable = false)
    private String tabloKodu;

    /** Sütun adı ya da dinamik alanın JSON anahtarı. */
    @Column(name = "alanadi", length = 50, nullable = false)
    private String alanAdi;

    /** Veri türü (örn. VARCHAR(30), BIGINT, JSONB). */
    @Column(name = "veritipi", length = 30, nullable = false)
    private String veriTipi;

    /** Alanın işlevi / açıklaması. */
    @Column(name = "islevi", length = 500)
    private String islevi;

    /** Kaynak: SISTEM (göçle açılan sütun) veya DINAMIK (temcusmde ile tanımlı ek alan). */
    @Column(name = "kaynak", length = 10, nullable = false)
    private String kaynak;

    /** Dinamik alanın tanımı (kaynak DINAMIK ise dolu). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mdevid")
    private TemcusmdeEntity ekAlanTanimi;

    /** Tablo kodu. */
    public String getTabloKodu() {
        return tabloKodu;
    }

    /** Tablo kodu. */
    public void setTabloKodu(String tabloKodu) {
        this.tabloKodu = tabloKodu;
    }

    /** Alan adı. */
    public String getAlanAdi() {
        return alanAdi;
    }

    /** Alan adı. */
    public void setAlanAdi(String alanAdi) {
        this.alanAdi = alanAdi;
    }

    /** Veri türü. */
    public String getVeriTipi() {
        return veriTipi;
    }

    /** Veri türü. */
    public void setVeriTipi(String veriTipi) {
        this.veriTipi = veriTipi;
    }

    /** Alanın işlevi. */
    public String getIslevi() {
        return islevi;
    }

    /** Alanın işlevi. */
    public void setIslevi(String islevi) {
        this.islevi = islevi;
    }

    /** Kaynak: SISTEM veya DINAMIK. */
    public String getKaynak() {
        return kaynak;
    }

    /** Kaynak: SISTEM veya DINAMIK. */
    public void setKaynak(String kaynak) {
        this.kaynak = kaynak;
    }

    /** Dinamik alanın tanımı (sistem alanında boş). */
    public TemcusmdeEntity getEkAlanTanimi() {
        return ekAlanTanimi;
    }

    /** Dinamik alanın tanımı (sistem alanında boş). */
    public void setEkAlanTanimi(TemcusmdeEntity ekAlanTanimi) {
        this.ekAlanTanimi = ekAlanTanimi;
    }
}
