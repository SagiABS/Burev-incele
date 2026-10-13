-- Özelleştirme (temcus) paketi: veri sözlüğü (temcusdba) ve tablolar arası alan eşleme (temcusidm).
-- Karar (ADR-09, belge 10): çalışma zamanında DDL ve Java yükleme yoktur; özelleştirme veri tabanlıdır.

-- Kapsam kontrol fonksiyonu genelleştirilir: temcusmde ve temcusidm ortak kullanır.
-- (Organizasyon / kuruluş / tesis yapı ağacında aktif olarak bağlı olmalıdır.)
ALTER FUNCTION temcusmde_kapsam_kontrol() RENAME TO temcus_kapsam_kontrol;

-- temcusdba : Tablolar ve alanları (veri sözlüğü).
-- Neden var: veritabanındaki tabloların alanlarını, veri türünü, işlevini ve kimin açtığını tek yerde tutmak;
-- temcusidm eşlemeleri kaynak ve hedef alanı buradan seçer. temcusmde ile eklenen dinamik alanlar da buradadır.
-- kaynak: SISTEM = göçle açılan gerçek sütun, DINAMIK = temcusmde ile tanımlanan ek alan (mdevid dolu).
-- Açan kişi: c_kullanici (SISTEM kayıtlarında 'SISTEM').
CREATE TABLE temcusdba (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tablokodu      VARCHAR(10)  NOT NULL,   -- tablo kodu (örn. mhtlevmuf)
    alanadi        VARCHAR(50)  NOT NULL,   -- sütun adı ya da dinamik alanın JSON anahtarı
    veritipi       VARCHAR(30)  NOT NULL,   -- veri türü (örn. VARCHAR(30), BIGINT, JSONB, TEXT, NUMBER)
    islevi         VARCHAR(500),            -- alanın işlevi / açıklaması
    kaynak         VARCHAR(10)  NOT NULL,   -- SISTEM veya DINAMIK
    mdevid         BIGINT       REFERENCES temcusmde (vid),   -- dinamik alanın tanımı (kaynak DINAMIK ise dolu)
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
    CONSTRAINT temcusdba_kaynak_ck CHECK (kaynak IN ('SISTEM', 'DINAMIK')),
    CONSTRAINT temcusdba_mde_ck CHECK ((kaynak = 'DINAMIK') = (mdevid IS NOT NULL)),
    CONSTRAINT temcusdba_tablokodu_ck CHECK (btrim(tablokodu) <> ''),
    CONSTRAINT temcusdba_alanadi_ck CHECK (btrim(alanadi) <> ''),
    CONSTRAINT temcusdba_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Sistem alanı bir tabloda bir kez; dinamik alan tanımı (mdevid) bir kez
CREATE UNIQUE INDEX temcusdba_sistem_uq ON temcusdba (tablokodu, alanadi) WHERE mdevid IS NULL;
CREATE UNIQUE INDEX temcusdba_mdevid_uq ON temcusdba (mdevid) WHERE mdevid IS NOT NULL;
CREATE INDEX temcusdba_tablokodu_ix ON temcusdba (tablokodu);

-- temcusidm : Tablolar arası alan eşleme (Temel Custom İdame).
-- Neden var: özel durumlarda bir tablodaki alanın başka tablodaki hangi alanla ilişkili olduğunu tutar;
-- kayıt atılırken bu ilişkiden yararlanılır. Kaynak ve hedef alan temcusdba'dan seçilir.
-- Kapsam temcusmde ile aynıdır: yalnız organizasyon = tümü; + kuruluş = kuruluşun tesisleri; + tesis = yalnız o tesis.
-- eslemetipi: KOPYA = değer aynen aktarılır; DONUSUM = donusumkodu ile kayıtlı dönüşümden geçer;
-- HESAP = donusumkodu ile kayıtlı hesaplama uzantısı (Strategy) çalışır. Kod yazan uzantı çalışma zamanında yüklenmez
-- (belge 10); donusumkodu çekirdekteki kayıtlı uzantıyı ya da müşterinin dış servisini işaret eder.
CREATE TABLE temcusidm (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orgvid         BIGINT       NOT NULL REFERENCES temorgorg (vid),
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    kaynakdbavid   BIGINT       NOT NULL REFERENCES temcusdba (vid),   -- kaynak tablo alanı
    hedefdbavid    BIGINT       NOT NULL REFERENCES temcusdba (vid),   -- hedef tablo alanı
    eslemetipi     VARCHAR(10)  NOT NULL,
    donusumkodu    VARCHAR(30),             -- kayıtlı dönüşüm / uzantı anahtarı (örn. KDV_HESAP)
    sira           INTEGER      NOT NULL DEFAULT 1,   -- aynı kaynak için çalışma sırası
    gecerlibas     DATE         NOT NULL,   -- geçerlilik başlangıcı
    gecerlibit     DATE,                    -- geçerlilik bitişi (boşsa süresiz)
    aciklama       VARCHAR(200),
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
    CONSTRAINT temcusidm_tip_ck CHECK (eslemetipi IN ('KOPYA', 'DONUSUM', 'HESAP')),
    CONSTRAINT temcusidm_kod_ck CHECK (eslemetipi = 'KOPYA' OR donusumkodu IS NOT NULL),
    CONSTRAINT temcusidm_alan_ck CHECK (kaynakdbavid <> hedefdbavid),
    CONSTRAINT temcusidm_kapsam_ck CHECK (tesvid IS NULL OR kurvid IS NOT NULL),
    CONSTRAINT temcusidm_tarih_ck CHECK (gecerlibit IS NULL OR gecerlibit >= gecerlibas),
    CONSTRAINT temcusidm_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Aynı kapsamda aynı kaynak-hedef eşlemesi, aynı başlangıç tarihiyle bir kez (aktif kayıtlar arasında)
CREATE UNIQUE INDEX temcusidm_eslesme_uq
    ON temcusidm (orgvid, COALESCE(kurvid, 0), COALESCE(tesvid, 0), kaynakdbavid, hedefdbavid, gecerlibas)
    WHERE NOT pasif;
CREATE INDEX temcusidm_kaynak_ix ON temcusidm (kaynakdbavid);
CREATE INDEX temcusidm_hedef_ix ON temcusidm (hedefdbavid);
CREATE INDEX temcusidm_kurvid_ix ON temcusidm (kurvid);
CREATE INDEX temcusidm_tesvid_ix ON temcusidm (tesvid);

CREATE TRIGGER temcusidm_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temcusidm
    FOR EACH ROW EXECUTE FUNCTION temcus_kapsam_kontrol();

-- Başlangıç (tohum): bu güne kadar açılan gerçek tablolar ve sütunları veritabanından okunur.
-- Sonraki göçlerle eklenecek tablolar için eşitleme servisi yazılacaktır.
INSERT INTO temcusdba (tablokodu, alanadi, veritipi, islevi, kaynak, c_kullanici, c_tarih, c_saat)
SELECT c.table_name, c.column_name,
       CASE WHEN c.data_type = 'character varying' THEN 'VARCHAR(' || c.character_maximum_length || ')'
            WHEN c.data_type = 'numeric' THEN 'NUMERIC(' || c.numeric_precision || ',' || c.numeric_scale || ')'
            ELSE upper(c.data_type) END,
       CASE c.column_name
           WHEN 'vid' THEN 'Kayıt anahtarı (otomatik artan, BIGINT IDENTITY).'
           WHEN 'pasif' THEN 'Kaydın pasif olup olmadığı; kayıtlar silinmez, pasif yapılır.'
           WHEN 'pasiftarihi' THEN 'Kaydın pasif yapıldığı tarih; aktifse boştur.'
           WHEN 'pasifnedeni' THEN 'Pasif neden kodu; ileride neden tablosuna yabancı anahtar olacaktır.'
           WHEN 'c_kullanici' THEN 'Kaydı oluşturan kullanıcı.'
           WHEN 'c_tarih' THEN 'Kaydın oluşturulma tarihi.'
           WHEN 'c_saat' THEN 'Kaydın oluşturulma saati.'
           WHEN 'u_kullanici' THEN 'Kaydı son güncelleyen kullanıcı.'
           WHEN 'u_tarih' THEN 'Kaydın son güncellenme tarihi.'
           WHEN 'u_saat' THEN 'Kaydın son güncellenme saati.'
           WHEN 'ekbilgiler' THEN 'Yönetici tanımlı ek alanların değerleri (JSONB); anahtarlar temcusmde.alanadi.'
           ELSE NULL END,
       'SISTEM', 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM information_schema.columns c
WHERE c.table_schema = current_schema()
  AND c.table_name ~ '^(tem|bel|muh|mht)[a-z]{3}[a-z]{3,4}$'
ORDER BY c.table_name, c.ordinal_position;
