package com.orangehrm.test;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
 import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test
public class ApiTest {
    public void verifyGetUserAPI() {

        SoftAssert softAssert = new SoftAssert();

        // Step1: Define API Endpoint
        String endPoint = "https://jsonplaceholder.typicode.com/users/1";
        ExtentManager.logStep("API Endpoint: " + endPoint);

        // Step2: Send GET Request
        ExtentManager.logStep("Sending GET Request to the API");
        Response response = APIUtility.sendGetRequest(endPoint);

        // Step3: validate status code
        ExtentManager.logStep("Validating API Response status code");
        boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);

        softAssert.assertTrue(isStatusCodeValid, "Status code is not as Expected");

        if (isStatusCodeValid) {
            ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("Status Code Validation Failed!");
        }

        // Step4: validate user name
        ExtentManager.logStep("Validating response body for username");
        String userName = APIUtility.getJsonValue(response, "username");
        boolean isUserNameValid = "Bret".equals(userName);
        softAssert.assertTrue(isUserNameValid, "Username is not valid");
        if (isUserNameValid) {
            ExtentManager.logStepValidationForAPI("Username Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("Username Validation Failed!");
        }

        // Step4: validate email
        ExtentManager.logStep("Validating response body for email");
        String userEmail = APIUtility.getJsonValue(response, "email");
        boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
        softAssert.assertTrue(isEmailValid, "Email is not valid");
        if (isEmailValid) {
            ExtentManager.logStepValidationForAPI("Email Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("Email Validation Failed!");
        }

        softAssert.assertAll();

    }
}
