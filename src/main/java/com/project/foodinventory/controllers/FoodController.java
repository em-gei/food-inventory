package com.project.foodinventory.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.foodinventory.dtos.FoodDTO;
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
	
	@GetMapping("/edit/{id}")
	public String getFoodEditPage(@PathVariable int id, Model model) {
		model.addAttribute(foodKey, foodService.findById(id));
		return "food-edit";
	}
	
	@GetMapping("/new")
	public String getFoodNewPage(Model model) {
		model.addAttribute(foodKey, new Food());
		return "food-edit";
	}
	
	@PostMapping
	public String save(@ModelAttribute("food") FoodDTO newFood) {
		foodService.save(new Food(newFood.getId(), newFood.getName()));
		return "redirect:/food";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		foodService.delete(id);
		return "redirect:/food";
	}

}
