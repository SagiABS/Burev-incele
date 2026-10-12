-- mhttemrol (menü: mht_1122_000) : Muhatap rol tanımları (müşteri, tedarikçi, personel...).
-- Neden var: bir muhatabın kuruluş bazında hangi rolleri üstlendiğini (mhtlevrol) bu tanımlara bağlamak için.
CREATE TABLE mhttemrol (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rol            VARCHAR(3)   NOT NULL,   -- kısa kod (örn. MUS, TED, TAS, PER)
    roladi         VARCHAR(50)  NOT NULL,   -- tam ad (örn. Müşteri)
    hspplnkodu     VARCHAR(30),             -- rolün varsayılan hesap planı şablon kodu (ileride hesap planına referans)
    rolkategorisi  VARCHAR(20)  NOT NULL,   -- iş fonksiyonu: SATIS, SATINALMA, FINANS, IK
    -- Pasif yapma bilgisi: üçü birlikte dolu (pasif) ya da üçü birlikte boş (aktif) olmalı
    pasif          BOOLEAN      NOT NULL DEFAULT FALSE,
    pasiftarihi    DATE,
    pasifnedeni    VARCHAR(10),             -- ileride neden tablosuna yabancı anahtar olacak
    -- Oluşturma / güncelleme izi
    c_kullanici    VARCHAR(30)  NOT NULL,
    c_tarih        DATE         NOT NULL,
    c_saat         TIME         NOT NULL,
    u_kullanici    VARCHAR(30),
    u_tarih        DATE,
    u_saat         TIME,
    CONSTRAINT mhttemrol_rol_uq UNIQUE (rol),
    CONSTRAINT mhttemrol_rol_ck CHECK (char_length(rol) = 3),
    CONSTRAINT mhttemrol_roladi_ck CHECK (btrim(roladi) <> ''),
    CONSTRAINT mhttemrol_kategori_ck CHECK (rolkategorisi IN ('SATIS','SATINALMA','FINANS','IK')),
    CONSTRAINT mhttemrol_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
