package com.project.foodinventory.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.foodinventory.controllers.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeController.class)
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private WebClient webClient;
	
	@Value("${home.message.key}")
	private String homeMessageKey;
	
	@Test
	public void testIndexPageShouldReturn200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testIndexShouldReturnIndexPage() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	public void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertEquals("Food Inventory", page.getTitleText());
	}

	@Test
	public void testHomePageWelcomeMessage() throws Exception {
		HtmlPage page = webClient.getPage("/");
		HtmlElement homeMessage = page.getHtmlElementById(homeMessageKey);
		assertNotNull(homeMessage);
	}

	@Test
	public void testWelcomeMessageInHomePageModel() throws Exception {
		String homeMessage = mvc.perform(get("/")).andReturn().getModelAndView().getModel().get(homeMessageKey)
				.toString();
		assertNotNull(homeMessage);
	}
}
