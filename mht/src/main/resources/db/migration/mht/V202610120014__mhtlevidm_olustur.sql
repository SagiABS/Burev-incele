-- mhtlevidm : Holding içi (intercompany) kuruluşlar arası muhatap-rol ilişkisi.
-- Neden var: A kuruluşu B'ye satış yaparken A için B "müşteri", B için A "tedarikçi" olur; iki tarafın
-- karşılıklı tanımı tek kayıtta tutulur. kkuroto: karşı kuruluşa kaydın otomatik eklenmesi isteniyor mu
-- (otomatik ekleme servis katmanında yapılacak).
CREATE TABLE mhtlevidm (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kurvid         BIGINT       NOT NULL REFERENCES temorgkur (vid),    -- kaynak kuruluş
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),    -- kaynak taraftaki muhatap
    rolvid         BIGINT       NOT NULL REFERENCES mhttemrol (vid),    -- kaynağın bu muhataba biçtiği rol
    kkuroto        BOOLEAN      NOT NULL DEFAULT FALSE,                 -- karşı kuruluşa otomatik kayıt
    kkurvid        BIGINT       NOT NULL REFERENCES temorgkur (vid),    -- karşı kuruluş
    kmuhatapvid    BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),    -- karşı taraftaki muhatap
    krolvid        BIGINT       NOT NULL REFERENCES mhttemrol (vid),   -- karşı rol
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
    CONSTRAINT mhtlevidm_kuruluslar_ck CHECK (kurvid <> kkurvid),
    CONSTRAINT mhtlevidm_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX mhtlevidm_iliski_aktif_uq
    ON mhtlevidm (kurvid, muhatapvid, rolvid, kkurvid, kmuhatapvid, krolvid) WHERE NOT pasif;
CREATE INDEX mhtlevidm_kkurvid_ix ON mhtlevidm (kkurvid);
