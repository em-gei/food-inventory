package com.project.foodinventory.controllers;

import com.project.foodinventory.dtos.MealDTO;
import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.FoodMeal;
import com.project.foodinventory.models.Meal;
import com.project.foodinventory.services.FoodMealService;
import com.project.foodinventory.services.FoodService;
import com.project.foodinventory.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/meal")
public class MealController {

    public static final String REDIRECT_MEAL = "redirect:/meal";
    @Autowired
    MealService mealService;

    @Autowired
    FoodMealService foodMealService;

    @Autowired
    FoodService foodService;

    @Value("${meal.content.key}")
    private String mealContentKey;

    @Value("${mealDTO.key}")
    private String mealDTOKey;

    @Value("${meal.key}")
    private String mealKey;

    @GetMapping
    public String getMealPage(Model model) {
        List<Meal> mealList = mealService.findAll();
        model.addAttribute(mealContentKey, mealList);
        return "meal";
    }

    @GetMapping("/new")
    public String getFoodNewPage(Model model) {
        model.addAttribute(mealKey, new Meal());
        return "meal-edit";
    }

    @GetMapping("/edit/{id}")
    public String getMealEditPage(@PathVariable long id, Model model) {
        Meal meal = mealService.findById(id);
        model.addAttribute(mealKey, meal);
        return "meal-edit";
    }

    @PostMapping
    public String save(@ModelAttribute("meal") Meal newMeal) {
        String description = newMeal.getDescription();
        if (description == null || description.isEmpty()) {
            return "redirect:/meal/new";
        } else {
            Meal meal = mealService.findById(newMeal.getId());
            if (meal == null) {
                meal = new Meal();
            }
            meal.setDescription(description);
            mealService.save(meal);
            return REDIRECT_MEAL;
        }
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        mealService.delete(id);
        return REDIRECT_MEAL;
    }

    @GetMapping("/link/{id}")
    public String getMealLinkPage(@PathVariable long id, Model model) {
        Meal meal = mealService.findById(id);
        MealDTO mealDTO = new MealDTO(meal);
        mealDTO.setAllFoods(foodService.findAll());
        model.addAttribute(mealDTOKey, mealDTO);
        model.addAttribute("foodToAdd", new Food());
        return "meal-link";
    }

    @RequestMapping("/remove/foodMeal/{foodMealId}")
    public String removeLinkedFood(@PathVariable Long foodMealId) {
        foodMealService.delete(foodMealId);
        return REDIRECT_MEAL;
    }

    @PostMapping("/{mealId}/add/food")
    public String addLinkedFood(@ModelAttribute("mealDTO") MealDTO mealDTO, @PathVariable long mealId, Model model) {
        if (mealDTO.getFoodToAdd() != null) {
            Meal meal = mealService.findById(mealId);
            FoodMeal foodMeal = new FoodMeal();
            foodMeal.setFood(mealDTO.getFoodToAdd());
            foodMeal.setMeal(meal);
            foodMealService.save(foodMeal);
        }
        return REDIRECT_MEAL;
    }
}
