package com.project.foodinventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.repositories.FoodRepository;

@Service
public class FoodService {
	
	@Autowired
	private FoodRepository foodRepository;

	public List<Food> findAll() {
		return foodRepository.findAll();
	}

	public Food findById(long id) {
		return foodRepository.findById(id).orElse(null);
	}

	public Food save(Food newFood) {
		return foodRepository.save(newFood);
	}

	public boolean delete(long id) {
		Food findById = findById(id);
		if (findById == null) {
			return false;
		}
		foodRepository.deleteById(id);
		return true;
	}

}
