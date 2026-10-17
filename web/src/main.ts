/**
 * Ön yüz giriş noktası: Vue uygulamasını ve PrimeVue temasını kurar.
 *
 * Neden var: tema ve bileşen kütüphanesi bir kez, burada yapılandırılır. Koyu tema şimdilik kapalıdır
 * ("darkModeSelector" hiç var olmayan bir sınıfa bağlanır); tasarım kararı verilince açılır.
 */
import Aura from '@primeuix/themes/aura';
import 'primeicons/primeicons.css';
import PrimeVue from 'primevue/config';
import { createApp } from 'vue';
import App from './App.vue';
import './stil.css';

createApp(App)
  .use(PrimeVue, {
    theme: {
      preset: Aura,
      options: { darkModeSelector: '.bur-koyu' },
    },
  })
  .mount('#uygulama');
