-- temorgmimr: temekr (ekran düzeni) paketi ve tabloları. siskod değerleri kullanıcıdan (14.10.2026).
-- sira kodları paket/tablo sırasına göre atanmıştır (temdbs 905000'den sonra); kullanıcı isterse değiştirir.
INSERT INTO temorgmimr (ustvid, sira, ana, tali, detay, adi, tanimi, islevi, terim, siskod, c_kullanici, c_tarih, c_saat)
VALUES ((SELECT vid FROM temorgmimr WHERE terim = 'MODUL' AND adi = 'tem'), 906000, 'tem', 'ekr', NULL, 'temekr', 'EKRAN DÜZENİ',
        'Genel ekran iskeletinin veriyle tanımlanması (esnek tasarım): ekran, bölüm, alan yerleşimi ve kolon tabloları. Alan yerleşimi ve kolonlar temcusdba alanlarından seçilir.',
        'PAKET', NULL, 'SISTEM', CURRENT_DATE, CURRENT_TIME);
INSERT INTO temorgmimr (ustvid, sira, ana, tali, detay, adi, tanimi, islevi, terim, siskod, c_kullanici, c_tarih, c_saat)
SELECT (SELECT vid FROM temorgmimr WHERE terim = 'PAKET' AND adi = 'temekr'), t.sira, 'tem', 'ekr', t.detay, t.adi, t.tanimi, t.islevi, 'TABLO', t.siskod, 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM (VALUES
    (906010, 'tan', 'temekrtan', 'Ekran Tanımları', 'Ekran: siskod + varyant, şablon (liste / belge), ana tablo, kapsam (organizasyon / kuruluş / tesis), sürüm ve durum (taslak / yayın). Varyant seçim koşulu belge türüne göre ekran seçer.', 'tem_1113_000'),
    (906020, 'bol', 'temekrbol', 'Ekran Bölümleri', 'Ekranın çerçeveleri: form, tablo, sekme grubu, özet; şablondaki yuvası, sırası, ilk açılışta açık/kapalı, sürüklenebilir. İç içe olabilir.', 'tem_700_000'),
    (906030, 'yer', 'temekryer', 'Ekran Alan Yerleşimleri', 'Form bölümündeki alanın satır, sütun, genişlik, kontrol türü ve görünürlük koşulu; alan temcusdba''dan seçilir.', 'tem_873_000'),
    (906040, 'kol', 'temekrkol', 'Ekran Kolon Tanımları', 'Tablo bölümündeki kolonların sırası, genişliği, biçimi ve toplam kuralı; alan temcusdba''dan seçilir.', 'tem_798_000')
) AS t (sira, detay, adi, tanimi, islevi, siskod);
