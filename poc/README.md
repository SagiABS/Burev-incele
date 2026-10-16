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
`python3 poc/olcum/yuk.py vue 1000` ve `python3 poc/olcum/yuk.py jsf 1000`; betik 60 sn beklerken
`jcmd <pid> GC.run` + `jcmd <pid> GC.heap_info` ile yığın kullanımını kaydedin. Her adayı **ayrı** başlatıp aynı `-Xmx` ile deneyin.
Karşılaştırılacaklar: açılış hatası, yığın (MB), ortanca/%95 süre, 1000 oturum sonrası bellek, kod miktarı, düzenden dinamik kolon kolaylığı.

## Bilinen belirsizlikler
- JoinFaces 6.1.1'in Spring Boot 4.1 / Java 25 ile uyumu doğrulanmadı (açılmazsa hata çıktısı kanıttır).
- `view` kapsamı, `p:splitter`, `p:columns` özellikleri PrimeFaces sürümüne göre değişebilir.
- Bu kod bulut ortamında derlenemedi (Maven deposu yok); Vue sayfası da CDN erişimi olmadığından tarayıcıda denenemedi.
