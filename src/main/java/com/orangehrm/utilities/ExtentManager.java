package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {


    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // Initialize extent report

    public synchronized  static ExtentReports getReporter(){
        if (extent == null){
           String  reportPath = System.getProperty("user.dir") +"/src/test/resources/ExtentReporting/ExtentReport.html";
           ExtentSparkReporter spark =  new ExtentSparkReporter(reportPath);
           spark.config().setReportName("automation test report");
           spark.config().setDocumentTitle("Orange report");
           spark.config().setTheme(Theme.DARK);

           extent = new ExtentReports();
           extent.attachReporter(spark);
           // Adding some system info

            extent.setSystemInfo("Oper System", System.getProperty("os.name"));
            extent.setSystemInfo("Java version", System.getProperty("java.version"));
            extent.setSystemInfo("User name", System.getProperty("user.name"));

        }

        return extent;
    }

    //start the test

    public synchronized  static ExtentTest startTest(String testName){
        ExtentTest extentTest = getReporter().createTest(testName);
        System.out.println(extentTest);
        test.set(extentTest);
        return extentTest;
    }

    //End a test
    public  synchronized static void endTest(){
//        if(test.get()!= null){
//            test.remove();
//        }
        getReporter().flush();
    }

    //Get current tThread's test name

    public synchronized static ExtentTest getTest(){
        return test.get();
    }

    //Method to get name of the current test

    public static String getTestName(){
        ExtentTest currentTest = getTest();
        if (currentTest != null){
            return currentTest.getModel().getName();
        } else {
            return "No test is active for current thread";
        }
    }

    // Log a step

    public static void logStep(String logMessage){
        ExtentTest info = getTest();
        if(info != null){
            info.info(logMessage);
        }else{
            System.out.println("ExtentTest is null for current thread " + logMessage);
        }
    }

    //Log a step validation for API
    public static void logStepValidationForAPI(String logMessage) {
        getTest().pass(logMessage);
    }


    // Log a step valid with screenshot

    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage){
        getTest().pass(logMessage);
        attachScreenShot(driver, screenShotMessage);
    }

    // log a failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage){
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
        attachScreenShot(driver, screenShotMessage);
    }

    //Log a Failure for API
    public static void logFailureAPI(String logMessage) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
    }



    // log a skip

    public static void logSkip(String logMessage){
        String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
        getTest().skip(colorMessage);
    }

    // Method to take screenshot with date and time

    public synchronized static String takeScreenshot(WebDriver driver, String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        // format date and time for filename
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        //Saving screen shot to file
        String destPath = System.getProperty("user.dir") +"/src/test/resources/screenshot/" + screenShotName +"_" + timeStamp + ".png";
        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // convert screenshot to base64
        String base64Format = convertToBase64(src);
        return base64Format;
    }

    // Attach screen shot using base64

    public synchronized static void attachScreenShot(WebDriver driver, String message){
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot"+ message);
            throw new RuntimeException(e);
        }
    }

    // convert screenshot to base64 format

    public static String convertToBase64(File screenShotFile) throws IOException {

        String base64Format = "";
        // Read the file content in to byte array
        byte[] fileContents = FileUtils.readFileToByteArray(screenShotFile);
        //convert byte array to base 64
        base64Format = Base64.getEncoder().encodeToString(fileContents);

        return base64Format;

    }


//Register webDriver for current thread

    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(), driver);
    }

}
