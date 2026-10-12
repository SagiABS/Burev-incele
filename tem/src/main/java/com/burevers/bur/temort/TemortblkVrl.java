package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

/**
 * TemortblkVrl - temortblk tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: tüm modüller için ortak bloke (kilit) kaydı; tablokodu + kayitvid hedef kartı gösterir (yabancı anahtar yok).
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "temortblk")
public class TemortblkVrl extends TemortPasifVrl {

    /** Bloke edilen kartın tablo kodu (örn. mhtlevmuf). */
    @Column(name = "tablokodu", length = 10, nullable = false)
    private String tabloKodu;

    /** Bloke edilen kartın vid'i. */
    @Column(name = "kayitvid", nullable = false)
    private Long kayitVid;

    /** Kilidin geçerli olduğu organizasyonun vid'i (temorgorg; boş olabilir). */
    @Column(name = "orgvid")
    private Long orgVid;

    /** Kilidin geçerli olduğu kuruluşun vid'i (temorgkur; boş olabilir). */
    @Column(name = "kurvid")
    private Long kurVid;

    /** Kilidin geçerli olduğu tesisin vid'i (temorgtes; boş olabilir). Üçünden en az biri dolu olmalıdır. */
    @Column(name = "tesvid")
    private Long tesVid;

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

    /** Kilidin geçerli olduğu organizasyonun vid'i (temorgorg; boş olabilir). */
    public Long getOrgVid() {
        return orgVid;
    }

    /** Kilidin geçerli olduğu organizasyonun vid'i (temorgorg; boş olabilir). */
    public void setOrgVid(Long orgVid) {
        this.orgVid = orgVid;
    }

    /** Kilidin geçerli olduğu kuruluşun vid'i (temorgkur; boş olabilir). */
    public Long getKurVid() {
        return kurVid;
    }

    /** Kilidin geçerli olduğu kuruluşun vid'i (temorgkur; boş olabilir). */
    public void setKurVid(Long kurVid) {
        this.kurVid = kurVid;
    }

    /** Kilidin geçerli olduğu tesisin vid'i (temorgtes; boş olabilir). Üçünden en az biri dolu olmalıdır. */
    public Long getTesVid() {
        return tesVid;
    }

    /** Kilidin geçerli olduğu tesisin vid'i (temorgtes; boş olabilir). Üçünden en az biri dolu olmalıdır. */
    public void setTesVid(Long tesVid) {
        this.tesVid = tesVid;
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
