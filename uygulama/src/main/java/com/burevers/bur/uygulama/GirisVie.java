package com.burevers.bur.uygulama;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Karşılama sayfası ({@code http://localhost:8080/}). "Vie" eki (View), Vaadin arayüz sınıfı demektir.
 *
 * <p>Amacı: Vaadin'in Java 25 ve Spring Boot 4 ile birlikte çalıştığını kanıtlamaktır.
 * Gerçek giriş (kullanıcı adı/şifre) kullanıcı ve yetki paketleri açılınca gelecek;
 * bu sayfa o zaman değiştirilecek veya kaldırılacaktır.
 */
@Route("")
@PageTitle("Burever")
public class GirisVie extends VerticalLayout {

    /**
     * Sayfayı oluşturur. {@link BurOzellikleri} Spring tarafından enjekte edilir; böylece sayfa,
     * uygulamanın hangi dağıtım profilinde (onprem/saas) çalıştığını gösterebilir.
     */
    public GirisVie(BurOzellikleri ozellikler) {
        add(new H1("Burever"));
        add(new Paragraph("Dağıtım profili: " + ozellikler.dagitimProfili()));

        // Düğme, arayüzün sunucu tarafında çalıştığını gösteren basit bir bildirim açar
        add(new Button("Bağlantıyı dene", olay -> Notification.show("Arayüz çalışıyor")));
    }
}
