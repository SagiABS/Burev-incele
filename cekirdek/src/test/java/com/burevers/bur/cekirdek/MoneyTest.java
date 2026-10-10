package com.burevers.bur.cekirdek;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

/**
 * Money sınıfının davranış testleri.
 * Muhasebede kuruş hatası kabul edilemez; bu testler temel kuralları güvence altına alır.
 */
class MoneyTest {

    @Test
    void ayniParaBirimindeTutarlarToplanir() {
        Money sonuc = Money.of("100.25", "TRY").topla(Money.of("50.75", "TRY"));

        assertThat(sonuc).isEqualTo(Money.of("151.00", "TRY"));
    }

    @Test
    void olcekFarkliOlsaDaAyniTutarEsitSayilir() {
        // 10.5 ile 10.5000 aynı para olmalı (kurucu ölçeği sabitliyor)
        assertThat(Money.of("10.5", "USD")).isEqualTo(Money.of("10.5000", "USD"));
    }

    @Test
    void farkliParaBirimleriToplanamaz() {
        Money tl = Money.of("10", "TRY");
        Money dolar = Money.of("10", "USD");

        assertThatThrownBy(() -> tl.topla(dolar))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Farklı para birimleri");
    }

    @Test
    void dortBasamaktanFazlaOndalikSessizceYuvarlanmaz() {
        // 5 ondalık basamak: kurucu hata vermeli, veri kaybı gizlenmemeli
        assertThatThrownBy(() -> Money.of("1.00005", "TRY"))
                .isInstanceOf(ArithmeticException.class);
    }

    @Test
    void carpmaAcikYuvarlamaModuylaYapilir() {
        // 100.00 * 0.20 vergi oranı = 20.00
        Money kdv = Money.of("100", "TRY").carp(new BigDecimal("0.20"), RoundingMode.HALF_UP);

        assertThat(kdv).isEqualTo(Money.of("20", "TRY"));
    }

    @Test
    void paraBirimininResmiBasamagunaYuvarlanir() {
        // TRY için 2 basamak: 10.0050 -> 10.01 (HALF_UP)
        Money yuvarlanmis = Money.of("10.0050", "TRY").yuvarla(RoundingMode.HALF_UP);

        assertThat(yuvarlanmis).isEqualTo(Money.of("10.01", "TRY"));
    }

    @Test
    void japonYeniOndalikliTutarlaTamSayiyaYuvarlanir() {
        // JPY için 0 basamak
        Money yuvarlanmis = Money.of("100.6000", "JPY").yuvarla(RoundingMode.HALF_UP);

        assertThat(yuvarlanmis).isEqualTo(Money.of("101", "JPY"));
    }

    @Test
    void negatifVeSifirKontrolleriCalisir() {
        assertThat(Money.of("5", "EUR").negatif().negatifMi()).isTrue();
        assertThat(Money.of("0", "EUR").sifirMi()).isTrue();
    }

    @Test
    void siralamaAyniParaBirimindeCalisir() {
        assertThat(Money.of("1", "SAR")).isLessThan(Money.of("2", "SAR"));
    }
}
