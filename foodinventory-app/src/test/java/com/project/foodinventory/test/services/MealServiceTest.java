package com.project.foodinventory.test.services;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.FoodMeal;
import com.project.foodinventory.models.Meal;
import com.project.foodinventory.repositories.MealRepository;
import com.project.foodinventory.services.MealService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
public class MealServiceTest {

    @Mock
    private MealRepository repository;

    @InjectMocks
    private MealService mealService;

    @Test
    public void testFindAllWithNoResults() {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Meal> findAll = mealService.findAll();
        assertTrue(findAll.isEmpty());
    }

    @Test
    public void testFindAllWithResults() {
        Meal meal1 = new Meal(1, "test1");
        Meal meal2 = new Meal(2, "test2");
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(meal1, meal2));

        List<Meal> findAll = mealService.findAll();

        assertEquals(2, findAll.size());
        assertThat(findAll.contains(meal1));
        assertThat(findAll.contains(meal2));
    }

    @Test
    public void testFindByIdExistingItem() {
        Meal meal = new Meal(1, "test");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(meal));

        Meal findById = mealService.findById(1);

        assertNotNull(findById);
        assertEquals(meal.getId(), findById.getId());
        assertEquals(meal.getDescription(), findById.getDescription());
    }

    @Test
    public void testFindByIdWhenMealNotExist() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Meal findById = mealService.findById(1);

        assertNull(findById);
    }

    @Test
    public void testSaveMeal() {
        Mockito.spy(repository);
        ArgumentCaptor<Meal> argument = ArgumentCaptor.forClass(Meal.class);

        Meal meal = new Meal();
        meal.setId(1);
        meal.setDescription("mealTest");
        FoodMeal foodMeal = new FoodMeal(2, new Food(1, "foodTest"), meal);
        meal.setFoodList(Arrays.asList(foodMeal));
        Mockito.when(repository.save(Mockito.any(Meal.class))).thenReturn(meal);

        Meal saved = mealService.save(meal);

        assertEquals(meal, saved);
        assertNotNull(saved.getFoodList());
        assertFalse(saved.getFoodList().isEmpty());

        Mockito.verify(repository).save(argument.capture());
        assertEquals(meal, argument.getValue());
    }

    @Test
    public void testDeleteExistingMeal() {
        Meal food = new Meal(1, "test");
        Mockito.spy(repository);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(food));

        assertTrue(mealService.delete(1));
        Mockito.verify(repository).deleteById(Long.valueOf(1));
    }

    @Test
    public void testDeleteMealThatNotExists() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertFalse(mealService.delete(1));
    }
}