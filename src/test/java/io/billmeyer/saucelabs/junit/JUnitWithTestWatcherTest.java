package io.billmeyer.saucelabs.junit;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JUnitWithTestWatcherTest extends JUnitBaseTest implements SauceOnDemandSessionIdProvider
{
    private String sessionId;

    public String getSessionId()
    {
        return sessionId;
    }

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, userName, accessKey, true);

    protected void runTest(DesiredCapabilities caps) throws MalformedURLException
    {
        caps.setCapability("platform", "macOS 10.12");
        caps.setCapability("seleniumVersion", "3.7.1");
        caps.setCapability("name", String.format("%s - %s [%s]",
                this.getClass().getSimpleName(), caps.getBrowserName(), new Date()));

        URL url = new URL("https://" + userName + ":" + accessKey + "@ondemand.saucelabs.com:443/wd/hub");

        driver = new RemoteWebDriver(url, caps);
        sessionId = driver.getSessionId().toString();

        System.out.printf("Test:       %s\n", caps.getCapability("name"));
        System.out.printf("Session ID: %s\n", sessionId);

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        try
        {
            driver.get(baseUrl);
            driver.findElement(By.id("lst-ib")).clear();
            driver.findElement(By.id("lst-ib")).sendKeys("Sauce Labs");

            WebElement element = driver.findElement(By.name("btnK"));

            // The following does not play well with the gecko driver (Firefox) so we use a JavascriptExecutor instead
//            Actions actions = new Actions(driver);
//            actions.moveToElement(element).click().perform();

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        }
        catch (WebDriverException e)
        {
            e.printStackTrace();
        }

        driver.quit();
    }
}