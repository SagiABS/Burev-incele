package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.icon.VaadinIcon;
import java.util.List;

/**
 * Sol menüdeki tek bir öğeyi (grup ya da işlem adımı) tanımlar.
 *
 * <p>Neden var: Menü, arayüz kodundan bağımsız bir veri modeliyle tutulur. Böylece menünün kaynağı
 * (şimdilik sabit liste; ileride veritabanındaki menü tabloları) değişse de ekran kodu değişmez.
 *
 * @param kod     menü içinde tekil kod (arama ve sekme anahtarı için)
 * @param baslik  kullanıcıya görünen ad
 * @param ikon    öğenin simgesi (boş olabilir)
 * @param siskod  işlem adımının sistem kodu (örn. {@code tem_893_000}); gruplarda {@code null}
 * @param altlar  alt öğeler; işlem adımlarında boş liste
 */
public record MenuOgesi(String kod, String baslik, VaadinIcon ikon, String siskod, List<MenuOgesi> altlar) {

    /** Grup (içinde başka öğeler olan açılır başlık) oluşturur. */
    public static MenuOgesi grup(String kod, String baslik, VaadinIcon ikon, MenuOgesi... altlar) {
        return new MenuOgesi(kod, baslik, ikon, null, List.of(altlar));
    }

    /** Tıklanınca ekran açan işlem adımı oluşturur. */
    public static MenuOgesi islem(String kod, String baslik, String siskod) {
        return new MenuOgesi(kod, baslik, null, siskod, List.of());
    }

    /** İşlem adımı mı (ekran açar), yoksa grup mu? */
    public boolean islemMi() {
        return siskod != null;
    }
}
