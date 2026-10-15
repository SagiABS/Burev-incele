-- Ekran düzeni: ilk (sistem varsayılanı) liste ekranları, kullanıcı kararı 14.10.2026 ("ilk ekranları sen kur").
-- Her ekran LISTE şablonudur: ORTA = veri satırları (GRID), ALT = ayrıntı sekmeleri (kapalı), SAG = özet paneli (kapalı).
-- Kolonlar veri sözlüğündeki (temcusdba) alanlardan üretilir; teknik alanlar (vid, pasif*, c_*, u_*, ekbilgiler) kolon olmaz.
-- siskod, ilgili tablonun temorgmimr kaydındaki siskod'dur. Belge giriş (BELGE) ekranları ilgili belge tabloları açılınca eklenir.
INSERT INTO temekrtan (siskod, varyant, baslik, sablon, tablokodu, surum, durum, c_kullanici, c_tarih, c_saat)
SELECT m.siskod, 'ANA', m.tanimi, 'LISTE', m.adi, 1, 'YAYIN', 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM temorgmimr m
WHERE m.terim = 'TABLO'
  AND m.adi IN ('temorgorg', 'temorgkur', 'temorgtes', 'temortprb', 'temortetk', 'temortktr', 'temortgeo', 'temortadr',
                'temortblok', 'temcusmde', 'mhtlevmuf', 'mhttemrol');

INSERT INTO temekrbol (ekranvid, kodu, turu, yuva, baslik, sira, surukle, acik, c_kullanici, c_tarih, c_saat)
SELECT e.vid, b.kodu, b.turu, b.yuva, b.baslik, b.sira, b.surukle, b.acik, 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM temekrtan e
CROSS JOIN (VALUES
    ('LISTE',  'GRID',  'ORTA', NULL::VARCHAR, 1, FALSE, TRUE),
    ('ALTPNL', 'SEKME', 'ALT',  'Ayrıntılar',  2, TRUE,  FALSE),
    ('SAGPNL', 'OZET',  'SAG',  'Özet',        3, TRUE,  FALSE)
) AS b (kodu, turu, yuva, baslik, sira, surukle, acik)
WHERE e.c_kullanici = 'SISTEM' AND e.sablon = 'LISTE';

INSERT INTO temekrkol (bolumvid, dbavid, sira, c_kullanici, c_tarih, c_saat)
SELECT b.vid, d.vid, row_number() OVER (PARTITION BY b.vid ORDER BY d.vid) * 10, 'SISTEM', CURRENT_DATE, CURRENT_TIME
FROM temekrbol b
JOIN temekrtan e ON e.vid = b.ekranvid
JOIN temcusdba d ON d.tablokodu = e.tablokodu AND d.kaynak = 'SISTEM'
WHERE b.kodu = 'LISTE'
  AND d.alanadi NOT IN ('vid', 'pasif', 'pasiftarihi', 'pasifnedeni', 'ekbilgiler')
  AND d.alanadi NOT LIKE 'c\_%' AND d.alanadi NOT LIKE 'u\_%';
