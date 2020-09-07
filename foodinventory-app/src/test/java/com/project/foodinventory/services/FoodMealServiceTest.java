package com.project.foodinventory.services;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.FoodMeal;
import com.project.foodinventory.models.Meal;
import com.project.foodinventory.repositories.FoodMealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FoodMealServiceTest {

    @InjectMocks
    private FoodMealService service;

    @Mock
    private FoodMealRepository repository;

    @Test
    public void testFindAllWithNoResults() {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        List<FoodMeal> findAll = service.findAll();
        assertTrue(findAll.isEmpty());
    }

    @Test
    public void testFindAllWithResults() {
        Meal meal = new Meal(1, "meal1");
        Food food1 = new Food(1, "food1");
        Food food2 = new Food(2, "food2");
        FoodMeal foodMeal1 = new FoodMeal(1, food1, meal);
        FoodMeal foodMeal2 = new FoodMeal(2, food2, meal);
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(foodMeal1, foodMeal2));

        List<FoodMeal> findAll = service.findAll();

        assertEquals(2, findAll.size());
        assertThat(findAll.contains(foodMeal1));
        assertThat(findAll.contains(foodMeal2));
    }

    @Test
    public void testFindByIdExistingItem() {
        Meal meal = new Meal(1, "meal1");
        Food food = new Food(1, "food1");
        FoodMeal foodMeal = new FoodMeal(1, food, meal);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foodMeal));

        FoodMeal findById = service.findById(1);

        assertNotNull(findById);
        assertEquals(foodMeal.getId(), findById.getId());
        assertEquals(foodMeal.getFood().getName(), findById.getFood().getName());
        assertEquals(foodMeal.getMeal().getDescription(), findById.getMeal().getDescription());
    }

    @Test
    public void testFindByIdWhenFoodMealNotExist() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        FoodMeal findById = service.findById(1);

        assertNull(findById);
    }

    @Test
    public void testSaveFoodMeal() {
        Meal meal = new Meal(1, "meal1");
        Food food = new Food(1, "food1");
        FoodMeal foodMeal = new FoodMeal(1, food, meal);

        Mockito.when(repository.save(Mockito.any(FoodMeal.class))).thenReturn(foodMeal);

        FoodMeal saved = service.save(foodMeal);

        assertEquals(foodMeal, saved);
    }

    @Test
    public void testDeleteExistingFoodMeal() {
        Meal meal = new Meal(1, "meal1");
        Food food = new Food(1, "food1");
        FoodMeal foodMeal = new FoodMeal(1, food, meal);

        Mockito.spy(repository);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foodMeal));

        assertTrue(service.delete(1));
        Mockito.verify(repository).deleteById(Long.valueOf(1));
    }

    @Test
    public void testDeleteFoodMealThatNotExists() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertFalse(service.delete(1));
    }
}