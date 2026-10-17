package com.burevers.bur.pocjsf;

import com.burevers.bur.temdil.TemdilcvrService;
import com.burevers.bur.temdil.TemdilcvrService.Cozucu;
import com.burevers.bur.temdil.TemdilcvrService.Dil;
import com.burevers.bur.temekr.TemekrduzenDto;
import com.burevers.bur.temekr.TemekrduzenService;
import com.burevers.bur.temekr.TemekrkapsamDto;
import com.burevers.bur.temekr.TemekrveriService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Kanıt çalışması: liste ekranının JSF arka bileşeni (view kapsamlı).
 *
 * <p>Neden var: temekr düzenini okuyup PrimeFaces tablosuna dinamik kolon olarak verir; satırlar
 * tembel yüklenir (sayfa/sıralama sunucuda). Bilerek yalnızca DTO ve haritalar tutulur: JPA nesnesi
 * view kapsamında tutulursa her kullanıcı için bellekte kalır ve durum sızıntısı riski doğar.
 *
 * <p>"view" kapsamı JoinFaces'in Spring'e eklediği kapsamdır (DOĞRULANMADI: açılışta "No Scope registered for
 * scope name 'view'" hatası alınırsa kapsam adı/ek bağımlılık gerekir).
 */
@Component("ekranBean")
@Scope("view")
public class EkranBean {

    /** Arayüzde gösterilen kolon: alan adı, çözülmüş etiket, sıralanabilirlik. */
    public record KolonGorunumu(String alan, String etiket, boolean siralanabilir) {
    }

    private static final String VARSAYILAN_EKRAN = "tem_893_000";

    private final TemekrduzenService duzenler;
    private final TemekrveriService veriler;
    private final TemdilcvrService cevirmen;

    private TemekrduzenDto duzen;
    private TemekrduzenDto.Bolum izgara;
    private List<Dil> diller;
    private String dilKodu;
    private List<KolonGorunumu> kolonlar;
    private LazyDataModel<Map<String, Object>> model;

    /** Bağımlılıklar kurucudan enjekte edilir (alan enjeksiyonu yasak). */
    public EkranBean(TemekrduzenService duzenler, TemekrveriService veriler, TemdilcvrService cevirmen) {
        this.duzenler = duzenler;
        this.veriler = veriler;
        this.cevirmen = cevirmen;
    }

    /** Görünüm açılırken düzeni okur, kolonları ve tembel veri modelini hazırlar. */
    @PostConstruct
    public void baslat() {
        String siskod = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().getOrDefault("ekran", VARSAYILAN_EKRAN);
        duzen = duzenler.duzenGetir(siskod, "ANA", TemekrkapsamDto.sistem())
                .orElseThrow(() -> new IllegalStateException("Ekran düzeni yok: " + siskod));
        izgara = duzen.turdekiBolum("GRID").orElseThrow();
        diller = cevirmen.aktifDiller();
        dilKodu = cevirmen.varsayilanDilKodu();
        kolonlariHazirla();
        model = new LazyDataModel<>() {
            @Override
            public int count(Map<String, FilterMeta> filtreler) {
                return (int) veriler.toplamKayit(duzen);
            }

            @Override
            public List<Map<String, Object>> load(int ilk, int sayfaBoyu, Map<String, SortMeta> siralama,
                                                  Map<String, FilterMeta> filtreler) {
                String alan = null;
                boolean artan = true;
                if (!siralama.isEmpty()) {
                    SortMeta s = siralama.values().iterator().next();
                    alan = s.getField();
                    artan = s.getOrder() != SortOrder.DESCENDING;
                }
                return veriler.sayfaGetir(duzen, izgara.kodu(), alan, artan, ilk, sayfaBoyu).satirlar();
            }

            @Override
            public String getRowKey(Map<String, Object> satir) {
                return String.valueOf(satir.get("vid"));
            }
        };
    }

    /** Dil değişince etiketler yeniden çözülür; veri yeniden okunmaz. */
    public void dilDegisti() {
        kolonlariHazirla();
    }

    private void kolonlariHazirla() {
        Cozucu c = cevirmen.cozucuGetir(dilKodu, null, null, null);
        kolonlar = izgara.kolonlar().stream().filter(TemekrduzenDto.Kolon::gorunur)
                .map(k -> new KolonGorunumu(k.alanAdi(),
                        k.etiket() != null ? k.etiket()
                                : c.metin(k.alanAdi(), "alan." + duzen.tabloKodu() + "." + k.alanAdi(),
                                        "alan." + k.alanAdi()),
                        k.siralanabilir()))
                .toList();
    }

    /** Sayfa yönü: Arapçada "rtl". */
    public String getYon() {
        return diller.stream().filter(d -> d.kodu().equals(dilKodu)).findFirst()
                .map(d -> d.sagdanSola() ? "rtl" : "ltr").orElse("ltr");
    }

    public String getBaslik() {
        return duzen.baslik();
    }

    public List<KolonGorunumu> getKolonlar() {
        return kolonlar;
    }

    public List<Dil> getDiller() {
        return diller;
    }

    public String getDilKodu() {
        return dilKodu;
    }

    public void setDilKodu(String dilKodu) {
        this.dilKodu = dilKodu;
    }

    public LazyDataModel<Map<String, Object>> getModel() {
        return model;
    }
}
