package com.project.foodinventory.models;

import javax.persistence.*;

@Entity
public class Food {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    public Food() {
        // Empty default constructor for JPA
    }

    public Food(int id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}
