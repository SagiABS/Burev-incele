-- temorgsozl: ekran düzeni (temekr) terimleri.
INSERT INTO temorgsozl (terim, tanimi, aciklamasi, c_kullanici, c_tarih, c_saat) VALUES
    ('ekr', 'Ekran', 'Ekran düzeni paketi temekr: genel ekran iskeletinin veriyle tanımlanması (ekran, bölüm, alan yerleşimi, kolon).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('bol', 'Bölüm', 'Ekranın bir çerçevesi (temekrbol): form, tablo, sekme grubu ya da özet; iç içe olabilir.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('yer', 'Yerleşim', 'Form bölümündeki alanın satır, sütun ve genişlik konumu (temekryer).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('kol', 'Kolon', 'Tablo (grid) bölümündeki kolon tanımı (temekrkol).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('sablon', 'Ekran şablonu', 'Ekranın ana yerleşimi: LISTE (liste ekranı) veya BELGE (belge giriş ekranı).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('LISTE', 'Liste şablonu', 'Ortada veri satırları, altta ayrıntı sekmeleri, sağda özet paneli olan ekran şablonu.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('BELGE', 'Belge giriş şablonu', 'Üstte başlık formları, ortada kalem tablosu, altta kalem formları, sağda özet olan ekran şablonu.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('FORM', 'Form bölümü', 'Alanların satır ve sütunlara yerleştirildiği bölüm türü.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('GRID', 'Tablo bölümü', 'Kayıtların satır satır listelendiği bölüm türü; kolonları temekrkol tanımlar.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('SEKME', 'Sekme grubu', 'Sekmeli düğmelerle altındaki bölümleri değiştiren bölüm türü.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('OZET', 'Özet bölümü', 'Toplamlar ve özet bilgilerin gösterildiği bölüm türü.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('yuva', 'Yuva', 'Bölümün şablondaki yeri: ORTAUST, ORTA, ORTAALT, ALT, SAG.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('varyant', 'Varyant', 'Aynı siskod için birden çok ekran (giriş 1, giriş 2, belge türüne göre düzen) ayırt etmek için kod.', 'SISTEM', CURRENT_DATE, CURRENT_TIME);
