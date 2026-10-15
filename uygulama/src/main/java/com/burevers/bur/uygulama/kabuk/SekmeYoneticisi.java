package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Ekranları üst sekmelerde açan yönetici (en çok {@value #EN_COK_SEKME} sekme).
 *
 * <p>Neden var: Menüden bir adım seçilince ekran yeni sekmede açılır; aynı ekran zaten açıksa o sekmeye
 * geçilir. Diğer sekmelerin içeriği gizlenir ama silinmez, böylece yarım kalan giriş kaybolmaz.
 * Sekmeler sığmayınca Vaadin kaydırma okları gösterir; ayrı bir "diğerleri" listesi sonraki adımdır.
 */
public class SekmeYoneticisi extends VerticalLayout {

    /** Aynı anda açık olabilecek en çok sekme sayısı. */
    public static final int EN_COK_SEKME = 10;

    private final Tabs sekmeler = new Tabs();
    private final Div icerik = new Div();
    /** Açık ekranlar: siskod → sekme (ekleme sırası korunur). */
    private final Map<String, Tab> acikSekmeler = new LinkedHashMap<>();
    private final Map<Tab, Component> sekmeIcerikleri = new LinkedHashMap<>();

    /** Boş bir sekme alanı oluşturur. */
    public SekmeYoneticisi() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        icerik.setSizeFull();
        sekmeler.setWidthFull();
        sekmeler.addSelectedChangeListener(olay -> gorunurlugunuGuncelle());
        add(sekmeler, icerik);
        setFlexGrow(1, icerik);
    }

    /**
     * Ekranı sekmede açar; zaten açıksa o sekmeye geçer.
     *
     * @param siskod ekranın sistem kodu (sekme anahtarı)
     * @param baslik sekme başlığı
     * @param ekran  sekmenin içeriği; yalnızca yeni sekme açılacaksa kullanılır
     */
    public void ac(String siskod, String baslik, Supplier<Component> ekran) {
        Tab var = acikSekmeler.get(siskod);
        if (var != null) {
            sekmeler.setSelectedTab(var);
            return;
        }
        if (acikSekmeler.size() >= EN_COK_SEKME) {
            Mesaj.uyari("En çok " + EN_COK_SEKME + " sekme açılabilir; önce birini kapatın.");
            return;
        }
        Component govde = ekran.get();
        govde.getElement().getStyle().set("height", "100%");
        Tab sekme = sekmeOlustur(siskod, baslik);
        acikSekmeler.put(siskod, sekme);
        sekmeIcerikleri.put(sekme, govde);
        icerik.add(govde);
        sekmeler.add(sekme);
        sekmeler.setSelectedTab(sekme);
    }

    /** Başlık + kapatma düğmesi olan sekmeyi üretir. */
    private Tab sekmeOlustur(String siskod, String baslik) {
        Button kapat = new Button(VaadinIcon.CLOSE.create());
        kapat.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
        kapat.getElement().setAttribute("aria-label", baslik + " sekmesini kapat");
        HorizontalLayout etiket = new HorizontalLayout(new Span(baslik), kapat);
        etiket.setAlignItems(FlexComponent.Alignment.CENTER);
        etiket.setSpacing(false);
        Tab sekme = new Tab(etiket);
        kapat.addClickListener(olay -> kapat(siskod));
        return sekme;
    }

    /** Sekmeyi ve içeriğini kapatır; kapanan seçiliyse komşu sekme seçilir. */
    private void kapat(String siskod) {
        Tab sekme = acikSekmeler.remove(siskod);
        if (sekme == null) {
            return;
        }
        Component govde = sekmeIcerikleri.remove(sekme);
        // Seçili sekme kapanırsa Tabs kendiliğinden komşuyu seçer; yine de içerik görünürlüğü yenilenir
        sekmeler.remove(sekme);
        if (govde != null) {
            icerik.remove(govde);
        }
        gorunurlugunuGuncelle();
    }

    /** Yalnızca seçili sekmenin içeriğini gösterir. */
    private void gorunurlugunuGuncelle() {
        Tab secili = sekmeler.getSelectedTab();
        sekmeIcerikleri.forEach((sekme, govde) -> govde.setVisible(sekme.equals(secili)));
    }
}
