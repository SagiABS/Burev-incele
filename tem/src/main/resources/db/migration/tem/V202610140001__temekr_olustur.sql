-- Ekran düzeni paketi (temekr): genel ekran iskeletinin veriyle tanımlanması (ESNEK TASARIM, belge 11).
-- Fikir: hangi alanın hangi bölümde, bölümün neresinde ve hangi çerçevede duracağı kodla değil veriyle tanımlanır.
--   temekrtan : ekran tanımı (siskod + varyant, şablon, ana tablo, kapsam, sürüm)
--   temekrbol : bölüm / çerçeve (iç içe olabilir; sekme grubu, form, tablo, özet)
--   temekryer : form bölümündeki alan yerleşimi (alan temcusdba'dan seçilir; dinamik alanlar dahil)
--   temekrkol : tablo (grid) bölümündeki kolon tanımı
-- Kalıtım: kapsam boşsa sistem varsayılanı; organizasyon / kuruluş / tesis dolu ise o kapsama özel düzen (temcusmde ile aynı kural).
-- Sürüm: düzen TASLAK -> YAYIN; kapsam başına tek YAYIN kaydı.
-- Güvenlik: düzende olmayan alan ekranda yoktur; alan yetkisi (temkulaly) sunucuda uygulanır, düzen yetkiyi aşamaz.

-- temekrtan : Ekran tanımı.
-- siskod ekranı açan menü adımının kodudur; aynı siskod için birden çok varyant olabilir (giriş 1, giriş 2, belge türüne göre).
-- secimalani + secimdegeri: varyantın hangi koşulda seçileceği (örn. belgeturu = FATURA); ikisi birlikte dolu ya da boş.
CREATE TABLE temekrtan (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    siskod         VARCHAR(20)  NOT NULL,   -- menü adımının siskod'u
    varyant        VARCHAR(10)  NOT NULL DEFAULT 'ANA',
    baslik         VARCHAR(100) NOT NULL,
    sablon         VARCHAR(10)  NOT NULL,   -- LISTE (liste ekranı) veya BELGE (belge giriş ekranı)
    tablokodu      VARCHAR(10)  NOT NULL,   -- ekranın ana tablosu
    secimalani     VARCHAR(50),             -- varyant seçim koşulu: alan
    secimdegeri    VARCHAR(30),             -- varyant seçim koşulu: değer
    orgvid         BIGINT       REFERENCES temorgorg (vid),   -- boşsa sistem varsayılanı
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    surum          INTEGER      NOT NULL DEFAULT 1,
    durum          VARCHAR(10)  NOT NULL DEFAULT 'TASLAK',   -- TASLAK veya YAYIN
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
    CONSTRAINT temekrtan_sablon_ck CHECK (sablon IN ('LISTE', 'BELGE')),
    CONSTRAINT temekrtan_durum_ck CHECK (durum IN ('TASLAK', 'YAYIN')),
    CONSTRAINT temekrtan_siskod_ck CHECK (siskod ~ '^(tem|bel|muh|mht)_[0-9]+_[0-9]{3}$'),
    CONSTRAINT temekrtan_varyant_ck CHECK (btrim(varyant) <> ''),
    CONSTRAINT temekrtan_secim_ck CHECK ((secimalani IS NULL) = (secimdegeri IS NULL)),
    CONSTRAINT temekrtan_kapsam_ck CHECK ((kurvid IS NULL OR orgvid IS NOT NULL) AND (tesvid IS NULL OR kurvid IS NOT NULL)),
    CONSTRAINT temekrtan_surum_ck CHECK (surum > 0),
    CONSTRAINT temekrtan_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Aynı ekran, varyant ve kapsamda sürüm numarası bir kez; ve kapsam başına tek yayındaki sürüm
CREATE UNIQUE INDEX temekrtan_surum_uq
    ON temekrtan (siskod, varyant, COALESCE(orgvid, 0), COALESCE(kurvid, 0), COALESCE(tesvid, 0), surum) WHERE NOT pasif;
CREATE UNIQUE INDEX temekrtan_yayin_uq
    ON temekrtan (siskod, varyant, COALESCE(orgvid, 0), COALESCE(kurvid, 0), COALESCE(tesvid, 0))
    WHERE durum = 'YAYIN' AND NOT pasif;
CREATE INDEX temekrtan_siskod_ix ON temekrtan (siskod);
CREATE INDEX temekrtan_orgvid_ix ON temekrtan (orgvid);
CREATE INDEX temekrtan_kurvid_ix ON temekrtan (kurvid);
CREATE INDEX temekrtan_tesvid_ix ON temekrtan (tesvid);
-- Kapsam yapı ağacında bağlı olmalı (ortak fonksiyon, temcus göçünde tanımlıdır)
CREATE TRIGGER temekrtan_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temekrtan
    FOR EACH ROW EXECUTE FUNCTION temcus_kapsam_kontrol();

-- temekrbol : Ekran bölümü (çerçeve).
-- yuva: bölümün şablondaki yeri. LISTE şablonu: ORTA (veri satırları), ALT (ayrıntı sekmeleri), SAG (özet/ekler paneli).
-- BELGE şablonu: ORTAUST, ORTA, ORTAALT, SAG. turu: FORM (alanlar), GRID (tablo), SEKME (sekme grubu), OZET (toplamlar).
-- ustvid ile bölümler iç içe olabilir (örn. SEKME grubunun altında FORM sekmeleri). acik: ilk açılışta açık mı;
-- alt ve sağ paneller kapalı gelir, kullanıcı açar ya da sürükler. kaynaktablo: GRID bölümünün veri tablosu (boşsa ekranın ana tablosu).
CREATE TABLE temekrbol (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ekranvid       BIGINT       NOT NULL REFERENCES temekrtan (vid),
    ustvid         BIGINT       REFERENCES temekrbol (vid),
    kodu           VARCHAR(20)  NOT NULL,   -- ekran içinde benzersiz bölüm kodu
    turu           VARCHAR(10)  NOT NULL,
    yuva           VARCHAR(10)  NOT NULL,
    baslik         VARCHAR(100),
    sira           INTEGER      NOT NULL,
    sutunsayisi    SMALLINT     NOT NULL DEFAULT 1,       -- FORM bölümünün sütun sayısı (1-12)
    surukle        BOOLEAN      NOT NULL DEFAULT FALSE,   -- kullanıcı sürükleyip boyutlandırabilir mi?
    acik           BOOLEAN      NOT NULL DEFAULT TRUE,    -- ilk açılışta açık mı?
    kaynaktablo    VARCHAR(10),
    ayar           JSONB,                   -- seyrek kullanılan görünüm ayarları
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
    CONSTRAINT temekrbol_turu_ck CHECK (turu IN ('FORM', 'GRID', 'SEKME', 'OZET')),
    CONSTRAINT temekrbol_yuva_ck CHECK (yuva IN ('ORTAUST', 'ORTA', 'ORTAALT', 'ALT', 'SAG')),
    CONSTRAINT temekrbol_kodu_ck CHECK (btrim(kodu) <> ''),
    CONSTRAINT temekrbol_ust_ck CHECK (ustvid IS NULL OR ustvid <> vid),
    CONSTRAINT temekrbol_sutun_ck CHECK (sutunsayisi BETWEEN 1 AND 12),
    CONSTRAINT temekrbol_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temekrbol_kodu_uq ON temekrbol (ekranvid, kodu) WHERE NOT pasif;
CREATE INDEX temekrbol_ustvid_ix ON temekrbol (ustvid);
CREATE INDEX temekrbol_ekran_ix ON temekrbol (ekranvid, yuva, sira);

-- Üst bölüm aynı ekrana ait olmalı ve üst zincirde döngü olmamalı.
CREATE FUNCTION temekrbol_ust_kontrol() RETURNS trigger AS $$
DECLARE
    ust_ekran BIGINT;
BEGIN
    IF NEW.ustvid IS NULL THEN
        RETURN NEW;
    END IF;
    SELECT ekranvid INTO ust_ekran FROM temekrbol WHERE vid = NEW.ustvid;
    IF ust_ekran IS DISTINCT FROM NEW.ekranvid THEN
        RAISE EXCEPTION 'Üst bölüm (%) başka bir ekrana ait', NEW.ustvid USING ERRCODE = '23514';
    END IF;
    IF EXISTS (WITH RECURSIVE zincir AS (
                   SELECT vid, ustvid FROM temekrbol WHERE vid = NEW.ustvid
                   UNION
                   SELECT b.vid, b.ustvid FROM temekrbol b JOIN zincir z ON b.vid = z.ustvid)
               SELECT 1 FROM zincir WHERE vid = NEW.vid) THEN
        RAISE EXCEPTION 'Üst bölüm zincirinde döngü oluşur (bölüm %)', NEW.vid USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER temekrbol_ust_kontrol_tg
    BEFORE INSERT OR UPDATE ON temekrbol
    FOR EACH ROW EXECUTE FUNCTION temekrbol_ust_kontrol();

-- temekryer : Form bölümündeki alan yerleşimi.
-- Neden var: veritabanından gelen alanın hangi bölümde, hangi satır ve sütunda, kaç sütun genişliğinde duracağını belirler.
-- Alan temcusdba'dan seçilir (dinamik alanlar dahil). kontrol: METIN, NOT (çok satırlı), SAYI, TARIH, LISTE (açılır liste),
-- ARASEC (arayarak seç), ONAY (işaret kutusu). ekledugme / ayardugme: ARASEC yanındaki "ekle" ve "ayar" düğmeleri.
-- gorunurkosul: belge türüne göre görünürlük (örn. belgeturu=FATURA); boşsa her zaman görünür.
CREATE TABLE temekryer (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bolumvid       BIGINT       NOT NULL REFERENCES temekrbol (vid),
    dbavid         BIGINT       NOT NULL REFERENCES temcusdba (vid),
    satir          SMALLINT     NOT NULL,
    sutun          SMALLINT     NOT NULL,
    kapsama        SMALLINT     NOT NULL DEFAULT 1,       -- kaç sütun genişliğinde
    etiket         VARCHAR(100),                          -- boşsa alanın varsayılan etiketi
    kontrol        VARCHAR(10)  NOT NULL DEFAULT 'METIN',
    saltokunur     BOOLEAN      NOT NULL DEFAULT FALSE,
    zorunlu        BOOLEAN      NOT NULL DEFAULT FALSE,
    ekledugme      BOOLEAN      NOT NULL DEFAULT FALSE,
    ayardugme      BOOLEAN      NOT NULL DEFAULT FALSE,
    gorunurkosul   VARCHAR(100),
    ayar           JSONB,
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
    CONSTRAINT temekryer_konum_ck CHECK (satir >= 1 AND sutun BETWEEN 1 AND 12 AND kapsama BETWEEN 1 AND 12 AND sutun + kapsama - 1 <= 12),
    CONSTRAINT temekryer_kontrol_ck CHECK (kontrol IN ('METIN', 'NOT', 'SAYI', 'TARIH', 'LISTE', 'ARASEC', 'ONAY')),
    CONSTRAINT temekryer_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temekryer_alan_uq ON temekryer (bolumvid, dbavid) WHERE NOT pasif;
CREATE UNIQUE INDEX temekryer_konum_uq ON temekryer (bolumvid, satir, sutun) WHERE NOT pasif;
CREATE INDEX temekryer_dbavid_ix ON temekryer (dbavid);

-- Alan yerleşimi yalnızca FORM ve OZET bölümlerine yapılabilir.
CREATE FUNCTION temekryer_bolum_kontrol() RETURNS trigger AS $$
DECLARE
    bolum_turu VARCHAR(10);
BEGIN
    SELECT turu INTO bolum_turu FROM temekrbol WHERE vid = NEW.bolumvid;
    IF bolum_turu NOT IN ('FORM', 'OZET') THEN
        RAISE EXCEPTION 'Alan yerleşimi yalnızca FORM ve OZET bölümlerine yapılır (bölüm %, tür %)', NEW.bolumvid, bolum_turu USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER temekryer_bolum_kontrol_tg
    BEFORE INSERT OR UPDATE ON temekryer
    FOR EACH ROW EXECUTE FUNCTION temekryer_bolum_kontrol();

-- temekrkol : Tablo (grid) bölümündeki kolon tanımı.
-- Neden var: veri satırlarında hangi alanların, hangi sırayla, hangi genişlik ve biçimde, toplamlı ya da filtreli görüneceğini belirler.
-- Alan temcusdba'dan seçilir. toplam: YOK, TOPLAM, ORTALAMA, SAYI (kolon altı özet).
CREATE TABLE temekrkol (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bolumvid       BIGINT       NOT NULL REFERENCES temekrbol (vid),
    dbavid         BIGINT       NOT NULL REFERENCES temcusdba (vid),
    sira           INTEGER      NOT NULL,
    etiket         VARCHAR(100),                          -- boşsa alanın varsayılan etiketi
    genislik       SMALLINT,                              -- piksel (boşsa otomatik)
    bicim          VARCHAR(10),                           -- örn. TUTAR, TARIH, YUZDE
    toplam         VARCHAR(10)  NOT NULL DEFAULT 'YOK',
    siralanabilir  BOOLEAN      NOT NULL DEFAULT TRUE,
    filtrelenebilir BOOLEAN     NOT NULL DEFAULT TRUE,
    gorunur        BOOLEAN      NOT NULL DEFAULT TRUE,    -- ilk açılışta görünür mü? (kullanıcı kolon seçiminden açabilir)
    ayar           JSONB,
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
    CONSTRAINT temekrkol_toplam_ck CHECK (toplam IN ('YOK', 'TOPLAM', 'ORTALAMA', 'SAYI')),
    CONSTRAINT temekrkol_genislik_ck CHECK (genislik IS NULL OR genislik BETWEEN 20 AND 1000),
    CONSTRAINT temekrkol_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temekrkol_alan_uq ON temekrkol (bolumvid, dbavid) WHERE NOT pasif;
CREATE UNIQUE INDEX temekrkol_sira_uq ON temekrkol (bolumvid, sira) WHERE NOT pasif;
CREATE INDEX temekrkol_dbavid_ix ON temekrkol (dbavid);

-- Kolon tanımı yalnızca GRID bölümlerine yapılır.
CREATE FUNCTION temekrkol_bolum_kontrol() RETURNS trigger AS $$
DECLARE
    bolum_turu VARCHAR(10);
BEGIN
    SELECT turu INTO bolum_turu FROM temekrbol WHERE vid = NEW.bolumvid;
    IF bolum_turu <> 'GRID' THEN
        RAISE EXCEPTION 'Kolon tanımı yalnızca GRID bölümlerine yapılır (bölüm %, tür %)', NEW.bolumvid, bolum_turu USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER temekrkol_bolum_kontrol_tg
    BEFORE INSERT OR UPDATE ON temekrkol
    FOR EACH ROW EXECUTE FUNCTION temekrkol_bolum_kontrol();

-- Veri sözlüğüne (temcusdba) yeni temekr tablolarının sütunları eklenir.
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
  AND c.table_name LIKE 'temekr%'
ORDER BY c.table_name, c.ordinal_position;
