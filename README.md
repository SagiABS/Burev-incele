# Burever

Çok modüllü, modüler monolit ERP. Bu depo **Faz 0 iskeletidir**: yapı, mimari testler ve çalışan en küçük dikey dilim (giriş sayfası + tek REST + veritabanı bağlantısı). **Henüz paket ve tablo yoktur**; adları birlikte karar verilince açılır.

## Gereksinimler
| Araç | Sürüm |
|---|---|
| JDK | 25 veya üzeri |
| Maven | 3.9 veya üzeri |
| PostgreSQL | `dburevers` veritabanı (localhost) |

## Çalıştırma
```bash
# Şifre depoya yazılmaz; ortam değişkeniyle verilir
export BUREVER_DB_SIFRE='...'

# Derle ve testleri çalıştır (birim + mimari; veritabanı gerekmez)
mvn -B verify

# Uygulamayı çalıştır
mvn -pl uygulama spring-boot:run
```
Kontrol: arayüz <http://localhost:8080/> · REST <http://localhost:8080/api/v1/sistem/bilgi> · sağlık <http://localhost:8080/actuator/health>

## Yapı
```
burever/                  groupId com.burevers · Java kök paketi com.burevers.bur
├─ cekirdek/              saf Java: Money (çatı bağımlılığı yok)
├─ tem/                   temel modül (şimdilik boş)
├─ uygulama/              çalıştırılabilir Spring Boot + Vaadin kabuğu
├─ testler/mimari-testler/ ArchUnit kuralları
└─ ops/                   ortam değişkeni örneği
```
Yeni modüller (bel, muh, mht) sırası gelince eklenir. Java paketleri modül adını tekrar etmez: `com.burevers.bur.<paketkodu>` (örn. `temorg`).

## Paket kodu dili
- **Paket kodu 6 karakter:** modül(3) + grup(3), örn. `temorg`. Java paketi = paket kodu: `com.burevers.bur.temorg`.
- **Tablo kodu 9 karakter:** paket kodu(6) + ad(3), örn. `temorgorg`. Tüm tablolarda anahtar `vid` (bigserial).
- **Sınıf adı ekleri:** `Vrl` (varlık), `Rep` (repository), `Srv` (servis), `Vie` (Vaadin görünümü), `Uc` (REST).
- Kodların anlamı Excel'deki "Sözlük" sayfasındadır (tek kaynak: `1_Burevers_Yapı_Mimar.xlsx`).

## Kurallar (otomatik denetlenir: `testler/mimari-testler`)
- Çekirdek Spring/JPA/Vaadin'e ve alan modüllerine bağımlı olamaz.
- Bağımlılık yönü: `tem` ← `bel` ← `muh`, `tem` ← `mht`; başka modüller arası bağımlılık yasak.
- Paketler arasında döngü yok; paket kodu 6 karakter dilinde olmalı.
- Alan enjeksiyonu yasak; paket ve sınıf adları ASCII.

## Kodlama kuralları
- Her sınıf ve metodun başında Türkçe açıklama (Javadoc); zor satırlara Türkçe satır içi yorum.
- Tutarlar `double` ile değil `Money` ile taşınır.
- Göçler: `V<yyyyMMddHHmm><sıra>__aciklama.sql`, modülün `db/migration/<modul>/` klasörüne; uygulanmış göç değiştirilmez.

## Doğrulanması gerekenler
İskelet, yazıldığı ortamda **derlenemedi** (Maven deposuna erişim yok). İlk iş: `mvn -B verify`, ardından `mvn -pl uygulama dependency:tree` (Vaadin starter'ının ücretsiz çekirdek mi ticari bileşen mi çektiğini görmek için).
