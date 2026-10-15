package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;
import java.util.Map;

/**
 * Genel LİSTE ekranı şablonu: üstte düğme çubuğu, ortada tablo, altında toplam kayıt sayısı;
 * alt ve sağ paneller ilk açılışta KAPALIDIR (açma/kapama düğmesiyle açılır).
 *
 * <p>Neden var: Her liste ekranı aynı iskeleti kullanır (belge 11). Hangi kolonların görüneceği
 * ileride {@code temekrkol} tablosundan okunacaktır; şimdilik tablo boştur ve kolonsuzdur.
 * Düğmeler henüz iş yapmaz; yalnızca yerleşim ve yetki davranışı (dışa aktar izni) gösterilir.
 */
public class ListeSablonu extends VerticalLayout {

    private static final String IZIN_DISARI_AKTAR = "DISARIAKTAR";

    private final Div altPanel = new Div();
    private final Div sagPanel = new Div();
    private final Span toplamKayit = new Span();

    /**
     * @param siskod ekranın sistem kodu (düzen ve yetki bu koda göre belirlenir)
     * @param baslik ekran başlığı
     * @param yetki  düğme yetkilerini soran sağlayıcı
     */
    public ListeSablonu(String siskod, String baslik, YetkiSaglayicisi yetki) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        Grid<Map<String, Object>> tablo = new Grid<>();
        tablo.setSizeFull();
        // Veri kaynağı sonraki adımda bağlanır; şimdilik boş liste
        tablo.setItems(List.of());
        guncelleToplam(0);

        // Alt ve sağ paneller ilk açılışta kapalı
        altPanel.setVisible(false);
        altPanel.setWidthFull();
        altPanel.setHeight("200px");
        altPanel.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct, #ccc)");
        altPanel.add(new Span("Alt panel"));

        sagPanel.setVisible(false);
        sagPanel.setWidth("300px");
        sagPanel.setHeightFull();
        sagPanel.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct, #ccc)");
        sagPanel.add(new Span("Sağ panel"));

        VerticalLayout orta = new VerticalLayout(tablo, altPanel);
        orta.setPadding(false);
        orta.setSpacing(false);
        orta.setSizeFull();
        orta.setFlexGrow(1, tablo);

        HorizontalLayout govde = new HorizontalLayout(orta, sagPanel);
        govde.setSizeFull();
        govde.setPadding(false);
        govde.setSpacing(false);
        govde.setFlexGrow(1, orta);

        HorizontalLayout altSerit = new HorizontalLayout(toplamKayit);
        altSerit.setWidthFull();
        altSerit.setPadding(true);

        add(dugmeCubugu(siskod, yetki), govde, altSerit);
        setFlexGrow(1, govde);
    }

    /** Üstteki düğme çubuğu: Kaydet, Kopya, Yazdır, Sil, Dışa Aktar, Parametre, Görüntü + panel düğmeleri. */
    private Component dugmeCubugu(String siskod, YetkiSaglayicisi yetki) {
        Button kaydet = dugme("Kaydet", VaadinIcon.CHECK);
        kaydet.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button kopya = dugme("Kopya", VaadinIcon.COPY);
        Button yazdir = dugme("Yazdır", VaadinIcon.PRINT);
        Button sil = dugme("Sil", VaadinIcon.TRASH);
        sil.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button disariAktar = dugme("Dışa Aktar", VaadinIcon.DOWNLOAD);
        // İzin yoksa düğme hiç görünmez (asıl denetim yine sunucuda yapılmalıdır)
        disariAktar.setVisible(yetki.izinVarMi(siskod, IZIN_DISARI_AKTAR));
        Button parametre = dugme("Parametre", VaadinIcon.COG);
        Button goruntu = dugme("Görüntü", VaadinIcon.EYE);

        for (Button d : List.of(kaydet, kopya, yazdir, sil, disariAktar, parametre, goruntu)) {
            d.addClickListener(olay -> Mesaj.bilgi("'" + d.getText() + "' henüz hazır değil"));
        }

        Button altAc = panelDugmesi("Alt panel", VaadinIcon.ANGLE_DOWN, altPanel);
        Button sagAc = panelDugmesi("Sağ panel", VaadinIcon.ANGLE_LEFT, sagPanel);

        HorizontalLayout sol = new HorizontalLayout(kaydet, kopya, yazdir, sil, disariAktar, parametre, goruntu);
        HorizontalLayout sag = new HorizontalLayout(altAc, sagAc);
        HorizontalLayout cubuk = new HorizontalLayout(sol, sag);
        cubuk.setWidthFull();
        cubuk.setPadding(true);
        cubuk.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return cubuk;
    }

    private static Button dugme(String ad, VaadinIcon simge) {
        Button d = new Button(ad, simge.create());
        d.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return d;
    }

    /** Verilen paneli açıp kapatan düğme. */
    private static Button panelDugmesi(String ad, VaadinIcon simge, Component panel) {
        Button d = new Button(simge.create());
        d.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        d.getElement().setAttribute("title", ad + " aç/kapat");
        d.getElement().setAttribute("aria-label", ad + " aç/kapat");
        d.addClickListener(olay -> panel.setVisible(!panel.isVisible()));
        return d;
    }

    /** "Toplam kayıt" yazısını günceller. */
    public final void guncelleToplam(long adet) {
        toplamKayit.setText("Toplam kayıt: " + adet);
    }
}
