package com.burevers.bur.uygulama.kabuk;

import java.util.List;

/**
 * Menü ağacının kaynağını soyutlar.
 *
 * <p>Neden var: Menü tabloları (temfhrtaf/temfhrrtb) henüz yok. Arayüz yalnızca bu arayüze bağlıdır;
 * tablolar gelince veritabanından okuyan bir uygulama yazılır, ekran kodu aynı kalır.
 * Menü, giriş yapan kullanıcının yetkisine göre süzülmüş olarak dönmelidir (yetki gelince eklenecek).
 */
public interface MenuSaglayicisi {

    /** Kullanıcıya gösterilecek menünün kök öğelerini döndürür. */
    List<MenuOgesi> menuAgaci();
}
