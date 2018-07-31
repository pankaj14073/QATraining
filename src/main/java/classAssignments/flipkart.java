package classAssignments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

public class flipkart
{
static WebDriver driver;
static String baseUrl;
static String currentWindow;
static WebDriverWait wait;

static By emailL=By.cssSelector("form>div:first-child >input[type='text']");
static By passwordL=By.cssSelector("form>div >input[type='password']");
static By searchBoxL=By.cssSelector("input[name='q']");
static By sortToHighLowPriceL= By.cssSelector("div[class*='col-10-12']>div:first-child>div>div:nth-of-type(2)>div:nth-of-type(4)");
static By firstElementL=By.cssSelector("div[class*='col-10-12']:nth-of-type(2)>div:nth-of-type(2)>div>div:first-child>div>a:first-child>div:first-child");
static By priceL=By.cssSelector("div[id='container']>div>div:nth-of-type(1)>div>div>div:first-child>div>div:nth-child(2)>div:nth-child(2)>div:nth-child(3)>div:first-child>div>div:first-child");
static By cartButtonL=By.cssSelector("li[class='col col-6-12']:nth-of-type(1)");

    public static void setBrowser(String browser,String url)
    {

        baseUrl=url;
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
        wait= new WebDriverWait(driver,20);

    }


    @Test
    public void testRunner() throws Exception {

        setBrowser("chrome","https://flipkart.com");
        doLogin("anuragi.pankaj@gmail.com","9968285308");
        searchItem("earphones");
        chooseFav();
        switchWindow();
        String expectedPrice=addToKart();
        String actualPrice=getInnerText("//div[@style='left: auto;']/div/div/div/div[3]/div/div[2]");
        Assert.assertEquals(expectedPrice,actualPrice);
    }

    private String getInnerText(String locator)
    {
        return driver.findElement(By.xpath(locator)).getText();
    }

    private String addToKart()
    {
        System.out.println("ADDING TO CART..");
        wait.until(ExpectedConditions.presenceOfElementLocated(priceL));
        String amount=driver.findElement(priceL).getText();
        driver.findElement(cartButtonL).click();
        return amount;
    }

    private void switchWindow()
    {
        System.out.println("WINDOW SWITCHING..");

        currentWindow=driver.getWindowHandle();
        Set<String> windows= driver.getWindowHandles();
        for(String window : windows)
        {
            if(!window.equals(currentWindow))
            {
                driver.switchTo().window(window);
                break;
            }
        }

    }

    private void chooseFav()
    {
        System.out.println("CHOOSING FAV..");

        //sorting and choosing the highest
        wait.until(ExpectedConditions.elementToBeClickable(sortToHighLowPriceL));
        driver.findElement(sortToHighLowPriceL).click();
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(firstElementL)));
        driver.findElement(firstElementL).click();

    }

    private void searchItem(String earphones)
    {
        System.out.println("SEARCHING FOR ITEM..");
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(searchBoxL)));
        driver.findElement(searchBoxL).sendKeys(earphones);
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxL));
        driver.findElement(searchBoxL).sendKeys(Keys.ENTER);
    }

    private void doLogin(String email, String password)
    {
        System.out.println("LOGIN..");

        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(emailL));
        driver.findElement(emailL).sendKeys(email);
        driver.findElement(passwordL).sendKeys(password);
        driver.findElement(passwordL).sendKeys(Keys.ENTER);
    }
}
