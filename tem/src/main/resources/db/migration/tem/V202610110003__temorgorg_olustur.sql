-- temorgorg (menü: tem_893_000) : Organizasyon (holding) tanımı. Kiracı = Organizasyon (ADR-04).
CREATE TABLE temorgorg (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kodu           VARCHAR(3)   NOT NULL,
    adi            VARCHAR(200) NOT NULL,
    acilistarihi   DATE         NOT NULL,
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
    CONSTRAINT temorgorg_kodu_uq UNIQUE (kodu),
    CONSTRAINT temorgorg_kodu_ck CHECK (char_length(kodu) = 3),
    CONSTRAINT temorgorg_adi_ck  CHECK (btrim(adi) <> ''),
    CONSTRAINT temorgorg_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
