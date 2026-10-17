-- Kanıt çalışması örnek verisi: Organizasyon (temorgorg) tablosuna 5000 deneme satırı ekler.
-- Neden var: iki ekran adayını (Vue / PrimeFaces) aynı, dolu bir liste üzerinde karşılaştırmak için.
-- Yalnızca GELİŞTİRME veritabanında çalıştırın. vid kimlik sütunudur, veritabanı kendisi üretir.
-- Kodlar 3 harflidir (kısıt: char_length = 3): 'AAA'..'HAA' aralığından 5000 benzersiz kod üretilir.
INSERT INTO temorgorg (kodu, adi, acilistarihi, c_kullanici, c_tarih, c_saat)
SELECT chr(65 + (g - 1) / 676) || chr(65 + (g - 1) / 26 % 26) || chr(65 + (g - 1) % 26),
       'Deneme organizasyonu ' || g,
       DATE '2020-01-01' + (g % 1000),
       'poc', CURRENT_DATE, CURRENT_TIME
FROM generate_series(1, 5000) g;
-- Geri alma: DELETE FROM temorgorg WHERE c_kullanici = 'poc';
