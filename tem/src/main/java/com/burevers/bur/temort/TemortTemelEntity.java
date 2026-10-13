package com.burevers.bur.temort;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import org.hibernate.Hibernate;

/**
 * TemortTemelEntity - tüm tabloların ortak üst sınıfı.
 * Ne işe yarar: anahtar (vid) ve oluşturma/güncelleme izi (kullanıcı, tarih, saat) alanlarını taşır.
 * Neden var: bu alanlar her tabloda aynıdır; tek yerde tanımlanarak tekrar önlenir.
 * Not: oturum açma (kimlik doğrulama) henüz yoktur; kullanıcı şimdilik "sistem" yazılır (ADR-08 bekliyor).
 */
@MappedSuperclass
public abstract class TemortTemelEntity {

    /** Geçici kullanıcı adı: kimlik doğrulama gelene kadar iz alanlarına bu değer yazılır. */
    private static final String VARSAYILAN_KULLANICI = "sistem";

    /** Satır anahtarı; veritabanı (IDENTITY) üretir. Yabancı anahtarlar bu sayıyı tutar. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vid", updatable = false)
    private Long vid;

    /** Kaydı oluşturan kullanıcı. */
    @Column(name = "c_kullanici", length = 30, nullable = false, updatable = false)
    private String olusturanKullanici;

    /** Kaydın oluşturulma tarihi. */
    @Column(name = "c_tarih", nullable = false, updatable = false)
    private LocalDate olusturmaTarihi;

    /** Kaydın oluşturulma saati. */
    @Column(name = "c_saat", nullable = false, updatable = false)
    private LocalTime olusturmaSaati;

    /** Kaydı son güncelleyen kullanıcı. */
    @Column(name = "u_kullanici", length = 30)
    private String guncelleyenKullanici;

    /** Son güncelleme tarihi. */
    @Column(name = "u_tarih")
    private LocalDate guncellemeTarihi;

    /** Son güncelleme saati. */
    @Column(name = "u_saat")
    private LocalTime guncellemeSaati;

    /** Kayıt eklenmeden hemen önce oluşturma izini doldurur. */
    @PrePersist
    void olusturmaIziniYaz() {
        if (olusturanKullanici == null) {
            olusturanKullanici = VARSAYILAN_KULLANICI;
        }
        olusturmaTarihi = LocalDate.now();
        olusturmaSaati = LocalTime.now();
    }

    /** Kayıt güncellenmeden hemen önce güncelleme izini doldurur. */
    @PreUpdate
    void guncellemeIziniYaz() {
        guncelleyenKullanici = VARSAYILAN_KULLANICI;
        guncellemeTarihi = LocalDate.now();
        guncellemeSaati = LocalTime.now();
    }

    /** Satır anahtarı. */
    public Long getVid() {
        return vid;
    }

    /** Kaydı oluşturan kullanıcı. */
    public String getOlusturanKullanici() {
        return olusturanKullanici;
    }

    /** Kaydın oluşturulma tarihi. */
    public LocalDate getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    /** Kaydın oluşturulma saati. */
    public LocalTime getOlusturmaSaati() {
        return olusturmaSaati;
    }

    /** Kaydı son güncelleyen kullanıcı. */
    public String getGuncelleyenKullanici() {
        return guncelleyenKullanici;
    }

    /** Son güncelleme tarihi. */
    public LocalDate getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    /** Son güncelleme saati. */
    public LocalTime getGuncellemeSaati() {
        return guncellemeSaati;
    }

    /** İki nesne, aynı sınıftan ve aynı (boş olmayan) vid'e sahipse eşittir. Vekil (proxy) sınıflar da doğru karşılaştırılır. */
    @Override
    public boolean equals(Object diger) {
        if (this == diger) {
            return true;
        }
        if (diger == null || Hibernate.getClass(this) != Hibernate.getClass(diger)) {
            return false;
        }
        TemortTemelEntity d = (TemortTemelEntity) diger;
        return vid != null && Objects.equals(vid, d.getVid());
    }

    /** Sınıfa bağlı sabit özet değer; vid sonradan atansa da değişmez (koleksiyonlarda güvenlidir). */
    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
