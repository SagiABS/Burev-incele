-- mhtlevkur : Muhatap - kuruluş ataması.
-- Neden var: merkezi muhatap kartının hangi kuruluşlarda (şirketlerde) kullanılabildiğini belirlemek için.
CREATE TABLE mhtlevkur (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kurvid         BIGINT       NOT NULL REFERENCES temorgkur (vid),
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
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
    CONSTRAINT mhtlevkur_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX mhtlevkur_cift_aktif_uq ON mhtlevkur (kurvid, muhatapvid) WHERE NOT pasif;
CREATE INDEX mhtlevkur_muhatapvid_ix ON mhtlevkur (muhatapvid);
