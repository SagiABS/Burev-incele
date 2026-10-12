-- temortetk (menü: tem_1468_000) : Etiket tanımları.
-- Neden var: kuruluş/tesis gibi kayıtlara bağlanabilen, sistem genelinde ortak etiketleri tutar.
-- alftanimi = alternatif tanım. dbtablosu ileride tablo tanımları tablosuna yabancı anahtar olacak.
CREATE TABLE temortetk (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dbtablosu      VARCHAR(10)  NOT NULL,   -- etiketin ait olduğu tablo (sistem referansı, örn. temorgkur)
    etiketkodu     VARCHAR(4)   NOT NULL,
    etiketadi      VARCHAR(50)  NOT NULL,
    etkgrubu       VARCHAR(10),
    alftanimi      VARCHAR(100),             -- alternatif tanım
    etiketnot      VARCHAR(200),
    acilistarihi   DATE         NOT NULL,
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
    CONSTRAINT temortetk_etiketkodu_ck CHECK (btrim(etiketkodu) <> ''),
    CONSTRAINT temortetk_etiketadi_ck  CHECK (btrim(etiketadi) <> ''),
    CONSTRAINT temortetk_dbtablosu_ck  CHECK (btrim(dbtablosu) <> ''),
    CONSTRAINT temortetk_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL)),
    -- Aynı tablo için aynı etiket kodu iki kez tanımlanamaz
    CONSTRAINT temortetk_tablo_kod_uq UNIQUE (dbtablosu, etiketkodu)
);
