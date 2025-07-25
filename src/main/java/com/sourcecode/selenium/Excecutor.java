package com.sourcecode.selenium;

import java.util.Hashtable;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class Excecutor {
   public static void main(String[] args) throws Exception {
	   Map<String,Object> preferences =new Hashtable<String,Object>();  
	   preferences.put("download.prompt_for_download", false);
	   preferences.put("download.default_directory", "d:\\books");
	   
	   ChromeOptions options = new ChromeOptions();
	   options.setExperimentalOption("prefs", preferences);	
	  
	   
	   WebDriver wd=new ChromeDriver(options);
	   
	   wd.get("https://ncert.nic.in/textbook.php");
	   
	  Select select1 =new Select(wd.findElement(By.name("tclass")));
	  Select select2 =new Select(wd.findElement(By.name("tsubject")));
	  Select select3 =new Select(wd.findElement(By.name("tbook")));
	  
	  select1.selectByIndex(10);
	  select2.selectByIndex(5);
	  select3.selectByIndex(1);
	  //Thread.sleep(5000);
	  wd.findElement(By.name("button")).click(); 
	  
	  WebElement web = wd.findElement(By.linkText("Download complete book"));
	  
	  JavascriptExecutor js=(JavascriptExecutor)wd;
	  js.executeScript("arguments[0].click()", web);
   }
}