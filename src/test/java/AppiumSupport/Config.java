package AppiumSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import JunitTest.TestRunner;
import Utils.Validate;



public class Config {
	
	private static Config instance = Config.getInstance();
	private static Properties configProperties;
	

	
	private Config() {
		try {
			configProperties = loadProperties("./conf/config.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("------------------- Config Detail--------------------------");
		System.out.println("Device Name: " + getProperty("deviceName"));
		System.out.println("Platform Name: " + getProperty("platformName"));
		System.out.println("-----------------------------------------------------------");
	}
	
	private Properties loadProperties(String file) throws IOException {
		Properties properties = new Properties();
		InputStream input = new FileInputStream(file);
		properties.load(input);
		input.close();
		return properties ;
	}
	
	public static Config getInstance() {
		if(instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	public String getProperty(String key) {
		String value = configProperties.getProperty(key);
		if(Validate.isBlank(value)) {
			System.out.println("Cannot locate the property value with key " + key + ".");
		}
		return value;
	}

	public FileInputStream getTestFileInputStream() {
		//String targetDB = Config.getInstance().getProperty("target.db");
		try {
			File classpathRoot = new File(System.getProperty("user.dir"));
            File fileDir = new File(classpathRoot, "/conf");
            File file = new File (fileDir, "TestCase.xls");
			return new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
