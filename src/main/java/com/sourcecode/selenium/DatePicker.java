package com.sourcecode.selenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DatePicker {
  public static void main(String[] args) throws InterruptedException {
	
	ChromeOptions options = new ChromeOptions();
	options.addArguments("--incognito");
	WebDriver wd = new ChromeDriver(options);
	wd.get("https://www.globalsqa.com/demoSite/practice/datepicker/default.html");
	WebElement we=wd.findElement(By.id("datepicker"));
	new WebDriverWait(wd, Duration.ofSeconds(20))
	.until(ExpectedConditions
	.elementToBeClickable(By.id("datepicker"))).click();
	WebElement prev = wd.findElement(By.cssSelector("#ui-datepicker-div > div > a.ui-datepicker-prev.ui-corner-all"));
    while(true) {
     prev.click();
     WebElement title = wd.findElement(By.className("ui-datepicker-title"));
     if(title.getText().contains("April") && title.getText().contains("2015"))
    	 break;
	 prev=wd.findElement(By.cssSelector("#ui-datepicker-div > div > a.ui-datepicker-prev.ui-corner-all"));
  
    }
  }
}