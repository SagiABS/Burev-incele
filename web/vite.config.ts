/**
 * Vite ayarı: geliştirme sunucusu ve API yönlendirmesi.
 *
 * Neden var: geliştirirken ön yüz 5173 portunda çalışır; "/api" istekleri Spring Boot sunucusuna
 * (varsayılan 8081: kanıt çalışması API'si; ana uygulamaya taşınınca 8080) aktarılır. Böylece tarayıcıda
 * CORS sorunu yaşanmaz. Üretimde ön yüz derlenip sunucunun statik klasöründen verilir.
 */
import vue from '@vitejs/plugin-vue';
import { defineConfig } from 'vite';

const apiAdresi = process.env.BUREVER_API ?? 'http://localhost:8081';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: { '/api': apiAdresi },
  },
  build: { outDir: 'dist' },
});
