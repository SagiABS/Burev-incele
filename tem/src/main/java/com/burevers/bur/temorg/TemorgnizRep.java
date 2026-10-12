package com.burevers.bur.temorg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemorgnizRep - TemorgnizVrl için veri erişim arayüzü.
 * Ne işe yarar: yapı ağacı bağlantılarını bulur, ekler, günceller.
 * Neden var: Spring Data gerçek uygulamayı kendisi üretir; elle SQL yazılmaz.
 */
public interface TemorgnizRep extends JpaRepository<TemorgnizVrl, Long> {

    /** Bir organizasyonun aktif (pasif olmayan) bağlantılarını getirir. */
    List<TemorgnizVrl> findByOrganizasyonVidAndPasifFalse(Long organizasyonVid);
}
