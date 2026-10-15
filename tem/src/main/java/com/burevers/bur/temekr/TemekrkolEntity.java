package com.burevers.bur.temekr;

import com.burevers.bur.temcus.TemcusdbaEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * TemekrkolEntity - temekrkol tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: tablo (grid) bölümündeki bir kolonun sırasını, genişliğini, biçimini ve toplam kuralını tutar (Kolon tanımı).
 * Neden var: veri satırlarında hangi alanların nasıl görüneceği veriyle belirlenir.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temekrkol")
public class TemekrkolEntity extends TemortPasifEntity {

    /** Kolonun ait olduğu GRID bölümü. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bolumvid", nullable = false)
    private TemekrbolEntity bolum;

    /** Kolondaki alan (temcusdba). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dbavid", nullable = false)
    private TemcusdbaEntity alan;

    /** Kolon sırası. */
    @Column(name = "sira", nullable = false)
    private Integer sira;

    /** Başlık (boşsa alanın varsayılan etiketi). */
    @Column(name = "etiket", length = 100)
    private String etiket;

    /** Piksel genişliği (boşsa otomatik). */
    @Column(name = "genislik")
    private Short genislik;

    /** Gösterim biçimi (örn. TUTAR, TARIH, YUZDE). */
    @Column(name = "bicim", length = 10)
    private String bicim;

    /** Kolon altı özet: YOK, TOPLAM, ORTALAMA, SAYI. */
    @Column(name = "toplam", length = 10, nullable = false)
    private String toplam = "YOK";

    /** Sıralanabilir mi? */
    @Column(name = "siralanabilir", nullable = false)
    private boolean siralanabilir = true;

    /** Filtrelenebilir mi? */
    @Column(name = "filtrelenebilir", nullable = false)
    private boolean filtrelenebilir = true;

    /** İlk açılışta görünür mü? */
    @Column(name = "gorunur", nullable = false)
    private boolean gorunur = true;

    /** Seyrek kullanılan görünüm ayarları (JSONB). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ayar")
    private Map<String, Object> ayar;

    /** Kolonun ait olduğu GRID bölümü. */
    public TemekrbolEntity getBolum() {
        return bolum;
    }

    /** Kolonun ait olduğu GRID bölümü. */
    public void setBolum(TemekrbolEntity bolum) {
        this.bolum = bolum;
    }

    /** Kolondaki alan (temcusdba). */
    public TemcusdbaEntity getAlan() {
        return alan;
    }

    /** Kolondaki alan (temcusdba). */
    public void setAlan(TemcusdbaEntity alan) {
        this.alan = alan;
    }

    /** Kolon sırası. */
    public Integer getSira() {
        return sira;
    }

    /** Kolon sırası. */
    public void setSira(Integer sira) {
        this.sira = sira;
    }

    /** Başlık (boşsa alanın varsayılan etiketi). */
    public String getEtiket() {
        return etiket;
    }

    /** Başlık (boşsa alanın varsayılan etiketi). */
    public void setEtiket(String etiket) {
        this.etiket = etiket;
    }

    /** Piksel genişliği (boşsa otomatik). */
    public Short getGenislik() {
        return genislik;
    }

    /** Piksel genişliği (boşsa otomatik). */
    public void setGenislik(Short genislik) {
        this.genislik = genislik;
    }

    /** Gösterim biçimi (örn. TUTAR, TARIH, YUZDE). */
    public String getBicim() {
        return bicim;
    }

    /** Gösterim biçimi (örn. TUTAR, TARIH, YUZDE). */
    public void setBicim(String bicim) {
        this.bicim = bicim;
    }

    /** Kolon altı özet: YOK, TOPLAM, ORTALAMA, SAYI. */
    public String getToplam() {
        return toplam;
    }

    /** Kolon altı özet: YOK, TOPLAM, ORTALAMA, SAYI. */
    public void setToplam(String toplam) {
        this.toplam = toplam;
    }

    /** Sıralanabilir mi? */
    public boolean isSiralanabilir() {
        return siralanabilir;
    }

    /** Sıralanabilir mi? */
    public void setSiralanabilir(boolean siralanabilir) {
        this.siralanabilir = siralanabilir;
    }

    /** Filtrelenebilir mi? */
    public boolean isFiltrelenebilir() {
        return filtrelenebilir;
    }

    /** Filtrelenebilir mi? */
    public void setFiltrelenebilir(boolean filtrelenebilir) {
        this.filtrelenebilir = filtrelenebilir;
    }

    /** İlk açılışta görünür mü? */
    public boolean isGorunur() {
        return gorunur;
    }

    /** İlk açılışta görünür mü? */
    public void setGorunur(boolean gorunur) {
        this.gorunur = gorunur;
    }

    /** Seyrek kullanılan görünüm ayarları (JSONB). */
    public Map<String, Object> getAyar() {
        return ayar;
    }

    /** Seyrek kullanılan görünüm ayarları (JSONB). */
    public void setAyar(Map<String, Object> ayar) {
        this.ayar = ayar;
    }
}
