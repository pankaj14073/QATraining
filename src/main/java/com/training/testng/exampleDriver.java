package com.training.testng;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class exampleDriver
{
    static WebDriver driver;
    public static void setBrowser(String browser)
    {

        if(browser.equals("firefox"))
        {
            System.setProperty("webdriver.firefox.marionette","/home/pankaj/Desktop/jars/drivers/geckodriver");
            driver=new FirefoxDriver();
        }
        else
        {
            System.setProperty("webdriver.chrome.driver","/home/pankaj/Desktop/jars/drivers/chromedriver");
            driver= new ChromeDriver();
        }
    }



    public boolean clickSuggestions(String subString)
    {
        String url="https://www.google.com";
        driver.get(url);
        WebElement search= driver.findElement(By.name("q"));
        search.sendKeys(subString);
        By suggestion= By.cssSelector("li[role=presentation]:nth-child(4)");
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(suggestion));
            System.out.println("DATA :"+driver.findElement(suggestion).getText());
            driver.quit();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    public boolean verifyUrl()
    {
        String expectedUrl="https://www.google.com/";
        driver.get(expectedUrl);
        String url=driver.getCurrentUrl();
        driver.quit();
        if(expectedUrl.equals(url))
            return true;
        else
            return  false;
    }

    @Test
    public void testRunner() throws Exception {

        setBrowser("chrome");
        Boolean result= verifyUrl();
        Assert.assertTrue(result);

    }

}
