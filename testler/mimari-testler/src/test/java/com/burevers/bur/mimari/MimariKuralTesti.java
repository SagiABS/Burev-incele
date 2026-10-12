package com.burevers.bur.mimari;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Mimari kural testleri: mimari kararları (ADR) ve adlandırma kurallarını koda döker.
 *
 * <p>Her test bir kuralı denetler. Bir kural ihlal edilirse test kırılır, CI derlemeyi durdurur.
 * Böylece mimari, dokümanda değil kodda korunur. Henüz paket açılmadığı için bazı kurallar boş
 * kümeyi denetler; bu bilinçlidir ({@code allowEmptyShould}) ve paketler açıldıkça devreye girer.
 */
class MimariKuralTesti {

    /** Denetlenecek tüm üretim sınıfları (testler hariç). */
    private static JavaClasses siniflar;

    @BeforeAll
    static void siniflariIceAktar() {
        // Test sınıfları denetim dışı tutulur; yalnızca üretim kodu incelenir
        siniflar = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(MimariSabitleri.KOK_PAKET);
    }

    /** Çekirdek saf Java'dır: Spring, JPA, Vaadin ve hiçbir alan modülüne/uygulamaya bağımlı olamaz. */
    @Test
    void cekirdekHicbirCatiyaVeModuleBagimliOlamaz() {
        List<String> yasaklar = new ArrayList<>(List.of(
                "org.springframework..", "jakarta.persistence..", "com.vaadin..",
                MimariSabitleri.KOK_PAKET + ".uygulama.."));
        MimariSabitleri.MODULLER.forEach(m -> yasaklar.add(MimariSabitleri.modulDeseni(m)));

        noClasses().that().resideInAPackage(MimariSabitleri.KOK_PAKET + ".cekirdek..")
                .should().dependOnClassesThat().resideInAnyPackage(yasaklar.toArray(String[]::new))
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /** Modüller arası bağımlılık yönü: tem ← bel ← muh ve tem ← mht. Ters veya yatay bağımlılık yasaktır. */
    @Test
    void modulBagimlilikYonuKorunur() {
        for (String modul : MimariSabitleri.MODULLER) {
            List<String> yasakModuller = new ArrayList<>(MimariSabitleri.MODULLER);
            yasakModuller.remove(modul);
            yasakModuller.removeAll(MimariSabitleri.IZINLI_BAGIMLILIKLAR.get(modul));

            String[] yasakDesenler = yasakModuller.stream()
                    .map(MimariSabitleri::modulDeseni).toArray(String[]::new);

            noClasses().that().resideInAPackage(MimariSabitleri.modulDeseni(modul))
                    .should().dependOnClassesThat().resideInAnyPackage(yasakDesenler)
                    .because(modul + " modülü bu modüllere bağımlı olamaz: " + yasakModuller)
                    .allowEmptyShould(true)
                    .check(siniflar);
        }
    }

    /** Alan modülleri ve çekirdek, uygulama paketine bağımlı olamaz (bağımlılık içe doğrudur). */
    @Test
    void alanModulleriUygulamayaBagimliOlamaz() {
        List<String> kaynaklar = new ArrayList<>(List.of(MimariSabitleri.KOK_PAKET + ".cekirdek.."));
        MimariSabitleri.MODULLER.forEach(m -> kaynaklar.add(MimariSabitleri.modulDeseni(m)));

        noClasses().that().resideInAnyPackage(kaynaklar.toArray(String[]::new))
                .should().dependOnClassesThat().resideInAPackage(MimariSabitleri.KOK_PAKET + ".uygulama..")
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /** Paketler (kök paketin doğrudan alt paketleri) arasında döngüsel bağımlılık olamaz. */
    @Test
    void paketlerArasindaDongusuzBagimlilikVardir() {
        slices().matching(MimariSabitleri.KOK_PAKET + ".(*)..")
                .should().beFreeOfCycles()
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /**
     * Paket kodu dili: kök paketin doğrudan alt paketi ya özel paket (cekirdek, uygulama) ya da
     * geçerli 6 karakterli paket kodu (modül + 3 harflik grup) olmalıdır.
     */
    @Test
    void paketKoduDiliKorunur() {
        ArchCondition<JavaClass> gecerliPaketKodu = new ArchCondition<>(
                "kök paketin altında özel paket veya geçerli paket koduyla başlamalı") {
            @Override
            public void check(JavaClass sinif, ConditionEvents olaylar) {
                String ad = sinif.getPackageName();
                String onEk = MimariSabitleri.KOK_PAKET + ".";
                if (!ad.startsWith(onEk)) {
                    return; // kök paketin kendisi (ana sınıf) denetim dışıdır
                }
                String kalan = ad.substring(onEk.length());
                String ilkParca = kalan.contains(".") ? kalan.substring(0, kalan.indexOf('.')) : kalan;
                boolean uygun = MimariSabitleri.OZEL_PAKETLER.contains(ilkParca)
                        || ilkParca.matches(MimariSabitleri.PAKET_KODU_DESENI);
                olaylar.add(new SimpleConditionEvent(sinif, uygun,
                        sinif.getName() + " geçerli bir paket koduna sahip değil: '" + ilkParca + "'"));
            }
        };

        classes().that().resideInAPackage(MimariSabitleri.KOK_PAKET + "..")
                .should(gecerliPaketKodu)
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /** Alan enjeksiyonu (@Autowired alan) yasaktır; bağımlılıklar kurucudan alınır. */
    @Test
    void alanEnjeksiyonuKullanilmaz() {
        noFields().should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /** Paket adları yalnızca küçük harf, rakam, alt çizgi ve nokta içerir (Türkçe karakter yok). */
    @Test
    void paketAdlariAsciiDir() {
        ArchCondition<JavaClass> asciiPaket = new ArchCondition<>(
                "yalnızca küçük harf, rakam, alt çizgi ve nokta içeren paket adına sahip olmalı") {
            @Override
            public void check(JavaClass sinif, ConditionEvents olaylar) {
                boolean uygun = sinif.getPackageName().matches("[a-z0-9_.]*");
                olaylar.add(new SimpleConditionEvent(sinif, uygun,
                        sinif.getName() + " sınıfının paket adı kurala uymuyor: " + sinif.getPackageName()));
            }
        };

        classes().should(asciiPaket)
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /** Sınıf adları yalnızca ASCII harf, rakam, alt çizgi ve $ içerir (Türkçe karakter yok). */
    @Test
    void sinifAdlariAsciiDir() {
        ArchCondition<JavaClass> asciiSinif = new ArchCondition<>(
                "yalnızca ASCII harf, rakam, alt çizgi ve $ içeren sınıf adına sahip olmalı") {
            @Override
            public void check(JavaClass sinif, ConditionEvents olaylar) {
                boolean uygun = sinif.getSimpleName().matches("[A-Za-z0-9_$]*");
                olaylar.add(new SimpleConditionEvent(sinif, uygun,
                        sinif.getName() + " sınıf adı ASCII değil"));
            }
        };

        classes().should(asciiSinif)
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /**
     * Tablo ve entity adlandırması: her @Entity sınıfının @Table adı geçerli tablo koduna (paket kodu + 3-4 harf,
     * en çok 10 karakter) uymalı, sınıf adı tablo kodunun baş harfi büyük hâli + "Entity" olmalı ve sınıf,
     * tablo kodunun ilk 6 karakteriyle adlanan pakette bulunmalıdır.
     */
    @Test
    void tabloVeEntityAdlandirmasiKorunur() {
        ArchCondition<JavaClass> kural = new ArchCondition<>(
                "tablo kodu, sınıf adı ve paket adı birbiriyle uyumlu olmalı") {
            @Override
            public void check(JavaClass sinif, ConditionEvents olaylar) {
                String tablo = sinif.tryGetAnnotationOfType("jakarta.persistence.Table")
                        .map(a -> String.valueOf(a.getProperties().get("name")))
                        .orElse("");
                boolean tabloUygun = tablo.matches(MimariSabitleri.TABLO_KODU_DESENI);
                boolean sinifUygun = tabloUygun
                        && sinif.getSimpleName().equals(Character.toUpperCase(tablo.charAt(0)) + tablo.substring(1) + "Entity");
                boolean paketUygun = tabloUygun
                        && sinif.getPackageName().equals(MimariSabitleri.KOK_PAKET + "." + tablo.substring(0, 6));
                olaylar.add(new SimpleConditionEvent(sinif, tabloUygun && sinifUygun && paketUygun,
                        sinif.getName() + ": tablo='" + tablo + "' kurala uymuyor (tablo kodu deseni, sınıf adı = Tablo+Entity, paket = ilk 6 karakter)"));
            }
        };

        classes().that().areAnnotatedWith("jakarta.persistence.Entity")
                .should(kural)
                .allowEmptyShould(true)
                .check(siniflar);
    }

    /**
     * Sınıf soneki: alan modüllerinin paketlerindeki (temort, temorg, mhtlev...) her üst düzey sınıf,
     * paket kodunun baş harfi büyük hâliyle başlamalı ve izin verilen standart sonekle bitmelidir
     * (Entity, Repository, Service, View, Dialog, Controller, Dto, Validator, Event). Ortak taban sınıflar muaftır.
     */
    @Test
    void sinifSoneklerineUyulur() {
        ArchCondition<JavaClass> kural = new ArchCondition<>(
                "paket kodu ile başlamalı ve izin verilen standart sonekle bitmeli") {
            @Override
            public void check(JavaClass sinif, ConditionEvents olaylar) {
                String onEk = MimariSabitleri.KOK_PAKET + ".";
                String paket = sinif.getPackageName();
                if (!paket.startsWith(onEk) || sinif.isNestedClass() || sinif.isAnonymous()) {
                    return;
                }
                String ilkParca = paket.substring(onEk.length());
                if (ilkParca.contains(".")) {
                    ilkParca = ilkParca.substring(0, ilkParca.indexOf('.'));
                }
                if (!ilkParca.matches(MimariSabitleri.PAKET_KODU_DESENI)) {
                    return; // cekirdek/uygulama gibi özel paketler bu kurala tabi değildir
                }
                String ad = sinif.getSimpleName();
                if (MimariSabitleri.TABAN_SINIFLAR.contains(ad)) {
                    return;
                }
                String paketOnEki = Character.toUpperCase(ilkParca.charAt(0)) + ilkParca.substring(1);
                boolean sonekUygun = MimariSabitleri.SINIF_SONEKLERI.stream()
                        .anyMatch(sonek -> ad.endsWith(sonek) && ad.length() > paketOnEki.length() + sonek.length());
                boolean uygun = ad.startsWith(paketOnEki) && sonekUygun;
                olaylar.add(new SimpleConditionEvent(sinif, uygun,
                        sinif.getName() + " adı kurala uymuyor: '" + paketOnEki + "' ile başlamalı, sonek: "
                                + MimariSabitleri.SINIF_SONEKLERI));
            }
        };

        classes().should(kural)
                .allowEmptyShould(true)
                .check(siniflar);
    }
}
