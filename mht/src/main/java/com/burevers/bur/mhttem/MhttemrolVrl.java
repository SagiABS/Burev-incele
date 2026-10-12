package com.burevers.bur.mhttem;

import com.burevers.bur.temort.TemortPasifVrl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * MhttemrolVrl - mhttemrol tablosunun JPA sınıfı (varlık).
 * Ne işe yarar: muhatap rol tanımı (müşteri, tedarikçi, personel...): kısa kod, ad, varsayılan hesap planı kodu ve iş kategorisi.
 * Neden var: muhatabın kuruluş bazındaki rolleri bu tanımlara bağlanır; kategori, rolün hangi iş akışını (satış, satın alma, finans, İK) tetikleyeceğini belirler.
 * Not: ortak alanlar (vid, pasif, oluşturma/güncelleme izi) tem modülündeki üst sınıflardan gelir.
 */
@Entity
@Table(name = "mhttemrol")
public class MhttemrolVrl extends TemortPasifVrl {

    /** Rol kısa kodu (3 karakter, benzersiz; örn. MUS, TED). */
    @Column(name = "rol", length = 3, nullable = false)
    private String rol;

    /** Rol adı. */
    @Column(name = "roladi", length = 50, nullable = false)
    private String rolAdi;

    /** Rolün varsayılan hesap planı şablon kodu (isteğe bağlı). */
    @Column(name = "hspplnkodu", length = 30)
    private String hesapPlanKodu;

    /** Rolün iş fonksiyonu: SATIS, SATINALMA, FINANS, IK. */
    @Column(name = "rolkategorisi", length = 20, nullable = false)
    private String rolKategorisi;

    /** Rol kısa kodu. */
    public String getRol() {
        return rol;
    }

    /** Rol kısa kodu. */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /** Rol adı. */
    public String getRolAdi() {
        return rolAdi;
    }

    /** Rol adı. */
    public void setRolAdi(String rolAdi) {
        this.rolAdi = rolAdi;
    }

    /** Varsayılan hesap planı şablon kodu. */
    public String getHesapPlanKodu() {
        return hesapPlanKodu;
    }

    /** Varsayılan hesap planı şablon kodu. */
    public void setHesapPlanKodu(String hesapPlanKodu) {
        this.hesapPlanKodu = hesapPlanKodu;
    }

    /** Rolün iş fonksiyonu. */
    public String getRolKategorisi() {
        return rolKategorisi;
    }

    /** Rolün iş fonksiyonu. */
    public void setRolKategorisi(String rolKategorisi) {
        this.rolKategorisi = rolKategorisi;
    }
}
