package com.sourcecode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GoogleSearchTest {
    public static void main(String[] args) {
        // Optional: Set path to chromedriver if not in system PATH
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        // Go to Google
        driver.get("https://bing.com");

        // Find the search box
        WebElement searchBox = driver.findElement(By.name("q"));

        // Type search query
        searchBox.sendKeys("Selenium Java tutorial");

        // Submit the form
        searchBox.submit();

        // Wait a bit to see results
        try {
            Thread.sleep(15000);  // 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close browser
        driver.quit();
    }
}
