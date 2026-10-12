-- temortadr : Ortak adres havuzu.
-- Neden var: aynı adres birden çok muhatap/kuruluş tarafından tekrar kaydedilmeden kullanılabilsin.
-- Muhatapla bağ bu tabloda DEĞİL, mht modülündeki ilişki tablosundadır (tem, mht'ye bağımlı olamaz).
-- adrestipi alanı tablo netleşince (temortatp) eklenecek.
CREATE TABLE temortadr (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    adresbasligi   VARCHAR(50)  NOT NULL,   -- hızlı seçim için ad (örn. Merkez Depo)
    ulkevid        BIGINT       NOT NULL REFERENCES temortgeo (vid),
    sehirvid       BIGINT       NOT NULL REFERENCES temortgeo (vid),
    bolgevid       BIGINT       NOT NULL REFERENCES temortgeo (vid),   -- ilçe/bölge
    mahallevid     BIGINT       REFERENCES temortgeo (vid),            -- listeden seçilirse
    mahalletext    VARCHAR(100),                                       -- listede yoksa serbest metin
    adrestext      VARCHAR(500) NOT NULL,   -- cadde, sokak, açık adres
    binano         VARCHAR(10),
    katno          VARCHAR(5),
    daireno        VARCHAR(10),
    postakodu      VARCHAR(10),
    cografiurl     VARCHAR(500),            -- harita/konum bağlantısı
    enlem          NUMERIC(11,8),           -- -90..90, 8 ondalık
    boylam         NUMERIC(11,8),           -- -180..180, 8 ondalık
    ekbilgiler     JSONB,                   -- yönetici tanımlı ek alanlar (temorgmde)
    refadresvid    BIGINT       REFERENCES temortadr (vid),   -- seçilince bilgileri kopyalanan adres
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
    CONSTRAINT temortadr_baslik_ck CHECK (btrim(adresbasligi) <> ''),
    CONSTRAINT temortadr_adrestext_ck CHECK (btrim(adrestext) <> ''),
    CONSTRAINT temortadr_enlem_ck  CHECK (enlem  IS NULL OR enlem  BETWEEN -90  AND 90),
    CONSTRAINT temortadr_boylam_ck CHECK (boylam IS NULL OR boylam BETWEEN -180 AND 180),
    -- Koordinat ya ikisi birden dolu ya ikisi birden boş
    CONSTRAINT temortadr_koordinat_ck CHECK ((enlem IS NULL) = (boylam IS NULL)),
    CONSTRAINT temortadr_refadres_ck CHECK (refadresvid IS NULL OR refadresvid <> vid),
    CONSTRAINT temortadr_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE INDEX temortadr_sehirvid_ix ON temortadr (sehirvid);
CREATE INDEX temortadr_ulkevid_ix ON temortadr (ulkevid);
CREATE INDEX temortadr_ekbilgiler_gin ON temortadr USING GIN (ekbilgiler);
