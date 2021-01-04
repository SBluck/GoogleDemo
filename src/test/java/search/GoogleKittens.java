package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
@FixMethodOrder()
public class GoogleKittens {

	private static String URL = "https://www.google.com";
	private static WebDriver driver;
	private static ExtentReports extent;
	private static ExtentTest test;

	@BeforeClass
	public static void init() {
		extent = new ExtentReports("src/test/resources/reports/report1.html", false);
		test = extent.startTest("Google search Extent testing");
		
		System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
		Map<String, Object> prefs = new HashMap<String, Object>();
		ChromeOptions cOptions = new ChromeOptions();
		cOptions.setHeadless(true);

		driver = new ChromeDriver(cOptions);
//		driver.manage().window().maximize();

		// Settings
		prefs.put("profile.default_content_setting_values.cookies", 2);
		prefs.put("network.cookie.cookieBehavior", 2);
		prefs.put("profile.block_third_party_cookies", true);

		// Create ChromeOptions to disable Cookies pop-up
		cOptions.setExperimentalOption("prefs", prefs);

		driver.manage().window().setSize(new Dimension(1366, 768));

// use implicit in before - waits before running tests
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);

	}
	
	@Before
	public void setup() {
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);

	}

	@AfterClass
	public static void tearDown() {
		driver.quit();
		extent.endTest(test);
		extent.flush();
		extent.close();
	}

	// Checks google page title
	@Test
	public void googleTitleTest() {	
		driver.get(URL);
//		original assertEquals test
//		assertEquals("Google", driver.getTitle());
		if(driver.getTitle().equals("Google")) {
			test.log(LogStatus.PASS, "Page Title: Success");
		} else {
			test.log(LogStatus.FAIL,"Page Title: Failed");
		}
		
		// Check url: original test
//		assertEquals("https://www.google.com/", driver.getCurrentUrl());
		
		if(driver.getCurrentUrl().equals("https://www.google.com/")) {
			test.log(LogStatus.PASS, "Page URL: Success");
		} else {
			test.log(LogStatus.FAIL,"Page URL: Failed");
		}
		
	}

	// Checks current URL
//	@Test
//	public void googleUrlTest() {
//		
//	
//	}

	@Test
	public void searchTest()  {
		driver.get(URL + "/images");

		// locates the input box with name of "q"
		WebElement input = driver.findElement(By.name("q"));

		// sends "Kittens" to input box (i.e. "q" element)
		input.sendKeys("Kittens");
		input.submit();

		// find element by css selector (from Copy selector) //
//	  WebElement image =  driver.findElement(
//			  By.cssSelector(".islrg > div.islrc > div:nth-child(4) > a.wXeWr.islib.nfEiy.mM5pbd > div.bRMDJf.islir > img")); 
		// image.click();

		// wait for search result class to finish loading then attempt to locate
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.className("islrc")));

		// Set element location results as result
		WebElement result = driver.findElement(By.className("islrc"));

		// Adds all elements with an img tag to listResult
		List<WebElement> listResult = result.findElements(By.tagName("img"));
		System.out.println(listResult.size());

		// Creates a new action called action
		Actions action = new Actions(driver);

		// Using the action, this will click on the first element in the list
		action.moveToElement(listResult.get(0)).click().perform();

		// Using getAttribute we are able to get the src containing a link, copy and
		// paste the output into your browser
		String kittenImgURL = listResult.get(3).getAttribute("src").toString();
		System.out.println(kittenImgURL);
	}

}
