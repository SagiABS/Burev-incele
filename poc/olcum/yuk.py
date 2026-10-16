#!/usr/bin/env python3
"""Kanıt çalışması yük aracı: N eş zamanlı "kullanıcı" simüle eder ve sunucu belleğini karşılaştırmak için hazırlar.

Ne yapar: Her kullanıcı kendi çerez kabıyla (ayrı oturum) ekranı açar; Vue adayında düzen+veri uçlarını,
PrimeFaces adayında ekran.xhtml sayfasını ister. Bittiğinde oturumları AÇIK tutar (varsayılan 60 sn),
bu sürede sunucuda bellek ölçülür:  jcmd <pid> GC.run  sonra  jcmd <pid> GC.heap_info
Kullanım:  python3 yuk.py vue 1000     |     python3 yuk.py jsf 1000
"""
import concurrent.futures as cf
import http.cookiejar
import sys
import time
import urllib.request

ADAYLAR = {
    "vue": ("http://localhost:8081", ["/api/ekran/tem_893_000?dil=tr", "/api/veri/tem_893_000/LISTE?adet=50"]),
    "jsf": ("http://localhost:8082", ["/ekran.xhtml?ekran=tem_893_000"]),
}


def kullanici(taban, yollar):
    """Tek kullanıcı: ayrı çerez kabı, istekleri sırayla yapar; süre (sn) ve hata sayısı döner."""
    kap = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))
    t0, hata = time.time(), 0
    for yol in yollar:
        try:
            kap.open(taban + yol, timeout=30).read()
        except Exception:
            hata += 1
    return time.time() - t0, hata


def main():
    aday, n = sys.argv[1], int(sys.argv[2])
    taban, yollar = ADAYLAR[aday]
    t0 = time.time()
    with cf.ThreadPoolExecutor(max_workers=min(n, 100)) as havuz:
        sonuc = list(havuz.map(lambda _: kullanici(taban, yollar), range(n)))
    sureler = sorted(s for s, _ in sonuc)
    print(f"{aday}: {n} kullanıcı, toplam {time.time() - t0:.1f} sn, hata {sum(h for _, h in sonuc)}, "
          f"ortanca {sureler[n // 2] * 1000:.0f} ms, %95 {sureler[int(n * .95)] * 1000:.0f} ms")
    print("Şimdi sunucuda ölç:  jcmd <pid> GC.run ; jcmd <pid> GC.heap_info   (60 sn beklenir)")
    time.sleep(int(sys.argv[3]) if len(sys.argv) > 3 else 60)


main()
