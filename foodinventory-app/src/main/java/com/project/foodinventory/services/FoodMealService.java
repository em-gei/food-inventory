package com.project.foodinventory.services;

import com.project.foodinventory.models.FoodMeal;
import com.project.foodinventory.repositories.FoodMealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodMealService {

    @Autowired
    private FoodMealRepository foodMealRepository;

    public List<FoodMeal> findAll() {
        return foodMealRepository.findAll();
    }

    public FoodMeal findById(long id) {
        return foodMealRepository.findById(id).orElse(null);
    }

    public FoodMeal save(FoodMeal newMeal) {
        return foodMealRepository.save(newMeal);
    }

    public boolean delete(long id) {
        FoodMeal findById = findById(id);
        if (findById == null) {
            return false;
        }
        foodMealRepository.deleteById(id);
        return true;
    }
}
