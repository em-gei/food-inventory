package com.project.foodinventory.services;

import com.project.foodinventory.models.Meal;
import com.project.foodinventory.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;


    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    public Meal findById(long id) {
        return mealRepository.findById(id).orElse(null);
    }

    public Meal save(Meal newMeal) {
        return mealRepository.save(newMeal);
    }

    public boolean delete(long id) {
        Meal findById = findById(id);
        if (findById == null) {
            return false;
        }
        mealRepository.deleteById(id);
        return true;
    }

}
