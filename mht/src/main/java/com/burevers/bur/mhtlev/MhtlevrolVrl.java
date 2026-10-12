package com.burevers.bur.mhtlev;

import com.burevers.bur.temort.TemortPasifVrl;
import com.burevers.bur.mhttem.MhttemrolVrl;
import com.burevers.bur.temorg.TemorgkurVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * MhtlevrolVrl - mhtlevrol tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatabın kuruluş bazında üstlendiği roller ve muhasebe hesap kodu.
 * Neden var: veritabanı satırını Java nesnesi olarak okuyup yazabilmek için.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhtlevrol")
public class MhtlevrolVrl extends TemortPasifVrl {

    /** Rolün atandığı muhatap. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muhatapvid", nullable = false)
    private MhtlevmufVrl muhatap;

    /** Rolün geçerli olduğu kuruluş. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kurvid", nullable = false)
    private TemorgkurVrl kurulus;

    /** Atanan rol (mhttemrol). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rolvid", nullable = false)
    private MhttemrolVrl rol;

    /** Kuruluşa özel muhasebe hesap kodu; rolden gelir, kullanıcı değiştirebilir. */
    @Column(name = "hspplnkodu", length = 30, nullable = false)
    private String hesapPlanKodu;

    /** Rolün atandığı muhatap. */
    public MhtlevmufVrl getMuhatap() {
        return muhatap;
    }

    /** Rolün atandığı muhatap. */
    public void setMuhatap(MhtlevmufVrl muhatap) {
        this.muhatap = muhatap;
    }

    /** Rolün geçerli olduğu kuruluş. */
    public TemorgkurVrl getKurulus() {
        return kurulus;
    }

    /** Rolün geçerli olduğu kuruluş. */
    public void setKurulus(TemorgkurVrl kurulus) {
        this.kurulus = kurulus;
    }

    /** Atanan rol (mhttemrol). */
    public MhttemrolVrl getRol() {
        return rol;
    }

    /** Atanan rol (mhttemrol). */
    public void setRol(MhttemrolVrl rol) {
        this.rol = rol;
    }

    /** Kuruluşa özel muhasebe hesap kodu; rolden gelir, kullanıcı değiştirebilir. */
    public String getHesapPlanKodu() {
        return hesapPlanKodu;
    }

    /** Kuruluşa özel muhasebe hesap kodu; rolden gelir, kullanıcı değiştirebilir. */
    public void setHesapPlanKodu(String hesapPlanKodu) {
        this.hesapPlanKodu = hesapPlanKodu;
    }
}
