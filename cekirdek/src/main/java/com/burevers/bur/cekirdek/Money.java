package com.burevers.bur.cekirdek;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Para tutarı değer nesnesi: bir tutar ve onun para birimini birlikte taşır.
 *
 * <p>Neden var? ERP'de tutarlar asla {@code double} ile tutulmaz (kuruş hataları oluşur) ve
 * tutar, para biriminden ayrı dolaşmamalıdır. Bu sınıf ikisini tek nesnede toplar (ADR-12).
 *
 * <p>Kurallar:
 * <ul>
 *   <li>Tutar her zaman {@link #ISLEM_OLCEGI} ondalık basamakla saklanır (veritabanı: NUMERIC(19,4)).</li>
 *   <li>Farklı para birimleri doğrudan toplanamaz; kur çevirimi temel tanımlar (tem) modülünün işidir.</li>
 *   <li>Yuvarlama gizlice yapılmaz; her zaman yuvarlama modu açıkça verilir. Ülkeye göre
 *       yuvarlama kuralı değişebileceği için karar çağıran koddadır (ülke paketleri).</li>
 * </ul>
 *
 * <p>Bu sınıf değişmezdir (immutable); her işlem yeni bir nesne döndürür.
 *
 * @param tutar      işlem ölçeğinde (4 basamak) tutar
 * @param paraBirimi ISO 4217 para birimi (ör. TRY, USD, EUR, SAR)
 */
public record Money(BigDecimal tutar, Currency paraBirimi) implements Comparable<Money> {

    /** İşlemlerde kullanılan ondalık basamak sayısı. Veritabanı sütun ölçeğiyle aynı olmalıdır. */
    public static final int ISLEM_OLCEGI = 4;

    /**
     * Kurucu: değerleri doğrular ve tutarın ölçeğini sabitler.
     *
     * <p>Ölçeği sabitlemek önemlidir: 10.5 ile 10.5000 aynı para olmalı ve {@code equals} ile
     * eşit çıkmalıdır (BigDecimal normalde ölçeğe duyarlıdır).
     * Tutar 4 basamaktan fazla ondalık içeriyorsa hata verilir; sessiz yuvarlama yapılmaz.
     */
    public Money {
        Objects.requireNonNull(tutar, "tutar boş olamaz");
        Objects.requireNonNull(paraBirimi, "paraBirimi boş olamaz");
        // RoundingMode verilmediği için yuvarlama gerekirse ArithmeticException fırlatır (bilinçli)
        tutar = tutar.setScale(ISLEM_OLCEGI);
    }

    /**
     * Metinden Money üretir. Örnek: {@code Money.of("1250.50", "TRY")}.
     *
     * @param tutar            ondalık nokta ile yazılmış tutar
     * @param paraBirimiKodu   ISO 4217 para birimi kodu
     * @throws IllegalArgumentException para birimi kodu geçersizse
     */
    public static Money of(String tutar, String paraBirimiKodu) {
        return new Money(new BigDecimal(tutar), Currency.getInstance(paraBirimiKodu));
    }

    /** Verilen para biriminde sıfır tutar döndürür. */
    public static Money sifir(Currency paraBirimi) {
        return new Money(BigDecimal.ZERO, paraBirimi);
    }

    /**
     * İki tutarı toplar. Para birimleri aynı olmak zorundadır.
     *
     * @throws IllegalArgumentException para birimleri farklıysa
     */
    public Money topla(Money diger) {
        ayniParaBirimiMi(diger);
        return new Money(tutar.add(diger.tutar), paraBirimi);
    }

    /**
     * Bu tutardan diğerini çıkarır. Para birimleri aynı olmak zorundadır.
     *
     * @throws IllegalArgumentException para birimleri farklıysa
     */
    public Money cikar(Money diger) {
        ayniParaBirimiMi(diger);
        return new Money(tutar.subtract(diger.tutar), paraBirimi);
    }

    /** İşareti ters çevirir (ör. alacak/borç dönüşümü için). */
    public Money negatif() {
        return new Money(tutar.negate(), paraBirimi);
    }

    /**
     * Tutarı bir çarpanla çarpar (ör. miktar, vergi oranı) ve sonucu işlem ölçeğine yuvarlar.
     *
     * @param carpan         çarpan (ör. 0.20 = yüzde 20)
     * @param yuvarlamaModu  4. ondalıktan sonrası için yuvarlama kuralı (açıkça verilmelidir)
     */
    public Money carp(BigDecimal carpan, RoundingMode yuvarlamaModu) {
        Objects.requireNonNull(carpan, "carpan boş olamaz");
        Objects.requireNonNull(yuvarlamaModu, "yuvarlamaModu boş olamaz");
        BigDecimal sonuc = tutar.multiply(carpan).setScale(ISLEM_OLCEGI, yuvarlamaModu);
        return new Money(sonuc, paraBirimi);
    }

    /**
     * Tutarı para biriminin resmi ondalık basamağına yuvarlar (ör. TRY ve USD için 2, JPY için 0)
     * ve yine işlem ölçeğinde döndürür. Belge toplamı yazdırırken veya fişe geçerken kullanılır.
     *
     * @throws IllegalStateException para biriminin ondalık basamağı tanımsızsa (sanal para birimleri)
     */
    public Money yuvarla(RoundingMode yuvarlamaModu) {
        Objects.requireNonNull(yuvarlamaModu, "yuvarlamaModu boş olamaz");
        int basamak = paraBirimi.getDefaultFractionDigits();
        if (basamak < 0) {
            throw new IllegalStateException(paraBirimi + " için ondalık basamak sayısı tanımlı değil");
        }
        return new Money(tutar.setScale(basamak, yuvarlamaModu), paraBirimi);
    }

    /** Tutar sıfır mı? */
    public boolean sifirMi() {
        return tutar.signum() == 0;
    }

    /** Tutar sıfırdan küçük mü? */
    public boolean negatifMi() {
        return tutar.signum() < 0;
    }

    /**
     * Sıralama: yalnızca aynı para birimindeki tutarlar karşılaştırılabilir.
     *
     * @throws IllegalArgumentException para birimleri farklıysa
     */
    @Override
    public int compareTo(Money diger) {
        ayniParaBirimiMi(diger);
        return tutar.compareTo(diger.tutar);
    }

    /** Okunur biçim, ör. "1250.5000 TRY". */
    @Override
    public String toString() {
        return tutar.toPlainString() + " " + paraBirimi.getCurrencyCode();
    }

    /** Para birimi aynı değilse işlemi durdurur; farklı birimler kurla çevrilmeden karıştırılamaz. */
    private void ayniParaBirimiMi(Money diger) {
        Objects.requireNonNull(diger, "diger boş olamaz");
        if (!paraBirimi.equals(diger.paraBirimi)) {
            throw new IllegalArgumentException(
                    "Farklı para birimleri doğrudan işlenemez: " + paraBirimi + " ve " + diger.paraBirimi);
        }
    }
}
