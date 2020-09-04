package com.project.foodinventory.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Meal {

    @Id
    @GeneratedValue
    private long id;

    private String description;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<FoodMeal> foodList;

    public Meal() {
        // Empty default constructor for JPA
    }

    public Meal(long id, String description) {
        this.id = id;
        this.description = description;
        foodList = new ArrayList();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FoodMeal> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodMeal> foodList) {
        this.foodList = foodList;
    }
}
