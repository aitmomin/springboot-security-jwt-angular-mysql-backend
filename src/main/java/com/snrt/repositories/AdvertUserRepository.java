package com.snrt.repositories;

import com.snrt.entities.AdvertID;
import com.snrt.entities.Advert_User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertUserRepository extends JpaRepository<Advert_User, AdvertID> {
}
