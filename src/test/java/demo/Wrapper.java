package demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant; 

public class Wrapper {

    static WebDriver driver;
    static String URL= "https://www.scrapethissite.com/pages/";

    public Wrapper()
    {
        System.out.println("Constructor: TestCases");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public static void goTo()
    {
        if(!driver.getCurrentUrl().equals(URL))
        {
            driver.get(URL);
        }
    }

    public static void click(By locator)
    {
        driver.findElement(locator).click();
    }

    public static void waitForElement(By locator)
    {
        WebDriverWait w= new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //find the teams and print the names, year of win and percentage win of the teams where win % is less than 40%
    public static void findTeams(By locator)
    {
        List<WebElement> rows= driver.findElements(locator);
        for(WebElement e: rows)
        {
            String teamName=e.findElement(By.xpath("./td[1]")).getText();
            String year= e.findElement(By.xpath("./td[3]")).getText();
            String winPercent=e.findElement(By.xpath("./td[6]")).getText();

            float winPer=Float.valueOf(winPercent);

            if(winPer<0.40)
            {
                System.out.println("Team Name: "+teamName+" year: "+year+" Win Percent of the team: "+winPer);
            }
        }
    } 
    
    //generate the hockey team data and store it into json doc.
    public static File convertToHashMapAndStoreInJSON(By locator) throws InterruptedException, IOException
    {
        ArrayList<HashMap<String, String>> list= new ArrayList<>();
        for(int i=2;i<5;i++)
        {
            List<WebElement> rows= driver.findElements(locator); //25
            for(WebElement row: rows) //1/25
            {
                long epoach= Instant.now().getEpochSecond();
                String epoach1= Long.toString(epoach);
                HashMap<String, String> map= new HashMap<>();
                List<WebElement> names= row.findElements(By.xpath("./td"));  //9
               

                    map.put("Team Name: ", names.get(0).getText());
                    map.put("Year: ", names.get(1).getText());
                  //  map.put("Win: ", names.get(2).getText());
                   // map.put("Looses: ", names.get(3).getText());
                   // map.put("OT Losses: ", names.get(4).getText());
                    map.put("Win %: ", names.get(5).getText());
                   // map.put("Goals For (GF): ", names.get(6).getText());
                   // map.put("Goals Against (GA): ", names.get(7).getText());
                   // map.put("+/-: ", names.get(8).getText());
                    map.put("Epoach Time: ", epoach1);

                    list.add(map);
                    
            }
                WebElement page=driver.findElement(By.xpath("//a[normalize-space()='"+i+"']"));
                page.click();
                Thread.sleep(5000);       
        }
            // for (HashMap<String, String> row : list) 
            // {
            //     System.out.println(row);
            // }
            ObjectMapper mapper = new ObjectMapper();
            String employeePrettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
            System.out.println(employeePrettyJson);
            String userDir = System.getProperty("user.dir");
            File f= new File(userDir+"//src//test//java//resources//hockey-team-data.json");
            mapper.writerWithDefaultPrettyPrinter()
            .writeValue(f,list);
           return f;
    }

    ////generate movies data according to year and store it into json doc.
    public static File findTop5AndconvertToJson() throws JsonProcessingException
    {
        ArrayList<HashMap<String, String>> list= new ArrayList<>();
        
        for(int i=2015;i>=2010;i--)
        {
            WebElement clickEle=driver.findElement(By.xpath("//a[text()='"+i+"']"));
            String movieYearText=clickEle.getText();
            clickEle.click();

            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
            w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));

            List<WebElement> rows= driver.findElements(By.xpath("//tbody/tr"));
            for(int j=0;j<5;j++)
            {
                long epoach= Instant.now().getEpochSecond();
                String epoach1= Long.toString(epoach);
                    Boolean isWinner=false;
                    if(driver.findElement(By.xpath("//tbody/tr/td[4]/i/../preceding-sibling::td[3]")).isDisplayed())
                    {
                        isWinner=true;
                        Assert.assertTrue(isWinner);
                    }

                    HashMap<String, String> map= new HashMap<>();
                    List<WebElement> movietilesEle= rows.get(j).findElements(By.xpath("./td"));

                    map.put("Movie Name: ",movietilesEle.get(0).getText());
                    map.put("Nominations: ", movietilesEle.get(1).getText());
                    map.put("Awards: ", movietilesEle.get(2).getText());
                    map.put("Movie Year: ", movieYearText);
                    map.put("Epoach Time: ", epoach1);
                    list.add(map);
            
            }

        }
     
        // for (HashMap<String, String> row : list) 
        //     {
        //         System.out.println(row);
        //     }

            ObjectMapper mapper = new ObjectMapper();
            String employeePrettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
            System.out.println(employeePrettyJson);
            String userDir = System.getProperty("user.dir");
            File f= new File(userDir+"//src//test//java//resources//oscar-winner-data.json");
            try {
                mapper.writerWithDefaultPrettyPrinter()
                .writeValue(f,list);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           return f;
    }

}
