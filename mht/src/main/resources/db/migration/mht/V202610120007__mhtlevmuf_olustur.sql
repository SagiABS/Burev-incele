-- mhtlevmuf : Muhatap (cari) ana kartı.
-- Neden var: müşteri, tedarikçi, personel, ortak, iletişim kişisi gibi tüm muhatapların tek merkezi kaydı.
-- Şahıs muhatapta ad/soyad dolu olur; iletişim kişileri de bu tabloda tutulur (mhtlevilt.iletisimmhtvid).
CREATE TABLE mhtlevmuf (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapkodu    VARCHAR(15)  NOT NULL,   -- muhatap (cari) kodu; şimdilik elle girilir
    muhatapadi     VARCHAR(250) NOT NULL,   -- resmi ticari ünvan
    ad             VARCHAR(100),            -- şahıs muhatap adı
    soyad          VARCHAR(100),            -- şahıs muhatap soyadı
    muhatapkisaadi VARCHAR(100),            -- kısa ad / marka
    kurturvid      BIGINT       NOT NULL REFERENCES temortktr (vid),   -- yasal şirket türü
    vergino        VARCHAR(30)  NOT NULL,   -- ülkeye göre VKN, TCKN, SSN, EIN...
    logourl        VARCHAR(500),            -- logo bağlantısı (bulut depolama)
    ekbilgiler     JSONB,                   -- yönetici tanımlı ek alanlar (temorgmde)
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
    CONSTRAINT mhtlevmuf_muhatapkodu_uq UNIQUE (muhatapkodu),
    CONSTRAINT mhtlevmuf_muhatapkodu_ck CHECK (btrim(muhatapkodu) <> ''),
    CONSTRAINT mhtlevmuf_muhatapadi_ck  CHECK (btrim(muhatapadi) <> ''),
    CONSTRAINT mhtlevmuf_vergino_ck     CHECK (btrim(vergino) <> ''),
    CONSTRAINT mhtlevmuf_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX mhtlevmuf_kurturvid_ix ON mhtlevmuf (kurturvid);
CREATE INDEX mhtlevmuf_vergino_ix ON mhtlevmuf (vergino);
CREATE INDEX mhtlevmuf_ekbilgiler_gin ON mhtlevmuf USING GIN (ekbilgiler);
