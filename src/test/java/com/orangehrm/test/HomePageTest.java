package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage1;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage1 homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage1(getDriver());
	}

	@Test
//	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo() {
//	public void verifyOrangeHRMLogo(String username, String password) {
	//	 ExtentManager.startTest("Home Page Verify Logo Test"); //--This has been
		// implemented in TestListener
		ExtentManager.logStep("Navigating to Login Page entering username and password");
//		loginPage.login(username, password);
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMlogo(), "Logo is not visible");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully!");
	}

}
