package com.burevers.bur.temkul;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemkuliznEntity - temkulizn tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: bir menü adımı ya da tablo üzerindeki bir işlemi (eylemi) tanımlar tutar (Kullanıcı izni).
 * Neden var: yetkiler bu izinler üzerinden role verilir.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulizn")
public class TemkuliznEntity extends TemortPasifEntity {

    /** Kaynak türü: MENU veya TABLO. */
    @Column(name = "kaynakturu", length = 10, nullable = false)
    private String kaynakTuru;

    /** Menü adımının siskod değeri ya da tablo kodu. */
    @Column(name = "kaynakkodu", length = 20, nullable = false)
    private String kaynakKodu;

    /** Eylem: GORUNTULE, EKLE, DEGISTIR, SIL, ONAYLA, DISARIAKTAR. */
    @Column(name = "eylem", length = 15, nullable = false)
    private String eylem;

    /** Görev ayrılığı uygulansın mı? Doğruysa kaydı oluşturan kişi bu izni aynı kayıt için kullanamaz. */
    @Column(name = "gorevayrilik", nullable = false)
    private boolean gorevAyrilik = false;

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Kaynak türü: MENU veya TABLO. */
    public String getKaynakTuru() {
        return kaynakTuru;
    }

    /** Kaynak türü: MENU veya TABLO. */
    public void setKaynakTuru(String kaynakTuru) {
        this.kaynakTuru = kaynakTuru;
    }

    /** Menü adımının siskod değeri ya da tablo kodu. */
    public String getKaynakKodu() {
        return kaynakKodu;
    }

    /** Menü adımının siskod değeri ya da tablo kodu. */
    public void setKaynakKodu(String kaynakKodu) {
        this.kaynakKodu = kaynakKodu;
    }

    /** Eylem: GORUNTULE, EKLE, DEGISTIR, SIL, ONAYLA, DISARIAKTAR. */
    public String getEylem() {
        return eylem;
    }

    /** Eylem: GORUNTULE, EKLE, DEGISTIR, SIL, ONAYLA, DISARIAKTAR. */
    public void setEylem(String eylem) {
        this.eylem = eylem;
    }

    /** Görev ayrılığı uygulansın mı? Doğruysa kaydı oluşturan kişi bu izni aynı kayıt için kullanamaz. */
    public boolean isGorevAyrilik() {
        return gorevAyrilik;
    }

    /** Görev ayrılığı uygulansın mı? Doğruysa kaydı oluşturan kişi bu izni aynı kayıt için kullanamaz. */
    public void setGorevAyrilik(boolean gorevAyrilik) {
        this.gorevAyrilik = gorevAyrilik;
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
