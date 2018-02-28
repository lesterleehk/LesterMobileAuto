package PageObjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import JunitTest.TestRunner;
import Test.TestCaseBasicInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPageAndroid extends BasePage implements LoginPageService{
	
	public LoginPageAndroid(AppiumDriver driver) {
		super(driver);
	}
	
	@AndroidFindBy(xpath = "//*[@class='android.widget.ImageView']")
	public AndroidElement floting_icon;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/settings']")
	public AndroidElement setting_icon;
	
	
	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'µÇ³ö')]")
	public AndroidElement logout_btn;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/btnSignIn']")
    public AndroidElement LoginBtn;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/etEmail']")
	public AndroidElement email_feild;

	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/etPassword']")
	public AndroidElement password_feild;
	
	@AndroidFindBy(xpath = "//*[@resource-id='android:id/button1']")
	public AndroidElement OK;
	
	
	@AndroidFindBy(xpath = "//android.widget.Button[@text='ÊÇ']")
	public AndroidElement Confirm_Logout_Btn;
	
	@AndroidFindBy(xpath = "//*[@resource-id='com.eventxtra.eventx:id/btn_email_signin']")
	public AndroidElement btn_email_signin;
	
	@AndroidFindBy(xpath = "com.eventxtra.eventx:id/btn_linkedin_signin")
	public AndroidElement btn_linkedin_signin;
	
	By By_btn_signin=By.xpath("//*[@resource-id='com.eventxtra.eventx:id/btnSignIn']");

	public void Login(AppiumDriver driver,String UID, String Pwd) {
		LoginBtn.click();
		btn_email_signin.click();
		email_feild.sendKeys(UID);
		LoginBtn.click();
		password_feild.sendKeys(Pwd);
		LoginBtn.click();
		OK.click();
	}
	public void Logout(AppiumDriver driver) {
		
		if (driver.findElements(By_btn_signin).size()>0){ 
			return;
		}
		setting_icon.click();
		logout_btn.click();
		Confirm_Logout_Btn.click();
		
		
	}
}
