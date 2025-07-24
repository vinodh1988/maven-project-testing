package com.testcode;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WikipediaSearchTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @Test
    public void C23_testSearchSeleniumOnWikipedia() {
       
		  driver.get("https://the-internet.herokuapp.com/drag_and_drop");
		  WebElement source = driver.findElement(By.xpath("//*[@id=\"column-a\"]"));
		  WebElement destination = driver.findElement(By.xpath("//*[@id=\"column-b\"]"));
		  Actions action=new Actions(driver);
		  action.pause(Duration.ofSeconds(2));
		  action.dragAndDrop(source, destination).build().perform();
		  action.pause(Duration.ofSeconds(2));
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
