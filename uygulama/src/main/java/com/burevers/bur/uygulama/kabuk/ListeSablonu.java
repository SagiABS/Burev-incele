package com.burevers.bur.uygulama.kabuk;

import com.burevers.bur.temdil.TemdilcvrService.Cozucu;
import com.burevers.bur.temekr.TemekrduzenDto;
import com.burevers.bur.temekr.TemekrveriService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Genel LİSTE ekranı şablonu: üstte araç çubuğu (ikon üstte, yazı altta), ortada sunucu tarafı sayfalanan tablo,
 * altta toplam kayıt şeridi; alt ve sağ paneller ilk açılışta KAPALIDIR, açıldığında SÜRÜKLENEREK boyutlandırılır.
 *
 * <p>Neden var: Her liste ekranı aynı iskeleti kullanır (belge 11). Kolonlar, paneller ve başlıklar koda gömülü değildir:
 * {@link TemekrduzenDto} düzeninden (temekrbol / temekrkol) kurulur; yazılar çeviri çözücüsünden ({@link Cozucu}) gelir,
 * yani seçili dile göre değişir. Düzende ALT ya da SAG yuvasında bölüm yoksa o panel ve düğmesi hiç görünmez.
 * Satırlar sunucu tarafında sayfalanır ({@link TemekrveriService}); tüm tablo belleğe alınmaz.
 * Düğmeler henüz iş yapmaz; yalnızca yerleşim ve yetki davranışı (dışa aktar izni) gösterilir.
 */
public class ListeSablonu extends VerticalLayout {

    private static final String IZIN_DISARI_AKTAR = "DISARIAKTAR";

    private final TemekrduzenDto duzen;
    private final TemekrveriService veriler;
    private final Cozucu metinler;

    private final Component tablo;
    private final Div altPanel;
    private final Div sagPanel;
    private final boolean altVar;
    private final boolean sagVar;
    private boolean altAcik;
    private boolean sagAcik;

    /** Tablo, alt ve sağ panellerin yerleşimini taşıyan alan; açma/kapama sırasında yeniden kurulur. */
    private final VerticalLayout govde = new VerticalLayout();
    private final Span toplamKayit = new Span();

    /**
     * @param duzen    veritabanından çözülmüş ekran düzeni
     * @param veriler  satırları sayfalayarak okuyan servis
     * @param yetki    düğme yetkilerini soran sağlayıcı
     * @param metinler seçili dil ve kapsam için metin çözücü
     */
    public ListeSablonu(TemekrduzenDto duzen, TemekrveriService veriler, YetkiSaglayicisi yetki, Cozucu metinler) {
        this.duzen = duzen;
        this.veriler = veriler;
        this.metinler = metinler;
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        Optional<TemekrduzenDto.Bolum> tabloBolumu = duzen.turdekiBolum("GRID");
        this.tablo = tabloBolumu.<Component>map(this::tabloOlustur)
                .orElseGet(() -> new Span(metinler.metin("Bu ekranın düzeninde tablo bölümü tanımlı değil.",
                        "mesaj.tabloyok")));

        // Alt ve sağ paneller yalnızca düzende tanımlıysa vardır; ilk açılış durumu bölümün 'acik' değeridir
        Optional<TemekrduzenDto.Bolum> alt = duzen.yuvadakiBolum("ALT");
        Optional<TemekrduzenDto.Bolum> sag = duzen.yuvadakiBolum("SAG");
        this.altVar = alt.isPresent();
        this.sagVar = sag.isPresent();
        this.altAcik = alt.map(TemekrduzenDto.Bolum::acik).orElse(false);
        this.sagAcik = sag.map(TemekrduzenDto.Bolum::acik).orElse(false);
        this.altPanel = panelOlustur(alt);
        this.sagPanel = panelOlustur(sag);

        govde.setSizeFull();
        govde.setPadding(false);
        govde.setSpacing(false);
        govde.addClassName("bur-govde");
        yerlesimiKur();

        Div altSerit = new Div(new Span(metinler.metin("Toplam kayıt", "liste.toplamkayit") + ": "), toplamKayit);
        altSerit.addClassName("bur-durum-seridi");
        guncelleToplam(0);

        add(aracCubugu(duzen.siskod(), yetki), govde, altSerit);
        setFlexGrow(1, govde);
    }

