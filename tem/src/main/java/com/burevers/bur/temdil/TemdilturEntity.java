package com.burevers.bur.temdil;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TemdilturEntity - temdiltur tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: projede desteklenen bir dili (kod, ad, kendi yazısıyla ad, yazı yönü, varsayılan mı) tutar (Proje dili).
 * Neden var: çok dillilik (ADR-11) için hangi dillerin açık olduğu ve Arapça gibi dillerin sağdan sola yazıldığı
 * veriyle tanımlanır; arayüz yönünü ve dil seçiciyi bu kayıtlar belirler.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temdiltur")
public class TemdilturEntity extends TemortPasifEntity {

    /** Dil kodu (küçük harf BCP-47), örn. tr, en, ar. */
    @Column(name = "kodu", length = 10, nullable = false)
    private String kodu;

    /** Sistem dilinde adı, örn. Türkçe. */
    @Column(name = "adi", length = 50, nullable = false)
    private String adi;

    /** Dilin kendi yazısıyla adı, örn. العربية (dil seçicide gösterilir). */
    @Column(name = "yerelad", length = 50, nullable = false)
    private String yerelAd;

    /** Yazı yönü: LTR (soldan sağa) veya RTL (sağdan sola). */
    @Column(name = "yon", length = 3, nullable = false)
    private String yon = "LTR";

    /** Kullanıcının dili ya da metin yoksa kullanılan dil mi? En fazla bir tane. */
    @Column(name = "varsayilan", nullable = false)
    private boolean varsayilan;

    /** Dil seçicideki sıra. */
    @Column(name = "sira", nullable = false)
    private Integer sira = 100;

    /** Dil kodu. */
    public String getKodu() {
        return kodu;
    }

    /** Dil kodu. */
    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    /** Sistem dilinde adı. */
    public String getAdi() {
        return adi;
    }

    /** Sistem dilinde adı. */
    public void setAdi(String adi) {
        this.adi = adi;
    }

    /** Dilin kendi yazısıyla adı. */
    public String getYerelAd() {
        return yerelAd;
    }

    /** Dilin kendi yazısıyla adı. */
    public void setYerelAd(String yerelAd) {
        this.yerelAd = yerelAd;
    }

    /** Yazı yönü: LTR veya RTL. */
    public String getYon() {
        return yon;
    }

    /** Yazı yönü: LTR veya RTL. */
    public void setYon(String yon) {
        this.yon = yon;
    }

    /** Varsayılan dil mi? */
    public boolean isVarsayilan() {
        return varsayilan;
    }

    /** Varsayılan dil mi? */
    public void setVarsayilan(boolean varsayilan) {
        this.varsayilan = varsayilan;
    }

    /** Dil seçicideki sıra. */
    public Integer getSira() {
        return sira;
    }

    /** Dil seçicideki sıra. */
    public void setSira(Integer sira) {
        this.sira = sira;
    }
}
