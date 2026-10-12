-- temortetk (menü: tem_1468_000) : Etiket tanımları.
-- Neden var: kuruluş/tesis gibi kayıtlara bağlanabilen, sistem genelinde ortak etiketleri tutar.
-- (alftanimi / etkgrubu / dbtablosu alanlarının tam anlamı kullanıcıdan teyit bekliyor.)
CREATE TABLE temortetk (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dbtablosu      VARCHAR(9)   NOT NULL,   -- etiketin ait olduğu tablo (sistem referansı, örn. temorgkur)
    etiketkodu     VARCHAR(3)   NOT NULL,
    etiketadi      VARCHAR(50)  NOT NULL,
    etkgrubu       VARCHAR(10),
    alftanimi      VARCHAR(100),
    etiketnot      VARCHAR(200),
    acilistarihi   DATE         NOT NULL,
    -- Pasif yapma bilgisi: üçü birlikte dolu (pasif) ya da üçü birlikte boş (aktif) olmalı
    pasif          BOOLEAN      NOT NULL DEFAULT FALSE,
    pasiftarihi    DATE,
    pasifnedeni    VARCHAR(10),
    -- Oluşturma / güncelleme izi
    c_kullanici    VARCHAR(30)  NOT NULL,
    c_tarih        DATE         NOT NULL,
    c_saat         TIME         NOT NULL,
    u_kullanici    VARCHAR(30),
    u_tarih        DATE,
    u_saat         TIME,
    CONSTRAINT temortetk_etiketkodu_ck CHECK (char_length(etiketkodu) = 3),
    CONSTRAINT temortetk_etiketadi_ck  CHECK (btrim(etiketadi) <> ''),
    CONSTRAINT temortetk_dbtablosu_ck  CHECK (char_length(dbtablosu) = 9),
    CONSTRAINT temortetk_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL)),
    -- Aynı tablo için aynı etiket kodu iki kez tanımlanamaz
    CONSTRAINT temortetk_tablo_kod_uq UNIQUE (dbtablosu, etiketkodu)
);
