package com.sourcecode.selenium;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class DragAndDrop {
   public static void main(String n[]) {
	      WebDriver wd=new ChromeDriver();
		  wd.get("https://the-internet.herokuapp.com/drag_and_drop");
		  WebElement source = wd.findElement(By.xpath("//*[@id=\"column-a\"]"));
		  WebElement destination = wd.findElement(By.xpath("//*[@id=\"column-b\"]"));
		  Actions action=new Actions(wd);
		  action.pause(Duration.ofSeconds(2));
		  action.dragAndDrop(source, destination).build().perform();
		  action.pause(Duration.ofSeconds(2));
		  try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  wd.close();
   }
   
}