-- mhtlevilt : Muhatap iletişim bilgileri.
-- Neden var: telefon, e-posta, KEP, web adreslerini ve ilgili kişiyi muhatap kartına bağlamak için.
-- İletişim kişisi de muhatap kartıdır (iletisimmhtvid); kişi seçilmeyecekse boş bırakılır (ilgilikisi serbest metin).
CREATE TABLE mhtlevilt (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    muhatapvid     BIGINT       NOT NULL REFERENCES mhtlevmuf (vid),
    iletisimmhtvid BIGINT       REFERENCES mhtlevmuf (vid),   -- iletişim kişisi (ad alanı dolu muhatap)
    iletisimtipi   VARCHAR(20)  NOT NULL,   -- TELEFON, EPOSTA, KEP, WEB
    iletisimbilgisi VARCHAR(150) NOT NULL,
    ilgilikisi     VARCHAR(100),   -- ilgili departman / yetkili adı (serbest metin)
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
    CONSTRAINT mhtlevilt_tipi_ck CHECK (iletisimtipi IN ('TELEFON','EPOSTA','KEP','WEB')),
    CONSTRAINT mhtlevilt_bilgi_ck CHECK (btrim(iletisimbilgisi) <> ''),
    CONSTRAINT mhtlevilt_kendi_ck CHECK (iletisimmhtvid IS NULL OR iletisimmhtvid <> muhatapvid),
    CONSTRAINT mhtlevilt_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX mhtlevilt_muhatapvid_ix ON mhtlevilt (muhatapvid);
CREATE INDEX mhtlevilt_iletisimmhtvid_ix ON mhtlevilt (iletisimmhtvid);
