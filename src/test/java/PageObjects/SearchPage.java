package PageObjects;

import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AppiumSupport.TraceLog;
import Test.TestCaseBasicInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class SearchPage extends BasePage implements SearchPageService{

	

	
	@AndroidFindBy(xpath = "//input[@id='index-kw']")
	public AndroidElement search_box;
	
	
	
	@AndroidFindBy(xpath = "//*[@resource-id='bk.androidreader:id/home_btn_view_n1']")
	public AndroidElement click;
	
			
	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Yahoo')]")
	public AndroidElement btn;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.android.chrome:id/search_box_text']")
	public AndroidElement search_box_text;
	
	public SearchPage(AppiumDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void Search(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		// TODO Auto-generated method stub
		By by  = By.id("bk.androidreader:id/home_btn_view_n1");
		WebElement we= driver.findElement(by);
		we.click();
		this.switchToLastWebView();
		//search_box.sendKeys("test");
		by = By.linkText("Catalog");
		we= driver.findElementByLinkText("Catalog");
		we.click();
		 TraceLog.logger.log(Level.INFO,"search_box done");
		
	}

}
