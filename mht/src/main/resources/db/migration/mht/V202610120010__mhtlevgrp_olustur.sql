-- mhtlevgrp : Muhatap - grup ilişkisi.
-- Neden var: bir muhatap birden çok gruba (örn. Yurt İçi Müşteri, Stratejik Tedarikçi) üye olabilsin.
-- Gruplar temortetk (etiket) tablosundadır.
CREATE TABLE mhtlevgrp (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
    grupvid        BIGINT       NOT NULL REFERENCES temortetk (vid),
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
    CONSTRAINT mhtlevgrp_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX mhtlevgrp_cift_aktif_uq ON mhtlevgrp (muhatapvid, grupvid) WHERE NOT pasif;
CREATE INDEX mhtlevgrp_grupvid_ix ON mhtlevgrp (grupvid);
