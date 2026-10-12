-- temorgmde : kapsam (organizasyon / kuruluş / tesis) düzeyi eklenir.
-- Kural (kullanıcı kararı):
--   * Yalnızca organizasyon seçilirse tanım o organizasyonun tüm kuruluş ve tesislerinde geçerlidir.
--   * Organizasyon + kuruluş seçilirse tanım o kuruluşun tüm tesislerinde geçerlidir.
--   * Organizasyon + kuruluş + tesis seçilirse yalnızca o tesiste geçerlidir.
--   * Ekranda yalnızca tesis seçilirse organizasyon ve kuruluş yapı ağacından (temorgniz) otomatik doldurulur (servis katmanı).
--   * Bir tesis, kendi tanımlarına ek olarak bağlı olduğu kuruluş ve organizasyon düzeyindeki tanımları da kapsar.
-- Eski "orgvid boşsa tüm organizasyonlar" anlamı kaldırıldı: orgvid artık zorunludur.
ALTER TABLE temorgmde ALTER COLUMN orgvid SET NOT NULL;
ALTER TABLE temorgmde ADD COLUMN kurvid BIGINT REFERENCES temorgkur (vid);
ALTER TABLE temorgmde ADD COLUMN tesvid BIGINT REFERENCES temorgtes (vid);
-- Tesis seçildiyse kuruluş da seçilmiş olmalı
ALTER TABLE temorgmde ADD CONSTRAINT temorgmde_kapsam_ck CHECK (tesvid IS NULL OR kurvid IS NOT NULL);

-- Aynı tablo+alan adı, aynı kapsamda bir kez tanımlanır
DROP INDEX temorgmde_tanim_uq;
CREATE UNIQUE INDEX temorgmde_tanim_uq
    ON temorgmde (tablokodu, alanadi, orgvid, COALESCE(kurvid, 0), COALESCE(tesvid, 0));
CREATE INDEX temorgmde_kurvid_ix ON temorgmde (kurvid);
CREATE INDEX temorgmde_tesvid_ix ON temorgmde (tesvid);

-- Seçilen organizasyon / kuruluş / tesis, yapı ağacında (temorgniz) aktif olarak birbirine bağlı olmalıdır.
CREATE FUNCTION temorgmde_kapsam_kontrol() RETURNS trigger AS $$
BEGIN
    IF NOT NEW.pasif AND NEW.kurvid IS NOT NULL THEN
        IF NOT EXISTS (SELECT 1 FROM temorgniz
                        WHERE orgvid = NEW.orgvid AND kurvid = NEW.kurvid
                          AND (NEW.tesvid IS NULL OR tesvid = NEW.tesvid)
                          AND NOT pasif) THEN
            RAISE EXCEPTION 'Organizasyon (%), kuruluş (%) ve tesis (%) yapı ağacında aktif olarak bağlı değil',
                NEW.orgvid, NEW.kurvid, NEW.tesvid USING ERRCODE = '23514';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER temorgmde_kapsam_kontrol_tg
    BEFORE INSERT OR UPDATE ON temorgmde
    FOR EACH ROW EXECUTE FUNCTION temorgmde_kapsam_kontrol();
