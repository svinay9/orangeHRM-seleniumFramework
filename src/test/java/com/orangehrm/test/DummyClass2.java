package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.Test;

public class DummyClass2 extends BaseClass {

    @Test
    public void dummyTest2(){
        // ExtentManager.startTest("Verify Dummy2 test");  -- Implemented in Listener class
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the title");
        assert title.equals("OrangeHRM") : "Test Failed";
        System.out.println("Test Passed title matches");
        ExtentManager.logStep("Validation Successful");

    }
}
