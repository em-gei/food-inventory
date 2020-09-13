package com.project.foodinventory.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Food {

    @Id
    @GeneratedValue
    @Column(name = "FOOD_ID")
    private long id;

    private String name;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<FoodMeal> mealList;

    public Food() {
        // Empty default constructor for JPA
    }

    public Food(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
