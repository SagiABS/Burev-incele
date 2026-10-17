import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * Kanıt çalışması yük aracı: N "kullanıcıyı" (her biri ayrı oturum/çerez kabı) ekrana sokar ve oturumları açık tutar.
 *
 * <p>Neden var: Vue (durumsuz) ve PrimeFaces (sunucuda görünüm durumu) adaylarının 1000 aktif kullanıcıdaki
 * bellek farkını ölçmek. Python gerekmez; JDK yeter.
 *
 * <p>Kullanım (kök klasörden):
 * <pre>
 *   java poc/olcum/YukAraci.java vue 1000
 *   java poc/olcum/YukAraci.java jsf 1000 120     (son sayı: oturumları açık tutma süresi, sn; varsayılan 90)
 * </pre>
 * Açık tutma süresinde sunucuda: {@code jcmd <pid> GC.run} sonra {@code jcmd <pid> GC.heap_info}.
 */
public class YukAraci {

    /** Aday adı -> taban adres ve her kullanıcının sırayla isteyeceği yollar. */
    private static String[] aday(String ad) {
        return switch (ad) {
            case "vue" -> new String[] {"http://localhost:8081",
                    "/api/ekran/tem_893_000?dil=tr", "/api/veri/tem_893_000/LISTE?adet=50"};
            case "jsf" -> new String[] {"http://localhost:8082", "/ekran.xhtml?ekran=tem_893_000"};
            default -> throw new IllegalArgumentException("Aday: vue veya jsf");
        };
    }

    public static void main(String[] args) throws Exception {
        String[] a = aday(args[0]);
        int n = Integer.parseInt(args[1]);
        int bekle = args.length > 2 ? Integer.parseInt(args[2]) : 90;
        Semaphore es = new Semaphore(100); // aynı anda en çok 100 istek: sunucuyu boğmadan oturum biriktirir
        List<Long> sureler = new ArrayList<>();
        int[] hata = {0};
        long t0 = System.nanoTime();
        try (var havuz = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> isler = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                isler.add(havuz.submit(() -> {
                    HttpClient kullanici = HttpClient.newBuilder().cookieHandler(new CookieManager())
                            .connectTimeout(Duration.ofSeconds(30)).build();
                    long b = System.nanoTime();
                    try {
                        es.acquire();
                        for (int k = 1; k < a.length; k++) {
                            HttpRequest r = HttpRequest.newBuilder(URI.create(a[0] + a[k]))
                                    .timeout(Duration.ofSeconds(60)).build();
                            if (kullanici.send(r, HttpResponse.BodyHandlers.discarding()).statusCode() >= 400) {
                                synchronized (hata) { hata[0]++; }
                            }
                        }
                    } catch (Exception e) {
                        synchronized (hata) { hata[0]++; }
                    } finally {
                        es.release();
                        synchronized (sureler) { sureler.add((System.nanoTime() - b) / 1_000_000); }
                    }
                }));
            }
            for (Future<?> f : isler) f.get();
        }
        sureler.sort(null);
        System.out.printf("%s: %d kullanici, toplam %.1f sn, hata %d, ortanca %d ms, %%95 %d ms%n", args[0], n,
                (System.nanoTime() - t0) / 1e9, hata[0], sureler.get(n / 2), sureler.get((int) (n * .95)));
        System.out.println("Simdi sunucuda olc: jcmd <pid> GC.run  ve  jcmd <pid> GC.heap_info  (" + bekle + " sn beklenir)");
        Thread.sleep(bekle * 1000L);
    }
}
