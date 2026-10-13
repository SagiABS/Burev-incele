-- temorgmimr düzenlemesi (kullanıcı kararı, 13.10.2026):
--   * Modül ve paket kayıtlarında siskod olmaz (boş kalır); tablo ve sınıf kayıtlarında siskod zorunludur.
--   * Kullanıcı ve yetki yapısı temkul paketinde toplandığı için eski planlı tablolar kaldırılır:
--     temortkul, temortktp (-> temkultan, temkulrol), temorghuk, temfhrhuk (-> temkulizn, temkulriz, temkulata).
-- Not: kaldırılan tablolar kodda hiç açılmamıştı; bağlı kayıt (alt sınıf) yoktur.
DELETE FROM temorgmimr WHERE terim = 'TABLO' AND adi IN ('temortkul', 'temortktp', 'temorghuk', 'temfhrhuk');

ALTER TABLE temorgmimr ADD CONSTRAINT temorgmimr_siskod_kural_ck
    CHECK ((terim IN ('MODUL', 'PAKET')) = (siskod IS NULL));

-- Sözlükte yalnızca kaldırılan tablolara ait terimler silinir (huk: temorghuk/temfhrhuk, ktp: temortktp).
DELETE FROM temorgsozl WHERE terim IN ('huk', 'ktp');
