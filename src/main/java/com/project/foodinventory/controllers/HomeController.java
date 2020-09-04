package com.project.foodinventory.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@Value("${home.message.key}")
	private String homeMessageKey;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute(homeMessageKey, "Food Inventory Web Application");
		return "index";
	}
}
