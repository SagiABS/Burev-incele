/**
 * Sunucu REST uçlarına erişim.
 *
 * Neden var: fetch çağrıları ve hata işleme tek yerde olsun; bileşenler adresleri bilmesin.
 * Sunucu durumsuzdur: her istek kendi kendine yeter. Yetki ve kiracı süzgeci sunucuda uygulanır;
 * ön yüz hiçbir güvenlik kararı vermez (yalnızca gösterir).
 */
import type { Dil, EkranDuzeni, VeriSayfasi } from './tipler';

/** İsteği yapar; 2xx dışındaki durumlarda anlaşılır bir hata fırlatır. */
async function getir<T>(yol: string): Promise<T> {
  const cevap = await fetch(yol, { headers: { Accept: 'application/json' } });
  if (!cevap.ok) {
    throw new Error(`Sunucu hatası ${cevap.status}: ${yol}`);
  }
  return (await cevap.json()) as T;
}

/** Etkin proje dillerini getirir. */
export function dilleriGetir(): Promise<Dil[]> {
  return getir<Dil[]>('/api/diller');
}

/** Ekran düzenini seçilen dile göre çözülmüş etiketlerle getirir. */
export function ekranGetir(siskod: string, dil: string): Promise<EkranDuzeni> {
  return getir<EkranDuzeni>(`/api/ekran/${encodeURIComponent(siskod)}?dil=${encodeURIComponent(dil)}`);
}

/** Izgara bölümünün bir sayfasını getirir; sıralama ve sayfalama sunucuda yapılır. */
export function veriGetir(
  siskod: string,
  bolum: string,
  baslangic: number,
  adet: number,
  siralaAlan: string | null,
  artan: boolean,
): Promise<VeriSayfasi> {
  const sorgu = new URLSearchParams({ baslangic: String(baslangic), adet: String(adet), artan: String(artan) });
  if (siralaAlan) {
    sorgu.set('sirala', siralaAlan);
  }
  return getir<VeriSayfasi>(`/api/veri/${encodeURIComponent(siskod)}/${encodeURIComponent(bolum)}?${sorgu}`);
}
