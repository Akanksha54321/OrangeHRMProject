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

public class BaseClass_withoutLocalThread {

	protected static Properties prop;
	 protected static WebDriver driver;
	 private static ActionDriver actionDriver;

	public static final Logger logger = LoggerManager.getLogger(BaseClass_withoutLocalThread.class);
	
//	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
//
//	//Getter method for soft assert
//	public SoftAssert getSoftAssert() {
//		return softAssert.get();
//	}
	
	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
		
		//Start the Extent Report
		//ExtentManager.getReporter();  --This has been implemented in TestListener
	}

	@BeforeMethod
//	public synchronized void setup() throws IOException {
		public  void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());//give the name of the class
		//class name will come like Setting up webdriver for HomePageTest
		launchBrowser();
		configureBrowser();
		staticWait(2);
		//Sample logger message
		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warm message");

		
//	 Initialize the actionDriver only once without Thread local concept
	if (actionDriver == null) {
		  actionDriver = new ActionDriver(driver);
		  logger.info("ActionDriver instance is created. ");
		  logger.info("ActionDriver instance is created. "+Thread.currentThread().getId());
		  }
	}
		 
//
////		 Initialize ActionDriver for the current Thread
//		actionDriver.set(new ActionDriver(getDriver()));
//		logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());
//
//	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
//	private synchronized void launchBrowser() {
		
		private  void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			
			// Create ChromeOptions
//			ChromeOptions options = new ChromeOptions();
//			options.addArguments("--headless"); // Run Chrome in headless mode
//			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
//			//options.addArguments("--window-size=1920,1080"); // Set window size
//			options.addArguments("--disable-notifications"); // Disable browser notifications
//			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
//			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

			 driver = new ChromeDriver();
//			driver.set(new ChromeDriver()); // New Changes as per Thread
//			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			// Create FirefoxOptions
//			FirefoxOptions options = new FirefoxOptions();
//			options.addArguments("--headless"); // Run Firefox in headless mode
//			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
//			options.addArguments("--width=1920"); // Set browser width
//			options.addArguments("--height=1080"); // Set browser height
//			options.addArguments("--disable-notifications"); // Disable browser notifications
//			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
//			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			 driver = new FirefoxDriver();
//			driver.set(new FirefoxDriver()); // New Changes as per Thread
			 
		} else if (browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			 driver = new EdgeDriver();
//			 driver.set(new EdgeDriver()); // New Changes as per Thread

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
//		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		// maximize the browser
//		getDriver().manage().window().maximize();
		driver.manage().window().maximize();
		// Navigate to URL
		try {
	//		getDriver().get(prop.getProperty("url"));
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
//	public synchronized void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
		}
		System.out.println("WebDriver instance is closed.");
		logger.info("WebDriver instance is closed.");
//		driver.remove();
//		actionDriver.remove();
		 driver = null;
		 actionDriver = null;
		//ExtentManager.endTest(); --This has been implemented in TestListener
	}



	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
