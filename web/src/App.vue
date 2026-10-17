<!--
  Uygulama kabuğu: sol menü, üst çubuk (başlık + dil seçici) ve içerik alanı.

  Neden var: her ekranın ortak çerçevesi tek yerde olsun. Dil seçilince sayfanın yönü (sağdan sola / soldan sağa)
  burada ayarlanır. Menü şimdilik iki sabit kayıttır; gerçek menü veritabanından (temfhrtaf/temfhrrtb) gelecek.
-->
<script setup lang="ts">
import Message from 'primevue/message';
import Select from 'primevue/select';
import { onMounted, ref } from 'vue';
import { dilleriGetir } from './api';
import DuzenlemeFormu from './components/DuzenlemeFormu.vue';
import ListeSablonu from './components/ListeSablonu.vue';
import type { Dil } from './tipler';

type Sayfa = 'liste' | 'form';

const ORGANIZASYON_LISTESI = 'tem_893_000';

const diller = ref<Dil[]>([]);
const dil = ref('tr');
const sayfa = ref<Sayfa>('liste');
const listeBasligi = ref('');
const hata = ref('');

/** Seçilen dile göre sayfa dilini ve yönünü ayarlar (Arapça: sağdan sola). */
function dilUygula(kodu: string): void {
  const secili = diller.value.find((d) => d.kodu === kodu);
  document.documentElement.lang = kodu;
  document.documentElement.dir = secili?.yon === 'RTL' ? 'rtl' : 'ltr';
}

onMounted(async () => {
  try {
    diller.value = await dilleriGetir();
    dil.value = diller.value.find((d) => d.varsayilan)?.kodu ?? 'tr';
    dilUygula(dil.value);
  } catch (e) {
    hata.value = e instanceof Error ? e.message : String(e);
  }
});
</script>

<template>
  <div class="bur-kabuk">
    <aside class="bur-sol">
      <h1>Burever</h1>
      <button class="bur-menu-oge" :class="{ secili: sayfa === 'liste' }" @click="sayfa = 'liste'">
        <i class="pi pi-building"></i><span>{{ listeBasligi || 'Organizasyonlar' }}</span>
      </button>
      <button class="bur-menu-oge" :class="{ secili: sayfa === 'form' }" @click="sayfa = 'form'">
        <i class="pi pi-pencil"></i><span>Düzenleme formu (JSON Forms)</span>
      </button>
    </aside>
    <div class="bur-sag">
      <header class="bur-ust">
        <h2>{{ sayfa === 'liste' ? listeBasligi : 'Organizasyon formu (deneme)' }}</h2>
        <Select
          v-model="dil"
          :options="diller"
          optionLabel="yerelAd"
          optionValue="kodu"
          @change="dilUygula(dil)"
        />
      </header>
      <main class="bur-icerik">
        <Message v-if="hata" severity="error">{{ hata }}</Message>
        <ListeSablonu
          v-if="sayfa === 'liste'"
          :siskod="ORGANIZASYON_LISTESI"
          :dil="dil"
          @baslik="listeBasligi = $event"
        />
        <DuzenlemeFormu v-else :dil="dil" />
      </main>
    </div>
  </div>
</template>
