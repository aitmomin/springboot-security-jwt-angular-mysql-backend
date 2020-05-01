package com.snrt.repositories;

import com.snrt.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    @Query(value="SELECT * FROM advertisement a where a.count < a.max order by rand() LIMIT 1", nativeQuery = true)
    Advertisement getAdvertisementRandom();

    @Query(value="SELECT * " +
            "FROM advertisement a " +
            "WHERE a.id NOT IN (SELECT au.id_advert FROM advertisement a, advert_user au WHERE a.id=au.id_advert and au.id_user = ?1 ) " +
            "   and a.count < a.max" +
            " ORDER by rand() LIMIT 1", nativeQuery = true)
    Advertisement getAdvertisementRandom2(long id);
}
