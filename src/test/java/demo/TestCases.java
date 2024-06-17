package demo;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestCases extends Wrapper {
    public TestCases()
    {
        super();
    }
    @AfterClass
    public void endTest()
    {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }

    @Test(priority = 1, description = "Iterate through the table and collect the Team Name, Year and Win % for the teams with Win % less than 40% (0.40)")
    public  void testCase01(){
        System.out.println("Start Test case: testCase01");
        Wrapper.goTo();
        Wrapper.click(By.xpath("//a[contains(text(),'Hockey Teams')]"));
        Wrapper.waitForElement(By.xpath("//h1[contains(text(),'Hockey Teams')]"));
        Wrapper.findTeams(By.xpath("//tbody/tr[@class='team']"));
        System.out.println("end Test case: testCase01");
    }

    @Test(priority = 2, description = "Iterate through 4 pages of this data and store it in a ArrayList<HashMap> and store in JSon file")
    public  void testCase02() throws InterruptedException, IOException{
        System.out.println("Start Test case: testCase02");
        File f=Wrapper.convertToHashMapAndStoreInJSON(By.xpath("//tbody/tr[@class='team']"));
        Assert.assertTrue(f.exists(), "File does not exist");
        Assert.assertNotEquals(0, f.length(), "File is empty");
        System.out.println("end Test case: testCase02");
    }

    @Test(priority = 3, description="Click on each year present on the screen and find the top 5 movies on the list - store in an ArrayList<HashMap>.")
    public void testCase03() throws InterruptedException, JsonProcessingException
    {
        System.out.println("Start Test case: testCase03");
        Wrapper.goTo();
        Wrapper.click(By.xpath("//a[contains(text(), 'Oscar Winning Films')]"));
        Wrapper.waitForElement(By.xpath("//h1[contains(text(), 'Oscar Winning Films')]"));
        Thread.sleep(2000);
        //Wrapper.findTop5();
        File f=Wrapper.findTop5AndconvertToJson();
        Assert.assertTrue(f.exists(), "File does not exist");
        Assert.assertNotEquals(0, f.length(), "File is empty");
        System.out.println("end Test case: testCase03");

    }



}
