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
 * TemekryerEntity - temekryer tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: form bölümündeki bir alanın satır, sütun ve genişlik konumunu, kontrol türünü ve görünüm kurallarını tutar (Alan yerleşimi).
 * Neden var: alanın hangi bölümde ve nerede duracağı kodla değil veriyle tanımlanır.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temekryer")
public class TemekryerEntity extends TemortPasifEntity {

    /** Alanın yerleştirildiği FORM/OZET bölümü. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bolumvid", nullable = false)
    private TemekrbolEntity bolum;

    /** Yerleştirilen alan (temcusdba; dinamik alanlar dahil). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dbavid", nullable = false)
    private TemcusdbaEntity alan;

    /** Satır numarası. */
    @Column(name = "satir", nullable = false)
    private Short satir;

    /** Sütun numarası (1-12). */
    @Column(name = "sutun", nullable = false)
    private Short sutun;

    /** Kaç sütun genişliğinde. */
    @Column(name = "kapsama", nullable = false)
    private Short kapsama = (short) 1;

    /** Etiket (boşsa alanın varsayılan etiketi). */
    @Column(name = "etiket", length = 100)
    private String etiket;

    /** Kontrol türü: METIN, NOT, SAYI, TARIH, LISTE, ARASEC, ONAY. */
    @Column(name = "kontrol", length = 10, nullable = false)
    private String kontrol = "METIN";

    /** Varsayılan salt okunur mu? */
    @Column(name = "saltokunur", nullable = false)
    private boolean saltOkunur = false;

    /** Doldurulması zorunlu mu? */
    @Column(name = "zorunlu", nullable = false)
    private boolean zorunlu = false;

    /** Yanında "ekle" düğmesi var mı? */
    @Column(name = "ekledugme", nullable = false)
    private boolean ekleDugme = false;

    /** Yanında "ayar" düğmesi var mı? */
    @Column(name = "ayardugme", nullable = false)
    private boolean ayarDugme = false;

    /** Görünürlük koşulu (örn. belgeturu=FATURA); boşsa her zaman görünür. */
    @Column(name = "gorunurkosul", length = 100)
    private String gorunurKosul;

    /** Seyrek kullanılan görünüm ayarları (JSONB). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ayar")
    private Map<String, Object> ayar;

    /** Alanın yerleştirildiği FORM/OZET bölümü. */
    public TemekrbolEntity getBolum() {
        return bolum;
    }

    /** Alanın yerleştirildiği FORM/OZET bölümü. */
    public void setBolum(TemekrbolEntity bolum) {
        this.bolum = bolum;
    }

    /** Yerleştirilen alan (temcusdba; dinamik alanlar dahil). */
    public TemcusdbaEntity getAlan() {
        return alan;
    }

    /** Yerleştirilen alan (temcusdba; dinamik alanlar dahil). */
    public void setAlan(TemcusdbaEntity alan) {
        this.alan = alan;
    }

    /** Satır numarası. */
    public Short getSatir() {
        return satir;
    }

    /** Satır numarası. */
    public void setSatir(Short satir) {
        this.satir = satir;
    }

    /** Sütun numarası (1-12). */
    public Short getSutun() {
        return sutun;
    }

    /** Sütun numarası (1-12). */
    public void setSutun(Short sutun) {
        this.sutun = sutun;
    }

    /** Kaç sütun genişliğinde. */
    public Short getKapsama() {
        return kapsama;
    }

    /** Kaç sütun genişliğinde. */
    public void setKapsama(Short kapsama) {
        this.kapsama = kapsama;
    }

    /** Etiket (boşsa alanın varsayılan etiketi). */
    public String getEtiket() {
        return etiket;
    }

    /** Etiket (boşsa alanın varsayılan etiketi). */
    public void setEtiket(String etiket) {
        this.etiket = etiket;
    }

    /** Kontrol türü: METIN, NOT, SAYI, TARIH, LISTE, ARASEC, ONAY. */
    public String getKontrol() {
        return kontrol;
    }

    /** Kontrol türü: METIN, NOT, SAYI, TARIH, LISTE, ARASEC, ONAY. */
    public void setKontrol(String kontrol) {
        this.kontrol = kontrol;
    }

    /** Varsayılan salt okunur mu? */
    public boolean isSaltOkunur() {
        return saltOkunur;
    }

    /** Varsayılan salt okunur mu? */
    public void setSaltOkunur(boolean saltOkunur) {
        this.saltOkunur = saltOkunur;
    }

    /** Doldurulması zorunlu mu? */
    public boolean isZorunlu() {
        return zorunlu;
    }

    /** Doldurulması zorunlu mu? */
    public void setZorunlu(boolean zorunlu) {
        this.zorunlu = zorunlu;
    }

    /** Yanında "ekle" düğmesi var mı? */
    public boolean isEkleDugme() {
        return ekleDugme;
    }

    /** Yanında "ekle" düğmesi var mı? */
    public void setEkleDugme(boolean ekleDugme) {
        this.ekleDugme = ekleDugme;
    }

    /** Yanında "ayar" düğmesi var mı? */
    public boolean isAyarDugme() {
        return ayarDugme;
    }

    /** Yanında "ayar" düğmesi var mı? */
    public void setAyarDugme(boolean ayarDugme) {
        this.ayarDugme = ayarDugme;
    }

    /** Görünürlük koşulu (örn. belgeturu=FATURA); boşsa her zaman görünür. */
    public String getGorunurKosul() {
        return gorunurKosul;
    }

    /** Görünürlük koşulu (örn. belgeturu=FATURA); boşsa her zaman görünür. */
    public void setGorunurKosul(String gorunurKosul) {
        this.gorunurKosul = gorunurKosul;
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
