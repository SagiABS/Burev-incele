# Burever ön yüzü (web)

ADR-23: Vue 3 + TypeScript + PrimeVue + JSON Forms; sunucu durumsuz REST API. Ekranlar veritabanındaki
`temekr*` düzeninden çizilir (kendi ince çizicimiz); düzenleme formları JSON Forms ile.

## Önkoşul
- Node.js 22 LTS (https://nodejs.org). Kontrol: `node -v` ve `npm -v`.

## Çalıştırma (geliştirme)
1. API sunucusunu başlatın. Şimdilik `PocVueUygulamasi` (port 8081, parola `BUREVER_DB_SIFRE` ortam değişkeniyle).
2. Komut satırında `web` klasöründe:
   ```
   cd D:\Burevers\burev\web
   npm install
   npm run dev
   ```
3. Tarayıcıda http://localhost:5173/ açın. `/api` istekleri 8081'e yönlenir.
   Başka adres için: `set BUREVER_API=http://localhost:8080` (sonra `npm run dev`).

## Diğer komutlar
- `npm run tipkontrol`: tür denetimi (vue-tsc).
- `npm run build`: üretim derlemesi (`dist/`).

## Durum
Bu iskelet bulut ortamında derlenemedi (npm erişimi yok); ilk çalıştırmada `npm install` ve `npm run tipkontrol` çıktısını
paylaşın. `package-lock.json` ilk `npm install` sonrasında depoya eklenmelidir.
