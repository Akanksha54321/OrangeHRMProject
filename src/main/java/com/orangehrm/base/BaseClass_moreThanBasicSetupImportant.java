package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass_moreThanBasicSetupImportant {

	protected static Properties prop;
	 protected static WebDriver driver;
	 private static ActionDriver actionDriver;

	public static final Logger logger = LoggerManager.getLogger(BaseClass_moreThanBasicSetupImportant.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
		
	}

	@BeforeMethod

		public  void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());//give the name of the class
		//class name will come like Setting up webdriver for HomePageTest
		launchBrowser();
		configureBrowser();
		staticWait(2);

		
	// Initialize the actionDriver only once
	if (actionDriver == null) {
		  actionDriver = new ActionDriver(driver);
		  System.out.println("ActionDriver instance is created. ");
		  }
	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
		
		private  void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			

			 driver = new ChromeDriver();

			logger.info("ChromeDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			 driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			
		
			 driver = new EdgeDriver();

		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */

	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		// maximize the browser
		driver.manage().window().maximize();
		// Navigate to URL
		try {
			driver.get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL:" + e.getMessage());
		}
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}
 
	// Driver setter method
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	// Getter Method for WebDriver
	public static WebDriver getDriver() {

		if (driver == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver;

	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver;

	}


	@AfterMethod
	public  void tearDown() {

		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
		}
		System.out.println("WebDriver instance is closed.");
		logger.info("WebDriver instance is closed.");

		 driver = null;
		 actionDriver = null;
	}
	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
