package com.project.foodinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.foodinventory.models.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
