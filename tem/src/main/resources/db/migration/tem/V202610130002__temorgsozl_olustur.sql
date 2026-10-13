-- temorgsozl : Temel sözlük. Sistemde kullandığımız özel kodları, terimleri ve tanımları tutar.
-- Neden var: kısa kodların (örn. tem = Temel) ve terimlerin anlamı tek yerde, uygulama içinde okunabilsin.
-- Başlangıç kayıtları bu göçte tohum veri olarak eklenir.
CREATE TABLE temorgsozl (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    terim          VARCHAR(30)   NOT NULL,   -- kod ya da terim (örn. tem, siskod, Entity)
    tanimi         VARCHAR(100)  NOT NULL,   -- kısa tanım (örn. Temel)
    aciklamasi     VARCHAR(1000),            -- ayrıntılı açıklama
    -- Pasif yapma bilgisi: üçü birlikte dolu (pasif) ya da üçü birlikte boş (aktif) olmalı
    pasif          BOOLEAN       NOT NULL DEFAULT FALSE,
    pasiftarihi    DATE,
    pasifnedeni    VARCHAR(10),              -- ileride neden tablosuna yabancı anahtar olacak
    -- Oluşturma / güncelleme izi
    c_kullanici    VARCHAR(30)   NOT NULL,
    c_tarih        DATE          NOT NULL,
    c_saat         TIME          NOT NULL,
    u_kullanici    VARCHAR(30),
    u_tarih        DATE,
    u_saat         TIME,
    CONSTRAINT temorgsozl_terim_uq UNIQUE (terim),
    CONSTRAINT temorgsozl_terim_ck CHECK (btrim(terim) <> ''),
    CONSTRAINT temorgsozl_tanimi_ck CHECK (btrim(tanimi) <> ''),
    CONSTRAINT temorgsozl_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);

