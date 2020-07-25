package com.projct.foodinventory.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class FoodControllerE2E {

    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port + "/food";
    private WebDriver driver;
    private long timestamp;

    @BeforeClass
    public static void setupClass() {
        // setup Chrome Driver
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
        timestamp = new Date().getTime();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testCreateNewFood() throws Exception {
        driver.get(baseUrl);
        // add a user using the "Create food" link
        driver.findElement(By.cssSelector("a[href*='/new")).click();
        // Await page to load
        Thread.sleep(500);

        WebElement formContainer = driver.findElement(By.id("formContainer"));

        formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
        // press Save
        driver.findElement(By.name("btn_submit")).click();

        // we are redirected to the food list page the table shows the created food
        assertThat(driver.findElement(By.id("foodTable")).getText()).contains(String.valueOf(timestamp));
    }

    @Test
    public void testEditExistingFood() throws Exception {
        driver.get(baseUrl);
        // add a user using the "Create food" link
        driver.findElement(By.cssSelector("a[href*='/new")).click();
        // Await page to load
        Thread.sleep(500);

        WebElement formContainer = driver.findElement(By.id("formContainer"));

        formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
        // press Save
        driver.findElement(By.name("btn_submit")).click();

        // Await page to load
        Thread.sleep(500);
        // Retrieve created food specific delete link
        WebElement foodTable = driver.findElement(By.id("foodTable"));
        foodTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("edit_link")).click();

        // Await page to refresh
        Thread.sleep(500);

        driver.findElement(By.id("formContainer")).findElement(By.name("name")).sendKeys("_edited");
        // press Save
        driver.findElement(By.name("btn_submit")).click();

        // Await page to load
        Thread.sleep(500);
        // Retrieve edited food
        driver.findElement(By.id("foodTable")).findElement(By.name(timestamp + "_edited"));
    }

    @Test
    public void testDeleteCreatedFood() throws Exception {
        driver.get(baseUrl);
        // add food using the "Create food" link
        driver.findElement(By.cssSelector("a[href*='/new")).click();
        // Await page to load
        Thread.sleep(500);

        WebElement formContainer = driver.findElement(By.id("formContainer"));
        // Save new food with timestamp as name
        formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
        // press Save
        driver.findElement(By.name("btn_submit")).click();

        // Await page to load
        Thread.sleep(500);
        // Retrieve created food specific delete link
        WebElement foodTable = driver.findElement(By.id("foodTable"));
        foodTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("delete_link")).click();

        // Await page to refresh
        Thread.sleep(500);

        // Check that food is not present anymore
        try {
            foodTable.findElement(By.name(String.valueOf(timestamp)));
            Assert.fail();
        } catch (Exception e) {
            // Food succesfully deleted
        }
    }
}
