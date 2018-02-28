package AppiumSupport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TraceLog {
	public static Logger logger = Logger.getLogger(TraceLog.class.getName());  
	public static LogManager logManager = LogManager.getLogManager();  
	      
	static {  
        InputStream inputStream = null;  
        try {  
            //读取配制文件  
            logManager.readConfiguration(TraceLog.class.getResourceAsStream("log.properties"));
            //添加Logger  
            //logger.setLevel(Level.ALL);
			//logger.info("initializing - trying to load configuration file ...");
            logManager.addLogger(logger);  
           
            
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    } 
	
	public static Logger getNewLogger(Logger logger) {
		 logManager.addLogger(logger);
		return logger;  
		
	}
	
	   public static void main(String[] args) {
		   logger.info("info");
		   logger.warning("warning");
		   logger.fine("fine");
	   }
}
