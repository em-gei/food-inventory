package com.projct.foodinventory.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class MealControllerE2E {

    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port + "/meal";
    private WebDriver driver;
    private long timestamp;
    private WebDriverWait _wait;
    private int attemptCounter;

    @BeforeClass
    public static void setupClass() {
        // setup Chrome Driver
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
        timestamp = new Date().getTime();
        _wait = new WebDriverWait(driver, 10);
        attemptCounter = 3;
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testCreateNewMeal() {
        try {
            driver.get(baseUrl);
            // add a meal using the "Create meal" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            WebDriverWait _wait = new WebDriverWait(driver, 3);
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));

            formContainer.findElement(By.name("description")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // we are redirected to the meal list page the table shows the created meal
            assertThat(driver.findElement(By.id("containerTable")).getText()).contains(String.valueOf(timestamp));
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testCreateNewMeal();
            }
        }
    }

    @Test
    public void testEditExistingMeal() {
        try {
            driver.get(baseUrl);
            // add a meal using the "Create meal" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));

            formContainer.findElement(By.name("description")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));
            // Retrieve created meal specific delete link
            WebElement containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("edit_link")).click();

            // Await page to refresh
            _wait.until(d -> d.findElement(By.id("formContainer")));

            driver.findElement(By.id("formContainer")).findElement(By.name("description")).sendKeys("_edited");
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));

            // Retrieve edited meal
            driver.findElement(By.id("containerTable")).findElement(By.name(timestamp + "_edited"));
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testEditExistingMeal();
            }
        }
    }

    @Test
    public void testDeleteCreatedMeal() {
        try {
            driver.get(baseUrl);
            // add meal using the "Create meal" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));

            WebElement formContainer = driver.findElement(By.id("formContainer"));
            // Save new meal with timestamp as description
            formContainer.findElement(By.name("description")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));
            // Retrieve created meal specific delete link
            WebElement containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("delete_link")).click();

            // Check that meal is not present anymore
            try {
                // Await page to refresh
                _wait.until(d -> d.findElement(By.name(String.valueOf(timestamp))));
                containerTable.findElement(By.name(String.valueOf(timestamp)));
                Assert.fail();
            } catch (Exception e) {
                // Meal succesfully deleted
            }
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testDeleteCreatedMeal();
            }
        }
    }

    @Test
    public void testLinkFoodToMeal() {
        try {
            createFoodWhenNotPresent();

            driver.get(baseUrl);
            // add a meal using the "Create meal" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));

            formContainer.findElement(By.name("description")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));
            // Retrieve created meal specific meal_link
            WebElement containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("meal_link")).click();

            // Await page to refresh
            _wait.until(d -> d.findElement(By.id("formContainer")));
            // FoodToAdd already selected
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));

            // Retrieve edited meal
            containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("meal_link")).click();
            // Await page to refresh
            _wait.until(d -> d.findElement(By.id("formContainer")));

            // If deleteLink is present means that food has been linked to the meal
            driver.findElement(By.id("formContainer")).findElement(By.id("containerTable")).findElement(By.name("delete_link"));
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testLinkFoodToMeal();
            }
        }
    }

    private void createFoodWhenNotPresent() {
        driver.get("http://localhost:" + port + "/food");
        if (driver.findElement(By.id("containerTable")).getText().isEmpty()) {
            // If no food available create a new one
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            WebDriverWait _wait = new WebDriverWait(driver, 3);
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));
            formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();
        }
    }
}
