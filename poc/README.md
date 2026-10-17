# Kanıt çalışması: ekran teknolojisi (ADR-23 öncesi)

Aynı ekran (Organizasyonlar listesi: temekr düzeni + dil seçici tr/en/ar-RTL + sürüklenebilir panel) iki adayla kurulur.
Ana derlemeye GİRMEZ; yalnızca `-Ppoc` ile derlenir. Kiracı süzgeci/yetki bilerek yok.

| Aday | Klasör | Port | Durum modeli |
|---|---|---|---|
| Spring Boot REST + Vue 3 | `poc-vue` | 8081 | Durumsuz (oturum yok) |
| Spring Boot + JoinFaces + PrimeFaces | `poc-primefaces` | 8082 | Sunucuda görünüm durumu |

## Çalıştırma
1. Ana uygulamayla veritabanı göçlerini uygulayın (poc göç çalıştırmaz).
2. Dolu liste için: `psql -d dburevers -f poc/olcum/ornek_veri.sql` (5000 satır; geri alma komutu dosyada).
3. `set BUREVER_DB_SIFRE=...` (parola dosyaya yazılmaz), sonra Eclipse Goals: `-B -Ppoc verify`.
4. Vue: `PocVueUygulamasi` sınıfını çalıştırın -> http://localhost:8081/
   (Vue, jsdelivr CDN'inden yüklenir; tarayıcıda internet gerekir.)
5. PrimeFaces: `PocPrimefacesUygulamasi` -> http://localhost:8082/ekran.xhtml

## Ölçüm (1000 aktif kullanıcı)
`java poc/olcum/YukAraci.java vue 1000` ve `java poc/olcum/YukAraci.java jsf 1000` (kök klasörden; JDK yeter); betik 60 sn beklerken
`jcmd <pid> GC.run` + `jcmd <pid> GC.heap_info` ile yığın kullanımını kaydedin. Her adayı **ayrı** başlatıp aynı `-Xmx` ile deneyin.
Karşılaştırılacaklar: açılış hatası, yığın (MB), ortanca/%95 süre, 1000 oturum sonrası bellek, kod miktarı, düzenden dinamik kolon kolaylığı.

## Bilinen belirsizlikler
- JoinFaces 6.1.1'in Spring Boot 4.1 / Java 25 ile uyumu doğrulanmadı (açılmazsa hata çıktısı kanıttır).
- `view` kapsamı, `p:splitter`, `p:columns` özellikleri PrimeFaces sürümüne göre değişebilir.
- Bu kod bulut ortamında derlenemedi (Maven deposu yok); Vue sayfası da CDN erişimi olmadığından tarayıcıda denenemedi.

## Ölçüm adımları (Windows, ayrıntılı)
1. Yalnızca BİR aday çalışsın (ana uygulama 8080 dahil diğerlerini durdurun). Çalıştırma yapılandırmasında
   **Arguments > VM arguments** kutusuna aynı sınırı yazın: `-Xmx512m -XX:+UseG1GC`.
2. Komut satırında (JDK'nın `bin` klasörü PATH'te olmalı) süreç numarasını bulun: `jcmd` (listede aday sınıfının adı yazar).
3. Taban ölçüm: `jcmd <pid> GC.run` sonra `jcmd <pid> GC.heap_info` -> "used" değerini not edin.
4. Yük: kök klasörde `java poc/olcum/YukAraci.java vue 1000` (ya da `jsf 1000`). Bitince araç bekler;
   o sürede yine `jcmd <pid> GC.run` + `jcmd <pid> GC.heap_info` -> "used" değerini not edin.
5. Aracın yazdığı satırı (süre, hata, ortanca, %95) ve iki "used" değerini bana gönderin. Aynısını diğer aday için yapın.
Not: Oturum zaman aşımı varsayılan 30 dk'dır; ölçüm bu süre içinde yapılmalıdır.
