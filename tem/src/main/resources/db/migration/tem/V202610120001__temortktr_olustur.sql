-- temortktr : Şirket (muhatap) türü tanımları (A.Ş., Ltd., Şahıs vb.).
-- Neden var: muhatap kaydedilirken seçilen türe göre vergi no zorunluluğunu belirlemek için.
CREATE TABLE temortktr (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    turkodu        VARCHAR(5)   NOT NULL,   -- kısa kod (örn. AS, LTD, SAHIS)
    turadi         VARCHAR(100) NOT NULL,   -- tam ad
    vknzorunlu     BOOLEAN      NOT NULL DEFAULT FALSE,   -- vergi no girilmesi zorunlu mu?
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
    CONSTRAINT temortktr_turkodu_uq UNIQUE (turkodu),
    CONSTRAINT temortktr_turkodu_ck CHECK (btrim(turkodu) <> ''),
    CONSTRAINT temortktr_turadi_ck  CHECK (btrim(turadi) <> ''),
    CONSTRAINT temortktr_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
