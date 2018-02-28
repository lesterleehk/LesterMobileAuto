package Test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.PageFactory;

import PageObjects.BasePage;
import PageObjects.ContactPageAndroid;
import PageObjects.LoginPageAndroid;
import PageObjects.SearchPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class User extends TestCaseBasicInfo {
	
	protected LoginPageAndroid loginPage;
	protected ContactPageAndroid contactPage;
	protected SearchPage searchPage;
	

	public void intiElements(AppiumDriver driver) {
		loginPage = new LoginPageAndroid(driver);
		contactPage = new ContactPageAndroid(driver);
		searchPage= new SearchPage(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver, 
				BasePage.MAX_TIMEOUT_SECONDS, TimeUnit.SECONDS), loginPage);
		PageFactory.initElements(new AppiumFieldDecorator(driver, 
				BasePage.MAX_TIMEOUT_SECONDS, TimeUnit.SECONDS), contactPage);
		PageFactory.initElements(new AppiumFieldDecorator(driver, 
				BasePage.MAX_TIMEOUT_SECONDS, TimeUnit.SECONDS), searchPage);
	}

	
	public void TestUser(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		loginPage.Logout(driver);
		loginPage.Login(driver,TestObject.getUID(), TestObject.getPWD());
	
	}
	
	public void ContactSync(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		contactPage.ContactSync(driver,TestObject);

	}
	
	public void Search(AppiumDriver driver, TestCaseBasicInfo TestObject) {
		searchPage.Search(driver, TestObject);

	}
}
