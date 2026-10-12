-- temortprb (menü: tem_1251_000) : Para birimi tanımları.
-- Neden var: tutarların hangi para biriminde ve kaç ondalıkla tutulacağını belirler.
CREATE TABLE temortprb (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pbkodu         VARCHAR(3)   NOT NULL,
    pbadi          VARCHAR(50)  NOT NULL,
    decimal        SMALLINT     NOT NULL,   -- ondalık basamak sayısı
    decadi         VARCHAR(50),             -- ondalık kısmın adı (örn. kuruş)
    stdadi         VARCHAR(30),             -- standart ad (örn. TRY)
    ornek          VARCHAR(50),             -- örnek gösterim
    aciklama       VARCHAR(200),
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
    CONSTRAINT temortprb_pbkodu_uq UNIQUE (pbkodu),
    CONSTRAINT temortprb_pbkodu_ck CHECK (char_length(pbkodu) = 3),
    CONSTRAINT temortprb_pbadi_ck  CHECK (btrim(pbadi) <> ''),
    CONSTRAINT temortprb_decimal_ck CHECK (decimal BETWEEN 0 AND 4),
    CONSTRAINT temortprb_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
