package com.project.foodinventory.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FoodController {
	
	@Value("${home.message.key}")
	private String homeMessageKey;
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("homeMessage", "Food Inventory Web Application");
		return "index";
	}

	@GetMapping("/food")
	public String food() {
	    return "food";
	}

	@GetMapping("/categories")
	public String categories() {
	    return "categories";
	}

}
