package com.burevers.bur.temkul;

import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

/**
 * TemkulparEntity - temkulpar tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: parola özeti, dış kimlik (SSO), MFA ve kilitlenme bilgisini tutar (Kullanıcı giriş bilgisi).
 * Neden var: gizli bilgi ana kullanıcı tablosundan ayrı durur ve sorgularda yanlışlıkla çıkmaz.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temkulpar")
public class TemkulparEntity extends TemortPasifEntity {

    /** Giriş bilgisinin sahibi kullanıcı. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kullanicivid", nullable = false)
    private TemkultanEntity kullanici;

    /** Parola özeti (hash); düz metin asla saklanmaz. */
    @Column(name = "parolaozeti", length = 255)
    private String parolaOzeti;

    /** Parolanın son değiştirildiği an. */
    @Column(name = "parolatarihi")
    private OffsetDateTime parolaTarihi;

    /** İlk girişte parola değiştirmek zorunlu mu? */
    @Column(name = "degisimzorunlu", nullable = false)
    private boolean degisimZorunlu = false;

    /** Dış kimlik sağlayıcısı. */
    @Column(name = "ssosaglayici", length = 30)
    private String ssoSaglayici;

    /** Dış kimlik sağlayıcısındaki kullanıcı anahtarı. */
    @Column(name = "ssokimlik", length = 200)
    private String ssoKimlik;

    /** Çok adımlı doğrulama açık mı? */
    @Column(name = "mfaaktif", nullable = false)
    private boolean mfaAktif = false;

    /** MFA sırrı (şifrelenmiş saklanır). */
    @Column(name = "mfasirri", length = 255)
    private String mfaSirri;

    /** Üst üste hatalı giriş sayısı. */
    @Column(name = "hatalisayisi", nullable = false)
    private Short hataliSayisi = (short) 0;

    /** Hesap bu ana kadar kilitli. */
    @Column(name = "kilitbitis")
    private OffsetDateTime kilitBitis;

    /** Son başarılı giriş. */
    @Column(name = "songiris")
    private OffsetDateTime sonGiris;

    /** Giriş bilgisinin sahibi kullanıcı. */
    public TemkultanEntity getKullanici() {
        return kullanici;
    }

    /** Giriş bilgisinin sahibi kullanıcı. */
    public void setKullanici(TemkultanEntity kullanici) {
        this.kullanici = kullanici;
    }

    /** Parola özeti (hash); düz metin asla saklanmaz. */
    public String getParolaOzeti() {
        return parolaOzeti;
    }

    /** Parola özeti (hash); düz metin asla saklanmaz. */
    public void setParolaOzeti(String parolaOzeti) {
        this.parolaOzeti = parolaOzeti;
    }

    /** Parolanın son değiştirildiği an. */
    public OffsetDateTime getParolaTarihi() {
        return parolaTarihi;
    }

    /** Parolanın son değiştirildiği an. */
    public void setParolaTarihi(OffsetDateTime parolaTarihi) {
        this.parolaTarihi = parolaTarihi;
    }

    /** İlk girişte parola değiştirmek zorunlu mu? */
    public boolean isDegisimZorunlu() {
        return degisimZorunlu;
    }

    /** İlk girişte parola değiştirmek zorunlu mu? */
    public void setDegisimZorunlu(boolean degisimZorunlu) {
        this.degisimZorunlu = degisimZorunlu;
    }

    /** Dış kimlik sağlayıcısı. */
    public String getSsoSaglayici() {
        return ssoSaglayici;
    }

    /** Dış kimlik sağlayıcısı. */
    public void setSsoSaglayici(String ssoSaglayici) {
        this.ssoSaglayici = ssoSaglayici;
    }

    /** Dış kimlik sağlayıcısındaki kullanıcı anahtarı. */
    public String getSsoKimlik() {
        return ssoKimlik;
    }

    /** Dış kimlik sağlayıcısındaki kullanıcı anahtarı. */
    public void setSsoKimlik(String ssoKimlik) {
        this.ssoKimlik = ssoKimlik;
    }

    /** Çok adımlı doğrulama açık mı? */
    public boolean isMfaAktif() {
        return mfaAktif;
    }

    /** Çok adımlı doğrulama açık mı? */
    public void setMfaAktif(boolean mfaAktif) {
        this.mfaAktif = mfaAktif;
    }

    /** MFA sırrı (şifrelenmiş saklanır). */
    public String getMfaSirri() {
        return mfaSirri;
    }

    /** MFA sırrı (şifrelenmiş saklanır). */
    public void setMfaSirri(String mfaSirri) {
        this.mfaSirri = mfaSirri;
    }

    /** Üst üste hatalı giriş sayısı. */
    public Short getHataliSayisi() {
        return hataliSayisi;
    }

    /** Üst üste hatalı giriş sayısı. */
    public void setHataliSayisi(Short hataliSayisi) {
        this.hataliSayisi = hataliSayisi;
    }

    /** Hesap bu ana kadar kilitli. */
    public OffsetDateTime getKilitBitis() {
        return kilitBitis;
    }

    /** Hesap bu ana kadar kilitli. */
    public void setKilitBitis(OffsetDateTime kilitBitis) {
        this.kilitBitis = kilitBitis;
    }

    /** Son başarılı giriş. */
    public OffsetDateTime getSonGiris() {
        return sonGiris;
    }

    /** Son başarılı giriş. */
    public void setSonGiris(OffsetDateTime sonGiris) {
        this.sonGiris = sonGiris;
    }
}
