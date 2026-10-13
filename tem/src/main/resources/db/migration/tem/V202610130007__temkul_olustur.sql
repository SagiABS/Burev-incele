-- Kullanıcı ve yetkilendirme paketi (temkul): kullanıcı, parola, rol, izin, atama, alan yetkisi, vekâlet.
-- Kararlar (kullanıcı onayı 13.10.2026):
--   * Kimlik (temkultan, temkulpar) ile yetki (rol, izin, atama) ayrıdır; yetki kullanıcıya değil role verilir.
--   * Kullanıcı kiracıdan (organizasyon) bağımsızdır; bir kullanıcı birden fazla organizasyonda çalışabilir (temkulata).
--   * Giriş: kendi parolası ya da dış kimlik (SSO: OIDC/SAML) ikisi birlikte desteklenir (temkulpar).
--   * Alan yetkisi: yalnızca kısıtlama kaydedilir (GIZLI, SALTOKUNUR, MASKELI); kayıt yoksa alan düzenlenebilirdir.
--   * Yetki varsayılan olarak yasaktır; rolde YASAK işaretli izin her zaman kazanır.
--   * Görev ayrılığı: temkulizn.gorevayrilik doğruysa kaydı oluşturan kişi o izni (örn. onay) aynı kayıt için kullanamaz (servis katmanı uygular).

