package com.sourcecode.selenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


public class SeleniumDemo {
   public static void main(String[] args) {
	   //System.setProperty("webdriver.chrome.driver", "E:/selenium/chromedriver.exe");
       WebDriver wd=new ChromeDriver();
       wd.get("http://www.google.co.in");
       System.out.println(wd.getCurrentUrl());
       System.out.println(wd.getTitle());
       System.out.println("######################################################################################");
       //System.out.println(wd.getPageSource());
       wd.navigate().to("http://www.bing.com");
       WebElement searchBox = wd.findElement(By.name("q"));

       // Type search query
       searchBox.sendKeys("Selenium Java tutorial");

       // Submit the form
       searchBox.submit();
      
       wd.navigate().back();
       wd.navigate().to("http://www.amazon.in");
       try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       wd.close();
   }
       
 }