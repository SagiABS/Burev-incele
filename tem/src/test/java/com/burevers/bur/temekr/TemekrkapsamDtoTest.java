package com.burevers.bur.temekr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** Kapsam kalıtımı puanlamasının testi: en özel tanımın kazanması bu puanlara dayanır. */
class TemekrkapsamDtoTest {

    private final TemekrkapsamDto kapsam = new TemekrkapsamDto(1L, 10L, 100L);

    /** Hiçbir kapsamı olmayan tanım sistem varsayılanıdır (0). */
    @Test
    void sistemVarsayilaniSifirPuanAlir() {
        assertThat(kapsam.puan(null, null, null)).isZero();
    }

    /** Uyan organizasyon, kuruluş ve tesis tanımları giderek daha yüksek puan alır. */
    @Test
    void ozelKapsamDahaYuksekPuanAlir() {
        assertThat(kapsam.puan(1L, null, null)).isEqualTo(1);
        assertThat(kapsam.puan(1L, 10L, null)).isEqualTo(2);
        assertThat(kapsam.puan(1L, 10L, 100L)).isEqualTo(3);
    }

    /** Başka kiracıya ait tanım uymaz (-1). */
    @Test
    void baskaKapsamaAitTanimUymaz() {
        assertThat(kapsam.puan(2L, null, null)).isEqualTo(-1);
        assertThat(kapsam.puan(1L, 11L, null)).isEqualTo(-1);
        assertThat(kapsam.puan(1L, 10L, 101L)).isEqualTo(-1);
    }

    /** Oturum yokken (sistem kapsamı) yalnızca sistem varsayılanı uyar. */
    @Test
    void sistemKapsamiYalnizcaSistemDuzenineUyar() {
        TemekrkapsamDto sistem = TemekrkapsamDto.sistem();
        assertThat(sistem.puan(null, null, null)).isZero();
        assertThat(sistem.puan(1L, null, null)).isEqualTo(-1);
    }
}
