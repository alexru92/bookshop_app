package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MainPageSeleniumTests {

    private static ChromeDriver driver;

    @BeforeAll
    static void setup(){
        System.setProperty("webdriver.chrome.driver","C:\\Users\\atkachen\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown(){
        driver.quit();
    }

    @Test
    public void testMainPageAccess() throws InterruptedException {
        driver.get("http://localhost:8085/");
        Thread.sleep(2000);

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    public void testMainPageSearchByQuery() throws InterruptedException {
        driver.get("http://localhost:8085/");
        Thread.sleep(2000);

        WebElement element1 = driver.findElement(By.id("query"));
        element1.sendKeys("Just");
        Thread.sleep(2000);

        WebElement element2 = driver.findElement(By.id("search"));
        element2.submit();
        Thread.sleep(2000);

        assertTrue(driver.getPageSource().contains("Just a random String"));
    }
}
