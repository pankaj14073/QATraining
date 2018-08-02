package classAssignments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.*;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class gmail
{
    static String username;
    static String password;
    static WebDriver driver;
    static String baseUrl;
    static String currentWindow;
    static WebDriverWait wait;
    static By emailL=By.cssSelector("input[name='identifier']");
    static By passwordL=By.cssSelector("input[name='password']");
    static By firstEmail=By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']:nth-child(1)");
    static By emailRows=By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']");
    static By emailNames=By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']>td:nth-child(5)>div:nth-child(2)>span>span[email*='.com']");
    static By emailTittles=By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']>td:nth-child(6)>div>div>div>span>span");
    public static void setBrowser(String browser,String url)
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getResourceAsStream("config.properties");
            prop.load(input);
            username=prop.getProperty("gmail.username");
            password=prop.getProperty("gmail.password");

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
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);
             driver = new ChromeDriver(options);
        }
        wait= new WebDriverWait(driver,20);

    }
    @Test
    public void testRunner() throws Exception {
        setBrowser("chrome", "http://mail.google.com/");
        doLogin(username,password);
        wait.until(ExpectedConditions.elementToBeClickable(firstEmail));
        int countEmails=countEmails();
        System.out.println("count :"+countEmails);
                                                                      /* Scanner s= new Scanner(System.in);
                                                                       int nthMail=s.nextInt();*/

        String nthSender=   getNthValue(emailNames,2);               //getNthSender(nthMail);
        String nthTittles=  getNthValue(emailTittles,2);             //getNthTittles(nthMail);
        System.out.println(nthSender+":"+nthTittles);
        driver.quit();
    }

    private String getInnerText(By locator)
    {
        return  driver.findElement(locator).getText();
    }


    private String getNthValue(By locator,int n)
    {
        String value=null;
        try {
         value= driver.findElements(locator).get(n+1).getText();
        if(value.contains("'"))
        {
            value=value.split("'")[1];
        }
        else if(value.contains(","))
        {
            value=value.split(",")[0];
        }
       }
        catch (Exception e)
        {
            System.out.println("Error in Indexing!");
        }
        return value;
    }


    private String getNthTittles(int n)
    {
        String value=getInnerText(By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']:nth-child("+n+")>td:nth-child(6)>div>div>div>span>span"));
        return value;
    }

    private String getNthSender(int n)
    {
        String value=getInnerText(By.cssSelector("table[cellpadding='0']>tbody>tr[id*=':']:nth-child("+n+")>td:nth-child(5)>div:nth-child(2)>span>span[email*='.com']"));
            if(value.contains("'"))
                {
                    value=value.split("'")[1];
                }
                else if(value.contains(","))
                {
                    value=value.split(",")[0];
                }
    return value;
    }

    private int countEmails()
    {
        return driver.findElements(emailRows).size();
    }

    private void doLogin(String email, String password)
    {
        System.out.println("LOGIN..");
        driver.get(baseUrl);
        driver.manage().window().maximize();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailL));
        driver.findElement(emailL).sendKeys(email);
        driver.findElement(emailL).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordL));
        driver.findElement(passwordL).sendKeys(password);
        driver.findElement(passwordL).sendKeys(Keys.ENTER);
    }
}
