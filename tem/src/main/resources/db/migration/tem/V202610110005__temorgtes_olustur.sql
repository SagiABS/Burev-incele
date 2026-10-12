-- temorgtes (menü: tem_1128_000) : Tesis (şube) tanımı. Bir tesis aynı anda yalnızca bir kuruluşa bağlıdır (temorgniz).
CREATE TABLE temorgtes (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kodu           VARCHAR(3)   NOT NULL,
    adi            VARCHAR(200) NOT NULL,
    adikisa        VARCHAR(50),
    vergino        VARCHAR(30),
    donemi         SMALLINT,                -- dönem başlangıç ayı (1-12)
    etiket         VARCHAR(4),              -- temortetk etiket kodu; FK yok, servis katmanı doğrular
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
    CONSTRAINT temorgtes_kodu_uq UNIQUE (kodu),
    CONSTRAINT temorgtes_kodu_ck CHECK (char_length(kodu) = 3),
    CONSTRAINT temorgtes_adi_ck  CHECK (btrim(adi) <> ''),
    CONSTRAINT temorgtes_donemi_ck CHECK (donemi IS NULL OR donemi BETWEEN 1 AND 12),
    CONSTRAINT temorgtes_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
