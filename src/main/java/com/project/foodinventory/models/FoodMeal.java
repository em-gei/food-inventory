package com.project.foodinventory.models;

import javax.persistence.*;

@Entity
public class FoodMeal {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="FOOD_ID")
    private Food food;

    @ManyToOne
    @JoinColumn(name="MEAL_ID")
    private Meal meal;

    public FoodMeal() {
        // Empty default constructor for JPA
    }

    public FoodMeal(long id, Food food, Meal meal) {
        this.id = id;
        this.food = food;
        this.meal = meal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
