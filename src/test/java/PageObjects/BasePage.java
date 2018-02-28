package PageObjects;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import AppiumSupport.AppiumDriverHandler;
import AppiumSupport.AppiumDriverHandler.Context;
import AppiumSupport.TraceLog;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class BasePage {
	
    private String defaultContent; //切换到默认主页面
	private AppiumDriver driver;
	

	
	@AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='向上瀏覽']")
    public AndroidElement backBtn;
	
	public String pageActivity="com.eventxtra.eventx.PartyListActivity_";
	
	By backBy=By.xpath("//android.widget.ImageButton[@content-desc='向上瀏覽']");

    
	public static final int MAX_TIMEOUT_SECONDS = 30;
    
	public BasePage(AppiumDriver driver) {
    	this.driver=driver;
		
	}

	public void switchContent() {
		
	}
	
    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }
    
    /**
    * Switch to NATIVE_APP or WEBVIEW
    * @param sWindow window name
    */
    private void switchContext(Context context) {
    	//TraceLog.TraceLog.logger.log(Level.INFO,"Swith to Context: " + context.toString());
    	switch(context){
	        case NATIVE_APP:
	        	switchToNativeView();
	        	break;
	        case WEBVIEW:
	        	switchToLastWebView();
	        	break;
    	}
    }
    
    public void switchToLastWebView() {
    	try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Set<String> contextNames = driver.getContextHandles();
        List<String> webViewContextNames =  contextNames
                .stream()
                .filter(contextName -> contextName.contains("WEBVIEW_"))
                .collect(Collectors.toList());
        String currentContextView = "";
        TraceLog.logger.log(Level.INFO,"All contexts:" + contextNames);
        if (webViewContextNames.size() > 0){
            currentContextView = (String) webViewContextNames
                .toArray()[webViewContextNames.size()-1];
            
            TraceLog.logger.log(Level.INFO,"webview contexts:" + currentContextView);
            driver.context(currentContextView);
        }
        TraceLog.logger.log(Level.INFO,"All contexts:" + contextNames);
        TraceLog.logger.log(Level.INFO,"All webview contexts:" + webViewContextNames);
        TraceLog.logger.log(Level.INFO,"current context:" + driver.getContext());
    }
    
    public void switchToNativeView() {
        Set<String> contextNames = driver.getContextHandles();
        List<String> nativeViewContextNames = contextNames
                .stream()
                .filter(contextName -> contextName.contains(AppiumDriverHandler.Context.NATIVE_APP.toString()))
                .collect(Collectors.toList());
        String currentContextView = "";

        if (nativeViewContextNames.size() > 0) {
            currentContextView = (String) nativeViewContextNames
                .toArray()[nativeViewContextNames.size() - 1];
            driver.context(currentContextView);
        }
       
      TraceLog.logger.log(Level.INFO,"All contexts:" + contextNames);
      TraceLog.logger.log(Level.INFO,"All native contexts:" + nativeViewContextNames);
      TraceLog.logger.log(Level.INFO,"current context:" + driver.getContext());
    }

    public void switchToSpecificWebView(By selector) {
        switchToLastWebView();
        long begin = System.currentTimeMillis();
        do {
            try {
                List<MobileElement> elements = driver.findElements(selector);
                if (null != elements && elements.size() > 0) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            switchToLastWebView();
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while ((System.currentTimeMillis() - begin) < MAX_TIMEOUT_SECONDS * 1000);
    }
    public void verifyContent(String expected, String actual) {
    	
    }
    
    public void backToHome() {
    
    	do {
        	 backBtn.click();      
        	 try {
				Thread.sleep(2000);
				
				 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}while(driver.findElements(backBy).size()>0);
    }
    
    public void reset() {

    	 TraceLog.logger.warning("reset");  
    	 TraceLog.logger.warning("重启app...........");  
    	 //can only start appactivity
		 Activity activity = new Activity(AppiumDriverHandler.appPackage,AppiumDriverHandler.appActivity);
		 activity.setAppWaitPackage(AppiumDriverHandler.appPackage);
		 activity.setAppWaitActivity(AppiumDriverHandler.appActivity);
         ((AndroidDriver) driver).startActivity(activity);     
   
    }
    
}
