package com.orangehrm.base;

import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v142.systeminfo.model.Size;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;


import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


public class BaseClass {
    protected static Properties prop;

    //protected  static WebDriver driver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    public SoftAssert getSoftAssert(){
        return softAssert.get();
    }

    //private static ActionDriver actionDriver;



    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load config file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.Properties file loaded");

        //start the extent report
        //ExtentManager.getReporter(); //== Imlemented in testlistner class
    }


    @BeforeMethod
    //@Parameters("browser")
    public synchronized void setup() throws IOException {
        System.out.println("Setting up diver and its configuration");
        launchBrowser();
        configBrowser();
        staticWait(2);
        logger.info("WebDriver initialized and Browser maximized");
        logger.error("Error message");
        logger.trace("Trace message");
        logger.debug("Debug message");
        logger.fatal("Fatal message");
        logger.warn("Warning Message");
        //
        /*if (actionDriver == null){
            actionDriver = new ActionDriver(driver);
            logger.info("Action driver instance is created" + Thread.currentThread().getId());
        }*/
        // Initialize action driver for current thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("Action driver initialized for thread: " + Thread.currentThread().getId());
        //ExtentManager.startTest(ExtentManager.getTestName());


    }

    private synchronized void launchBrowser() {
        // Initialize web driver based in config file

        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
            // Create ChromeOptions
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            options.addArguments("--headless=new"); // Run Chrome in headless mode
            options.addArguments("--high-dpi-support=1");
            options.addArguments("--force-device-scale-factor=1");
            options.addArguments("--screen-info={1920x1080}"); // Set window

            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
            options.addArguments("--lang=en-US");
            //options.addArguments("--user-agent=test");

            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--hide-scrollbars");


            driver.set(new ChromeDriver(options));
            //getDriver().manage().window().setSize(new Dimension(1920,1080));
            System.out.println("Window size is " + getDriver().manage().window().getSize());
            ExtentManager.registerDriver(getDriver());
            logger.info("Chromedriver instance created");
        } else if (browser.equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver();
            // Create FirefoxOptions
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless=new"); // Run Chrome in headless mode
            options.addArguments("--window-size=1960,1080"); // Set window size
            options.addArguments("--lang=en-US");
            options.addArguments("--user-agent=test");
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode

            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments


            driver.set(new FirefoxDriver(options));
            ExtentManager.registerDriver(getDriver());
            logger.info("FireDriver instance created");
        } else if (browser.equalsIgnoreCase("edge")) {
            //driver = new EdgeDriver();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            //options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

            driver.set(new EdgeDriver(options));
            ExtentManager.registerDriver(getDriver());
            logger.info("EdgeDriver instance created");
        } else {
            throw new IllegalArgumentException("Undefined Argument" + browser);
        }

    }

    private void configBrowser() {
        // Implicit wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // maximize driver
        getDriver().manage().window().maximize();

        //navigate to url
        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to open browser " + e.getMessage());
        }
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Unable to quit: " + e.getMessage());
            }
        }
        logger.info("Web driver instance is closed");
        driver.remove();
        actionDriver.remove();
        //driver = null;
//        actionDriver = null;
      //  ExtentManager.endTest();//  -- Implemented in Listener class but not working

    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("Web driver not initialized");
            throw new IllegalArgumentException("WebDriver not initialized");
        }
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("ActionDriver not initialized");
            throw new IllegalArgumentException("actionDriver not initialized");
        }
        return actionDriver.get();
    }

    public void setDriver(ThreadLocal<WebDriver> driver) {

        BaseClass.driver = driver;
    }

    public static Properties getProp() {
        return prop;
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
