-- temorgsozl: dil paketi (temdil) terimleri.
INSERT INTO temorgsozl (terim, tanimi, aciklamasi, c_kullanici, c_tarih, c_saat) VALUES
    ('dil', 'Dil', 'Dil paketi temdil: proje dilleri ve çeviri metinleri (çok dillilik, ADR-11).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('tur', 'Dil türü / proje dili', 'temdiltur: projede desteklenen diller (kod, ad, yazı yönü, varsayılan).', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('cvr', 'Çeviri', 'temdilcvr: anahtar + dil + kapsam için çevrilmiş metin; kapsam doluysa sistem metnini ezer.', 'SISTEM', CURRENT_DATE, CURRENT_TIME),
    ('yon', 'Yazı yönü', 'LTR soldan sağa, RTL sağdan sola (Arapça); arayüz yönünü belirler.', 'SISTEM', CURRENT_DATE, CURRENT_TIME);

-- temekr sira kodları temdil ile çakıştığı için bir blok ileri alınır (906xxx -> 907xxx).
-- Not: temekr sira değerleri Claude'un önerisiydi; kullanıcı kodları (temdil 906000-906020) esas alınmıştır.
UPDATE temorgmimr SET sira = sira + 1000, u_kullanici = 'SISTEM', u_tarih = CURRENT_DATE, u_saat = CURRENT_TIME
WHERE adi IN ('temekr', 'temekrtan', 'temekrbol', 'temekryer', 'temekrkol');

-- temorgmimr: temdil paketi ve tabloları (siskod değerleri kullanıcıdan, 16.10.2026).
INSERT INTO temorgmimr (ustvid, sira, ana, tali, detay, adi, tanimi, islevi, terim, siskod, c_kullanici, c_tarih, c_saat)
VALUES ((SELECT vid FROM temorgmimr WHERE terim = 'MODUL' AND adi = 'tem'), 906000, 'tem', 'dil', NULL, 'temdil', 'DİL PAKETİ',
        'Çok dillilik: proje dilleri ve yazı yönü (temdiltur), anahtar bazlı çeviri metinleri ve kapsama özel ezmeler (temdilcvr).',
        'PAKET', NULL, 'SISTEM', CURRENT_DATE, CURRENT_TIME);
INSERT INTO temorgmimr (ustvid, sira, ana, tali, detay, adi, tanimi, islevi, terim, siskod, c_kullanici, c_tarih, c_saat)
SELECT (SELECT vid FROM temorgmimr WHERE terim = 'PAKET' AND adi = 'temdil'), t.sira, 'tem', 'dil', t.detay, t.adi, t.tanimi, t.islevi, 'TABLO', t.siskod, 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM (VALUES
    (906010, 'cvr', 'temdilcvr', 'Proje Dil Çevirileri', 'Anahtar + dil + kapsam için çevrilmiş metin. Kapsam (organizasyon/kuruluş/tesis) boşsa sistem metni, doluysa o kapsama özel ezme metnidir. Alan etiketi, düğme, menü, panel ve mesaj metinleri burada tutulur.', 'tem_694_000'),
    (906020, 'tur', 'temdiltur', 'Proje Dilleri', 'Projede desteklenen diller: dil kodu (tr, en, ar), adı, kendi yazısıyla adı, yazı yönü (soldan/sağdan), varsayılan dil.', 'tem_1091_000')
) AS t (sira, detay, adi, tanimi, islevi, siskod);
