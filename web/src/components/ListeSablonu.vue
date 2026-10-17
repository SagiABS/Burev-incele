<!--
  Liste şablonu: bir LISTE ekranını temekr düzenine göre çizer.

  Ne yapar: ekran düzenini (bölümler, kolonlar, etiketler) ve satırları sunucudan alır; ızgarayı PrimeVue DataTable ile
  çizer. Sayfalama ve sıralama sunucuda yapılır (tembel yükleme). Düzende alt/sağ panel varsa sürüklenebilir
  paneller (Splitter) olarak gösterir.
  Neden var: aynı şablon bütün liste ekranlarında kullanılır; ekran farkı koddan değil veritabanındaki düzenden gelir.
  Not: kiracı süzgeci ve yetki sunucudadır; bu bileşen yalnızca gösterir.
-->
<script setup lang="ts">
import Button from 'primevue/button';
import Column from 'primevue/column';
import DataTable, { type DataTablePageEvent, type DataTableSortEvent } from 'primevue/datatable';
import Message from 'primevue/message';
import Splitter from 'primevue/splitter';
import SplitterPanel from 'primevue/splitterpanel';
import { computed, onMounted, ref, watch } from 'vue';
import { ekranGetir, veriGetir } from '../api';
import type { EkranDuzeni } from '../tipler';

const props = defineProps<{ siskod: string; dil: string }>();
const emit = defineEmits<{ baslik: [metin: string] }>();

/** Sayfa başına satır sayısı. */
const SAYFA_BOYU = 50;

const duzen = ref<EkranDuzeni | null>(null);
const satirlar = ref<Record<string, unknown>[]>([]);
const toplam = ref(0);
const ilk = ref(0);
const siralaAlan = ref<string | null>(null);
const siralaYon = ref<number | null>(null);
const yukleniyor = ref(false);
const hata = ref('');
const altAcik = ref(false);
const sagAcik = ref(false);

const izgara = computed(() => duzen.value?.bolumler.find((b) => b.turu === 'GRID') ?? null);
const altBolum = computed(() => duzen.value?.bolumler.find((b) => b.yuva === 'ALT') ?? null);
const sagBolum = computed(() => duzen.value?.bolumler.find((b) => b.yuva === 'SAG') ?? null);

/** Arayüz metni: düzenle gelen çözülmüş metin; yoksa yedek metin. */
function metin(anahtar: string, yedek: string): string {
  return duzen.value?.metin[anahtar] ?? yedek;
}

/** Hata nesnesini gösterilecek metne çevirir. */
function hataMetni(e: unknown): string {
  return e instanceof Error ? e.message : String(e);
}

/** Düzeni (etiketler dahil) yükler; dil değişince yalnızca bu çağrılır, veri yeniden çekilmez. */
async function duzenYukle(): Promise<void> {
  try {
    duzen.value = await ekranGetir(props.siskod, props.dil);
    emit('baslik', duzen.value.baslik);
    altAcik.value = altBolum.value?.acik ?? false;
    sagAcik.value = sagBolum.value?.acik ?? false;
    hata.value = '';
  } catch (e) {
    hata.value = hataMetni(e);
  }
}

/** Geçerli sayfa ve sıralamaya göre satırları yükler. */
async function veriYukle(): Promise<void> {
  if (!izgara.value) {
    return;
  }
  yukleniyor.value = true;
  try {
    const sayfa = await veriGetir(
      props.siskod, izgara.value.kodu, ilk.value, SAYFA_BOYU, siralaAlan.value, siralaYon.value !== -1);
    satirlar.value = sayfa.satirlar;
    toplam.value = sayfa.toplam;
    hata.value = '';
  } catch (e) {
    hata.value = hataMetni(e);
  } finally {
    yukleniyor.value = false;
  }
}

function sayfaDegisti(olay: DataTablePageEvent): void {
  ilk.value = olay.first;
  void veriYukle();
}

function siralamaDegisti(olay: DataTableSortEvent): void {
  siralaAlan.value = typeof olay.sortField === 'string' ? olay.sortField : null;
  siralaYon.value = olay.sortOrder ?? 1;
  ilk.value = 0;
  void veriYukle();
}

onMounted(async () => {
  await duzenYukle();
  await veriYukle();
});
watch(() => props.dil, duzenYukle);
</script>

<template>
  <div class="bur-liste">
    <Message v-if="hata" severity="error">{{ hata }}</Message>
    <div v-if="duzen && !izgara" class="bur-uyari">Bu ekran için ızgara bölümü tanımlı değil.</div>

    <div v-if="izgara" class="bur-arac">
      <Button
        icon="pi pi-refresh"
        :label="metin('dugme.yenile', 'Yenile')"
        severity="secondary"
        size="small"
        @click="veriYukle"
      />
      <span class="bur-bosluk"></span>
      <Button
        v-if="altBolum"
        icon="pi pi-window-minimize"
        :label="metin('dugme.altpanel', 'Alt panel')"
        severity="secondary"
        size="small"
        @click="altAcik = !altAcik"
      />
      <Button
        v-if="sagBolum"
        icon="pi pi-sliders-h"
        :label="sagBolum.baslik"
        severity="secondary"
        size="small"
        @click="sagAcik = !sagAcik"
      />
    </div>

    <div v-if="izgara" style="flex: 1; min-height: 0">
      <Splitter :key="'sag' + (sagAcik && !!sagBolum)" layout="horizontal" style="height: 100%">
        <SplitterPanel :size="75" :minSize="30">
          <Splitter :key="'alt' + (altAcik && !!altBolum)" layout="vertical" style="height: 100%">
            <SplitterPanel :size="70" :minSize="20">
              <DataTable
                :value="satirlar"
                lazy
                paginator
                :rows="SAYFA_BOYU"
                :totalRecords="toplam"
                :first="ilk"
                :loading="yukleniyor"
                dataKey="vid"
                scrollable
                scrollHeight="flex"
                stripedRows
                :sortField="siralaAlan"
                :sortOrder="siralaYon"
                style="height: 100%"
                @page="sayfaDegisti"
                @sort="siralamaDegisti"
              >
                <Column
                  v-for="k in izgara.kolonlar"
                  :key="k.alan"
                  :field="k.alan"
                  :header="k.etiket"
                  :sortable="k.siralanabilir"
                />
                <template #paginatorstart>
                  <span>{{ metin('liste.toplamkayit', 'Toplam kayıt') }}: {{ toplam }}</span>
                </template>
              </DataTable>
            </SplitterPanel>
            <SplitterPanel v-if="altAcik && altBolum" :size="30" :minSize="10">
              <div class="bur-panel">{{ altBolum.baslik }}</div>
            </SplitterPanel>
          </Splitter>
        </SplitterPanel>
        <SplitterPanel v-if="sagAcik && sagBolum" :size="25" :minSize="10">
          <div class="bur-panel">{{ sagBolum.baslik }}</div>
        </SplitterPanel>
      </Splitter>
    </div>
  </div>
</template>
