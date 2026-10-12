package com.burevers.bur.temorg;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TemorgmdeEntity - temorgmde tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: organizasyon ek alan tanımı: bir tabloya yöneticinin eklediği dinamik alanın adı, başlığı, türü, zorunluluğu ve geçerlilik kapsamı (organizasyon / kuruluş / tesis).
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgmde")
public class TemorgmdeEntity extends TemortPasifEntity {

    /** Ek alanın ekleneceği tablo kodu. */
    @Column(name = "tablokodu", length = 10, nullable = false)
    private String tabloKodu;

    /** Teknik ad; JSON anahtarıdır (küçük harf, rakam, _). */
    @Column(name = "alanadi", length = 50, nullable = false)
    private String alanAdi;

    /** Ekranda görünen başlık. */
    @Column(name = "alanetiketi", length = 100, nullable = false)
    private String alanEtiketi;

    /** Veri türü: TEXT, NUMBER, DATE, BOOLEAN. */
    @Column(name = "verituru", length = 20, nullable = false)
    private String veriTuru;

    /** Alan doldurulmak zorunda mı? */
    @Column(name = "zorunlumu", nullable = false)
    private boolean zorunluMu;

    /** Tanımın ait olduğu organizasyon (zorunlu). Tek başına seçilirse tüm kuruluş ve tesislerde geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgvid", nullable = false)
    private TemorgorgEntity organizasyon;

    /** İsteğe bağlı kuruluş: seçilirse tanım yalnızca bu kuruluşun tüm tesislerinde geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurEntity kurulus;

    /** İsteğe bağlı tesis: seçilirse (kuruluş da dolu olmalı) tanım yalnızca bu tesiste geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesEntity tesis;

    /** Ek alanın ekleneceği tablo kodu. */
    public String getTabloKodu() {
        return tabloKodu;
    }

    /** Ek alanın ekleneceği tablo kodu. */
    public void setTabloKodu(String tabloKodu) {
        this.tabloKodu = tabloKodu;
    }

    /** Teknik ad; JSON anahtarıdır (küçük harf, rakam, _). */
    public String getAlanAdi() {
        return alanAdi;
    }

    /** Teknik ad; JSON anahtarıdır (küçük harf, rakam, _). */
    public void setAlanAdi(String alanAdi) {
        this.alanAdi = alanAdi;
    }

    /** Ekranda görünen başlık. */
    public String getAlanEtiketi() {
        return alanEtiketi;
    }

    /** Ekranda görünen başlık. */
    public void setAlanEtiketi(String alanEtiketi) {
        this.alanEtiketi = alanEtiketi;
    }

    /** Veri türü: TEXT, NUMBER, DATE, BOOLEAN. */
    public String getVeriTuru() {
        return veriTuru;
    }

    /** Veri türü: TEXT, NUMBER, DATE, BOOLEAN. */
    public void setVeriTuru(String veriTuru) {
        this.veriTuru = veriTuru;
    }

    /** Alan doldurulmak zorunda mı? */
    public boolean isZorunluMu() {
        return zorunluMu;
    }

    /** Alan doldurulmak zorunda mı? */
    public void setZorunluMu(boolean zorunluMu) {
        this.zorunluMu = zorunluMu;
    }

    /** Tanımın ait olduğu organizasyon. */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Tanımın ait olduğu organizasyon. */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Kapsamdaki kuruluş (boş olabilir). */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Kapsamdaki kuruluş (boş olabilir). */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Kapsamdaki tesis (boş olabilir). */
    public TemorgtesEntity getTesis() {
        return tesis;
    }

    /** Kapsamdaki tesis (boş olabilir). */
    public void setTesis(TemorgtesEntity tesis) {
        this.tesis = tesis;
    }
}
