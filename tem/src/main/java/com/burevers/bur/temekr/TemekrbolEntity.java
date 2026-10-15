package com.burevers.bur.temekr;

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
 * TemekrbolEntity - temekrbol tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: ekranın bir çerçevesini (form, tablo, sekme grubu, özet), şablondaki yuvasını ve ilk açılış durumunu tutar (Ekran bölümü).
 * Neden var: bölümler iç içe olabilir ve alt/sağ paneller ilk açılışta kapalı gelir.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temekrbol")
public class TemekrbolEntity extends TemortPasifEntity {

    /** Bölümün ait olduğu ekran. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ekranvid", nullable = false)
    private TemekrtanEntity ekran;

    /** Üst bölüm (iç içe düzen için). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ustvid")
    private TemekrbolEntity ust;

    /** Ekran içinde benzersiz bölüm kodu. */
    @Column(name = "kodu", length = 20, nullable = false)
    private String kodu;

    /** Tür: FORM, GRID, SEKME veya OZET. */
    @Column(name = "turu", length = 10, nullable = false)
    private String turu;

    /** Şablondaki yuva: ORTAUST, ORTA, ORTAALT, ALT veya SAG. */
    @Column(name = "yuva", length = 10, nullable = false)
    private String yuva;

    /** Bölüm başlığı. */
    @Column(name = "baslik", length = 100)
    private String baslik;

    /** Yuva içindeki sıra. */
    @Column(name = "sira", nullable = false)
    private Integer sira;

    /** FORM bölümünün sütun sayısı (1-12). */
    @Column(name = "sutunsayisi", nullable = false)
    private Short sutunSayisi = (short) 1;

    /** Kullanıcı sürükleyip boyutlandırabilir mi? */
    @Column(name = "surukle", nullable = false)
    private boolean surukle = false;

    /** İlk açılışta açık mı? */
    @Column(name = "acik", nullable = false)
    private boolean acik = true;

    /** GRID bölümünün veri tablosu (boşsa ekranın ana tablosu). */
    @Column(name = "kaynaktablo", length = 10)
    private String kaynakTablo;

    /** Seyrek kullanılan görünüm ayarları (JSONB). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ayar")
    private Map<String, Object> ayar;

    /** Bölümün ait olduğu ekran. */
    public TemekrtanEntity getEkran() {
        return ekran;
    }

    /** Bölümün ait olduğu ekran. */
    public void setEkran(TemekrtanEntity ekran) {
        this.ekran = ekran;
    }

    /** Üst bölüm (iç içe düzen için). */
    public TemekrbolEntity getUst() {
        return ust;
    }

    /** Üst bölüm (iç içe düzen için). */
    public void setUst(TemekrbolEntity ust) {
        this.ust = ust;
    }

    /** Ekran içinde benzersiz bölüm kodu. */
    public String getKodu() {
        return kodu;
    }

    /** Ekran içinde benzersiz bölüm kodu. */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Tür: FORM, GRID, SEKME veya OZET. */
    public String getTuru() {
        return turu;
    }

    /** Tür: FORM, GRID, SEKME veya OZET. */
    public void setTuru(String turu) {
        this.turu = turu;
    }

    /** Şablondaki yuva: ORTAUST, ORTA, ORTAALT, ALT veya SAG. */
    public String getYuva() {
        return yuva;
    }

    /** Şablondaki yuva: ORTAUST, ORTA, ORTAALT, ALT veya SAG. */
    public void setYuva(String yuva) {
        this.yuva = yuva;
    }

    /** Bölüm başlığı. */
    public String getBaslik() {
        return baslik;
    }

    /** Bölüm başlığı. */
    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    /** Yuva içindeki sıra. */
    public Integer getSira() {
        return sira;
    }

    /** Yuva içindeki sıra. */
    public void setSira(Integer sira) {
        this.sira = sira;
    }

    /** FORM bölümünün sütun sayısı (1-12). */
    public Short getSutunSayisi() {
        return sutunSayisi;
    }

    /** FORM bölümünün sütun sayısı (1-12). */
    public void setSutunSayisi(Short sutunSayisi) {
        this.sutunSayisi = sutunSayisi;
    }

    /** Kullanıcı sürükleyip boyutlandırabilir mi? */
    public boolean isSurukle() {
        return surukle;
    }

    /** Kullanıcı sürükleyip boyutlandırabilir mi? */
    public void setSurukle(boolean surukle) {
        this.surukle = surukle;
    }

    /** İlk açılışta açık mı? */
    public boolean isAcik() {
        return acik;
    }

    /** İlk açılışta açık mı? */
    public void setAcik(boolean acik) {
        this.acik = acik;
    }

    /** GRID bölümünün veri tablosu (boşsa ekranın ana tablosu). */
    public String getKaynakTablo() {
        return kaynakTablo;
    }

    /** GRID bölümünün veri tablosu (boşsa ekranın ana tablosu). */
    public void setKaynakTablo(String kaynakTablo) {
        this.kaynakTablo = kaynakTablo;
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
