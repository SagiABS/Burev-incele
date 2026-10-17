package com.burevers.bur.uygulama.kabuk;

import com.burevers.bur.temdil.TemdilcvrService;
import com.burevers.bur.temdil.TemdilcvrService.Cozucu;
import com.burevers.bur.temdil.TemdilcvrService.Dil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Uygulamanın ana yerleşimi ({@code http://localhost:8080/}): solda menü, üstte sekmeler, sağ üstte dil seçici.
 *
 * <p>Neden var: Tüm ekranlar bu kabuğun içinde sekme olarak açılır. Sol menüde "İşlem Bulucu" arama kutusu, renkli
 * başlıklı açılır/kapanır gruplar ve en altta kapat / yardım / uyarı / mesaj düğmeleri bulunur (taslak: Genel Ekran 1-2).
 * Tüm yazılar seçili dile göre {@link TemdilcvrService}'ten gelir; dil değişince sayfa yeniden yüklenir ve Arapça gibi
 * sağdan sola yazılan dillerde arayüz yönü çevrilir. Menü {@link MenuSaglayicisi}'ndan, tıklanan adımın ekranı
 * {@link EkranFabrikasi}'ndan gelir. Gerçek giriş ve kapsam (organizasyon/kuruluş/tesis) oturum bağlanınca eklenecektir.
 */
@Route("")
@PageTitle("Burever")
public class AnaSayfaView extends AppLayout {

    /** Seçilen dilin oturumda tutulduğu anahtar. */
    private static final String DIL_ANAHTARI = "bur.dil";
    private static final Locale TR = Locale.forLanguageTag("tr");

    private final MenuSaglayicisi menuSaglayicisi;
    private final EkranFabrikasi ekranFabrikasi;
    private final Cozucu metinler;
    private final Dil dil;
    private final SekmeYoneticisi sekmeler;
    private final Div menuAlani = new Div();
    /** En son tıklanan menü adımı (seçili görünümü için). */
    private Div seciliOge;

    /** Bağımlılıklar kurucudan alınır (alan enjeksiyonu yasaktır). */
    public AnaSayfaView(MenuSaglayicisi menuSaglayicisi, EkranFabrikasi ekranFabrikasi, TemdilcvrService diller) {
        this.menuSaglayicisi = menuSaglayicisi;
        this.ekranFabrikasi = ekranFabrikasi;

        List<Dil> aktifler = diller.aktifDiller();
        this.dil = seciliDil(aktifler, diller.varsayilanDilKodu());
        // Kapsam (organizasyon/kuruluş/tesis) oturum bağlanana kadar yok: yalnızca sistem metinleri kullanılır
        this.metinler = diller.cozucuGetir(dil.kodu(), null, null, null);
        this.sekmeler = new SekmeYoneticisi(metinler);

        setPrimarySection(Section.DRAWER);
        addToNavbar(new DrawerToggle(), baslik(), bosluk(), dilSecici(aktifler));
        addToDrawer(solPanel());
        setContent(sekmeler);
    }

    /** Sayfa açılınca stil eklenir, dilin yönü (soldan/sağdan) ve yerel ayarı uygulanır. */
    @Override
    protected void onAttach(AttachEvent olay) {
        super.onAttach(olay);
        UI ui = olay.getUI();
        BurStili.uygula(ui);
        ui.setLocale(Locale.forLanguageTag(dil.kodu()));
        ui.setDirection(dil.sagdanSola() ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT);
    }

    /** Oturumdaki dil geçerliyse onu, değilse varsayılan dili döndürür. */
    private static Dil seciliDil(List<Dil> aktifler, String varsayilanKod) {
        VaadinSession oturum = VaadinSession.getCurrent();
        Object kayitli = oturum == null ? null : oturum.getAttribute(DIL_ANAHTARI);
        for (Dil d : aktifler) {
            if (d.kodu().equals(kayitli)) {
                return d;
            }
        }
        for (Dil d : aktifler) {
            if (d.kodu().equals(varsayilanKod)) {
                return d;
            }
        }
        return new Dil(varsayilanKod, varsayilanKod, varsayilanKod, "LTR", true);
    }

    private static H1 baslik() {
        H1 baslik = new H1("Burever");
        baslik.getStyle().set("font-size", "1.25rem").set("margin", "0 0.5rem");
        return baslik;
    }

    private static Div bosluk() {
        Div bosluk = new Div();
        bosluk.getStyle().set("flex-grow", "1");
        return bosluk;
    }

    /** Sağ üstteki dil seçici; seçim oturuma yazılır ve sayfa yeniden yüklenir. */
    private Select<Dil> dilSecici(List<Dil> aktifler) {
        Select<Dil> secici = new Select<>();
        secici.setItems(aktifler);
        secici.setItemLabelGenerator(Dil::yerelAd);
        secici.setValue(dil);
        secici.setWidth("9rem");
        secici.getElement().setAttribute("title", metinler.metin("Dil", "dil.secim"));
        secici.getStyle().set("margin-inline-end", "0.75rem");
        secici.addValueChangeListener(olay -> {
            Dil yeni = olay.getValue();
            if (yeni != null && !yeni.kodu().equals(dil.kodu())) {
                VaadinSession.getCurrent().setAttribute(DIL_ANAHTARI, yeni.kodu());
                UI.getCurrent().getPage().reload();
            }
        });
        return secici;
    }

    /** Sol panel: başlık, "İşlem Bulucu" arama kutusu, kaydırılabilir menü ağacı, en altta sistem düğmeleri. */
    private Component solPanel() {
        Div panel = new Div();
        panel.addClassName("bur-sol");

        Span baslik = new Span("Burever");
        baslik.addClassName("bur-sol-baslik");
        Span etiket = new Span(metinler.metin("İşlem Bulucu", "ara.baslik"));
        etiket.addClassName("bur-sol-etiket");

        TextField ara = new TextField();
        ara.setPlaceholder(metinler.metin("İşlem ara...", "ara.islem"));
        ara.setPrefixComponent(VaadinIcon.SEARCH.create());
        ara.setClearButtonVisible(true);
        ara.setWidthFull();
        // Her tuşta değil, yazmayı bırakınca süz
        ara.setValueChangeMode(ValueChangeMode.LAZY);
        ara.addValueChangeListener(olay -> menuyuCiz(olay.getValue()));

        menuAlani.addClassName("bur-menu-alani");
        menuyuCiz("");

        panel.add(baslik, etiket, ara, menuAlani, sistemDugmeleri());
        return panel;
    }

    /** Menüyü verilen süzgece göre yeniden çizer; süzgeç doluyken eşleşen gruplar açık gelir. */
    private void menuyuCiz(String suzgec) {
        menuAlani.removeAll();
        seciliOge = null;
        String s = suzgec == null ? "" : suzgec.trim().toLowerCase(TR);
        for (MenuOgesi ogesi : menuSaglayicisi.menuAgaci()) {
            Component c = ogeyiCiz(ogesi, s);
            if (c != null) {
                menuAlani.add(c);
            }
        }
    }

    /** Menü öğesinin görünen adı: önce dile göre çeviri (menu.&lt;kod&gt;), yoksa öğenin kendi başlığı. */
    private String gorunenAd(MenuOgesi ogesi) {
        return metinler.metin(ogesi.baslik(), "menu." + ogesi.kod());
    }

    /** Öğeyi çizer; süzgece uymayan (ve altında da eşleşen olmayan) öğe için {@code null} döner. */
    private Component ogeyiCiz(MenuOgesi ogesi, String suzgec) {
        String ad = gorunenAd(ogesi);
        if (ogesi.islemMi()) {
            if (!suzgec.isEmpty() && !ad.toLowerCase(TR).contains(suzgec)) {
                return null;
            }
            Div oge = new Div(new Span(ad));
            oge.addClassName("bur-menu-oge");
            oge.getElement().setAttribute("role", "button");
            oge.getElement().setAttribute("tabindex", "0");
            Runnable ac = () -> {
                if (seciliOge != null) {
                    seciliOge.removeClassName("bur-aktif");
                }
                oge.addClassName("bur-aktif");
                seciliOge = oge;
                sekmeler.ac(ogesi.siskod(), ad, () -> ekranFabrikasi.olustur(ogesi.siskod(), ad, metinler));
            };
            oge.addClickListener(olay -> ac.run());
            oge.getElement().addEventListener("keydown", olay -> ac.run())
                    .setFilter("event.key === 'Enter' || event.key === ' '");
            return oge;
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
        Div icerik = new Div(cocuklar.toArray(new Component[0]));
        icerik.addClassName("bur-grup-icerik");

        Span isaret = new Span("−");
        isaret.addClassName("bur-grup-isaret");
        Span grupAdi = new Span(ad);
        grupAdi.addClassName("bur-grup-ad");
        Div baslik = new Div();
        baslik.addClassName("bur-grup-baslik");
        if (ogesi.ikon() != null) {
            baslik.add(ogesi.ikon().create());
        }
        baslik.add(grupAdi, isaret);
        baslik.getElement().setAttribute("role", "button");
        baslik.getElement().setAttribute("tabindex", "0");
        Runnable degistir = () -> {
            boolean acik = !icerik.isVisible();
            icerik.setVisible(acik);
            isaret.setText(acik ? "−" : "+");
        };
        baslik.addClickListener(olay -> degistir.run());
        baslik.getElement().addEventListener("keydown", olay -> degistir.run())
                .setFilter("event.key === 'Enter' || event.key === ' '");

        Div grup = new Div(baslik, icerik);
        grup.addClassName("bur-grup");
        return grup;
    }

    /** En alttaki düğmeler: kapat (çıkış), yardım, uyarılar, mesajlar. İşlevleri sonraki adımlarda bağlanır. */
    private Component sistemDugmeleri() {
        Div serit = new Div(
                sistemDugmesi(VaadinIcon.POWER_OFF, "dugme.kapat", "Kapat", "bur-kapat"),
                sistemDugmesi(VaadinIcon.QUESTION_CIRCLE, "dugme.yardim", "Yardım", ""),
                sistemDugmesi(VaadinIcon.BELL, "dugme.uyarilar", "Uyarılar", ""),
                sistemDugmesi(VaadinIcon.ENVELOPE, "dugme.mesajlar", "Mesajlar", ""));
        serit.addClassName("bur-sistem-serit");
        return serit;
    }

    private Div sistemDugmesi(VaadinIcon simge, String anahtar, String yedek, String ekSinif) {
        String ad = metinler.metin(yedek, anahtar);
        Div d = new Div(simge.create(), new Span(ad));
        d.addClassName("bur-sistem-dugme");
        if (!ekSinif.isEmpty()) {
            d.addClassName(ekSinif);
        }
        d.getElement().setAttribute("role", "button");
        d.getElement().setAttribute("tabindex", "0");
        d.getElement().setAttribute("title", ad);
        Runnable islem = () -> Mesaj.bilgi("'" + ad + "' " + metinler.metin("henüz hazır değil", "mesaj.henuzhazir"));
        d.addClickListener(olay -> islem.run());
        d.getElement().addEventListener("keydown", olay -> islem.run())
                .setFilter("event.key === 'Enter' || event.key === ' '");
        return d;
    }
}
