package com.burevers.bur.uygulama.kabuk;

import com.vaadin.flow.component.icon.VaadinIcon;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * GEÇİCİ menü kaynağı: menü tabloları gelene kadar arayüzü denemek için sabit bir ağaç verir.
 *
 * <p>Siskod değerleri kullanıcının verdiği gerçek kodlardır. Veritabanı tabanlı sağlayıcı yazılınca
 * bu sınıf silinir.
 */
@Component
public class GeciciMenuSaglayicisi implements MenuSaglayicisi {

    /** Deneme amaçlı menü ağacını döndürür. */
    @Override
    public List<MenuOgesi> menuAgaci() {
        return List.of(
                MenuOgesi.grup("tem-yapi", "Organizasyon Yapısı", VaadinIcon.BUILDING,
                        MenuOgesi.islem("temorgorg", "Organizasyonlar", "tem_893_000"),
                        MenuOgesi.islem("temorgkur", "Kuruluşlar", "tem_973_000"),
                        MenuOgesi.islem("temorgtes", "Tesisler", "tem_1128_000")),
                MenuOgesi.grup("tem-kullanici", "Kullanıcı ve Yetki", VaadinIcon.USERS,
                        MenuOgesi.islem("temkultan", "Kullanıcılar", "tem_1028_000"),
                        MenuOgesi.islem("temkulrol", "Roller", "tem_813_000"),
                        MenuOgesi.islem("temkulizn", "İzinler", "tem_644_000")),
                MenuOgesi.grup("mht", "Muhatap", VaadinIcon.HANDSHAKE,
                        MenuOgesi.islem("mhtlevmuf", "Muhatap Kartları", "mht_608_000")));
    }
}
