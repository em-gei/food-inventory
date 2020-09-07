package com.project.foodinventory.services;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.repositories.FoodRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodServiceIT {

    @Autowired
    FoodService foodService;

    @Autowired
    FoodRepository foodRepository;

    @Before
    public void setup() {
        // Reset table on startup
        foodRepository.deleteAll();
    }

    private Food insertFoodInDb() {
        Food food = new Food();
        food.setName("test");
        foodService.save(food);
        return food;
    }

    @Test
    public void testSaveAndRetrieveFood() {
        Food food = insertFoodInDb();

        Food retrivedFood = foodService.findById(food.getId());

        Assert.assertEquals(food.getId(), retrivedFood.getId());
        Assert.assertEquals(food.getName(), retrivedFood.getName());
    }

    @Test
    public void testDeleteFromDb() {
        Food food = insertFoodInDb();

        foodService.delete(food.getId());

        Assert.assertNull(foodService.findById(food.getId()));
    }

    @Test
    public void testUpdateFoodInDb() {
        Food food = insertFoodInDb();
        Food byId = foodService.findById(food.getId());

        byId.setName("newName");
        foodService.save(byId);

        Assert.assertEquals("newName", foodService.findById(food.getId()).getName());
    }
}
