package com.burevers.bur.mimari;

import java.util.List;
import java.util.Map;

/**
 * Mimari testlerin ortak sabitleri: modüller ve izin verilen bağımlılık yönleri.
 *
 * <p>Paket kodu 6 karakterdir: modül(3) + grup(3), örn. {@code temorg}. Tablo kodu 9 karakterdir:
 * paket kodu(6) + ad(3), örn. {@code temorgorg}. Java paketi paket kodudur (6 karakter).
 * Bir paketin hangi modüle ait olduğu, adının ilk 3 harfinden anlaşılır. Java paketleri modül adını
 * tekrar etmez: {@code com.burevers.bur.temorg} (alt paket {@code tem} yok).
 *
 * <p>Yeni modül (bel, muh, mht) açıldığında YALNIZCA burada güncellenir; kurallar otomatik uygulanır.
 */
final class MimariSabitleri {

    /** Tüm projenin kök paketi. */
    static final String KOK_PAKET = "com.burevers.bur";

    /** Alan modüllerinin üç harfli kodları. */
    static final List<String> MODULLER = List.of("tem", "bel", "muh", "mht");

    /**
     * Her modülün bağımlı OLABİLECEĞİ modüller (kendisi her zaman serbesttir).
     * Yön: tem ← bel ← muh ve tem ← mht. Bunun dışındaki her modüller arası bağımlılık yasaktır.
     */
    static final Map<String, List<String>> IZINLI_BAGIMLILIKLAR = Map.of(
            "tem", List.of(),
            "bel", List.of("tem"),
            "muh", List.of("tem", "bel"),
            "mht", List.of("tem"));

    /** Geçerli paket kodu: izin verilen modül kodu + 3 küçük ASCII harf (grup). */
    static final String PAKET_KODU_DESENI = "(tem|bel|muh|mht)[a-z]{3}";

    /** Alan modülü olmayan, özel anlamlı kök alt paketleri. */
    static final List<String> OZEL_PAKETLER = List.of("cekirdek", "uygulama");

    private MimariSabitleri() {
        // Yardımcı sınıf: örneği oluşturulamaz
    }

    /** Bir modülün paketlerini eşleştiren ArchUnit deseni, örn. {@code com.burevers.bur.tem*..}. */
    static String modulDeseni(String modul) {
        return KOK_PAKET + "." + modul + "*..";
    }
}
