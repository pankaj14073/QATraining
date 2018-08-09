package classAssignments;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class wordMeaning {

    static WebDriver driver;
    static WebDriverWait wait;
    static  FileInputStream inputStream ;
    static Workbook workbook;
    static Sheet sheet;
    static String FILE_NAME="/home/pankaj/Desktop/QATraining/sheet.xlsx";
    static ArrayList<String> result;

    @BeforeClass
    public void setBrowser() {

        System.setProperty("webdriver.chrome.driver", "/home/pankaj/Desktop/jars/drivers/chromedriver");
        driver = new ChromeDriver();
        result=new ArrayList<String>();
        wait = new WebDriverWait(driver, 20);
        try {
            inputStream = new FileInputStream(new File(FILE_NAME));
            workbook = WorkbookFactory.create(inputStream);
             sheet= workbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(dataProvider="wordData")
    public void testRunner(String word, String type, String rowN)
    {

        driver.get("https://en.oxforddictionaries.com/");
        String wordMeaning= getMeaning(word,type);
        sheet.getRow(Integer.parseInt(rowN)+1).createCell(2).setCellValue(wordMeaning);
        sheet.getRow(Integer.parseInt(rowN)+1).createCell(3).setCellValue(result.get(Integer.parseInt(rowN)));

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(false);
        sheet.getRow(Integer.parseInt(rowN)+1).setRowStyle(style);
        sheet.getRow(Integer.parseInt(rowN)+1).getCell(2).setCellStyle(style);

        System.out.println(word+" : "+ type+" : "+sheet.getRow(Integer.parseInt(rowN)).getCell(2));

    }

    @DataProvider(name="wordData")
    public Object[][] printExcel()
    {
        System.out.println("DATA PROVIDER CALL...");
        try {

            int RowNum =getRowCount(sheet);
            System.out.println(RowNum+":"+sheet.getLastRowNum());

            String[][] data = new String[RowNum][];
            int rowN=0;
            Iterator<Row> rowIterator=sheet.rowIterator(); rowIterator.next();
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                if(row.getCell(0)==null)
                    break;

                data[rowN]=new String[3];
                data[rowN][0]= String.valueOf(row.getCell(0));
                data[rowN][1] =String.valueOf(row.getCell(1));
                data[rowN][2]=String.valueOf(rowN);

                rowN++;
            }


        return data;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getRowCount(Sheet sheet)
    {
        int n=0;
        Iterator<Row> rowIterator=sheet.rowIterator(); rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(0) == null)
                break;
            n++;
        }
        return n;
    }

    private String getMeaning(String word, String givenType)
    {
        By search= By.id("query");
        wait.until(ExpectedConditions.visibilityOfElementLocated(search));
        driver.findElement(search).sendKeys(word);
        driver.findElement(search).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("header>h2")));
        List<WebElement> l= driver.findElements(By.xpath("//section[@class='gramb']/h3"));
        WebElement parent = null;
        String r="FAIL";
        String meaning="";
        for(WebElement e : l)
        {
            if(e.getText().equals(givenType))
            {
                r="PASS";
                parent=e;
                break;
            }
        }
        result.add(r);
        if(r.equals("PASS"))
        {

            l=parent.findElements(By.xpath("./../ul/li"));
            System.out.println("TOTAL (li) : "+parent.getText()+" "+l.size());
            for(WebElement e : l)
            {
                meaning=meaning+e.findElement(By.xpath("./div/p[1]/span[@class='ind']")).getText()+"\n";

                System.out.println(e.findElement(By.xpath("./div/p[1]/span[@class='ind']")).getText());
                List<WebElement> ol = e.findElements(By.xpath("./div/ol/li/span[@class='ind']"));
                for (WebElement olli : ol)
                {
                    meaning=meaning+olli.getText()+"\n";
                    System.out.println("            "+olli.getText());
                }
            }
        }

        return meaning;
    }

    @AfterClass
    public void closeEverything()
        {
            try
            {
                inputStream.close();
                FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

}


