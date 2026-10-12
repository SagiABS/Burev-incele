-- mhtlevmfa : Muhatap - adres ilişkisi.
-- Neden var: bir adres (temortadr) birden çok muhatap tarafından ortak kullanılabilsin.
-- adrestipi alanı (fatura, sevk, şube...) adres tipleri tablosu netleşince eklenecek.
CREATE TABLE mhtlevmfa (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
    adresvid       BIGINT       NOT NULL REFERENCES temortadr (vid),
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
    CONSTRAINT mhtlevmfa_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Aynı muhatap-adres çifti aktif olarak bir kez bulunur
CREATE UNIQUE INDEX mhtlevmfa_cift_aktif_uq ON mhtlevmfa (muhatapvid, adresvid) WHERE NOT pasif;
CREATE INDEX mhtlevmfa_adresvid_ix ON mhtlevmfa (adresvid);