    /**
     * Tablo bölümünün kolonlarından Grid kurar; satırlar her kaydırmada sunucudan sayfa sayfa gelir.
     * Başlık: ekrana özel etiket varsa o, yoksa dile göre çeviri (alan.&lt;tablo&gt;.&lt;alan&gt;, sonra alan.&lt;alan&gt;),
     * o da yoksa alan adı. Sıralama yalnızca 'siralanabilir' kolonlarda açıktır.
     */
    private Grid<Map<String, Object>> tabloOlustur(TemekrduzenDto.Bolum bolum) {
        Grid<Map<String, Object>> grid = new Grid<>();
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        for (TemekrduzenDto.Kolon k : bolum.kolonlar()) {
            // Dinamik (ek) alanlar ve gizli kolonlar bu adımda gösterilmez
            if (!k.gorunur() || !"SISTEM".equals(k.kaynak())) {
                continue;
            }
            String baslik = k.etiket() != null ? k.etiket()
                    : metinler.metin(k.alanAdi(), "alan." + duzen.tabloKodu() + "." + k.alanAdi(),
                            "alan." + k.alanAdi());
            Grid.Column<Map<String, Object>> sutun = grid
                    .addColumn(satir -> gosterim(satir.get(k.alanAdi())))
                    .setHeader(baslik)
                    .setKey(k.alanAdi())
                    .setResizable(true);
            if (k.genislik() != null) {
                sutun.setWidth(k.genislik() + "px").setFlexGrow(0);
            }
            if (k.siralanabilir()) {
                sutun.setSortProperty(k.alanAdi());
            }
        }

        grid.setItems(DataProvider.fromCallbacks(
                sorgu -> {
                    List<QuerySortOrder> siralar = sorgu.getSortOrders();
                    String alan = siralar.isEmpty() ? null : siralar.get(0).getSorted();
                    boolean artan = siralar.isEmpty() || siralar.get(0).getDirection() == SortDirection.ASCENDING;
                    return veriler.sayfaGetir(duzen, bolum.kodu(), alan, artan,
                            sorgu.getOffset(), sorgu.getLimit()).satirlar().stream();
                },
                sorgu -> {
                    long adet = veriler.toplamKayit(duzen);
                    guncelleToplam(adet);
                    return (int) Math.min(adet, Integer.MAX_VALUE);
                }));
        return grid;
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

    /** Alt ya da sağ paneli üretir: başlık (dile göre) ve kaydırılabilir içerik. */
    private Div panelOlustur(Optional<TemekrduzenDto.Bolum> bolum) {
        Div panel = new Div();
        panel.addClassName("bur-panel");
        if (bolum.isPresent()) {
            TemekrduzenDto.Bolum b = bolum.get();
            String baslik = metinler.metin(b.baslik() == null ? b.kodu() : b.baslik(),
                    "panel." + b.kodu().toLowerCase(Locale.ROOT));
            Span baslikAlani = new Span(baslik);
            baslikAlani.addClassName("bur-panel-baslik");
            Div icerik = new Div();
            icerik.addClassName("bur-panel-icerik");
            panel.add(baslikAlani, icerik);
        }
        return panel;
    }

    /**
     * Tablo ile açık panelleri yerleştirir. Açık panel, sürüklenebilir bir ayırıcıyla (SplitLayout) bağlanır;
     * kapalı panel hiç eklenmez, tablo tüm alanı kaplar. Açma/kapama düğmeleri bu metodu yeniden çağırır.
     */
    private void yerlesimiKur() {
        Component merkez = tablo;
        if (altVar && altAcik) {
            SplitLayout dikey = new SplitLayout(tablo, altPanel, SplitLayout.Orientation.VERTICAL);
            dikey.setSizeFull();
            dikey.setSplitterPosition(70);
            merkez = dikey;
        }
        Component tum = merkez;
        if (sagVar && sagAcik) {
            SplitLayout yatay = new SplitLayout(merkez, sagPanel, SplitLayout.Orientation.HORIZONTAL);
            yatay.setSizeFull();
            yatay.setSplitterPosition(75);
            tum = yatay;
        }
        govde.removeAll();
        govde.add(tum);
    }

    /**
     * Araç çubuğu: Kaydet, Kopya, Yazdır, Sil | Dışa Aktar (Excel/CSV/PDF/JSON), Parametre | Görüntü ve panel düğmeleri.
     * Düğmelerin yazıları dile göre gelir. Düzende olmayan panelin düğmesi gösterilmez.
     */
    private Component aracCubugu(String siskod, YetkiSaglayicisi yetki) {
        Div cubuk = new Div();
        cubuk.addClassName("bur-arac-cubugu");

        cubuk.add(
                aracDugmesi(VaadinIcon.CHECK_CIRCLE, "dugme.kaydet", "Kaydet", "bur-kaydet"),
                aracDugmesi(VaadinIcon.COPY, "dugme.kopya", "Kopya", ""),
                aracDugmesi(VaadinIcon.PRINT, "dugme.yazdir", "Yazdır", "bur-yazdir"),
                aracDugmesi(VaadinIcon.TRASH, "dugme.sil", "Sil", "bur-sil"),
                ayrac());

        // Dışa aktar: izin yoksa hiç görünmez (asıl denetim yine sunucuda yapılmalıdır)
        if (yetki.izinVarMi(siskod, IZIN_DISARI_AKTAR)) {
            MenuBar disariAktar = new MenuBar();
            var ana = disariAktar.addItem(metinler.metin("Dışa Aktar", "dugme.disaaktar"));
            for (String bicim : List.of("Excel", "CSV", "PDF", "JSON")) {
                ana.getSubMenu().addItem(bicim, olay -> henuzHazirDegil(bicim));
            }
            cubuk.add(disariAktar);
        }

        Div parametre = new Div(new Span(metinler.metin("Parametre", "dugme.parametre")));
        parametre.addClassName("bur-parametre");
        parametre.addClickListener(olay -> henuzHazirDegil(parametre.getText()));
        cubuk.add(parametre, ayrac());

        Select<String> goruntu = new Select<>();
        goruntu.setItems(metinler.metin("Varsayılan", "goruntu.varsayilan"));
        goruntu.setValue(metinler.metin("Varsayılan", "goruntu.varsayilan"));
        goruntu.setPlaceholder(metinler.metin("Görüntü", "dugme.goruntu"));
        goruntu.setWidth("11rem");
        cubuk.add(goruntu);

        Div bosluk = new Div();
        bosluk.addClassName("bur-arac-bosluk");
        cubuk.add(bosluk);

        if (altVar) {
            cubuk.add(panelDugmesi(VaadinIcon.ANGLE_DOUBLE_DOWN, "dugme.altpanel", "Alt panel aç/kapat", () -> {
                altAcik = !altAcik;
                yerlesimiKur();
            }));
        }
        if (sagVar) {
            cubuk.add(panelDugmesi(VaadinIcon.ANGLE_DOUBLE_LEFT, "dugme.sagpanel", "Sağ panel aç/kapat", () -> {
                sagAcik = !sagAcik;
                yerlesimiKur();
            }));
        }
        return cubuk;
    }

    /** İkonu üstte, yazısı altta bir araç düğmesi üretir; klavyeyle (Enter, Boşluk) de çalışır. */
    private Div aracDugmesi(VaadinIcon ikon, String anahtar, String yedek, String ekSinif) {
        String ad = metinler.metin(yedek, anahtar);
        Div d = new Div();
        d.addClassName("bur-arac");
        if (!ekSinif.isEmpty()) {
            d.addClassName(ekSinif);
        }
        Icon simge = ikon.create();
        d.add(simge, new Span(ad));
        d.getElement().setAttribute("role", "button");
        d.getElement().setAttribute("tabindex", "0");
        d.getElement().setAttribute("title", ad);
        Runnable islem = () -> henuzHazirDegil(ad);
        d.addClickListener(olay -> islem.run());
        d.getElement().addEventListener("keydown", olay -> islem.run())
                .setFilter("event.key === 'Enter' || event.key === ' '");
        return d;
    }

    /** Alt/sağ paneli açıp kapatan sade ikon düğmesi. */
    private Div panelDugmesi(VaadinIcon ikon, String anahtar, String yedek, Runnable islem) {
        String ad = metinler.metin(yedek, anahtar);
        Div d = new Div();
        d.addClassName("bur-arac");
        d.add(ikon.create());
        d.getElement().setAttribute("role", "button");
        d.getElement().setAttribute("tabindex", "0");
        d.getElement().setAttribute("title", ad);
        d.getElement().setAttribute("aria-label", ad);
        d.addClickListener(olay -> islem.run());
        d.getElement().addEventListener("keydown", olay -> islem.run())
                .setFilter("event.key === 'Enter' || event.key === ' '");
        return d;
    }

    private static Div ayrac() {
        Div a = new Div();
        a.addClassName("bur-arac-ayrac");
        return a;
    }

    /** İşlevi henüz bağlanmamış düğmeler için bilgi mesajı gösterir. */
    private void henuzHazirDegil(String ad) {
        Mesaj.bilgi("'" + ad + "' " + metinler.metin("henüz hazır değil", "mesaj.henuzhazir"));
    }

    /** "Toplam kayıt" sayısını günceller. */
    public final void guncelleToplam(long adet) {
        toplamKayit.setText(String.valueOf(adet));
    }
}
