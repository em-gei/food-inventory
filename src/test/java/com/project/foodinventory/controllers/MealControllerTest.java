package com.project.foodinventory.controllers;

import com.project.foodinventory.dtos.MealDTO;
import com.project.foodinventory.models.Food;
import com.project.foodinventory.models.Meal;
import com.project.foodinventory.services.FoodMealService;
import com.project.foodinventory.services.FoodService;
import com.project.foodinventory.services.MealService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MealController.class)
public class MealControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private FoodService foodService;

    @MockBean
    private FoodMealService foodMealService;

    @Value("${meal.content.key}")
    private String mealContentKey;

    @Value("${meal.key}")
    private String mealKey;

    @Value("${mealDTO.key}")
    private String mealDTOKey;

    @Test
    public void testMealPageExists() throws Exception {
        mvc.perform(get("/meal")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testShouldReturnMealPage() throws Exception {
        ModelAndViewAssert.assertViewName(mvc.perform(get("/meal")).andReturn().getModelAndView(), "meal");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMealPageEmptyContent() throws Exception {
        Mockito.when(mealService.findAll()).thenReturn(Collections.EMPTY_LIST);
        List<Meal> mealList = (List<Meal>) mvc.perform(get("/meal")).andReturn().getModelAndView().getModel()
                .get(mealContentKey);
        assertEquals(0, mealList.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMealPageWithContent() throws Exception {
        Mockito.when(mealService.findAll()).thenReturn(Arrays.asList(new Meal(1, "mealTest1"), new Meal(2, "mealTest2")));
        List<Meal> mealList = (List<Meal>) mvc.perform(get("/meal")).andReturn().getModelAndView().getModel()
                .get(mealContentKey);
        assertEquals(2, mealList.size());
    }

    @Test
    public void testShouldReturnCreateMealPage() throws Exception {
        ModelAndView modelAndView = mvc.perform(get("/meal/new")).andReturn().getModelAndView();
        Meal meal = (Meal) modelAndView.getModel().get(mealKey);

        ModelAndViewAssert.assertViewName(modelAndView, "meal-edit");
        assertNotNull(meal);
    }

    @Test
    public void testShouldReturnEditMealPage() throws Exception {
        Mockito.when(mealService.findById(1)).thenReturn(new Meal(1, "mealTest"));

        ModelAndView modelAndView = mvc.perform(get("/meal/edit/1")).andReturn().getModelAndView();
        Meal editingMeal = (Meal) modelAndView.getModel().get(mealKey);

        ModelAndViewAssert.assertViewName(modelAndView, "meal-edit");
        assertNotNull(editingMeal);
        assertEquals(1, editingMeal.getId());
        assertEquals("mealTest", editingMeal.getDescription());
    }

    @Test
    public void testMealSaveWithDescriptionNullDoNothing() throws Exception {
        Mockito.spy(mealService);
        Meal meal = new Meal();

        ModelAndView modelAndView = mvc.perform(post("/meal")
                .flashAttr("meal", meal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal/new");
        Mockito.verify(mealService, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testMealSaveWithEmptyDescriptionDoNothing() throws Exception {
        Mockito.spy(mealService);
        Meal meal = new Meal();
        meal.setDescription("");

        ModelAndView modelAndView = mvc.perform(post("/meal")
                .flashAttr("meal", meal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal/new");
        Mockito.verify(mealService, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testShouldSaveAndReturnToMealPage() throws Exception {
        Mockito.spy(mealService);
        Meal meal = new Meal();
        meal.setDescription("test");

        ModelAndView modelAndView = mvc.perform(post("/meal")
                .flashAttr("meal", meal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal");
        Mockito.verify(mealService).save(Mockito.any());
    }

    @Test
    public void testShouldDeleteMeal() throws Exception {
        Mockito.spy(mealService);
        ModelAndView modelAndView = mvc.perform(delete("/meal/delete/1")).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal");
        Mockito.verify(mealService).delete(1);
    }

    @Test
    public void testShouldReturnLinkMealPage() throws Exception {
        Mockito.when(mealService.findById(1)).thenReturn(new Meal(1, "mealTest"));
        Mockito.when(foodService.findAll()).thenReturn(Arrays.asList(new Food(1, "foodToAdd")));

        ModelAndView modelAndView = mvc.perform(get("/meal/link/1")).andReturn().getModelAndView();
        MealDTO editingMeal = (MealDTO) modelAndView.getModel().get(mealDTOKey);

        ModelAndViewAssert.assertViewName(modelAndView, "meal-link");
        assertNotNull(editingMeal);
        assertEquals(1, editingMeal.getMeal().getId());
        assertEquals("mealTest", editingMeal.getMeal().getDescription());
    }

    @Test
    public void testShouldRemoveLinkedFood() throws Exception {
        Mockito.spy(foodMealService);
        ModelAndView modelAndView = mvc.perform(delete("/meal/remove/foodMeal/1")).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal");
        Mockito.verify(foodMealService).delete(1);}

    @Test
    public void testShouldAddLinkedFood() throws Exception {
        Mockito.spy(foodMealService);

        MealDTO mealDTO = new MealDTO();
        Food foodToAdd = new Food(1, "foodToAdd");
        Meal meal = new Meal(1, "mealTest");
        mealDTO.setFoodToAdd(foodToAdd);

        Mockito.when(mealService.findById(Mockito.anyLong())).thenReturn(meal);
        Mockito.when(foodService.findById(foodToAdd.getId())).thenReturn(foodToAdd);

        ModelAndView modelAndView = mvc.perform(post("/meal/1/add/food")
                .flashAttr("mealDTO", mealDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal");
        Mockito.verify(foodMealService).save(Mockito.any());
    }

    @Test
    public void testAddLinkedFoodDoNothingWhenNull() throws Exception{
        Mockito.spy(foodMealService);
        MealDTO mealDTO = new MealDTO();

        ModelAndView modelAndView = mvc.perform(post("/meal/1/add/food")
                .flashAttr("mealDTO", mealDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/meal");
        Mockito.verify(foodMealService, Mockito.never()).save(Mockito.any());
    }
}
