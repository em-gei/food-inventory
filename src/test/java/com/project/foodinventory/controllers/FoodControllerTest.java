package com.project.foodinventory.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import com.project.foodinventory.models.Food;
import com.project.foodinventory.services.FoodService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = FoodController.class)
public class FoodControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private FoodService foodService;

	@Value("${food.content.key}")
	private String foodContentKey;

	@Value("${food.key}")
	private String foodKey;

	@Test
	public void testFoodPageShouldReturn200() throws Exception {
		mvc.perform(get("/food")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testShouldReturnFoodPage() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/food")).andReturn().getModelAndView(), "food");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFoodPageEmptyContent() throws Exception {
		Mockito.when(foodService.findAll()).thenReturn(Collections.EMPTY_LIST);
		List<Food> foodList = (List<Food>) mvc.perform(get("/food")).andReturn().getModelAndView().getModel()
				.get(foodContentKey);
		assertEquals(0, foodList.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFoodPageWithContent() throws Exception {
		Mockito.when(foodService.findAll()).thenReturn(Arrays.asList(new Food(1, "test1"), new Food(2, "test2")));
		List<Food> foodList = (List<Food>) mvc.perform(get("/food")).andReturn().getModelAndView().getModel()
				.get(foodContentKey);
		assertEquals(2, foodList.size());
	}

	@Test
	public void testShouldReturnCreateFoodPage() throws Exception {
		ModelAndView modelAndView = mvc.perform(get("/food/new")).andReturn().getModelAndView();
		Food food = (Food) modelAndView.getModel().get(foodKey);

		ModelAndViewAssert.assertViewName(modelAndView, "food-edit");
		assertNotNull(food);
	}

	@Test
	public void testShouldReturnEditFoodPage() throws Exception {
		Mockito.when(foodService.findById(1)).thenReturn(new Food(1, "test"));

		ModelAndView modelAndView = mvc.perform(get("/food/edit/1")).andReturn().getModelAndView();
		Food editingFood = (Food) modelAndView.getModel().get(foodKey);

		ModelAndViewAssert.assertViewName(modelAndView, "food-edit");
		assertNotNull(editingFood);
		assertEquals(1, editingFood.getId());
		assertEquals("test", editingFood.getName());
	}

	@Test
	public void testFoodSaveWithNameNullDoNothing() throws Exception {
		Mockito.spy(foodService);
		Food food = new Food();

		ModelAndView modelAndView = mvc.perform(post("/food")
				.flashAttr("food", food)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getModelAndView();

		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/food/new");
		Mockito.verify(foodService, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void testFoodSaveWithNameEmptyDoNothing() throws Exception {
		Mockito.spy(foodService);
		Food food = new Food();
		food.setName("");

		ModelAndView modelAndView = mvc.perform(post("/food")
				.flashAttr("food", food)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getModelAndView();

		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/food/new");
		Mockito.verify(foodService, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void testShouldSaveAndReturnToFoodPage() throws Exception {
		Mockito.spy(foodService);
		Food food = new Food();
		food.setName("test");

		ModelAndView modelAndView = mvc.perform(post("/food")
				.flashAttr("food", food)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getModelAndView();

		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/food");
		Mockito.verify(foodService).save(Mockito.any());
	}

	@Test
	public void testShouldEditFoodWhenServiceFindIt() throws Exception{
		Food existingFood = new Food(1, "test");
		Mockito.when(foodService.findById(Mockito.anyLong())).thenReturn(existingFood);

		Food updatedFood = new Food(1, "editedName");

		ModelAndView modelAndView = mvc.perform(post("/food")
				.flashAttr("food", updatedFood)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getModelAndView();

		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/food");
		Mockito.verify(foodService).save(Mockito.any());
		Assert.assertEquals(foodService.findById(1).getName(), "editedName");
	}

	@Test
	public void testShouldDeleteFood() throws Exception {
		Mockito.spy(foodService);
		ModelAndView modelAndView = mvc.perform(delete("/food/delete/1")).andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/food");
		Mockito.verify(foodService).delete(1);
	}

}
