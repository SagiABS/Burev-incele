-- Dil paketi (temdil): proje dilleri ve çeviri metinleri (ADR-11: arayüz için hazır metin + veritabanı ezmesi).
--   temdiltur : proje dilleri (tr, en, ar...) ve yazı yönü (soldan/sağdan)
--   temdilcvr : çeviri metinleri; anahtar + dil + kapsam -> metin
-- Metin seçim sırası (servis katmanı): ekrana özel etiket -> kapsamdaki (tesis > kuruluş > organizasyon) ezme metni
--   -> sistem metni -> varsayılan dildeki metin -> anahtarın kendisi/alan adı.
-- Anahtar biçimi: alan.<tablokodu>.<alanadi>, alan.<alanadi>, dugme.<ad>, menu.<kod>, mesaj.<ad>, panel.<ad>.

-- temdiltur : Proje dilleri.
-- kodu: dil kodu (küçük harf; tr, en, ar, pt-br). yon: LTR soldan sağa, RTL sağdan sola (Arapça).
-- varsayilan: kullanıcının dili ya da metni yoksa kullanılan dil; en fazla bir aktif varsayılan dil olabilir.
CREATE TABLE temdiltur (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kodu           VARCHAR(10)  NOT NULL,   -- dil kodu (BCP-47 küçük harf), örn. tr
    adi            VARCHAR(50)  NOT NULL,   -- sistem dilinde adı, örn. Türkçe
    yerelad        VARCHAR(50)  NOT NULL,   -- dilin kendi yazısıyla adı, örn. العربية
    yon            VARCHAR(3)   NOT NULL DEFAULT 'LTR',
    varsayilan     BOOLEAN      NOT NULL DEFAULT FALSE,
    sira           INTEGER      NOT NULL DEFAULT 100,   -- dil seçicideki sıra
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
    CONSTRAINT temdiltur_kodu_ck CHECK (kodu ~ '^[a-z]{2,3}(-[a-z0-9]{2,8})*$'),
    CONSTRAINT temdiltur_adi_ck CHECK (btrim(adi) <> '' AND btrim(yerelad) <> ''),
    CONSTRAINT temdiltur_yon_ck CHECK (yon IN ('LTR', 'RTL')),
    CONSTRAINT temdiltur_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL)),
    -- Pasif olan varsayılan dil olamaz
    CONSTRAINT temdiltur_varsayilan_ck CHECK (NOT (pasif AND varsayilan))
);
CREATE UNIQUE INDEX temdiltur_kodu_uq ON temdiltur (kodu) WHERE NOT pasif;
-- En fazla bir aktif varsayılan dil
CREATE UNIQUE INDEX temdiltur_varsayilan_uq ON temdiltur (varsayilan) WHERE varsayilan AND NOT pasif;

-- temdilcvr : Çeviri metinleri.
-- Kapsam (orgvid/kurvid/tesvid) boşsa metin sistem metnidir; dolu ise o kapsama özel ezme metnidir
-- (temcusmde ve temekrtan ile aynı kural). Bir anahtar bir dilde bir kapsamda yalnızca bir aktif metne sahip olur.
CREATE TABLE temdilcvr (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    anahtar        VARCHAR(100) NOT NULL,   -- örn. alan.temorgorg.kodu, dugme.kaydet
    dilvid         BIGINT       NOT NULL REFERENCES temdiltur (vid),
    orgvid         BIGINT       REFERENCES temorgorg (vid),   -- boşsa sistem metni
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    metin          VARCHAR(500) NOT NULL,
    aciklama       VARCHAR(200),            -- çevirmen notu
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
    CONSTRAINT temdilcvr_anahtar_ck CHECK (anahtar ~ '^[a-z0-9_.]+$'),
    CONSTRAINT temdilcvr_metin_ck CHECK (btrim(metin) <> ''),
    CONSTRAINT temdilcvr_kapsam_ck CHECK ((kurvid IS NULL OR orgvid IS NOT NULL) AND (tesvid IS NULL OR kurvid IS NOT NULL)),
    CONSTRAINT temdilcvr_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temdilcvr_anahtar_uq
    ON temdilcvr (anahtar, dilvid, COALESCE(orgvid, 0), COALESCE(kurvid, 0), COALESCE(tesvid, 0)) WHERE NOT pasif;
CREATE INDEX temdilcvr_dilvid_ix ON temdilcvr (dilvid);
CREATE INDEX temdilcvr_orgvid_ix ON temdilcvr (orgvid);
CREATE INDEX temdilcvr_kurvid_ix ON temdilcvr (kurvid);
CREATE INDEX temdilcvr_tesvid_ix ON temdilcvr (tesvid);
-- Kapsam yapı ağacında bağlı olmalı (ortak fonksiyon, temcus göçünde tanımlıdır)
CREATE TRIGGER temdilcvr_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temdilcvr
    FOR EACH ROW EXECUTE FUNCTION temcus_kapsam_kontrol();

-- İlk diller: Türkçe (varsayılan), İngilizce, Arapça (sağdan sola)
INSERT INTO temdiltur (kodu, adi, yerelad, yon, varsayilan, sira, c_kullanici, c_tarih, c_saat) VALUES
    ('tr', 'Türkçe', 'Türkçe', 'LTR', TRUE, 10, 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('en', 'İngilizce', 'English', 'LTR', FALSE, 20, 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('ar', 'Arapça', 'العربية', 'RTL', FALSE, 30, 'SISTEM', CURRENT_DATE, CURRENT_TIME);

-- Veri sözlüğüne (temcusdba) yeni temdil tablolarının sütunları eklenir.
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
           WHEN 'kodu' THEN 'Dil kodu (küçük harf BCP-47), örn. tr, en, ar.'
           WHEN 'yerelad' THEN 'Dilin kendi yazısıyla adı (dil seçicide gösterilir).'
           WHEN 'yon' THEN 'Yazı yönü: LTR soldan sağa, RTL sağdan sola.'
           WHEN 'varsayilan' THEN 'Kullanıcının dili ya da metin yoksa kullanılan dil; en fazla bir tane.'
           WHEN 'anahtar' THEN 'Metnin anahtarı: alan.<tablokodu>.<alanadi>, dugme.<ad>, menu.<kod>, mesaj.<ad>.'
           WHEN 'dilvid' THEN 'Metnin dili (temdiltur).'
           WHEN 'metin' THEN 'Çevrilmiş metin.'
           ELSE NULL END,
       'SISTEM', 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM information_schema.columns c
WHERE c.table_schema = current_schema()
  AND c.table_name IN ('temdiltur', 'temdilcvr')
ORDER BY c.table_name, c.ordinal_position;
