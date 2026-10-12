-- temorgniz (menü: tem_734_000) : Organizasyon - Kuruluş - Tesis ilişkisi (yapı ağacı).
-- Neden var: hangi tesisin hangi kuruluşa, hangi kuruluşun hangi organizasyona bağlı olduğunu tutar.
-- Kurallar:
--   1) Bir tesis aynı anda yalnızca BİR kuruluşa bağlı olabilir (aktif kayıtlar arasında).
--   2) Bir kuruluş aynı anda yalnızca BİR organizasyon altında olabilir (aktif kayıtlar arasında).
--   Pasif edilmiş kayıtlar bu kurallara takılmaz (geçmiş bağlantılar korunur).
CREATE TABLE temorgniz (
    vid            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orgvid         BIGINT       NOT NULL REFERENCES temorgorg (vid),
    kurvid         BIGINT       NOT NULL REFERENCES temorgkur (vid),
    tesvid         BIGINT       NOT NULL REFERENCES temorgtes (vid),
    etiket         VARCHAR(4),              -- temortetk etiket kodu; FK yok, servis katmanı doğrular
    acilistarihi   DATE         NOT NULL,
    pasif          BOOLEAN      NOT NULL DEFAULT FALSE,
    pasiftarihi    DATE,
    pasifnedeni    VARCHAR(10),             -- ileride neden tablosuna yabancı anahtar olacak
    c_kullanici    VARCHAR(30)  NOT NULL,
    c_tarih        DATE         NOT NULL,
    c_saat         TIME         NOT NULL,
    u_kullanici    VARCHAR(30),
    u_tarih        DATE,
    u_saat         TIME,
    CONSTRAINT temorgniz_pasif_ck CHECK (
        (pasif AND pasiftarihi IS NOT NULL AND pasifnedeni IS NOT NULL)
        OR (NOT pasif AND pasiftarihi IS NULL AND pasifnedeni IS NULL))
);

-- Kural 1: aktif tesis tek kayıtta görünebilir
CREATE UNIQUE INDEX temorgniz_tesvid_aktif_uq ON temorgniz (tesvid) WHERE NOT pasif;
-- Arama hızı için yabancı anahtar dizinleri
CREATE INDEX temorgniz_orgvid_ix ON temorgniz (orgvid);
CREATE INDEX temorgniz_kurvid_ix ON temorgniz (kurvid);

-- Kural 2: aktif bir kuruluş iki farklı organizasyon altında bulunamaz.
-- Eşzamanlı eklemelerde yarış olmaması için kuruluş bazlı advisory lock alınır.
CREATE FUNCTION temorgniz_kuruluz_kontrol() RETURNS trigger AS $$
BEGIN
    IF NOT NEW.pasif THEN
        PERFORM pg_advisory_xact_lock(hashtextextended('temorgniz.kurvid', 0) # NEW.kurvid);
        IF EXISTS (SELECT 1 FROM temorgniz
                    WHERE kurvid = NEW.kurvid AND NOT pasif
                      AND orgvid <> NEW.orgvid AND vid <> COALESCE(NEW.vid, -1)) THEN
            RAISE EXCEPTION 'Kuruluş (vid=%) aynı anda birden fazla organizasyon altında olamaz', NEW.kurvid
                USING ERRCODE = '23514';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER temorgniz_kuruluz_kontrol_tg
    BEFORE INSERT OR UPDATE ON temorgniz
    FOR EACH ROW EXECUTE FUNCTION temorgniz_kuruluz_kontrol();
