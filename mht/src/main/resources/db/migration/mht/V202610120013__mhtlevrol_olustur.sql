-- mhtlevrol : Muhatabın kuruluş bazında üstlendiği roller ve muhasebe hesap kodu.
-- Neden var: aynı muhatap A şirketinde müşteri, B şirketinde tedarikçi olabilir; hesap kodu rol + kuruluşa özeldir.
-- hspplnkodu roldan (mhttemrol) otomatik gelir, kullanıcı 30 karaktere kadar değiştirebilir.
CREATE TABLE mhtlevrol (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
    kurvid         BIGINT       NOT NULL REFERENCES temorgkur (vid),
    rolvid         BIGINT       NOT NULL REFERENCES mhttemrol (vid),
    hspplnkodu     VARCHAR(30)  NOT NULL,   -- kuruluşa özel muhasebe hesap kodu (örn. 120.01.001)
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
    CONSTRAINT mhtlevrol_hspplnkodu_ck CHECK (btrim(hspplnkodu) <> ''),
    CONSTRAINT mhtlevrol_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX mhtlevrol_uclu_aktif_uq ON mhtlevrol (muhatapvid, kurvid, rolvid) WHERE NOT pasif;
CREATE INDEX mhtlevrol_kurvid_ix ON mhtlevrol (kurvid);
CREATE INDEX mhtlevrol_rolvid_ix ON mhtlevrol (rolvid);
