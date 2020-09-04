package com.project.foodinventory.controllers;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.FoodMeal;
import com.project.foodinventory.models.Meal;
import com.project.foodinventory.repositories.FoodMealRepository;
import com.project.foodinventory.repositories.FoodRepository;
import com.project.foodinventory.repositories.MealRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MealControllerIT {

    @Autowired
    MealRepository repository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FoodMealRepository foodMealRepository;

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        driver = new HtmlUnitDriver();
        // Reset table on startup
        repository.deleteAll();
        repository.flush();
    }

    @Test
    public void testMealPresentInListPage() {
        Meal meal = new Meal();
        meal.setDescription("test");
        repository.save(meal);

        driver.get(baseUrl + "/meal");

        WebElement element = driver.findElement(By.id(String.valueOf(meal.getId())));
        Assert.assertNotNull(element);
    }

    @Test
    public void testMealEditPage() {
        Meal meal = new Meal();
        meal.setDescription("mealTest");
        repository.save(meal);

        driver.get(baseUrl + "/meal/edit/" + meal.getId());

        String formContainer = driver.findElement(By.id("formContainer")).getText();
        assertThat(formContainer).contains("Id", String.valueOf(meal.getId()), "Description", meal.getDescription());
    }

    @Test
    public void testSaveNewMeal() {
        assertTrue(repository.findAll().isEmpty());

        driver.get(baseUrl + "/meal/new");
        String mealDescription = "mealTest";
        driver.findElement(By.name("description")).sendKeys(mealDescription);
        driver.findElement(By.name("btn_submit")).click();

        List<Meal> all = repository.findAll();
        assertEquals(1, all.size());
        assertThat(all.get(0).getDescription()).isEqualTo(mealDescription);
    }

    @Test
    public void testDeleteFoodFromList() {
        Meal meal = new Meal();
        meal.setDescription("mealTest");
        repository.save(meal);

        driver.get(baseUrl + "/meal");
        WebElement element = driver.findElement(By.id(String.valueOf(meal.getId())));
        Assert.assertNotNull(element);

        driver.findElement(By.id("delete_link_" + meal.getId())).click();

        assertThat(repository.findById(meal.getId())).isEmpty();
    }

    @Test
    public void testAddLinkedFood() {
        assertTrue(repository.findAll().isEmpty());
        foodRepository.deleteAll();
        Food foodToLink = new Food(1, "foodToLink");
        foodRepository.save(foodToLink);
        Meal meal = new Meal();
        meal.setDescription("testMeal");
        repository.save(meal);

        driver.get(baseUrl + "/meal/link/" + meal.getId());
        // There's only one food available, already selected
        driver.findElement(By.name("btn_submit")).click();

        List<FoodMeal> all = foodMealRepository.findAll();
        assertEquals(1, all.size());
        assertThat(all.get(0).getFood().getName()).isEqualTo(foodToLink.getName());
        assertThat(all.get(0).getMeal().getId()).isEqualTo(meal.getId());
    }

    @Test
    public void testRemoveLinkedFood() {
        assertTrue(repository.findAll().isEmpty());
        foodRepository.deleteAll();

        Meal meal = new Meal();
        meal.setDescription("testMeal");
        repository.save(meal);

        Food food = new Food();
        food.setName("testFood");
        foodRepository.save(food);

        FoodMeal foodMeal = new FoodMeal();
        foodMeal.setFood(food);
        foodMeal.setMeal(meal);
        foodMealRepository.save(foodMeal);

        assertNotNull(foodMealRepository.findById(foodMeal.getId()));
        driver.get(baseUrl + "/meal/remove/foodMeal/" + foodMeal.getId());
        assertFalse(foodMealRepository.findById(foodMeal.getId()).isPresent());
    }
}
