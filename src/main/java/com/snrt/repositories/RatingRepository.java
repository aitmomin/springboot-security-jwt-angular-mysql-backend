package com.snrt.repositories;

import com.snrt.entities.Rating;
import com.snrt.entities.RatingID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, RatingID> {
}
