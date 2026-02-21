package com.orangehrm.listeners;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
       annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    // Trigger when a suite starts
    @Override
    public void onStart(ITestContext context) {
        //Initialize the Extent Report
        ExtentManager.getReporter();
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the extent report
        ExtentManager.endTest();
    }

    @Override
    public void onTestStart(ITestResult result) {
        //ITestListener.super.onTestStart(result);
        // Trigger when test starts
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test Started" + testName);
    }


    // Trigger when Test is success
    @Override
    public void onTestSuccess(ITestResult result) {
        //ITestListener.super.onTestSuccess(result);

        String testName = result.getMethod().getMethodName();
        if (!result.getTestClass().getName().toLowerCase().contains("api")) {
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passes successfully", "Test End: " + testName + "Test Passed");
        } else {
            ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
        }
    }

    // Trigger when Test Fails
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage);
        if (!result.getTestClass().getName().toLowerCase().contains("api")) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed", "Test End: " + testName + " - ❌ Test Failed");
        } else {
            ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
        }
    }

    //Trigger when Test Skip
    @Override
    public void onTestSkipped(ITestResult result) {
        //ITestListener.super.onTestSkipped(result);
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test is skipped" + testName);
    }


}
