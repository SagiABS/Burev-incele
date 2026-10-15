package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Uygulamanın ana yerleşimi ({@code http://localhost:8080/}): solda menü, üstte sekmeler.
 *
 * <p>Neden var: Tüm ekranlar bu kabuğun içinde sekme olarak açılır. Sol menüde işlem arama kutusu,
 * açılır/kapanır gruplar ve en altta kapat / yardım / uyarı / mesaj düğmeleri bulunur.
 * Menü {@link MenuSaglayicisi}'ndan, düğme yetkileri {@link YetkiSaglayicisi}'ndan gelir.
 * Bu sınıf eski karşılama sayfasının ({@code GirisView}) yerini alır; gerçek giriş sonra gelecektir.
 */
@Route("")
@PageTitle("Burever")
public class AnaSayfaView extends AppLayout {

    private static final Locale TR = Locale.forLanguageTag("tr");

    private final MenuSaglayicisi menuSaglayicisi;
    private final YetkiSaglayicisi yetkiSaglayicisi;
    private final SekmeYoneticisi sekmeler = new SekmeYoneticisi();
    private final VerticalLayout menuAlani = new VerticalLayout();

    /** Bağımlılıklar kurucudan alınır (alan enjeksiyonu yasaktır). */
    public AnaSayfaView(MenuSaglayicisi menuSaglayicisi, YetkiSaglayicisi yetkiSaglayicisi) {
        this.menuSaglayicisi = menuSaglayicisi;
        this.yetkiSaglayicisi = yetkiSaglayicisi;

        setPrimarySection(Section.DRAWER);
        addToNavbar(new DrawerToggle(), new H1("Burever"));
        addToDrawer(solPanel());
        setContent(sekmeler);
    }

    /** Sol panel: arama kutusu, menü ağacı, en altta sistem düğmeleri. */
    private Component solPanel() {
        TextField ara = new TextField();
        ara.setPlaceholder("İşlem ara...");
        ara.setPrefixComponent(VaadinIcon.SEARCH.create());
        ara.setClearButtonVisible(true);
        ara.setWidthFull();
        // Her tuşta değil, yazmayı bırakınca süz
        ara.setValueChangeMode(ValueChangeMode.LAZY);
        ara.addValueChangeListener(olay -> menuyuCiz(olay.getValue()));

        menuAlani.setPadding(false);
        menuAlani.setSpacing(false);
        menuyuCiz("");

        VerticalLayout panel = new VerticalLayout(ara, menuAlani, sistemDugmeleri());
        panel.setSizeFull();
        panel.setFlexGrow(1, menuAlani);
        menuAlani.getStyle().set("overflow", "auto");
        return panel;
    }

    /** Menüyü verilen süzgece göre yeniden çizer; süzgeç doluyken eşleşen gruplar açık gelir. */
    private void menuyuCiz(String suzgec) {
        menuAlani.removeAll();
        String s = suzgec == null ? "" : suzgec.trim().toLowerCase(TR);
        for (MenuOgesi ogesi : menuSaglayicisi.menuAgaci()) {
            Component c = ogeyiCiz(ogesi, s);
            if (c != null) {
                menuAlani.add(c);
            }
        }
    }

    /** Öğeyi çizer; süzgece uymayan (ve altında da eşleşen olmayan) öğe için {@code null} döner. */
    private Component ogeyiCiz(MenuOgesi ogesi, String suzgec) {
        if (ogesi.islemMi()) {
            if (!suzgec.isEmpty() && !ogesi.baslik().toLowerCase(TR).contains(suzgec)) {
                return null;
            }
            Button d = new Button(ogesi.baslik());
            d.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            d.setWidthFull();
            d.getStyle().set("justify-content", "flex-start");
            d.addClickListener(olay -> sekmeler.ac(ogesi.siskod(), ogesi.baslik(),
                    () -> new ListeSablonu(ogesi.siskod(), ogesi.baslik(), yetkiSaglayicisi)));
            return d;
        }
        List<Component> cocuklar = new ArrayList<>();
        for (MenuOgesi alt : ogesi.altlar()) {
            Component c = ogeyiCiz(alt, suzgec);
            if (c != null) {
                cocuklar.add(c);
            }
        }
        if (cocuklar.isEmpty()) {
            return null;
        }
        VerticalLayout icerik = new VerticalLayout(cocuklar.toArray(new Component[0]));
        icerik.setPadding(false);
        icerik.setSpacing(false);
        Details grup = new Details(ogesi.baslik(), icerik);
        grup.setWidthFull();
        // Arama yapılıyorsa eşleşen gruplar açık; yoksa kapalı
        grup.setOpened(!suzgec.isEmpty());
        return grup;
    }

    /** En alttaki düğmeler: kapat (çıkış), yardım, uyarılar, mesajlar. İşlevleri sonraki adımlarda bağlanır. */
    private Component sistemDugmeleri() {
        HorizontalLayout serit = new HorizontalLayout(
                simgeDugmesi(VaadinIcon.SIGN_OUT, "Kapat"),
                simgeDugmesi(VaadinIcon.QUESTION_CIRCLE, "Yardım"),
                simgeDugmesi(VaadinIcon.BELL, "Uyarılar"),
                simgeDugmesi(VaadinIcon.ENVELOPE, "Mesajlar"));
        serit.setWidthFull();
        serit.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        return serit;
    }

    private static Button simgeDugmesi(VaadinIcon simge, String ad) {
        Button d = new Button(simge.create());
        d.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        d.getElement().setAttribute("title", ad);
        d.getElement().setAttribute("aria-label", ad);
        d.addClickListener(olay -> Mesaj.bilgi("'" + ad + "' henüz hazır değil"));
        return d;
    }
}
