package com.burevers.bur.temcus;

import com.burevers.bur.temorg.TemorgkurEntity;
import com.burevers.bur.temorg.TemorgorgEntity;
import com.burevers.bur.temorg.TemorgtesEntity;
import com.burevers.bur.temort.TemortPasifEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * TemcusidmEntity - temcusidm (Temel Custom İdame / tablolar arası alan eşleme) tablosunun JPA sınıfı.
 * Ne işe yarar: bir tablodaki alanın başka bir tablodaki hangi alanla ilişkili olduğunu, hangi kapsamda
 * (organizasyon / kuruluş / tesis) ve nasıl (kopya, dönüşüm, hesap) eşleneceğini tutar.
 * Neden var: şirket ya da tesise özel durumlar kod yazmadan, veriyle yönetilebilsin; kayıt atılırken bu eşlemeden yararlanılsın.
 * Not: kaynak ve hedef alan temcusdba'dan seçilir; ortak alanlar üst sınıflardan gelir.
 */
@Entity
@Table(name = "temcusidm")
public class TemcusidmEntity extends TemortPasifEntity {

    /** Eşlemenin ait olduğu organizasyon (zorunlu). Tek başına seçilirse tüm kuruluş ve tesislerde geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgvid", nullable = false)
    private TemorgorgEntity organizasyon;

    /** İsteğe bağlı kuruluş: seçilirse eşleme yalnızca bu kuruluşun tüm tesislerinde geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurEntity kurulus;

    /** İsteğe bağlı tesis: seçilirse (kuruluş da dolu olmalı) eşleme yalnızca bu tesiste geçerlidir. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesEntity tesis;

    /** Kaynak tablo alanı (temcusdba'dan seçilir). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kaynakdbavid", nullable = false)
    private TemcusdbaEntity kaynakAlan;

    /** Hedef tablo alanı (temcusdba'dan seçilir). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hedefdbavid", nullable = false)
    private TemcusdbaEntity hedefAlan;

    /** Eşleme tipi: KOPYA, DONUSUM veya HESAP. */
    @Column(name = "eslemetipi", length = 10, nullable = false)
    private String eslemeTipi;

    /** Kayıtlı dönüşüm / uzantı anahtarı (KOPYA dışında zorunlu). */
    @Column(name = "donusumkodu", length = 30)
    private String donusumKodu;

    /** Aynı kaynak için çalışma sırası. */
    @Column(name = "sira", nullable = false)
    private Integer sira = 1;

    /** Geçerlilik başlangıcı. */
    @Column(name = "gecerlibas", nullable = false)
    private LocalDate gecerliBaslangic;

    /** Geçerlilik bitişi (boşsa süresiz). */
    @Column(name = "gecerlibit")
    private LocalDate gecerliBitis;

    /** Açıklama. */
    @Column(name = "aciklama", length = 200)
    private String aciklama;

    /** Organizasyon. */
    public TemorgorgEntity getOrganizasyon() {
        return organizasyon;
    }

    /** Organizasyon. */
    public void setOrganizasyon(TemorgorgEntity organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Kuruluş (boş olabilir). */
    public TemorgkurEntity getKurulus() {
        return kurulus;
    }

    /** Kuruluş (boş olabilir). */
    public void setKurulus(TemorgkurEntity kurulus) {
        this.kurulus = kurulus;
    }

    /** Tesis (boş olabilir). */
    public TemorgtesEntity getTesis() {
        return tesis;
    }

    /** Tesis (boş olabilir). */
    public void setTesis(TemorgtesEntity tesis) {
        this.tesis = tesis;
    }

    /** Kaynak alan. */
    public TemcusdbaEntity getKaynakAlan() {
        return kaynakAlan;
    }

    /** Kaynak alan. */
    public void setKaynakAlan(TemcusdbaEntity kaynakAlan) {
        this.kaynakAlan = kaynakAlan;
    }

    /** Hedef alan. */
    public TemcusdbaEntity getHedefAlan() {
        return hedefAlan;
    }

    /** Hedef alan. */
    public void setHedefAlan(TemcusdbaEntity hedefAlan) {
        this.hedefAlan = hedefAlan;
    }

    /** Eşleme tipi. */
    public String getEslemeTipi() {
        return eslemeTipi;
    }

    /** Eşleme tipi. */
    public void setEslemeTipi(String eslemeTipi) {
        this.eslemeTipi = eslemeTipi;
    }

    /** Dönüşüm / uzantı anahtarı. */
    public String getDonusumKodu() {
        return donusumKodu;
    }

    /** Dönüşüm / uzantı anahtarı. */
    public void setDonusumKodu(String donusumKodu) {
        this.donusumKodu = donusumKodu;
    }

    /** Çalışma sırası. */
    public Integer getSira() {
        return sira;
    }

    /** Çalışma sırası. */
    public void setSira(Integer sira) {
        this.sira = sira;
    }

    /** Geçerlilik başlangıcı. */
    public LocalDate getGecerliBaslangic() {
        return gecerliBaslangic;
    }

    /** Geçerlilik başlangıcı. */
    public void setGecerliBaslangic(LocalDate gecerliBaslangic) {
        this.gecerliBaslangic = gecerliBaslangic;
    }

    /** Geçerlilik bitişi. */
    public LocalDate getGecerliBitis() {
        return gecerliBitis;
    }

    /** Geçerlilik bitişi. */
    public void setGecerliBitis(LocalDate gecerliBitis) {
        this.gecerliBitis = gecerliBitis;
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
