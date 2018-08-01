package classAssignments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;


public class flipkart
{


static String username;
static String password;
static WebDriver driver;
static String baseUrl;
static String currentWindow;
static WebDriverWait wait;

static By emailL=By.cssSelector("form>div:first-child >input[type='text']");
static By passwordL=By.cssSelector("form>div >input[type='password']");
static By searchBoxL=By.cssSelector("input[name='q']");
static By sortToLowHighPriceL= By.cssSelector("div[class*='col-10-12']>div:first-child>div>div:nth-of-type(2)>div:nth-of-type(3)");
static By firstElementL=By.cssSelector("div[class*='col-10-12']:nth-of-type(2)>div:nth-of-type(2)>div>div:first-child>div>a:first-child>div:first-child");
static By priceL=By.cssSelector("div[class*='col-10-12']:nth-of-type(2)>div:nth-of-type(2)>div>div:first-child>div>a:nth-of-type(3)>div>div:first-child");
static By cartButtonL=By.cssSelector("div>ul>li:nth-child(1)>button");
static By cartPriceL=By.xpath("//div[@style='left: auto;']/div/div/div/div[1]/div[2]");

    public static void setBrowser(String browser,String url)
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getResourceAsStream("config.properties");
            prop.load(input);
            username=prop.getProperty("flipkart.username");
            password=prop.getProperty("flipkart.password");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

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
        doLogin(username,password);
        searchItem("earphones");
        String screenPrice=chooseFav();
        switchWindow();
        addToKart();
        String cartPrice=getInnerText(cartPriceL);

        System.out.println(screenPrice+":"+cartPrice);
        Assert.assertEquals(screenPrice,cartPrice);

        driver.quit();
    }

    private String getInnerText(By locator)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    private void addToKart()
    {
        System.out.println("ADDING TO CART..");
        wait.until(ExpectedConditions.elementToBeClickable(cartButtonL));
        driver.findElement(cartButtonL).click();
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

    private String chooseFav()
    {
        System.out.println("CHOOSING FAV..");

        //sorting and choosing the highest
        wait.until(ExpectedConditions.elementToBeClickable(sortToLowHighPriceL));
        driver.findElement(sortToLowHighPriceL).click();
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(firstElementL)));
        String price=driver.findElement(priceL).getText();
        driver.findElement(firstElementL).click();
        return price;

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
        driver.manage().window().maximize();
        wait.until(ExpectedConditions.presenceOfElementLocated(emailL));
        driver.findElement(emailL).sendKeys(email);
        driver.findElement(passwordL).sendKeys(password);
        driver.findElement(passwordL).sendKeys(Keys.ENTER);
    }
}
