package com.burevers.bur.temorg;

import com.burevers.bur.temort.TemortPasifVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

/**
 * TemorgblkVrl - temorgblk tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: tüm modüller için ortak bloke (kilit) kaydı; tablokodu + kayitvid hedef kartı gösterir (yabancı anahtar yok).
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temorgblk")
public class TemorgblkVrl extends TemortPasifVrl {

    /** Bloke edilen kartın tablo kodu (örn. mhtlevmuf). */
    @Column(name = "tablokodu", length = 10, nullable = false)
    private String tabloKodu;

    /** Bloke edilen kartın vid'i. */
    @Column(name = "kayitvid", nullable = false)
    private Long kayitVid;

    /** Kilidin geçerli olduğu organizasyon (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgvid")
    private TemorgorgVrl organizasyon;

    /** Kilidin geçerli olduğu kuruluş (boş olabilir). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurvid")
    private TemorgkurVrl kurulus;

    /** Kilidin geçerli olduğu tesis (boş olabilir). Üçünden en az biri dolu olmalıdır. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesvid")
    private TemorgtesVrl tesis;

    /** Hangi işlem türünün kilitli olduğu (satış, teslimat, ödeme...); değerler sonra tanımlanır. */
    @Column(name = "bloketipi", nullable = false)
    private Short blokeTipi;

    /** Kilidin devreye gireceği an. */
    @Column(name = "baslangictarihi", nullable = false)
    private OffsetDateTime baslangicTarihi;

    /** Kilidin kalkacağı an; boşsa süresiz. */
    @Column(name = "bitistarihi")
    private OffsetDateTime bitisTarihi;

    /** Bloke gerekçesi. */
    @Column(name = "blokenedeni", length = 250)
    private String blokeNedeni;

    /** İşlem anında kullanıcıya gösterilecek mesaj. */
    @Column(name = "blokeuyari", length = 250)
    private String blokeUyari;

    /** Bloke edilen kartın tablo kodu (örn. mhtlevmuf). */
    public String getTabloKodu() {
        return tabloKodu;
    }

    /** Bloke edilen kartın tablo kodu (örn. mhtlevmuf). */
    public void setTabloKodu(String tabloKodu) {
        this.tabloKodu = tabloKodu;
    }

    /** Bloke edilen kartın vid'i. */
    public Long getKayitVid() {
        return kayitVid;
    }

    /** Bloke edilen kartın vid'i. */
    public void setKayitVid(Long kayitVid) {
        this.kayitVid = kayitVid;
    }

    /** Kilidin geçerli olduğu organizasyon (boş olabilir). */
    public TemorgorgVrl getOrganizasyon() {
        return organizasyon;
    }

    /** Kilidin geçerli olduğu organizasyon (boş olabilir). */
    public void setOrganizasyon(TemorgorgVrl organizasyon) {
        this.organizasyon = organizasyon;
    }

    /** Kilidin geçerli olduğu kuruluş (boş olabilir). */
    public TemorgkurVrl getKurulus() {
        return kurulus;
    }

    /** Kilidin geçerli olduğu kuruluş (boş olabilir). */
    public void setKurulus(TemorgkurVrl kurulus) {
        this.kurulus = kurulus;
    }

    /** Kilidin geçerli olduğu tesis (boş olabilir). Üçünden en az biri dolu olmalıdır. */
    public TemorgtesVrl getTesis() {
        return tesis;
    }

    /** Kilidin geçerli olduğu tesis (boş olabilir). Üçünden en az biri dolu olmalıdır. */
    public void setTesis(TemorgtesVrl tesis) {
        this.tesis = tesis;
    }

    /** Hangi işlem türünün kilitli olduğu (satış, teslimat, ödeme...); değerler sonra tanımlanır. */
    public Short getBlokeTipi() {
        return blokeTipi;
    }

    /** Hangi işlem türünün kilitli olduğu (satış, teslimat, ödeme...); değerler sonra tanımlanır. */
    public void setBlokeTipi(Short blokeTipi) {
        this.blokeTipi = blokeTipi;
    }

    /** Kilidin devreye gireceği an. */
    public OffsetDateTime getBaslangicTarihi() {
        return baslangicTarihi;
    }

    /** Kilidin devreye gireceği an. */
    public void setBaslangicTarihi(OffsetDateTime baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    /** Kilidin kalkacağı an; boşsa süresiz. */
    public OffsetDateTime getBitisTarihi() {
        return bitisTarihi;
    }

    /** Kilidin kalkacağı an; boşsa süresiz. */
    public void setBitisTarihi(OffsetDateTime bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    /** Bloke gerekçesi. */
    public String getBlokeNedeni() {
        return blokeNedeni;
    }

    /** Bloke gerekçesi. */
    public void setBlokeNedeni(String blokeNedeni) {
        this.blokeNedeni = blokeNedeni;
    }

    /** İşlem anında kullanıcıya gösterilecek mesaj. */
    public String getBlokeUyari() {
        return blokeUyari;
    }

    /** İşlem anında kullanıcıya gösterilecek mesaj. */
    public void setBlokeUyari(String blokeUyari) {
        this.blokeUyari = blokeUyari;
    }
}
