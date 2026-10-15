package com.burevers.bur.uygulama.kabuk;

import com.burevers.bur.temekr.TemekrduzenDto;
import com.burevers.bur.temekr.TemekrveriService;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Genel LİSTE ekranı şablonu: üstte düğme çubuğu, ortada tablo, altında toplam kayıt sayısı;
 * alt ve sağ paneller ilk açılışta KAPALIDIR (açma/kapama düğmesiyle açılır).
 *
 * <p>Neden var: Her liste ekranı aynı iskeleti kullanır (belge 11). Kolonlar, paneller ve başlıklar koda
 * gömülü değildir: {@link TemekrduzenDto} düzeninden (temekrbol / temekrkol) kurulur. Düzende ALT ya da SAG
 * yuvasında bölüm yoksa o panel ve düğmesi hiç görünmez; bölümün {@code acik} değeri ilk açılış durumunu belirler.
 * Satırlar sunucu tarafında sayfalanır ({@link TemekrveriService}); tüm tablo belleğe alınmaz.
 * Düğmeler henüz iş yapmaz; yalnızca yerleşim ve yetki davranışı (dışa aktar izni) gösterilir.
 */
public class ListeSablonu extends VerticalLayout {

    private static final String IZIN_DISARI_AKTAR = "DISARIAKTAR";

    private final Div altPanel = new Div();
    private final Div sagPanel = new Div();
    private final Span toplamKayit = new Span();
    private final TemekrduzenDto duzen;
    private final TemekrveriService veriler;

    /**
     * @param duzen   veritabanından çözülmüş ekran düzeni
     * @param veriler satırları sayfalayarak okuyan servis
     * @param yetki   düğme yetkilerini soran sağlayıcı
     */
    public ListeSablonu(TemekrduzenDto duzen, TemekrveriService veriler, YetkiSaglayicisi yetki) {
        this.duzen = duzen;
        this.veriler = veriler;
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        Optional<TemekrduzenDto.Bolum> tabloBolumu = duzen.turdekiBolum("GRID");
        Component tablo = tabloBolumu.<Component>map(this::tabloOlustur)
                .orElseGet(() -> new Span("Bu ekranın düzeninde tablo bölümü tanımlı değil."));

        // Alt ve sağ paneller yalnızca düzende tanımlıysa vardır; ilk açılış durumu bölümün 'acik' değeridir
        Optional<TemekrduzenDto.Bolum> alt = duzen.yuvadakiBolum("ALT");
        Optional<TemekrduzenDto.Bolum> sag = duzen.yuvadakiBolum("SAG");
        altPanel.setVisible(alt.map(TemekrduzenDto.Bolum::acik).orElse(false));
        altPanel.setWidthFull();
        altPanel.setHeight("200px");
        altPanel.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct, #ccc)");
        altPanel.add(new Span(alt.map(TemekrduzenDto.Bolum::baslik).orElse("")));

        sagPanel.setVisible(sag.map(TemekrduzenDto.Bolum::acik).orElse(false));
        sagPanel.setWidth("300px");
        sagPanel.setHeightFull();
        sagPanel.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct, #ccc)");
        sagPanel.add(new Span(sag.map(TemekrduzenDto.Bolum::baslik).orElse("")));

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

        add(dugmeCubugu(duzen.siskod(), yetki, alt.isPresent(), sag.isPresent()), govde, altSerit);
        setFlexGrow(1, govde);
    }

    /**
     * Tablo bölümünün kolonlarından Grid kurar; satırlar her kaydırmada sunucudan sayfa sayfa gelir.
     * Başlık ve genişlik düzenden, sıralama yalnızca 'siralanabilir' kolonlarda açıktır.
     */
    private Grid<Map<String, Object>> tabloOlustur(TemekrduzenDto.Bolum bolum) {
        Grid<Map<String, Object>> tablo = new Grid<>();
        tablo.setSizeFull();
        for (TemekrduzenDto.Kolon k : bolum.kolonlar()) {
            // Dinamik (ek) alanlar ve gizli kolonlar bu adımda gösterilmez
            if (!k.gorunur() || !"SISTEM".equals(k.kaynak())) {
                continue;
            }
            Grid.Column<Map<String, Object>> sutun = tablo
                    .addColumn(satir -> gosterim(satir.get(k.alanAdi())))
                    .setHeader(k.etiket())
                    .setKey(k.alanAdi())
                    .setResizable(true);
            if (k.genislik() != null) {
                sutun.setWidth(k.genislik() + "px").setFlexGrow(0);
            }
            if (k.siralanabilir()) {
                sutun.setSortProperty(k.alanAdi());
            }
        }

        tablo.setItems(DataProvider.fromCallbacks(
                sorgu -> {
                    List<QuerySortOrder> siralar = sorgu.getSortOrders();
                    String alan = siralar.isEmpty() ? null : siralar.get(0).getSorted();
                    boolean artan = siralar.isEmpty()
                            || siralar.get(0).getDirection() == com.vaadin.flow.data.provider.SortDirection.ASCENDING;
                    return veriler.sayfaGetir(duzen, bolum.kodu(), alan, artan,
                            sorgu.getOffset(), sorgu.getLimit()).satirlar().stream();
                },
                sorgu -> {
                    long adet = veriler.toplamKayit(duzen);
                    guncelleToplam(adet);
                    return (int) Math.min(adet, Integer.MAX_VALUE);
                }));
        return tablo;
    }

    /** Hücre değerini yazıya çevirir: boş değer boş görünür, mantıksal değer Evet/Hayır olur. */
    private static String gosterim(Object deger) {
        if (deger == null) {
            return "";
        }
        if (deger instanceof Boolean b) {
            return b ? "Evet" : "Hayır";
        }
        return deger.toString();
    }

    /** Üstteki düğme çubuğu: Kaydet, Kopya, Yazdır, Sil, Dışa Aktar, Parametre, Görüntü + panel düğmeleri. */
    private Component dugmeCubugu(String siskod, YetkiSaglayicisi yetki, boolean altVar, boolean sagVar) {
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
        HorizontalLayout sag = new HorizontalLayout();
        // Düzende olmayan panelin açma düğmesi de gösterilmez
        if (altVar) {
            sag.add(altAc);
        }
        if (sagVar) {
            sag.add(sagAc);
        }
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
