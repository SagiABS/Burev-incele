package com.burevers.bur.temekr;

import java.util.List;

/**
 * Bir ekranın çözülmüş düzeni: ekran bilgisi, bölümleri ve tablo bölümlerinin kolonları.
 *
 * <p>Neden var: Arayüz, JPA nesnelerini (tembel yüklemeli) doğrudan görmesin diye düzen, değişmez bir
 * kopya olarak verilir. Aynı kopya önbellekte de tutulur; tüm kullanıcılar paylaşır, bu yüzden değişmezdir.
 *
 * @param ekranVid  ekran tanımının vid'i
 * @param siskod    ekranın sistem kodu
 * @param varyant   varyant adı (örn. ANA)
 * @param baslik    ekran başlığı
 * @param sablon    LISTE veya BELGE
 * @param tabloKodu ekranın ana tablosu
 * @param surum     düzen sürümü
 * @param bolumler  üst düzey ve iç içe tüm bölümler, yuva ve sıraya göre
 */
public record TemekrduzenDto(Long ekranVid, String siskod, String varyant, String baslik, String sablon,
                             String tabloKodu, int surum, List<Bolum> bolumler) {

    /** Düzendeki bir bölüm (çerçeve). {@code ustKodu} boşsa üst düzey bölümdür. */
    public record Bolum(String kodu, String ustKodu, String turu, String yuva, String baslik, int sira,
                        boolean surukle, boolean acik, List<Kolon> kolonlar) {

        /** Bu bölüm, verilen türde mi (GRID, FORM, SEKME, OZET)? */
        public boolean turuMu(String tur) {
            return tur.equals(turu);
        }
    }

    /** Tablo (GRID) bölümündeki bir kolon; alan bilgisi veri sözlüğünden (temcusdba) gelir. */
    public record Kolon(String alanAdi, String veriTipi, String kaynak, String etiket, Integer genislik,
                        String bicim, String toplam, boolean siralanabilir, boolean filtrelenebilir,
                        boolean gorunur) {
    }

    /** Verilen yuvadaki ilk bölümü bulur (örn. "ALT", "SAG", "ORTA"); yoksa boş döner. */
    public java.util.Optional<Bolum> yuvadakiBolum(String yuva) {
        return bolumler.stream().filter(b -> yuva.equals(b.yuva())).findFirst();
    }

    /** Verilen türdeki ilk bölümü bulur; yoksa boş döner. */
    public java.util.Optional<Bolum> turdekiBolum(String tur) {
        return bolumler.stream().filter(b -> b.turuMu(tur)).findFirst();
    }
}
