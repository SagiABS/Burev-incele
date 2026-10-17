<!--
  Düzenleme formu denemesi: JSON Forms ile veriyle tanımlı form.

  Ne yapar: bir JSON şemasından (alan türleri, zorunluluk, uzunluk) ve bir "arayüz şemasından" (alanların yerleşimi)
  formu çizer; doğrulama tarayıcıda yapılır. Etiketler seçilen dile göre değişir.
  Neden var: ADR-23 kararındaki "düzenleme ekranları veriyle tanımlanır" yaklaşımını sınamak. Şemalar şimdilik
  kodda sabittir; gerçekte temekr form düzeninden üretilecek. Kayıt sunucuya gönderilmez (deneme).
  Sınırlar: doğrulama hata iletileri JSON Forms'un varsayılan (İngilizce) metinleridir; çevirisi sonraki adımdır.
-->
<script setup lang="ts">
import { JsonForms } from '@jsonforms/vue';
import { vanillaRenderers } from '@jsonforms/vue-vanilla';
import '@jsonforms/vue-vanilla/vanilla.css';
import Button from 'primevue/button';
import { computed, ref } from 'vue';

const props = defineProps<{ dil: string }>();

/** Dil başına alan etiketleri (gerçekte temdilcvr'den gelecek). */
const ETIKETLER: Record<string, Record<string, string>> = {
  tr: { kodu: 'Kodu', adi: 'Adı', acilistarihi: 'Açılış Tarihi', kaydet: 'Kaydet', kaydedildi: 'Kaydedildi (deneme: sunucuya gönderilmedi)' },
  en: { kodu: 'Code', adi: 'Name', acilistarihi: 'Opening Date', kaydet: 'Save', kaydedildi: 'Saved (trial: not sent to the server)' },
  ar: { kodu: 'الرمز', adi: 'الاسم', acilistarihi: 'تاريخ الافتتاح', kaydet: 'حفظ', kaydedildi: 'تم الحفظ (تجريبي: لم يُرسل إلى الخادم)' },
};

/** Seçili dilin etiketi; dil tanımsızsa Türkçe. */
function e(anahtar: string): string {
  return (ETIKETLER[props.dil] ?? ETIKETLER.tr)[anahtar] ?? anahtar;
}

/** Veri şeması: alan türleri ve kısıtlar (temorgorg: kod 3 karakter, ad zorunlu, açılış tarihi zorunlu). */
const sema = computed(() => ({
  type: 'object',
  properties: {
    kodu: { type: 'string', title: e('kodu'), minLength: 3, maxLength: 3 },
    adi: { type: 'string', title: e('adi'), minLength: 1, maxLength: 100 },
    acilistarihi: { type: 'string', format: 'date', title: e('acilistarihi') },
  },
  required: ['kodu', 'adi', 'acilistarihi'],
}));

/** Arayüz şeması: alanların sırası ve yerleşimi. */
const arayuzSemasi = {
  type: 'VerticalLayout',
  elements: [
    { type: 'Control', scope: '#/properties/kodu' },
    { type: 'Control', scope: '#/properties/adi' },
    { type: 'Control', scope: '#/properties/acilistarihi' },
  ],
};

const renderers = Object.freeze([...vanillaRenderers]);
const veri = ref<Record<string, unknown>>({ kodu: '', adi: '', acilistarihi: '' });
const hatalar = ref<unknown[]>([]);
const mesaj = ref('');

/** Form değişince veriyi ve doğrulama hatalarını saklar. */
function degisti(olay: { data?: Record<string, unknown>; errors?: unknown[] }): void {
  veri.value = olay.data ?? {};
  hatalar.value = olay.errors ?? [];
  mesaj.value = '';
}

function kaydet(): void {
  mesaj.value = e('kaydedildi');
}
</script>

<template>
  <div class="bur-form">
    <JsonForms
      :data="veri"
      :schema="sema"
      :uischema="arayuzSemasi"
      :renderers="renderers"
      @change="degisti"
    />
    <Button :label="e('kaydet')" :disabled="hatalar.length > 0" @click="kaydet" />
    <p v-if="mesaj">{{ mesaj }}</p>
    <pre>{{ JSON.stringify(veri, null, 2) }}</pre>
  </div>
</template>
