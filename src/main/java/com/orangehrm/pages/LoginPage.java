package com.orangehrm.pages;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;
    // Define locators
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[text()=' Login ']");

    //*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button
    private By errorMessage  = By.xpath("//p[text()='Invalid credentials']");

    private By sideBar = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/div/div/button/i");

    //Initialize the ActionDriver object by passing WebDriver instance
//	public LoginPage(WebDriver driver) {
//		 this.actionDriver= new ActionDriver(driver);
//	}

    public LoginPage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }


    // Method to perform login
    public  void  login(String username, String password){
        actionDriver.enterText(userNameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    //Method to check error message
    public boolean isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);
    }

    // method to get error messagetext

    public String getErrorMessageText(){
        return actionDriver.getText(errorMessage);
    }

    //verify error is correct

    public boolean verifyErrorMessage(String expectedError){
       return actionDriver.compareText(errorMessage,expectedError);
    }

    //Method to Navigate to PIM tab
    public void clickOnSideBar() {
        actionDriver.click(sideBar);
    }
}
