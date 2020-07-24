package com.project.foodinventory.controllers;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.repositories.FoodRepository;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FoodControllerIT {

    @Autowired
    FoodRepository foodRepository;

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        driver = new HtmlUnitDriver();
        // Reset table on startup
        foodRepository.deleteAll();
        foodRepository.flush();
    }

    @Test
    public void testFoodPresentInListPage() {
        Food food = new Food();
        food.setName("test");
        foodRepository.save(food);

        driver.get(baseUrl + "/food");

        WebElement element = driver.findElement(By.id(String.valueOf(food.getId())));
        Assert.assertNotNull(element);
    }

    @Test
    public void testFoodEditPage() {
        Food food = new Food();
        food.setName("test");
        foodRepository.save(food);

        driver.get(baseUrl + "/food/edit/" + food.getId());

        String formContainer = driver.findElement(By.id("formContainer")).getText();
        assertThat(formContainer).contains("Id", String.valueOf(food.getId()), "Name", food.getName());
    }

    @Test
    public void testSaveNewFood() {
        assertTrue(foodRepository.findAll().isEmpty());

        driver.get(baseUrl + "/food/new");
        String foodName = "foodTest";
        driver.findElement(By.name("name")).sendKeys(foodName);
        driver.findElement(By.name("btn_submit")).click();

        List<Food> all = foodRepository.findAll();
        assertTrue(all.size() == 1);
        assertThat(all.get(0).getName()).isEqualTo(foodName);
    }

    @Test
    public void testDeleteFoodFromList() {
        Food food = new Food();
        food.setName("test");
        foodRepository.save(food);

        driver.get(baseUrl + "/food");
        WebElement element = driver.findElement(By.id(String.valueOf(food.getId())));
        Assert.assertNotNull(element);

        driver.findElement(By.id("delete_link_" + food.getId())).click();

        assertThat(foodRepository.findById(food.getId())).isEmpty();
    }
}
