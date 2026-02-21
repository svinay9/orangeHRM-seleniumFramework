package com.orangehrm.test;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homepage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homepage = new HomePage(getDriver());
    }
    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username, String password){
       //ExtentManager.startTest("Verify Home Page test"); -- Implemented in Listener class
        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(username,password);
        //homepage.clickOnSideBar();
        ExtentManager.logStep("Verify logo is visible or not");
        Assert.assertTrue(homepage.verifyOrangeHRMlogo(), "Logo not visible");
        ExtentManager.logStep("Validation successful");
        homepage.logout();
        ExtentManager.logStep("Successfully log out");
    }
}
