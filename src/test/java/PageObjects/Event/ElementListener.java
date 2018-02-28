package PageObjects.Event;

import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import AppiumSupport.TraceLog;
import JunitTest.TestRunner;
import io.appium.java_client.events.api.general.ElementEventListener;  
  
public class ElementListener implements  
        ElementEventListener {  
	
	
  
    public void beforeClickOn(WebElement arg0, WebDriver arg1) {  
  
        //messages.add("Attempt to click on the element");  
    	
    	TraceLog.logger.log(Level.INFO,"准备点击:"+splitElement(arg0));
    
    }  
      
    public void afterClickOn(WebElement arg0, WebDriver arg1) {  
        //messages.add("The element was clicked");  
    	TraceLog.logger.log(Level.INFO,"点击:" +splitElement(arg0));  
  
    }  
  
    public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {  
        //messages.add("Attempt to change value of the element");  
    	TraceLog.logger.log(Level.INFO,"准备改变控件:" +splitElement(arg0)+"数值");  
          
  
    }  
  
    public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {  
        //messages.add("The value of the element was changed");  
    	TraceLog.logger.log(Level.INFO,"控件:" + splitElement(arg0) + "数值已改变");  
  
    }  
  
//  @Override  
//  protected void add() {  
//      // SingleListeners.listeners.put(ElementListener.class, this);  
//  
//  }  
  
    
    public void beforeChangeValueOf(WebElement element, WebDriver driver,  
            CharSequence[] keysToSend) {  
        // TODO 自动生成的方法存根  
  
    }  
  
 
    public void afterChangeValueOf(WebElement element, WebDriver driver,  
            CharSequence[] keysToSend) {  
        // TODO 自动生成的方法存根  
  
    }  
      //获取操作的控件字符串  
      private String splitElement(WebElement element) {    
            String str = element.toString().split("-> ")[1];    
            return str.substring(0, str.length() - 1);    
        }    
}  
