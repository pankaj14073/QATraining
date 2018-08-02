package classAssignments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;


public class javaAPI
{


    static WebDriver driver;
    static WebDriverWait wait;


    @BeforeClass
    public void setBrowser()
    {

        System.setProperty("webdriver.chrome.driver","/home/pankaj/Desktop/jars/drivers/chromedriver");
        driver= new ChromeDriver();
        wait= new WebDriverWait(driver,20);

    }


    @Test
    public void verify1PackageName() throws Exception
    {
        driver.get("http://seleniumhq.github.io/selenium/docs/api/java/index.html");

        switchToFrame("packageListFrame");
        int n= chooseRandom(By.cssSelector("ul[title='Packages']>li"));
        String packageName= driver.findElement(By.cssSelector("ul>li:nth-of-type("+n+")>a")).getText();
        driver.findElement(By.cssSelector("ul>li:nth-of-type("+n+")>a")).click();

        driver.switchTo().defaultContent();
        switchToFrame("packageFrame");


        String displayedPacageName=driver.findElement(By.tagName("h1")).getText();
        System.out.println("1.Verify package Name :   {"+packageName +" : "+displayedPacageName+"}");
        Assert.assertEquals(packageName,displayedPacageName);
    }

    @Test
    public void verify2ClassName()
    {
        int n= chooseRandom(By.cssSelector("ul[title='Classes']>li"));
        String className=driver.findElement(By.cssSelector("ul[title='Classes']>li:nth-child("+n+")>a")).getText();
        driver.findElement(By.cssSelector("ul[title='Classes']>li:nth-child("+n+")>a")).click();
        driver.switchTo().defaultContent();

        switchToFrame("classFrame");

        String displayedClassName=driver.findElement(By.tagName("h2")).getText().split(" ")[1];

        System.out.println("2.Verify class Name :   {"+className+"  :  "+displayedClassName+"}");
        Assert.assertTrue   (displayedClassName.contains(className));
    }

    @Test
    public void verify3ClassDetails()
    {
        List<WebElement> fieldsE=driver.findElements(By.xpath("//a[@name='field.summary']/following-sibling::table/tbody/tr/td[2]/code/span/a"));
        int i=0;
        System.out.println("Field-Names :"+fieldsE.size());
        for(WebElement e : fieldsE)
        {
            i++;
            System.out.println(i+". "+ e.getText());
        }


        List<WebElement> constructorE=driver.findElements(By.xpath("//a[@name='constructor.summary']/following-sibling::table/tbody/tr/td/code/span"));
         i=0;
        System.out.println("Constructor-Names :"+constructorE.size());
        for(WebElement e : constructorE)
        {
            i++;
            System.out.println(i+". "+ e.getText()+"()");
        }


        List<WebElement> methodE=driver.findElements(By.xpath("//a[@name='method.summary']/following-sibling::table/tbody/tr/td[2]/code/span"));
        i=0;
        System.out.println("Method-Names :"+methodE.size());
        for(WebElement e : methodE)
        {
            i++;
            System.out.println(i+". "+ e.getText()+"()");
        }


    }

    private void switchToFrame(String frameName)
    {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(frameName)));
        driver.switchTo().frame(frameName);
    }


    private int chooseRandom(By selector)
    {

        int max=driver.findElements(selector).size();
        Random random = new Random();
        int randomNumber = random.nextInt(max) + 1;

        return randomNumber;
    }

    @AfterClass
    public void quitDriver()
    {
        System.out.println("CLOSING Driver");
        //driver.quit();
    }
}
