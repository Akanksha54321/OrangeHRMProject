package com.orangehrm.test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage1;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	
	private LoginPage loginPage;
	private HomePage1 homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage  = new HomePage1(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
//	@Test(priority=1)
	public void verifyValidLoginTest(String username, String password) {
//		public void verifyValidLoginTest() {	
//		ExtentManager.startTest("Valid Login Test"); //--This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
//		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successfull login ");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully!");
		staticWait(2);
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
//	@Test(priority=2)
	public void inValidLoginTest(String username, String password) {
//		public void inValidLoginTest() {
//		ExtentManager.startTest("In-valid Login Test"); //--This has been implemented in TestListener
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login(username, password);
//		loginPage.login("Admin", "admin123");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message");
		ExtentManager.logStep("Validation Successful");
	}

}
