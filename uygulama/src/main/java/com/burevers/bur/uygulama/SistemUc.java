package com.burevers.bur.uygulama;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sistem bilgisi REST uç noktası: {@code GET /api/v1/sistem/bilgi}. "Uc" eki, REST uç noktası demektir.
 *
 * <p>Dikey diliminin parçasıdır: REST katmanının ve veritabanı bağlantısının çalıştığını kanıtlar.
 * Hiçbir tabloya dokunmaz; yalnızca {@code SELECT 1} ile bağlantıyı yoklar (tablolar henüz açılmadı).
 *
 * <p>Uç nokta sürümlüdür ({@code /api/v1}); dış entegrasyonlar için sözleşme böyle korunur.
 */
@RestController
@RequestMapping("/api/v1/sistem")
public class SistemUc {

    private final BurOzellikleri ozellikler;
    private final JdbcClient jdbc;
    private final ObjectProvider<BuildProperties> derlemeBilgisi;

    /**
     * Bağımlılıklar kurucu üzerinden enjekte edilir (alan enjeksiyonu yasaktır; mimari test denetler).
     * BuildProperties, IDE'den çalıştırmada bulunmayabilir; bu yüzden ObjectProvider ile isteğe bağlı alınır.
     */
    public SistemUc(BurOzellikleri ozellikler,
                    JdbcClient jdbc,
                    ObjectProvider<BuildProperties> derlemeBilgisi) {
        this.ozellikler = ozellikler;
        this.jdbc = jdbc;
        this.derlemeBilgisi = derlemeBilgisi;
    }

    /** Uç noktanın döndürdüğü JSON yapısı. */
    public record SistemBilgisi(String uygulama, String surum, String dagitimProfili, boolean veritabaniBagli) {
    }

    /** Uygulama adı, sürümü, dağıtım profili ve veritabanı bağlantı durumunu döndürür. */
    @GetMapping("/bilgi")
    public SistemBilgisi bilgi() {
        BuildProperties derleme = derlemeBilgisi.getIfAvailable();
        String surum = derleme != null ? derleme.getVersion() : "gelistirme";

        // Bağlantı yoklaması: veritabanı yanıt verirse 1 döner
        boolean bagli = jdbc.sql("SELECT 1").query(Integer.class).optional().isPresent();

        return new SistemBilgisi("Burever", surum, ozellikler.dagitimProfili(), bagli);
    }
}
