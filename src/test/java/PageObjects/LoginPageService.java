package PageObjects;

import Test.TestCaseBasicInfo;
import io.appium.java_client.AppiumDriver;

public interface LoginPageService {
	public void Login(AppiumDriver driver,String UID, String Pwd);
	public void Logout(AppiumDriver driver);
}
