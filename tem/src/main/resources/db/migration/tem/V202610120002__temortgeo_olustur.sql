-- temortgeo : Coğrafi lokasyon ağacı (Ülke > Bölge > Şehir > İlçe > Mahalle).
-- Neden var: adreslerin metin yerine standart (ISO 3166) lokasyonlara bağlanması için.
-- Tek tablo, parentvid ile kendine bağlanır (hiyerarşi).
CREATE TABLE temortgeo (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parentvid      BIGINT       REFERENCES temortgeo (vid),   -- bir üst lokasyon (ülkede boş)
    lokasyontipi   VARCHAR(20)  NOT NULL,   -- ULKE, BOLGE, SEHIR, ILCE, MAHALLE
    lokasyonadi    VARCHAR(150) NOT NULL,
    isokodu        VARCHAR(10),             -- ISO 3166-1 / 3166-2 (örn. TR, TR-34)
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
    CONSTRAINT temortgeo_tipi_ck CHECK (lokasyontipi IN ('ULKE','BOLGE','SEHIR','ILCE','MAHALLE')),
    CONSTRAINT temortgeo_adi_ck  CHECK (btrim(lokasyonadi) <> ''),
    -- Üst lokasyon yalnızca ülke olmayan kayıtlarda zorunludur
    CONSTRAINT temortgeo_parent_ck CHECK ((lokasyontipi = 'ULKE') = (parentvid IS NULL)),
    CONSTRAINT temortgeo_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- ISO kodu varsa benzersizdir
CREATE UNIQUE INDEX temortgeo_isokodu_uq ON temortgeo (isokodu) WHERE isokodu IS NOT NULL;
CREATE INDEX temortgeo_parentvid_ix ON temortgeo (parentvid);
