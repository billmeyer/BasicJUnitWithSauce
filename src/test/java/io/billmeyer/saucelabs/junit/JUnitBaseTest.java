package io.billmeyer.saucelabs.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.junit.Assert.fail;

public abstract class JUnitBaseTest
{
    protected RemoteWebDriver driver;
    protected String baseUrl;
    protected StringBuffer verificationErrors = new StringBuffer();

    protected String userName = System.getenv("SAUCE_USERNAME");
    protected String accessKey = System.getenv("SAUCE_ACCESS_KEY");

    @Before
    public void setUp() throws Exception
    {
        baseUrl = "https://www.google.com/";
    }

    @After
    public void tearDown() throws Exception
    {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString))
        {
            fail(verificationErrorString);
        }
    }

    @Test
    public void testFirefox() throws Exception
    {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("version", "57.0");

        runTest(caps);
    }

    @Test
    public void testChrome() throws Exception
    {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("version", "62.0");
        caps.setCapability("chromedriverVersion", "2.33");

        runTest(caps);
    }

    protected abstract void runTest(DesiredCapabilities caps) throws Exception;
}
