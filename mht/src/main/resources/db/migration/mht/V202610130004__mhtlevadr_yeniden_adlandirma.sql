-- Tablo yeniden adlandırmaları (kullanıcı kararı, 13.10.2026):
--   mhtlevmfa -> mhtlevadr  (muhatap - adres ilişkisi)
-- Neden var: uygulanmış göçler değiştirilmediği için ad değişikliği yeni bir göçle yapılır.
-- Tabloyla birlikte kısıt, dizin, sıra (sequence), tetikleyici ve ad ön ekiyle başlayan fonksiyonlar da yeniden adlandırılır.
-- Not: tem tablolarının adları tem modülündeki V202610130003 göçünde değişir.
DO $$
DECLARE
    cift   TEXT[];
    eski   TEXT;
    yeni   TEXT;
    r      RECORD;
BEGIN
    FOREACH cift SLICE 1 IN ARRAY ARRAY[['mhtlevmfa','mhtlevadr']] LOOP
        eski := cift[1];
        yeni := cift[2];
        EXECUTE format('ALTER TABLE %I RENAME TO %I', eski, yeni);
        -- kısıtlar (birincil/yabancı anahtar, benzersiz, kontrol); arkasındaki dizinler de yeniden adlanır
        FOR r IN SELECT conname FROM pg_constraint
                  WHERE conrelid = yeni::regclass AND left(conname, length(eski) + 1) = eski || '_' LOOP
            EXECUTE format('ALTER TABLE %I RENAME CONSTRAINT %I TO %I', yeni, r.conname,
                           yeni || substr(r.conname, length(eski) + 1));
        END LOOP;
        -- kısıta bağlı olmayan dizinler
        FOR r IN SELECT indexname FROM pg_indexes
                  WHERE schemaname = current_schema() AND tablename = yeni
                    AND left(indexname, length(eski) + 1) = eski || '_' LOOP
            EXECUTE format('ALTER INDEX %I RENAME TO %I', r.indexname, yeni || substr(r.indexname, length(eski) + 1));
        END LOOP;
        -- kimlik (identity) sırası
        EXECUTE format('ALTER SEQUENCE IF EXISTS %I RENAME TO %I', eski || '_vid_seq', yeni || '_vid_seq');
        -- tetikleyiciler
        FOR r IN SELECT tgname FROM pg_trigger
                  WHERE tgrelid = yeni::regclass AND NOT tgisinternal
                    AND left(tgname, length(eski) + 1) = eski || '_' LOOP
            EXECUTE format('ALTER TRIGGER %I ON %I RENAME TO %I', r.tgname, yeni, yeni || substr(r.tgname, length(eski) + 1));
        END LOOP;
        -- tablo adıyla başlayan fonksiyonlar
        FOR r IN SELECT p.oid::regprocedure AS imza, p.proname FROM pg_proc p
                  WHERE p.pronamespace = current_schema()::regnamespace
                    AND left(p.proname, length(eski) + 1) = eski || '_' LOOP
            EXECUTE format('ALTER FUNCTION %s RENAME TO %I', r.imza, yeni || substr(r.proname, length(eski) + 1));
        END LOOP;
    END LOOP;
END
$$;
