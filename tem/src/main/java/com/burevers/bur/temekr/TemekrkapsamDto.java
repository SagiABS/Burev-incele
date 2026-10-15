package com.burevers.bur.temekr;

/**
 * Ekran düzeninin hangi kapsam için çözüleceğini söyler: organizasyon / kuruluş / tesis.
 *
 * <p>Neden var: Aynı ekranın düzeni kapsama göre farklı olabilir (belge 11, kalıtım): tesis düzeni, kuruluş
 * düzenini; kuruluş düzeni organizasyon düzenini; o da sistem varsayılanını geçersiz kılar. Bu sınıf,
 * "hangi tanım bu kapsama uyar ve ne kadar özel" sorusunu tek yerde, saf Java olarak (test edilebilir) yanıtlar.
 *
 * @param organizasyonVid çalışılan organizasyon (boş = yalnızca sistem düzeni aranır)
 * @param kurulusVid      çalışılan kuruluş (boş olabilir)
 * @param tesisVid        çalışılan tesis (boş olabilir)
 */
public record TemekrkapsamDto(Long organizasyonVid, Long kurulusVid, Long tesisVid) {

    /** Yalnızca sistem varsayılanının aranacağı kapsam (oturum bilgisi gelene kadar kullanılır). */
    public static TemekrkapsamDto sistem() {
        return new TemekrkapsamDto(null, null, null);
    }

    /**
     * Bir ekran tanımının (org/kur/tes alanları) bu kapsama ne kadar uyduğunu hesaplar.
     *
     * @return -1: tanım bu kapsama uymuyor; 0: sistem varsayılanı; 1: organizasyon; 2: kuruluş; 3: tesis
     *         (büyük puan daha özeldir ve kazanır)
     */
    public int puan(Long tanimOrg, Long tanimKur, Long tanimTes) {
        if (tanimTes != null) {
            return tanimTes.equals(tesisVid) ? 3 : -1;
        }
        if (tanimKur != null) {
            return tanimKur.equals(kurulusVid) ? 2 : -1;
        }
        if (tanimOrg != null) {
            return tanimOrg.equals(organizasyonVid) ? 1 : -1;
        }
        return 0;
    }
}