-- Başlangıç (tohum) kayıtları
INSERT INTO temorgsozl (terim, tanimi, aciklamasi, c_kullanici, c_tarih, c_saat)
SELECT t.terim, t.tanimi, t.aciklamasi, 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM (VALUES
    ('tem', 'Temel', 'Projenin temel bileşenlerini ifade eder: organizasyon yapısı, ortak tanımlar ve taban sınıflar. Diğer modüller buna bağımlıdır (tem ← bel ← muh, tem ← mht).'),
    ('bel', 'Belge', 'Modül kodu olarak belge/fiş katmanını, mhtlevbel tablosunda muhatap resmi belgelerini ifade eder. (Modül anlamı teyit bekliyor.)'),
    ('muh', 'Muhasebe', 'Muhasebe modülünün kodudur.'),
    ('mht', 'Muhatap', 'Muhatap (cari: müşteri, tedarikçi, personel vb.) modülünün kodudur.'),
    ('org', 'Organizasyon', 'Organizasyon yapısı paketi (temorg) ve organizasyon (holding) tablosu temorgorg. Sistemde kiracı = organizasyon.'),
    ('ort', 'Ortak', 'Tüm modüllerin ortak kullandığı tanım ve taban sınıf paketi (temort).'),
    ('lev', 'Muhatap işlem', 'mhtlev paketinin kısa kodu: muhatap kartı ve ona bağlı ilişki tabloları. (Anlamı teyit bekliyor.)'),
    ('kur', 'Kuruluş', 'Kuruluş (şirket) tanımı: temorgkur; muhatabın kuruluş ataması: mhtlevkur.'),
    ('tes', 'Tesis', 'Tesis (şube) tanımı: temorgtes. Bir tesis aynı anda yalnızca bir kuruluşa bağlıdır.'),
    ('niz', 'Yapı ilişkisi', 'Organizasyon-Kuruluş-Tesis ilişkisi (temorgniz); yapı ağacını tutar.'),
    ('mde', 'Ek alan tanımı', 'Organizasyon ek alan tanımları (temorgmde): kod yazmadan dinamik alan eklenmesini sağlar.'),
    ('mimr', 'Mimari', 'Burevers mimari kayıtları tablosu (temorgmimr): modül, paket, tablo ve sınıf hiyerarşisi.'),
    ('sozl', 'Sözlük', 'Temel sözlük tablosu (temorgsozl): sistemde kullanılan özel kod, terim ve tanımları tutar.'),
    ('etk', 'Etiket', 'Etiket tanımları (temortetk): kayıtlara bağlanabilen ortak etiketler.'),
    ('prb', 'Para birimi', 'Para birimi tanımları (temortprb): para birimi ve ondalık basamak bilgisi.'),
    ('ktr', 'Şirket türü', 'Şirket (muhatap) türü tanımları (temortktr): A.Ş., Ltd., şahıs vb.'),
    ('geo', 'Coğrafya', 'Coğrafi lokasyon ağacı (temortgeo): ülke > bölge > şehir > ilçe > mahalle.'),
    ('adr', 'Adres', 'Ortak adres havuzu (temortadr); mhtlevmfa üzerinden muhataplara bağlanır.'),
    ('blk', 'Bloke', 'Tablo genel bloke tanımları (temortblk): kayıt üzerindeki kilit/bloke bilgisi.'),
    ('rol', 'Rol', 'Muhatap rol tanımları (mhttemrol) ve muhatabın kuruluş bazlı rolleri (mhtlevrol): müşteri, tedarikçi, personel vb.'),
    ('muf', 'Muhatap ana kart', 'Muhatap (cari) ana kartı (mhtlevmuf).'),
    ('mfa', 'Muhatap adres ilişkisi', 'Muhatap - adres ilişkisi (mhtlevmfa).'),
    ('ilt', 'İletişim', 'Muhatap iletişim bilgileri (mhtlevilt): telefon, e-posta vb.'),
    ('grp', 'Grup', 'Muhatap - grup ilişkisi (mhtlevgrp).'),
    ('idm', 'Holding içi ilişki', 'Holding içi (intercompany) kuruluşlar arası muhatap-rol ilişkisi (mhtlevidm).'),
    ('vid', 'Vekil kimlik', 'Her tablonun otomatik artan (BIGINT IDENTITY) birincil anahtarı; yabancı anahtar sütunları ...vid adını alır. Ekranlarda kullanıcıya kod ve ad ile gösterilir, vid gösterilmez.'),
    ('ustvid', 'Üst kayıt', 'Hiyerarşik tablolarda aynı tablodaki üst kaydın vid değeri.'),
    ('siskod', 'Sistematik kod', 'Menü/arama kısa kodu: <modül>_<ebced>_<sıra>; sıra 000 tablo/pakettir, 001... sınıflarıdır.'),
    ('ebced', 'Ebced değeri', 'Tablo/paket adının ebced hesabıyla bulunan sayı; siskod içinde kullanılır, değerleri kullanıcı verir.'),
    ('pasif', 'Pasif', 'Kayıtlar silinmez, pasif yapılır. pasif, pasiftarihi, pasifnedeni üçlüsü birlikte dolu ya da birlikte boş olmalıdır.'),
    ('pasiftarihi', 'Pasif tarihi', 'Kaydın pasif yapıldığı tarih; aktifse boştur.'),
    ('pasifnedeni', 'Pasif nedeni', 'Pasif neden kodu (en çok 10 karakter); ileride neden tablosuna yabancı anahtar olacaktır.'),
    ('c_kullanici', 'Oluşturan kullanıcı', 'Kaydı oluşturan kullanıcı; c_tarih ve c_saat ile birlikte oluşturma izidir.'),
    ('u_kullanici', 'Güncelleyen kullanıcı', 'Kaydı son güncelleyen kullanıcı; u_tarih ve u_saat ile birlikte güncelleme izidir.'),
    ('etkgrubu', 'Etiket grubu', 'Muhatap kartlarını ve diğer tablolardaki tanımları gruplamak için kullanılan grup bilgisi.'),
    ('bloketipi', 'Bloke tipi', 'Blokenin ne tür olduğunu belirtir (satış, teslimat, ödeme gibi); kayıt girişlerinde kontrol için kullanılır. Değerleri sonra tanımlanır.'),
    ('alftanimi', 'Alternatif tanım', 'Kaydın alternatif (örn. yabancı dil) tanımı.'),
    ('dbtablosu', 'Veritabanı tablosu', 'Kaydın ilgili olduğu tablo kodu; ileride tablo tanımları tablosuna yabancı anahtar olacaktır.'),
    ('ekbilgiler', 'Ek bilgiler', 'Tabloların JSONB sütunu; temorgmde ile tanımlanan dinamik alan değerleri alan adı anahtarıyla burada tutulur.'),
    ('bolgevid', 'Bölge vid', 'Coğrafi lokasyon ağacında bölgeyi gösteren anahtar alan.'),
    ('Entity', 'Varlık sınıfı', 'Tablonun JPA karşılığı olan Java sınıfı soneki.'),
    ('Repository', 'Veri erişim arayüzü', 'Spring Data deposu soneki; sorgular burada tanımlanır.'),
    ('Service', 'Servis', 'İş kurallarının yer aldığı sınıf soneki.'),
    ('View', 'Ekran', 'Vaadin ekran (sayfa) sınıfı soneki.'),
    ('Dialog', 'Diyalog penceresi', 'Vaadin diyalog (açılır pencere) sınıfı soneki.'),
    ('Controller', 'Denetleyici', 'REST/web uç noktası sınıfı soneki.'),
    ('Dto', 'Veri aktarım nesnesi', 'Katmanlar arası veri taşıyan sınıf soneki.'),
    ('Validator', 'Doğrulayıcı', 'Doğrulama kuralı sınıfı soneki.'),
    ('Event', 'Olay', 'Uygulama olay sınıfı soneki.'),
    ('Test', 'Test', 'Test sınıfı soneki.'),
    ('tablo kodu', 'Tablo kodu', 'En çok 10 karakter: paket kodu (6) + tablo adı (3 veya 4). Entity adı da bu kodla başlar; Java paketi tablo kodunun ilk 6 karakteridir.'),
    ('paket kodu', 'Paket kodu', '6 karakter: modül (3) + paket adı (3), örn. temorg.')
) AS t (terim, tanimi, aciklamasi);
