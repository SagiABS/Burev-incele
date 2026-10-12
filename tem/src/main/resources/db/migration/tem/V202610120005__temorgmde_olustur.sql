-- temorgmde : Organizasyon ek alan tanımları (metadata).
-- Neden var: yönetici kod yazmadan bir tabloya (örn. mhtlevmuf) yeni alan tanımlayabilsin.
-- Girilen değerler ilgili tablonun ekbilgiler (JSONB) sütununda, alanadi anahtarıyla tutulur.
CREATE TABLE temorgmde (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tablokodu      VARCHAR(10)  NOT NULL,   -- ek alanın ekleneceği tablo
    alanadi        VARCHAR(50)  NOT NULL,   -- teknik ad = JSON anahtarı (küçük harf, rakam, _)
    alanetiketi    VARCHAR(100) NOT NULL,   -- ekranda görünen başlık
    verituru       VARCHAR(20)  NOT NULL,   -- TEXT, NUMBER, DATE, BOOLEAN
    zorunlumu      BOOLEAN      NOT NULL DEFAULT FALSE,
    orgvid         BIGINT       REFERENCES temorgorg (vid),   -- boşsa tüm organizasyonlar
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
    CONSTRAINT temorgmde_tablokodu_ck CHECK (btrim(tablokodu) <> ''),
    CONSTRAINT temorgmde_alanadi_ck   CHECK (alanadi ~ '^[a-z][a-z0-9_]*$'),
    CONSTRAINT temorgmde_etiket_ck    CHECK (btrim(alanetiketi) <> ''),
    CONSTRAINT temorgmde_verituru_ck  CHECK (verituru IN ('TEXT','NUMBER','DATE','BOOLEAN')),
    CONSTRAINT temorgmde_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Aynı tablo+alan adı, aynı organizasyon kapsamında bir kez tanımlanır (orgvid boşsa "tümü" kapsamı)
CREATE UNIQUE INDEX temorgmde_tanim_uq ON temorgmde (tablokodu, alanadi, COALESCE(orgvid, 0));
