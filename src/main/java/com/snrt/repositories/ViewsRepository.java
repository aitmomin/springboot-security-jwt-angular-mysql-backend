package com.snrt.repositories;

import com.snrt.entities.Views;
import com.snrt.entities.ViewsID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewsRepository extends JpaRepository<Views, ViewsID> {
}
