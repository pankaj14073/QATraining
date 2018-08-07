package classAssignments;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

public class shutterstock {

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
    public void verify1upload()
    {
        driver.get("https://www.shutterstock.com/");
        uploadFile("/home/pankaj/Desktop/you.jpg");
    }


    @Test
    public void verify2ImageId()
    {
        int random=chooseRandom(By.cssSelector(".li.js_item.mosaiced"));
        String[] tmp=driver.findElement(By.cssSelector(".li.js_item.mosaiced:nth-child("+random+")>div>img")).getAttribute("src").split("-");
        String id=tmp[tmp.length-1].split(".j")[0];
        driver.findElement(By.cssSelector(".li.js_item.mosaiced:nth-child("+random+")>div>img")).click();
        tmp=driver.findElement(By.cssSelector(".product-meta.image.col-xs-12>p")).getText().split(": ");
        String actualId=tmp[tmp.length-1];
        Assert.assertEquals(id,actualId);
        System.out.println("3.verified ID:"+id);
    }

    @Test
    public void verify3SaveDownoadShare()
    {
        driver.findElement(By.cssSelector(".a.js_lightbox_add")).click();
        By saveL=By.cssSelector("#lightbox-body>div>h3");
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveL));
        String save= driver.findElement(saveL).getText();
        Assert.assertEquals(save,"Save to Collection");
        System.out.println("4.verified Save popup");

        driver.findElement(By.cssSelector("div[class^='lightbox']>div>button[class='close']")).click();
        driver.findElements(By.cssSelector("a[class=a]")).get(0).click();
        String  signup=driver.findElement(By.cssSelector("#iframe-container>div>div>form:nth-child(2)>legend")).getText();
        Assert.assertEquals(signup,"Create your Free Account");
        System.out.println("5.verified signIn popup ");

        driver.findElement(By.cssSelector("button[data-track='click.signUp.close']")).click();
        driver.findElements(By.cssSelector("a[class=a]")).get(1).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#share-padded-modal-title")));
        String share=driver.findElement(By.cssSelector("#share-padded-modal-title")).getText();
        Assert.assertEquals(share,"Share this image");
        driver.findElement(By.cssSelector("#share_modal>div>div>div>button")).click();
        System.out.println("6.verified Share popup");

    }
    private void uploadFile(String path)
    {
        wait.until(ExpectedConditions.visibilityOf(driver.findElements(By.id("reverse-image-camera")).get(1)));
        driver.findElements(By.id("reverse-image-camera")).get(1).click();
        driver.findElement(By.cssSelector("input[type=file]")).sendKeys(path);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".search-clarification.text-left>b>h1")));
        String resultText=driver.findElement(By.cssSelector(".search-clarification.text-left>b>h1")).getText();
        Assert.assertEquals(resultText,"Search by image");
        System.out.println("1.verified "+resultText);
        boolean result= driver.findElement(By.cssSelector(".li.js_item.mosaiced:first-child>div>img")).getAttribute("src").length()!=0;
        Assert.assertTrue(result);
        System.out.println("2.verified Result imaged");

    }
    private int chooseRandom(By selector)
    {

        int max=driver.findElements(selector).size();
        Random random = new Random();
        int randomNumber = random.nextInt(max) + 1;

        return randomNumber;
    }

}
