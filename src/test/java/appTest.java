import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class appTest {

    private static WebDriver driver;
    private static final String LINK_TRAIN = "//span[contains(@class,'chNavIcon appendBottom2 chSprite chTrains')]";
    private static final String CHECK_PMR_STATUS_CHECKBOX = "//span[contains(text(),'CHECK PNR STATUS')]" ;
    private static final String INPUT_PNR = "//input[@id='pnr']" ;
    private static final String BUTTON_CHECK_STATUS = "//a[text()='CHECK STATUS']" ;
    private static final String CHECK_PNR_TEXT= "//p[contains(@class, 'appendBottom')]" ;

    private static final String PNR_TEXT = "0123456789" ;

    private static ChromeDriverService service;
    public static WebDriverWait wait;
    @BeforeTest
    public static void before(){
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(System.getenv("webdriver.chrome.driver")))
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, SECONDS);
        driver.get("https://www.makemytrip.com/");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    public void test_app() throws InterruptedException {

        driver.findElement(By.xpath(LINK_TRAIN)).click();
        driver.findElement(By.xpath(CHECK_PMR_STATUS_CHECKBOX)).click();
        driver.findElement(By.xpath(INPUT_PNR)).sendKeys(PNR_TEXT);
        Thread.sleep(5000);
        driver.findElement(By.xpath(BUTTON_CHECK_STATUS)).click();

        //Check
        Thread.sleep(10000);
        String actualnumber  = driver.findElements(By.xpath(CHECK_PNR_TEXT)).get(0).getText().replace("PNR ","");
        Assert.assertEquals(actualnumber, PNR_TEXT);
    }

    @AfterTest
    public static void after(){
        driver.quit();
    }
}
