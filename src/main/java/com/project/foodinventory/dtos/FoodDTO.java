package com.project.foodinventory.dtos;

import com.project.foodinventory.models.Food;

public class FoodDTO {

    private int id;
    private String name;

    public FoodDTO() {}

    public FoodDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FoodDTO dtoFromFood(Food food) {
        return new FoodDTO(food.getId(), food.getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FoodDTO [id=" + id + ", name=" + name + "]";
    }

}
