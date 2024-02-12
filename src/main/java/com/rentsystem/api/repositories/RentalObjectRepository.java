package com.rentsystem.api.repositories;

import com.rentsystem.api.models.RentalObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalObjectRepository extends JpaRepository<RentalObject, Long> {
}
