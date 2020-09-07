package com.project.foodinventory.repositories;

import com.project.foodinventory.models.FoodMeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodMealRepository extends JpaRepository<FoodMeal, Long> {

}
