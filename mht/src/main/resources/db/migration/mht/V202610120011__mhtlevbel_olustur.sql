-- mhtlevbel : Muhatap resmi belgeleri (vergi levhası, imza sirküleri, ticaret sicil gazetesi...).
-- Neden var: belgelerin dosya adresini ve son geçerlilik tarihini izleyip süresi dolanları takip etmek için.
-- Dosyanın kendisi veritabanında değil bulut depolamada tutulur; burada yalnızca adresi vardır.
CREATE TABLE mhtlevbel (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
    belgetipi      VARCHAR(50)  NOT NULL,
    documenturl    VARCHAR(500) NOT NULL,   -- belgenin depolama adresi/yolu
    songecerliliktarihi DATE,   -- yasal son geçerlilik tarihi (varsa)
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
    CONSTRAINT mhtlevbel_belgetipi_ck CHECK (btrim(belgetipi) <> ''),
    CONSTRAINT mhtlevbel_url_ck CHECK (btrim(documenturl) <> ''),
    CONSTRAINT mhtlevbel_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX mhtlevbel_muhatapvid_ix ON mhtlevbel (muhatapvid);
CREATE INDEX mhtlevbel_gecerlilik_ix ON mhtlevbel (songecerliliktarihi);
