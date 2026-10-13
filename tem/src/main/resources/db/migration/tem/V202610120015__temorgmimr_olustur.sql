-- temorgmimr : Burevers mimari (yapı) kayıtları: modül, paket, klasör, tablo ve sınıflar (hiyerarşik).
-- Neden var: Yapı Mimarı Excel'inin veritabanı karşılığıdır; menü kısa kodunun (siskod) ve uygulama içi
-- belgelemenin kaynağıdır. Kayıtlar Flyway göçleriyle (tohum veri) eklenir; kodla senkronu mimari testle denetlenir.
-- Hiyerarşi: modül > paket > tablo > sınıf (ustvid ile).
-- siskod: <modül 3 harf>_<ebced>_<sıra 3 hane>; ebced ve sıra değerlerini kullanıcı verir.
-- ana / tali / detay: modül, paket grubu ve tablo adı bölümleri (örn. mht / lev / muf = mhtlevmuf).
CREATE TABLE temorgmimr (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ustvid         BIGINT       REFERENCES temorgmimr (vid),   -- üst kayıt (modülde boş)
    sira           INTEGER      NOT NULL,   -- sıralama kodu (örn. 201010)
    ana            VARCHAR(3),              -- modül kodu (örn. mht)
    tali           VARCHAR(3),              -- paket kodunun grup bölümü (örn. lev)
    detay          VARCHAR(4),              -- tablo adı bölümü (örn. muf, blok)
    adi            VARCHAR(60)  NOT NULL,   -- teknik ad (örn. temorgtes, TemorgtesView)
    tanimi         VARCHAR(200),            -- Türkçe tanım (örn. Tesis Tanımla)
    islevi         VARCHAR(1000),           -- kaydın işlevi ve önemli bilgi açıklamaları
    terim          VARCHAR(20)  NOT NULL,   -- kaydın türü
    siskod         VARCHAR(20),             -- sistematik kod (menü/arama kodu); ebced gelene kadar boş olabilir
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
    CONSTRAINT temorgmimr_siskod_uq UNIQUE (siskod),
    CONSTRAINT temorgmimr_siskod_ck CHECK (siskod ~ '^(tem|bel|muh|mht)_[0-9]+_[0-9]{3}$'),
    CONSTRAINT temorgmimr_adi_ck CHECK (btrim(adi) <> ''),
    CONSTRAINT temorgmimr_terim_ck CHECK (terim IN (
        'MODUL','PAKET','KLASOR','TABLO','ENTITY','REPOSITORY','SERVICE','VIEW','DIALOG',
        'CONTROLLER','DTO','VALIDATOR','EVENT','TEST')),
    CONSTRAINT temorgmimr_ust_ck CHECK (ustvid IS NULL OR ustvid <> vid),
    CONSTRAINT temorgmimr_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX temorgmimr_ustvid_ix ON temorgmimr (ustvid, sira);
