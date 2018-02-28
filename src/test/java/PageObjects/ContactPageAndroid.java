package PageObjects;

import java.util.logging.Level;

import AppiumSupport.TraceLog;
import JunitTest.TestRunner;
import Test.TestCaseBasicInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ContactPageAndroid extends BasePage implements ContactPageService{

	
	public String pageActivity="com.eventxtra.eventx.PartyListActivity_";
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/connection_status_bar']")
	public AndroidElement Confirm_Sync;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/toolbars']/android.widget.ImageButton[contains(@index,0)]")
    public AndroidElement backBtn;
	
	
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/imgLogo']")
    public AndroidElement existingActBtn;
	

	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/btnCreateEvent']/android.widget.TextView[contains(@index,1)]")
	public AndroidElement newBtn;
	
	public ContactPageAndroid(AppiumDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void ContactSync(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		// TODO Auto-generated method stub
		
		this.switchToNativeView();
		
		existingActBtn.click();
		String text=Confirm_Sync.getText();
		//TraceLog.logger.log(Level.FINE,"sync result text="+text);
		backToHome();
		String currentActivity= ((AndroidDriver) driver).currentActivity();
		TraceLog.logger.log(Level.INFO,"currentActivity:" + currentActivity);
		reset();
		//existingActBtn.click();
		
	}

	public void TakePhoto(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		
		//keyevent https://testerhome.com/topics/799
		
		
	}

}
