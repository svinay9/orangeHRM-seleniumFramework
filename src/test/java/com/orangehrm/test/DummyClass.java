package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {

    @Test
    public void dummyTest(){
        // ExtentManager.startTest("Verify Dummy test"); -- Implemented in Listener class
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the title");
        assert title.equals("OrangeHRM") : "Test Failed";
        System.out.println("Test Passed title matches");
//        ExtentManager.logSkip("This case is skipped");
//        throw new SkipException("Skipping test as a part of testing");

    }
}
