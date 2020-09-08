package com.project.foodinventory.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.services.FoodService;

@Controller
@RequestMapping("/food")
public class FoodController {

    @Value("${food.content.key}")
    private String foodContentKey;

    @Value("${food.key}")
    private String foodKey;

    @Autowired
    FoodService foodService;


    @GetMapping
    public String getFoodPage(Model model) {
        List<Food> foodList = foodService.findAll();
        model.addAttribute(foodContentKey, foodList);
        return "food";
    }

    @GetMapping("/new")
    public String getFoodNewPage(Model model) {
        model.addAttribute(foodKey, new Food());
        return "food-edit";
    }

    @GetMapping("/edit/{id}")
    public String getFoodEditPage(@PathVariable int id, Model model) {
        Food food = foodService.findById(id);
        model.addAttribute(foodKey, food);
        return "food-edit";
    }

    @PostMapping
    public String save(@ModelAttribute("food") Food newFood) {
        String name = newFood.getName();
        if (name == null || name.isEmpty()) {
            return "redirect:/food/new";
        } else {
            Food food = foodService.findById(newFood.getId());
            if (food == null) {
                food = new Food();
            }
            food.setName(name);
            foodService.save(food);
            return "redirect:/food";
        }
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        foodService.delete(id);
        return "redirect:/food";
    }

}
