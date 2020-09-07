package com.project.foodinventory.repositories;

import com.project.foodinventory.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
