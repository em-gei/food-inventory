package com.projct.foodinventory.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HomeControllerE2E {

    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port;
    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        // setup Chrome Driver
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testHomePage() {
        driver.get(baseUrl);
        // the food page link is present with href containing /food
        driver.findElement(By.cssSelector("a[href*='/food"));
    }
}
