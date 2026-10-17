package com.burevers.bur.temdil;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

/** Metin çözücünün sıralı anahtar, yedek dil ve yer tutucu davranışlarının testi. */
class TemdilcvrServiceTest {

    private final TemdilcvrService.Cozucu cozucu = new TemdilcvrService.Cozucu(
            Map.of("alan.temorgorg.kodu", "Organizasyon Kodu", "alan.adi", "Adı", "mesaj.ensekme", "En çok {0} sekme"),
            Map.of("alan.kodu", "Code", "alan.adi", "Name"));

    /** Özel anahtar genel anahtardan önce gelir. */
    @Test
    void ozelAnahtarOnceBulunur() {
        assertThat(cozucu.metin("yedek", "alan.temorgorg.kodu", "alan.kodu")).isEqualTo("Organizasyon Kodu");
    }

    /** İstenen dilde yoksa yedek (varsayılan) dildeki metin kullanılır. */
    @Test
    void istenenDildeYoksaYedekDilKullanilir() {
        assertThat(cozucu.metin("yedek", "alan.temorgkur.kodu", "alan.kodu")).isEqualTo("Code");
    }

    /** Asıl dilde bulunan metin yedek dili yener. */
    @Test
    void asilDilYedekDilYener() {
        assertThat(cozucu.metin("yedek", "alan.adi")).isEqualTo("Adı");
    }

    /** Hiçbir yerde yoksa çağıranın verdiği yedek metin döner. */
    @Test
    void hicbiriYoksaYedekMetinDoner() {
        assertThat(cozucu.metin("kodu_yok", "alan.yok")).isEqualTo("kodu_yok");
    }

    /** {0} gibi yer tutucular değerlerle doldurulur. */
    @Test
    void yerTutucuDoldurulur() {
        assertThat(cozucu.bicimli("mesaj.ensekme", "x", 10)).isEqualTo("En çok 10 sekme");
    }
}
