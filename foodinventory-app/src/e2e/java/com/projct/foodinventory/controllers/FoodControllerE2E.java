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

public class FoodControllerE2E {

    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port + "/food";
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
    public void testCreateNewFood() {
        try {
            driver.get(baseUrl);
            // add a food using the "Create food" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));

            formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // we are redirected to the food list page the table shows the created food
            assertThat(driver.findElement(By.id("containerTable")).getText()).contains(String.valueOf(timestamp));
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testCreateNewFood();
            }
        }
    }

    @Test
    public void testEditExistingFood() {
        try {
            driver.get(baseUrl);
            // add a food using the "Create food" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));
            WebElement formContainer = driver.findElement(By.id("formContainer"));

            formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));
            // Retrieve created food specific delete link
            WebElement containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("edit_link")).click();

            // Await page to refresh
            _wait.until(d -> d.findElement(By.id("formContainer")));

            driver.findElement(By.id("formContainer")).findElement(By.name("name")).sendKeys("_edited");
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));

            // Retrieve edited food
            driver.findElement(By.id("containerTable")).findElement(By.name(timestamp + "_edited"));
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testEditExistingFood();
            }
        }
    }

    @Test
    public void testDeleteCreatedFood() {
        try {
            driver.get(baseUrl);
            // add food using the "Create food" link
            driver.findElement(By.cssSelector("a[href*='/new")).click();
            // Await page to load
            _wait.until(d -> d.findElement(By.id("formContainer")));

            WebElement formContainer = driver.findElement(By.id("formContainer"));
            // Save new food with timestamp as name
            formContainer.findElement(By.name("name")).sendKeys(String.valueOf(timestamp));
            // press Save
            driver.findElement(By.name("btn_submit")).click();

            // Await page to load
            _wait.until(d -> d.findElement(By.id("containerTable")));
            // Retrieve created food specific delete link
            WebElement containerTable = driver.findElement(By.id("containerTable"));
            containerTable.findElement(By.name(String.valueOf(timestamp))).findElement(By.name("delete_link")).click();

            // Check that food is not present anymore
            try {
                // Await page to refresh
                _wait.until(d -> d.findElement(By.name(String.valueOf(timestamp))));
                containerTable.findElement(By.name(String.valueOf(timestamp)));
                Assert.fail();
            } catch (Exception e) {
                // Food succesfully deleted
            }
        } catch (Exception exception) {
            // try again
            if (attemptCounter-- == 0) {
                this.testDeleteCreatedFood();
            }
        }

    }
}
