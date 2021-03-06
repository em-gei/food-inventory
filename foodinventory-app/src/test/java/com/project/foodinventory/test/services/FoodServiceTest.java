package com.project.foodinventory.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.project.foodinventory.services.FoodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.repositories.FoodRepository;

@RunWith(MockitoJUnitRunner.class)
public class FoodServiceTest {

	@InjectMocks
	private FoodService foodService;

	@Mock
	private FoodRepository repository;

	@Test
	public void testFindAllWithNoResults() {
		Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
		List<Food> findAll = foodService.findAll();
		assertTrue(findAll.isEmpty());
	}

	@Test
	public void testFindAllWithResults() {
		Food food1 = new Food(1, "test1");
		Food food2 = new Food(2, "test2");
		Mockito.when(repository.findAll()).thenReturn(Arrays.asList(food1, food2));
		
		List<Food> findAll = foodService.findAll();
		
		assertEquals(2, findAll.size());
		assertThat(findAll.contains(food1));
		assertThat(findAll.contains(food2));
	}

	@Test
	public void testFindByIdExistingItem() {
		Food food = new Food(1, "test");
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(food));
		
		Food findById = foodService.findById(1);
		
		assertNotNull(findById);
		assertEquals(food.getId(), findById.getId());
		assertEquals(food.getName(), findById.getName());
	}
	
	@Test
	public void testFindByIdWhenFoodNotExist() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Food findById = foodService.findById(1);
		
		assertNull(findById);
	}
	
	@Test
	public void testSaveFood() {
		Mockito.spy(repository);
		ArgumentCaptor<Food> argument = ArgumentCaptor.forClass(Food.class);

		Food food = new Food();
		food.setId(1);
		food.setName("test");
		Mockito.when(repository.save(Mockito.any(Food.class))).thenReturn(food);
		
		Food saved = foodService.save(food);
		
		assertEquals(food, saved);
		Mockito.verify(repository).save(argument.capture());
		assertEquals(food, argument.getValue());
	}
	
	@Test
	public void testDeleteExistingFood() {
		Food food = new Food(1, "test");
		Mockito.spy(repository);
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(food));
		
		assertTrue(foodService.delete(food.getId()));
		Mockito.verify(repository).deleteById(food.getId());
	}

	@Test
	public void testDeleteFoodThatNotExists() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertFalse(foodService.delete(1));
	}
	
}
