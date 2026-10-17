/**
 * Sunucu (REST) ile ön yüz arasındaki veri sözleşmesi.
 *
 * Neden var: sunucudan gelen JSON'un biçimi tek yerde tanımlı olsun; bileşenler bu türleri kullanır ve
 * sözleşme değişince derleyici hatayı gösterir. Alan adları sunucudaki DTO'larla birebir aynıdır.
 */

/** Proje dilleri (temdiltur): seçici için kod, ad ve yazı yönü. */
export interface Dil {
  kodu: string;
  adi: string;
  yerelAd: string;
  /** "LTR" veya "RTL" */
  yon: string;
  varsayilan: boolean;
}

/** Izgara kolonu; etiket sunucuda seçilen dile göre çözülmüş gelir. */
export interface Kolon {
  alan: string;
  etiket: string;
  siralanabilir: boolean;
  genislik: number | null;
}

/** Ekranın bir bölümü: ızgara, alt panel, sağ panel vb. (temekrbol). */
export interface Bolum {
  kodu: string;
  turu: string;
  /** ORTA, ALT, SAG */
  yuva: string;
  baslik: string;
  surukle: boolean;
  acik: boolean;
  kolonlar: Kolon[];
}

/** Bir ekranın düzeni (temekr düzeni) ve arayüz metinleri. */
export interface EkranDuzeni {
  siskod: string;
  baslik: string;
  tabloKodu: string;
  /** "ltr" veya "rtl" */
  yon: string;
  /** Arayüz metinleri: anahtar -> çözülmüş metin */
  metin: Record<string, string>;
  bolumler: Bolum[];
}

/** Izgaranın bir sayfası; satırlar alan adı -> değer haritasıdır. */
export interface VeriSayfasi {
  satirlar: Record<string, unknown>[];
  toplam: number;
}
