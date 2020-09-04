package com.project.foodinventory.dtos;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealDTO {

    private Meal meal;

    private Food foodToAdd;

    private List<Food> allFoods;

    public MealDTO() {}

    public MealDTO(Meal meal) {
        this.meal = meal;
        this.foodToAdd = new Food();
        allFoods = new ArrayList<>();
    }

    public Food getFoodToAdd() {
        return foodToAdd;
    }

    public void setFoodToAdd(Food foodToAdd) {
        this.foodToAdd = foodToAdd;
    }

    public List<Food> getAllFoods() {
        return allFoods;
    }

    public void setAllFoods(List<Food> allFoods) {
        this.allFoods = allFoods;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
