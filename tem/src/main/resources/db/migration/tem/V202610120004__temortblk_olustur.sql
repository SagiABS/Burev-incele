-- temortblk : Tablo genel bloke tanımları (tüm modüller için ortak kilit kaydı).
-- Neden var: bir kartın (muhatap, ürün vb.) belirli bir organizasyon/kuruluş/tesis kırılımında,
-- belirli bir tarih aralığında belirli işlem türüne (satış, teslimat, ödeme...) kapatılabilmesi için.
-- tablokodu + kayitvid birlikte hedef kartı gösterir (polimorfik bağ; yabancı anahtar YOKTUR).
CREATE TABLE temortblk (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tablokodu      VARCHAR(10)  NOT NULL,   -- bloke edilen kartın tablo kodu (örn. mhtlevmuf)
    kayitvid       BIGINT       NOT NULL,   -- o tablodaki kartın vid'i
    orgvid         BIGINT       REFERENCES temorgorg (vid),
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    bloketipi      SMALLINT     NOT NULL,   -- hangi işlem türü kilitli (satış, teslimat, ödeme...); değerleri sonra tanımlanır
    baslangictarihi TIMESTAMPTZ NOT NULL,
    bitistarihi    TIMESTAMPTZ,             -- boşsa süresiz
    blokenedeni    VARCHAR(250),
    blokeuyari      VARCHAR(250),            -- işlem anında kullanıcıya gösterilecek mesaj
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
    CONSTRAINT temortblk_tablokodu_ck CHECK (btrim(tablokodu) <> ''),
    CONSTRAINT temortblk_bloketipi_ck CHECK (bloketipi > 0),
    CONSTRAINT temortblk_tarih_ck CHECK (bitistarihi IS NULL OR bitistarihi > baslangictarihi),
    -- Kırılım: organizasyon, kuruluş, tesis alanlarından en az biri dolu olmalı
    CONSTRAINT temortblk_kirilim_ck CHECK (orgvid IS NOT NULL OR kurvid IS NOT NULL OR tesvid IS NOT NULL),
    CONSTRAINT temortblk_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX temortblk_kayit_ix ON temortblk (tablokodu, kayitvid);