-- temkultan : Kullanıcı tanımı.
-- Neden var: uygulamayı kullanan kişi ya da servis hesabının kimlik bilgisi (parola temkulpar'dadır).
-- kodu giriş adıdır ve c_kullanici alanlarında kullanılır (en çok 30 karakter). tipi: IC = çalışan, DIS = dış kullanıcı, SERVIS = servis hesabı.
CREATE TABLE temkultan (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kodu           VARCHAR(30)  NOT NULL,   -- kullanıcı kodu (giriş adı)
    adi            VARCHAR(100) NOT NULL,   -- ad soyad
    unvani         VARCHAR(100),            -- unvan
    epostasi       VARCHAR(150),            -- e-posta adresi
    tipi           VARCHAR(10)  NOT NULL DEFAULT 'IC',
    dili           VARCHAR(5),              -- arayüz dili (örn. tr, en)
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
    CONSTRAINT temkultan_tipi_ck CHECK (tipi IN ('IC', 'DIS', 'SERVIS')),
    CONSTRAINT temkultan_kodu_ck CHECK (btrim(kodu) <> ''),
    CONSTRAINT temkultan_adi_ck CHECK (btrim(adi) <> ''),
    CONSTRAINT temkultan_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkultan_kodu_uq ON temkultan (lower(kodu));
CREATE UNIQUE INDEX temkultan_epostasi_uq ON temkultan (lower(epostasi)) WHERE epostasi IS NOT NULL;

-- temkulpar : Kullanıcı giriş bilgisi (parola, dış kimlik, MFA, kilitlenme).
-- Neden var: gizli bilgi ana kullanıcı tablosundan ayrı durur; sorgularda yanlışlıkla çıkmaz.
-- parolaozeti: yalnızca güçlü özet (Argon2/bcrypt), düz metin asla saklanmaz. SSO kullanılırsa parola boştur,
-- ssosaglayici + ssokimlik dolar. En az bir giriş yöntemi dolu olmalıdır. mfasirri uygulama tarafından şifrelenmiş saklanır.
CREATE TABLE temkulpar (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kullanicivid   BIGINT       NOT NULL REFERENCES temkultan (vid),
    parolaozeti    VARCHAR(255),            -- parola özeti (hash)
    parolatarihi   TIMESTAMPTZ,             -- parolanın son değiştirildiği an
    degisimzorunlu BOOLEAN      NOT NULL DEFAULT FALSE,   -- ilk girişte parola değiştirme zorunlu mu?
    ssosaglayici   VARCHAR(30),             -- dış kimlik sağlayıcısı (örn. OIDC sağlayıcı adı)
    ssokimlik      VARCHAR(200),            -- dış kimlik sağlayıcısındaki kullanıcı anahtarı (subject)
    mfaaktif       BOOLEAN      NOT NULL DEFAULT FALSE,   -- çok adımlı doğrulama açık mı?
    mfasirri       VARCHAR(255),            -- MFA sırrı (şifrelenmiş)
    hatalisayisi   SMALLINT     NOT NULL DEFAULT 0,       -- üst üste hatalı giriş sayısı
    kilitbitis     TIMESTAMPTZ,             -- hesap bu ana kadar kilitli
    songiris       TIMESTAMPTZ,             -- son başarılı giriş
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
    CONSTRAINT temkulpar_yontem_ck CHECK (parolaozeti IS NOT NULL OR ssokimlik IS NOT NULL),
    CONSTRAINT temkulpar_sso_ck CHECK ((ssosaglayici IS NULL) = (ssokimlik IS NULL)),
    CONSTRAINT temkulpar_hata_ck CHECK (hatalisayisi >= 0),
    CONSTRAINT temkulpar_mfa_ck CHECK (NOT mfaaktif OR mfasirri IS NOT NULL),
    CONSTRAINT temkulpar_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Bir kullanıcının aktif tek giriş bilgisi kaydı olur
CREATE UNIQUE INDEX temkulpar_kullanici_uq ON temkulpar (kullanicivid) WHERE NOT pasif;
CREATE UNIQUE INDEX temkulpar_sso_uq ON temkulpar (ssosaglayici, ssokimlik) WHERE ssokimlik IS NOT NULL AND NOT pasif;

-- temkulrol : Kullanıcı rolü (yetki grubu).
-- Neden var: izinler kullanıcıya değil role verilir. ustvid ile üst rol tanımlanabilir (alt rol, üst rolün izinlerini de kullanır).
-- orgvid boşsa sistem rolü (tüm organizasyonlarda geçerli), doluysa yalnızca o organizasyona özel roldür.
CREATE TABLE temkulrol (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orgvid         BIGINT       REFERENCES temorgorg (vid),   -- boşsa sistem rolü
    ustvid         BIGINT       REFERENCES temkulrol (vid),   -- üst rol
    kodu           VARCHAR(20)  NOT NULL,   -- rol kodu
    adi            VARCHAR(100) NOT NULL,   -- rol adı
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
    CONSTRAINT temkulrol_kodu_ck CHECK (btrim(kodu) <> ''),
    CONSTRAINT temkulrol_adi_ck CHECK (btrim(adi) <> ''),
    CONSTRAINT temkulrol_ust_ck CHECK (ustvid IS NULL OR ustvid <> vid),
    CONSTRAINT temkulrol_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkulrol_kodu_uq ON temkulrol (COALESCE(orgvid, 0), upper(kodu));
CREATE INDEX temkulrol_orgvid_ix ON temkulrol (orgvid);
CREATE INDEX temkulrol_ustvid_ix ON temkulrol (ustvid);

-- Üst rol zincirinde döngü ve organizasyon uyumsuzluğu engellenir.
-- Sistem rolünün üst rolü de sistem rolü olmalı; organizasyon rolünün üst rolü sistem rolü ya da aynı organizasyonun rolü olmalı.
CREATE FUNCTION temkulrol_ust_kontrol() RETURNS trigger AS $$
DECLARE
    ust_org BIGINT;
BEGIN
    IF NEW.ustvid IS NULL THEN
        RETURN NEW;
    END IF;
    IF EXISTS (WITH RECURSIVE zincir AS (
                   SELECT vid, ustvid FROM temkulrol WHERE vid = NEW.ustvid
                   UNION
                   SELECT r.vid, r.ustvid FROM temkulrol r JOIN zincir z ON r.vid = z.ustvid)
               SELECT 1 FROM zincir WHERE vid = NEW.vid) THEN
        RAISE EXCEPTION 'Üst rol zincirinde döngü oluşur (rol %)', NEW.vid USING ERRCODE = '23514';
    END IF;
    SELECT orgvid INTO ust_org FROM temkulrol WHERE vid = NEW.ustvid;
    IF ust_org IS NOT NULL AND ust_org IS DISTINCT FROM NEW.orgvid THEN
        RAISE EXCEPTION 'Üst rol başka bir organizasyona ait (rol %, üst rol %)', NEW.vid, NEW.ustvid USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER temkulrol_ust_kontrol_tg
    BEFORE INSERT OR UPDATE ON temkulrol
    FOR EACH ROW EXECUTE FUNCTION temkulrol_ust_kontrol();

-- temkulizn : İzin tanımı (kaynak + eylem).
-- Neden var: bir menü adımı ya da tablo üzerinde yapılabilecek işlemleri (görüntüle, ekle, değiştir, sil, onayla, dışa aktar) listeler.
-- kaynakturu MENU ise kaynakkodu menü adımının siskod'u, TABLO ise tablo kodudur.
-- gorevayrilik doğruysa: kaydı oluşturan kişi bu izni aynı kayıt için kullanamaz (örn. kendi kaydını onaylayamaz); servis katmanı uygular.
CREATE TABLE temkulizn (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kaynakturu     VARCHAR(10)  NOT NULL,   -- MENU veya TABLO
    kaynakkodu     VARCHAR(20)  NOT NULL,   -- menü siskod'u ya da tablo kodu
    eylem          VARCHAR(15)  NOT NULL,
    gorevayrilik   BOOLEAN      NOT NULL DEFAULT FALSE,   -- görev ayrılığı kuralı uygulansın mı?
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
    CONSTRAINT temkulizn_tur_ck CHECK (kaynakturu IN ('MENU', 'TABLO')),
    CONSTRAINT temkulizn_eylem_ck CHECK (eylem IN ('GORUNTULE', 'EKLE', 'DEGISTIR', 'SIL', 'ONAYLA', 'DISARIAKTAR')),
    CONSTRAINT temkulizn_kod_ck CHECK (btrim(kaynakkodu) <> ''),
    CONSTRAINT temkulizn_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkulizn_tanim_uq ON temkulizn (kaynakturu, kaynakkodu, eylem);

-- temkulriz : Rol - izin ilişkisi.
-- Neden var: bir role hangi iznin verildiğini ya da açıkça yasaklandığını tutar. Varsayılan yasaktır; yasak = DOĞRU kayıt her zaman kazanır.
CREATE TABLE temkulriz (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rolvid         BIGINT       NOT NULL REFERENCES temkulrol (vid),
    izinvid        BIGINT       NOT NULL REFERENCES temkulizn (vid),
    yasak          BOOLEAN      NOT NULL DEFAULT FALSE,   -- doğruysa izin açıkça yasaklanır
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
    CONSTRAINT temkulriz_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkulriz_rol_izin_uq ON temkulriz (rolvid, izinvid) WHERE NOT pasif;
CREATE INDEX temkulriz_izinvid_ix ON temkulriz (izinvid);

-- temkulata : Kullanıcı - rol ataması (veri kapsamıyla).
-- Neden var: kullanıcının hangi rolü hangi kapsamda (organizasyon / kuruluş / tesis) kullandığını ve ne zaman geçerli olduğunu tutar.
-- Aynı kullanıcı farklı organizasyon, kuruluş ya da tesiste farklı rollere sahip olabilir.
-- Kapsam kuralı temcusmde ile aynıdır: yalnız organizasyon = tümü; + kuruluş = kuruluşun tesisleri; + tesis = yalnız o tesis.
CREATE TABLE temkulata (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kullanicivid   BIGINT       NOT NULL REFERENCES temkultan (vid),
    rolvid         BIGINT       NOT NULL REFERENCES temkulrol (vid),
    orgvid         BIGINT       NOT NULL REFERENCES temorgorg (vid),
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    gecerlibas     DATE         NOT NULL,   -- geçerlilik başlangıcı
    gecerlibit     DATE,                    -- geçerlilik bitişi (boşsa süresiz)
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
    CONSTRAINT temkulata_kapsam_ck CHECK (tesvid IS NULL OR kurvid IS NOT NULL),
    CONSTRAINT temkulata_tarih_ck CHECK (gecerlibit IS NULL OR gecerlibit >= gecerlibas),
    CONSTRAINT temkulata_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkulata_tanim_uq
    ON temkulata (kullanicivid, rolvid, orgvid, COALESCE(kurvid, 0), COALESCE(tesvid, 0), gecerlibas) WHERE NOT pasif;
CREATE INDEX temkulata_kullanici_ix ON temkulata (kullanicivid);
CREATE INDEX temkulata_rolvid_ix ON temkulata (rolvid);
CREATE INDEX temkulata_orgvid_ix ON temkulata (orgvid);
CREATE INDEX temkulata_kurvid_ix ON temkulata (kurvid);
CREATE INDEX temkulata_tesvid_ix ON temkulata (tesvid);

-- Seçilen organizasyon / kuruluş / tesis yapı ağacında bağlı olmalı (ortak fonksiyon, temcus göçünde tanımlıdır).
CREATE TRIGGER temkulata_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temkulata
    FOR EACH ROW EXECUTE FUNCTION temcus_kapsam_kontrol();

-- Organizasyona özel bir rol, yalnızca o organizasyonda atanabilir.
CREATE FUNCTION temkulata_rol_kontrol() RETURNS trigger AS $$
DECLARE
    rol_org BIGINT;
BEGIN
    SELECT orgvid INTO rol_org FROM temkulrol WHERE vid = NEW.rolvid;
    IF rol_org IS NOT NULL AND rol_org <> NEW.orgvid THEN
        RAISE EXCEPTION 'Rol (%) başka bir organizasyona özeldir (organizasyon %)', NEW.rolvid, NEW.orgvid USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER temkulata_rol_kontrol_tg
    BEFORE INSERT OR UPDATE ON temkulata
    FOR EACH ROW EXECUTE FUNCTION temkulata_rol_kontrol();

-- temkulaly : Rol bazlı alan yetkisi.
-- Neden var: bir rolün bir tablonun hangi alanını göremeyeceğini (GIZLI), değiştiremeyeceğini (SALTOKUNUR) ya da maskeli göreceğini (MASKELI) tutar.
-- Yalnızca kısıtlama kaydedilir; kayıt yoksa alan her zaman düzenlenebilirdir. Alan temcusdba'dan seçilir (dinamik alanlar dahil).
-- Uygulama: gizli alan sunucu tarafında veri nesnesine hiç konmaz; maskeli alan maskelenerek konur (yalnızca ekranda gizlemek güvenlik sayılmaz).
-- maske: MASKELI için gösterim deseni (örn. SON4 = yalnızca son 4 karakter).
CREATE TABLE temkulaly (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rolvid         BIGINT       NOT NULL REFERENCES temkulrol (vid),
    dbavid         BIGINT       NOT NULL REFERENCES temcusdba (vid),
    erisim         VARCHAR(10)  NOT NULL,
    maske          VARCHAR(30),
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
    CONSTRAINT temkulaly_erisim_ck CHECK (erisim IN ('GIZLI', 'SALTOKUNUR', 'MASKELI')),
    CONSTRAINT temkulaly_maske_ck CHECK (maske IS NULL OR erisim = 'MASKELI'),
    CONSTRAINT temkulaly_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
CREATE UNIQUE INDEX temkulaly_rol_alan_uq ON temkulaly (rolvid, dbavid) WHERE NOT pasif;
CREATE INDEX temkulaly_dbavid_ix ON temkulaly (dbavid);

-- temkulvkl : Kullanıcı vekâleti.
-- Neden var: izinli ya da yokken onay yetkisinin tarih aralığıyla bir ya da birkaç kişiye, sıra ve tutar limitine göre devredilmesi.
-- Örnek: vekil sırası 1 = 10.000 TRY'ye kadar, sıra 2 = 100.000 TRY'ye kadar, sıra 3 = limitsiz.
-- tutarlimiti boşsa limitsizdir; dolu ise para birimi (prbvid) zorunludur. Kapsam kuralı temcusmde ile aynıdır.
CREATE TABLE temkulvkl (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    verenvid       BIGINT       NOT NULL REFERENCES temkultan (vid),   -- vekâlet veren
    vekilvid       BIGINT       NOT NULL REFERENCES temkultan (vid),   -- vekil
    orgvid         BIGINT       NOT NULL REFERENCES temorgorg (vid),
    kurvid         BIGINT       REFERENCES temorgkur (vid),
    tesvid         BIGINT       REFERENCES temorgtes (vid),
    sira           SMALLINT     NOT NULL,   -- vekil sırası (1 = ilk başvurulan)
    tutarlimiti    NUMERIC(19,4),           -- vekilin onaylayabileceği en yüksek tutar (boşsa limitsiz)
    prbvid         BIGINT       REFERENCES temortprb (vid),   -- limitin para birimi
    gecerlibas     DATE         NOT NULL,
    gecerlibit     DATE         NOT NULL,
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
    CONSTRAINT temkulvkl_kisi_ck CHECK (verenvid <> vekilvid),
    CONSTRAINT temkulvkl_sira_ck CHECK (sira > 0),
    CONSTRAINT temkulvkl_limit_ck CHECK (tutarlimiti IS NULL OR tutarlimiti > 0),
    CONSTRAINT temkulvkl_prb_ck CHECK ((tutarlimiti IS NULL) = (prbvid IS NULL)),
    CONSTRAINT temkulvkl_tarih_ck CHECK (gecerlibit >= gecerlibas),
    CONSTRAINT temkulvkl_kapsam_ck CHECK (tesvid IS NULL OR kurvid IS NOT NULL),
    CONSTRAINT temkulvkl_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);
-- Aynı vekâlet verenin aynı kapsam ve başlangıçta bir sıra numarası bir kez kullanılır; aynı vekil de bir kez
CREATE UNIQUE INDEX temkulvkl_sira_uq
    ON temkulvkl (verenvid, orgvid, COALESCE(kurvid, 0), COALESCE(tesvid, 0), gecerlibas, sira) WHERE NOT pasif;
CREATE UNIQUE INDEX temkulvkl_vekil_uq
    ON temkulvkl (verenvid, vekilvid, orgvid, COALESCE(kurvid, 0), COALESCE(tesvid, 0), gecerlibas) WHERE NOT pasif;
CREATE INDEX temkulvkl_vekilvid_ix ON temkulvkl (vekilvid);
CREATE INDEX temkulvkl_orgvid_ix ON temkulvkl (orgvid);
CREATE INDEX temkulvkl_kurvid_ix ON temkulvkl (kurvid);
CREATE INDEX temkulvkl_tesvid_ix ON temkulvkl (tesvid);
CREATE INDEX temkulvkl_prbvid_ix ON temkulvkl (prbvid);

CREATE TRIGGER temkulvkl_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temkulvkl
    FOR EACH ROW EXECUTE FUNCTION temcus_kapsam_kontrol();

-- Veri sözlüğüne (temcusdba) yeni temkul tablolarının sütunları eklenir.
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
  AND c.table_name LIKE 'temkul%'
ORDER BY c.table_name, c.ordinal_position;
