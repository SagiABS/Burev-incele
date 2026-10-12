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
 * Ne işe yarar: organizasyon ek alan tanımı: bir tabloya yöneticinin eklediği dinamik alanın adı, başlığı, türü ve zorunluluğu.
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

    /** Yalnızca bu organizasyonda geçerli; boşsa tümünde. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgvid")
    private TemorgorgEntity organizasyon;

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

    /** Yalnızca bu organizasyonda geçerli; boşsa tümünde. */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Yalnızca bu organizasyonda geçerli; boşsa tümünde. */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }
}
