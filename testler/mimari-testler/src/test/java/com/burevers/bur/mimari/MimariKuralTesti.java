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
}
