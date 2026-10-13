-- temorgsozl: kullanıcı ve yetkilendirme (temkul) terimleri; mevcut 'rol' ve 'kul' kayıtları güncellenir.
UPDATE temorgsozl SET aciklamasi = 'Rol: muhatap rol tanımları (mhttemrol) ve muhatabın kuruluş bazlı rolleri (mhtlevrol); kullanıcı rolleri temkulrol tablosundadır (izinler role verilir).', u_kullanici = 'SISTEM', u_tarih = CURRENT_DATE, u_saat = CURRENT_TIME WHERE terim = 'rol';
UPDATE temorgsozl SET aciklamasi = 'Kullanıcı: kullanıcı ve yetkilendirme paketi temkul (kullanıcı tanımı temkultan, rol temkulrol...).', u_kullanici = 'SISTEM', u_tarih = CURRENT_DATE, u_saat = CURRENT_TIME WHERE terim = 'kul';
INSERT INTO temorgsozl (terim, tanimi, aciklamasi, c_kullanici, c_tarih, c_saat) VALUES
    ('par', 'Parola', 'Kullanıcı giriş bilgisi tablosu temkulpar: parola özeti, dış kimlik (SSO), MFA, kilitlenme.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('izn', 'İzin', 'Kaynak (menü adımı ya da tablo) + eylem; temkulizn.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('riz', 'Rol-izin ilişkisi', 'Rolün hangi izne sahip olduğu ya da hangi izinin yasaklandığı; temkulriz.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('ata', 'Atama', 'Kullanıcı–rol ataması, veri kapsamı ve geçerlilik tarihiyle; temkulata.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('aly', 'Alan yetkisi', 'Rol bazlı alan kısıtı: gizli, salt okunur, maskeli; temkulaly.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('vkl', 'Vekâlet', 'Onay yetkisinin sıra ve tutar limitine göre devri; temkulvkl.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('tan', 'Tanım', 'Tanım tablosu soneki: temkultan = kullanıcı tanımları.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('gorevayrilik', 'Görev ayrılığı', 'temkulizn alanı: doğruysa kaydı oluşturan kişi o izni (örn. onay) aynı kayıt için kullanamaz.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('yasak', 'Yasak', 'temkulriz alanı: doğruysa izin açıkça yasaklanır; yasak her zaman kazanır.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('GIZLI', 'Gizli alan', 'Alan yetkisi: alan sunucu tarafında veri nesnesine hiç konmaz.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('SALTOKUNUR', 'Salt okunur alan', 'Alan yetkisi: alan görünür ama değiştirilemez.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('MASKELI', 'Maskeli alan', 'Alan yetkisi: alan maskelenerek gösterilir (örn. son 4 karakter).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('temkul', 'Kullanıcı ve yetkilendirme paketi', 'Paket kodu temkul: kullanıcı, parola, rol, izin, atama, alan yetkisi ve vekâlet tabloları.', 'SISTEM', CURRENT_DATE, CURRENT_TIME);
