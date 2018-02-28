package AppiumSupport;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import PageObjects.Event.AlertListener;
import PageObjects.Event.AppiumEventListener;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppiumDriverHandler{

	private static Config conf = Config.getInstance(); 
	private static AppiumDriverHandler instance = AppiumDriverHandler.getInstance();
	//public static final String appPackage ="com.tencent.mm";//"com.eventxtra.eventx";
	//public static final String appPackage ="com.eventxtra.eventx";
	public static final String appPackage ="bk.androidreader";
	//public static final String appActivity = "com.tencent.mm.ui.LauncherUI";//"com.eventxtra.eventx.SplashActivity_";
	//public static final String appActivityLaunch = "com.eventxtra.eventx.ActivityLaunch";
	//public static final String appActivity = "com.eventxtra.eventx.SplashActivity_";
	//public static final String appActivityLaunch = "com.eventxtra.eventx.ActivityLaunch";
	
	//public static final String appActivity = "org.chromium.chrome.browser.document.ChromeLauncherActivity";
	//public static final String appActivity = "org.chromium.chrome.browser.LauncherShourtcutActivity";
		
	public static final String appActivity ="bk.androidreader.StartLoadActivity";
	//public static final String appActivity ="org.chromium.chrome.browser.ChromeTabbedActivity";
	private AppiumDriver driver;

    public static OS executionOS;
    
    public Context driverContext;
    public enum Context {
    	NATIVE_APP,
    	WEBVIEW
    }
    
	private AppiumDriverHandler () {
		if (conf.getProperty("TestingOS").equalsIgnoreCase(OS.ANDROID.toString())) {
			executionOS = OS.ANDROID;
		}else {
			executionOS = OS.IOS;
		}
	}

    public enum OS {
        ANDROID,
        IOS
    }

	public static AppiumDriverHandler getInstance() {
		if(instance == null) {
			instance = new AppiumDriverHandler();
		}
		return instance;
	}
   
	public synchronized AppiumDriver getAppiumDriver() {
		
		if (driver == null) {
			driver = initAppiumDriver();
		}
		return driver;
	}

	private AppiumDriver initAppiumDriver()  {
	
			DesiredCapabilities capabilities = new DesiredCapabilities();
			File classpathRoot;
	        File appDir;
	        File app;
			

			switch(executionOS){
	          
	          case ANDROID:
	              classpathRoot = new File(System.getProperty("user.dir"));
	              appDir = new File(classpathRoot, "/app/Android");
	               app = new File (appDir, conf.getProperty("apk.name"));
	               ChromeOptions options = new ChromeOptions();
	              
	               options.setExperimentalOption("androidPackage", appPackage);
	               options.setExperimentalOption("androidUseRunningApp", true);
	               options.setExperimentalOption("androidActivity", appActivity);
	               //options.setExperimentalOption("androidProcess", "com.tencent.mm:tools")
	               
	               capabilities.setCapability("recreateChromeDriverSessions", true);
	               capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "4000");
	              capabilities.setCapability("platformName", conf.getProperty("platformName"));
	              capabilities.setCapability("automationName", conf.getProperty("automationName"));
	              //capabilities.setCapability("automationName", "Appium");
	             // capabilities.setCapability("browserName","Chrome"); 
	              capabilities.setCapability("deviceName", conf.getProperty("deviceName"));
	              capabilities.setCapability("platformVersion",conf.getProperty("platformVersion"));
	              //capabilities.setCapability("app", app.getAbsolutePath());
	              capabilities.setCapability("autoAcceptAlerts", true);
	      		capabilities.setCapability("autoGrantPermissions", true);
	      		//capabilities.setCapability("autoWebview", true);
	      		
	      		//capabilities.setCapability("screenshotWaitTimeout", "60");
	      		  capabilities.setCapability("noReset", true);
	              capabilities.setCapability("appPackage", appPackage);
	              capabilities.setCapability("appActivity", appActivity);
	              capabilities.setCapability("appWaitActivity",appActivity);
	              
	              //capabilities.setCapability("unicodeKeyboard", "True");
	              //capabilities.setCapability("resetKeyboard", "True"); 
	              //capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	              try {
					driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	              //create a object for EventFiringWebDriver Class
	           
	              driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver,  
	                      new AlertListener(), new AppiumEventListener());
	              System.out.println("AndroidDriver created");
	              TraceLog.logger.log(Level.INFO,"AndroidDriver starting");
	              //String s = ((AndroidDriver)driver).StartActivity("com.iwobanas.screenrecorder.pro", "com.iwobanas.screenrecorder.RecorderActivity");
	              break;
		    case IOS:
		    	classpathRoot = new File(System.getProperty("user.dir"));
		        appDir = new File(classpathRoot, "/app/iOS/");
		        app = new File(appDir, "ContactsSimulator.app");
		        capabilities = new DesiredCapabilities();
		        capabilities.setCapability("platformName", "ios");
		        capabilities.setCapability("deviceName", "iPhone 5s");
		        capabilities.setCapability("app", app.getAbsolutePath());
		        try {
					driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        break;
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			return driver;


		
	}
	
	public void quitAppiumDriver() {
		if(driver != null){
			driver.quit();		
		}
		driver = null;
	}

}
