-- temorgmimr düzenlemesi (kullanıcı kararı, 13.10.2026): mhttemgrp tablosu olmayacak.
-- Neden: muhatap grupları temortetk (etiket) tablosundadır; mhtlevgrp.grupvid doğrudan temortetk.vid'e yabancı anahtardır.
-- Not: mhttemgrp kodda hiç açılmamıştı; yalnızca planlı mimari kaydı silinir.
DELETE FROM temorgmimr WHERE terim = 'TABLO' AND adi = 'mhttemgrp';
