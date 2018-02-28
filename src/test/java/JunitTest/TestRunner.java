package JunitTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.support.PageFactory;

import AppiumSupport.AppiumDriverHandler;
import AppiumSupport.Config;
import AppiumSupport.TraceLog;
import PageObjects.BasePage;
import PageObjects.LoginPageAndroid;
import Report.ScreenShotOnFailed;
import Report.TestReport;
import Test.TestCaseBasicInfo;
import Test.User;
import Utils.DateTimeUtils;
import Utils.POIUtils;
import Utils.PropertiesFileUtils;
import Utils.ReflectionUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

@RunWith(Parameterized.class)
public class TestRunner {

	public static AppiumDriverHandler driverHandler = AppiumDriverHandler.getInstance(); 
	private static HashMap<String, TestCaseBasicInfo> ID_TestDataObjects = new HashMap<String, TestCaseBasicInfo>();
	private static int numFailCases = 0;
	private static int totalTestSuite = 0;
	private static int testSuiteNo = 1;
	private static int totalExecution = 0;
	//initial logon user has no appiumdriver
	private static String initialUID = "yuenlee.hk@gmail.com";
	private String UID;

	private int thisTestNo;
	private TestCaseBasicInfo TestDataObject;

	public TestRunner(TestCaseBasicInfo TestDataObject, String objID, String UID) {
		this.TestDataObject = TestDataObject;
		TestDataObject.setTestRunner(this);
		this.UID=UID;
		// set user
	}

	public static void addTestDataObject(String ID, TestCaseBasicInfo obj) {
		if (ID_TestDataObjects.containsKey(ID)) {
			System.out.println("Duplicate ID:" + ID);
		} else {
			ID_TestDataObjects.put(ID, obj);
		}
	}

	public TestCaseBasicInfo getTestDataObject() {
		return TestDataObject;
	}

	public static TestCaseBasicInfo getTestDataObject(String ID) {
		TestCaseBasicInfo obj_tmp = null;
		if (ID_TestDataObjects.containsKey(ID)) {
			obj_tmp = ID_TestDataObjects.get(ID);
		}
		return obj_tmp;
	}
	

	@Rule
	public TestReport testReport = new TestReport();
	
	@Rule
	public ScreenShotOnFailed screenShootRule = new ScreenShotOnFailed();
	
	@BeforeClass
	public static void executedBeforeAllTestCases() {

	}

	@Before
	public void setUp() throws Exception {
		
	}

	private static boolean isPrevTestResultDeleted() {
		try {
			File file = new File(Config.getInstance().getProperty("test.result"));
			if (file.exists()) {
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
					return true;
				} else {
					System.out.println("Delete " + Config.getInstance().getProperty("test.result") + " operation is failed.");
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Transform each row in excel into java object
	@Parameters(name = "{1}")
	// name = "{1}" = Use TestDataObject.toString() as test case name
	public static Collection<Object[]> data() throws IOException {
		if (!isPrevTestResultDeleted()) {
			return null;
		}
		Collection<Object[]> objList = new ArrayList<Object[]>();

		try {
			//HSSFWorkbook wb = new HSSFWorkbook(Config.getInstance().getTestFileInputStream());
			HSSFWorkbook wb = new HSSFWorkbook(Config.getInstance().getTestFileInputStream());
			// load all tests: all test cases are configured in EKPMain page
			String sheetName_main = "EKPMain";
			int dataRowIndex_start = 1;
			int funcType_columnIndex = 0;
			int sheetName_columnIndex = 1;
			int rowNum_columnIndex = 2;
			int label_columnIndex = 3;
			int author_columnIndex = 4;

			HSSFSheet sheet = wb.getSheet(sheetName_main);

			ArrayList<String> funcTypes = POIUtils.getColumnFromExcel(sheet, funcType_columnIndex, dataRowIndex_start);

			ArrayList<String> sheetNames = POIUtils.getColumnFromExcel(sheet, sheetName_columnIndex, dataRowIndex_start, funcTypes.size());

			ArrayList<String> rowNums = POIUtils.getColumnFromExcel(sheet, rowNum_columnIndex, dataRowIndex_start, funcTypes.size());

			ArrayList<String> labels = POIUtils.getColumnFromExcel(sheet, label_columnIndex, dataRowIndex_start, funcTypes.size());

			ArrayList<String> authors = POIUtils.getColumnFromExcel(sheet, author_columnIndex, dataRowIndex_start, funcTypes.size());

			for (int j = 0; j < funcTypes.size(); j++) {
				String funcType = funcTypes.get(j);
				String sheetName = sheetNames.get(j);
				String rowNum = "", label = labels.get(j), author = authors.get(j);

				if (rowNums != null && rowNums.size() > j) {
					rowNum = rowNums.get(j);
				}

				sheet = wb.getSheet(sheetName);
				if (sheet != null) {
					int row = 1;

					if (rowNum.equals("")) {
						// Iterate through each rows one by one if not define
						// line number, skip row 1 as it is field names
						boolean found = false;
						for (int i = 2; i <= sheet.getPhysicalNumberOfRows(); i++) {
							row = i;
							TestCaseBasicInfo obj = POIUtils.loadTestCaseFromExcelRow(sheetName, funcType, row, wb);
							if (obj != null & !objList.contains(obj)) { // avoid
																		// duplicate
																		// test
																		// cases
								
								Object objectArray[]=new Object[] { obj, obj.toString(),obj.getUID() };
								objList.add(objectArray);
								addTestDataObject(obj.getID(), obj);
								found = true;
							}
							if (obj != null) {
								obj.setLabel(label);
							}
						}
						if (!found) {
							System.out.println("<b>TestDriver: data()-1: Cannot find method:" + funcType + " in sheet:" + sheetName);
						}
					} else {
						// only iterate rows specified by rowLine
						String[] rowNum_array = rowNum.split(";");
						for (String rowNum_str : rowNum_array) {
							row = Integer.parseInt(rowNum_str);
							TestCaseBasicInfo obj = POIUtils.loadTestCaseFromExcelRow(sheetName, funcType, row, wb);
							if (obj != null & !objList.contains(obj)) { // avoid
																		// duplicate
																		// test
																		// cases
								Object objectArray[]=new Object[] { obj, obj.toString(),obj.getUID() };
								objList.add(objectArray);
								addTestDataObject(obj.getID(), obj);
							}
							if (obj == null) {
								System.out.println("<b>TestDriver: data()-2:Cannot find method:" + funcType + " in sheet:" + sheetName + " row:" + rowNum);
							} else {
								obj.setLabel(label);
							}
						}
					}
				} else {
					System.out.println("<b>Cannot find file:" + sheetName_main);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		totalTestSuite = objList.size();
		return objList;
	}

	
	private static synchronized void switchUser(AppiumDriver driver, TestCaseBasicInfo testObject) {
		System.out.println("current UID "+initialUID);
		
		if (TestRunner.initialUID.equalsIgnoreCase(testObject.getUID())) {
		
			return;
		}
		LoginPageAndroid loginPage = new LoginPageAndroid(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver, 
				BasePage.MAX_TIMEOUT_SECONDS, TimeUnit.SECONDS), loginPage);
		try {
			String testObject_UID = testObject.getUID();
			String testObject_Pwd="";
			if (testObject_UID.equals("")) {// not setup -> defined in super class. 
				String UID = "UID";
				String PWD = "PWD";
				//Search it in super class.ie: Online>LearningModule>TestObject
				Field UID_field = ReflectionUtils.getField_superClz(
						testObject.getClass(), UID);
				Field PWD_Feild = ReflectionUtils.getField_superClz(
						testObject.getClass(), PWD);
				if (UID_field != null) {
					UID_field.setAccessible(true);
					testObject_UID = (String) UID_field.get(testObject);
					testObject_Pwd = (String) PWD_Feild.get(testObject);
					
				} else {
					//set site.admin as default user
					testObject_UID=Config.getInstance().getProperty("user.admin");
					testObject_Pwd=Config.getInstance().getProperty("user.admin.pass");
				
				}
			}
		
			
			System.out.println("testObject_UID: "+testObject_UID);
			
			loginPage.Logout(driver);
			loginPage.Login(driver, testObject_UID, testObject_Pwd);
			
			TestRunner.initialUID=testObject.getUID();
			System.out.println("switched initialUID "+initialUID);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test(timeout = 3000000)
	public void test() {
		double startTime, endtime, totaltime;
		startTime = System.currentTimeMillis();
		// 1. print test case info (author, test case no.)
		System.out.print("Starting: (" + testSuiteNo + "/" + totalTestSuite + ")\t" + TestDataObject.toString() + "\t");
		System.out.println(DateTimeUtils.getCurrentTimeAsStr());
		thisTestNo = testSuiteNo;
		testSuiteNo++;

		try {
			this.testReport.setAppiumDriver(driverHandler.getAppiumDriver());
			this.screenShootRule.setAppiumDriver(driverHandler.getAppiumDriver());
			TestDataObject.intiElements(driverHandler.getAppiumDriver());
			
			// 2. execute the test case
			if (executeTestMethod(TestDataObject, driverHandler.getAppiumDriver())) {
				// 3. do tasks after execution, will not reach this code if
				// exception occur
				// logger.succeeded(TestDataObject);
			}
		} catch (Exception e) {
			handFailCaseReporting(e, TestDataObject);
		} finally {
			// 5. do finish task
			// logger.finished(TestDataObject);
			endtime = System.currentTimeMillis();
			totaltime = (endtime - startTime) / 1000;
			System.out.println("Finished (" + thisTestNo + "/" + totalTestSuite + ") " + TestDataObject.toString() + " in " + totaltime + " secs ");
		}
	}

	private boolean executeTestMethod(TestCaseBasicInfo testCaseInfo, AppiumDriver driver) throws Exception {
		boolean success = false;
		double startTime, endtime;
		
		this.testReport.setTestDataObject(testCaseInfo);
		this.screenShootRule.setTestDataObject(testCaseInfo);
		
		
		System.out.println(testCaseInfo.toString());
		switchUser(driver,testCaseInfo);
		
		if (testCaseInfo.getFuncType().length() > 0) {
			testCaseInfo.setTestRunner(this);
			

			
			// 1. Switch user if new test case use different logon user
			//TestRunner.switchUser(testCaseInfo, newUser);

			System.out.println("2 testObject.getUID(): "+testCaseInfo.getUID());
			
			Method method = null;
			// 2.1 Execute method directly if no test suites

			if (testCaseInfo.getTestSuite().trim().length() == 0) {
				if (testCaseInfo.getObjectParams().size() == 0) {
					// 3.1 if no object param, WebDriver is the only param
					totalExecution++;
					this.TestDataObject = testCaseInfo;
					
					method = testCaseInfo.getClass().getMethod(testCaseInfo.getFuncType(), AppiumDriver.class, TestCaseBasicInfo.class);
					method.invoke(testCaseInfo, driver, ((TestCaseBasicInfo)testCaseInfo));
					// testReport.SaveSuccessTestReportToExcel();
					success = true;

				} else {
					// 3.2 if has object params, WebDriver and ObjectInput
					// are the params
					StringBuilder sb = new StringBuilder();
					System.out.println("\tStarting object inputs in " + testCaseInfo.getFuncType());
					for (TestCaseBasicInfo objectParam : testCaseInfo.getObjectParams()) {
						sb.append(System.lineSeparator() + "\t\"").append(objectParam.toString()).append("\" ");
					}
					totalExecution++;
					this.TestDataObject = testCaseInfo;
					method = testCaseInfo.getClass().getMethod(testCaseInfo.getFuncType(), AppiumDriver.class, TestCaseBasicInfo.class, ArrayList.class);
					startTime = System.currentTimeMillis();
					method.invoke(testCaseInfo, driver,((TestCaseBasicInfo)testCaseInfo), testCaseInfo.getObjectParams());
					endtime = System.currentTimeMillis();
					// testReport.SaveSuccessTestReportToExcel();
					System.out.println("\tFinished object input: " + sb.toString() + (endtime - startTime) / 1000 + " secs");
					success = true;
				}

			} else {
				// 2.2 If "TestSuite" field is not empty, ignore test suite
				// but execute its test cases

				ArrayList<TestCaseBasicInfo> testCases = testCaseInfo.getTestCaseArray();
				if (testCases != null && !testCases.isEmpty()) {
					System.out.println("Starting test suite:" + testCaseInfo.toString());

					for (TestCaseBasicInfo testCase : testCases) {
						if (testCase != null) {
							// stored original ID
							String ID = testCase.getID();
							// IMPORTANT: modify id for reporting purpose
							// only
							testCase.setID(testCaseInfo.getID() + "{" + testCase.getID() + "}");
							startTime = System.currentTimeMillis();
							boolean testResult = executeTestMethod(testCase, driver);
							endtime = System.currentTimeMillis();
							System.out.println("\tFinished sub case: " + testCase.toString() + " in " + (endtime - startTime) / 1000 + " secs");
							// reset back to original ID'
							testCase.setID(ID);
							if (!testResult) {
								System.out.println("ERROR: one test case fail, then skip all coming test cases in test suite");
								break;
							}

						}
					}
					System.out.println("Finshed Test suite: \"" + testCaseInfo.toString() + "\"");
					this.TestDataObject = testCaseInfo;
					success = true;
				} else {
					System.out.println("<b>ERROR: Loading test cases: \n" + testCaseInfo.getTestSuite() + " in test suite: " + testCaseInfo.getFuncType() + "</b>");
					throw new RuntimeException("ERROR: Loading test cases: \n" + testCaseInfo.getTestSuite() + " in test suite: " + testCaseInfo.getFuncType());
				}
			}
		} else {
			System.out.println("methodName:" + testCaseInfo.getFuncType() + "() is not defined in class:" + testCaseInfo.getClass().getName());
		}
		return success;
	}

	private void handFailCaseReporting(Exception e, TestCaseBasicInfo obj) {
		numFailCases++;
		System.out.println("FAILED (" + thisTestNo + "/" + totalTestSuite + ") " + TestDataObject.toString());
		POIUtils.filterDebugMsg(e, TestDataObject);
		org.junit.Assert.fail(e.getMessage());
	}

	@After
	public void after() {

	}

	@AfterClass
	public static void tearDown() throws Exception {
		// close the window that uses plugin container before driver.quit();
		System.out.println("getting into tearDown now");
		// Runtime.getRuntime().exec("taskkill /F /IM plugin-container.exe");
		System.out.println("Total test case run:" + totalExecution);
		System.out.println("Total test case fail:" + numFailCases);
		System.out.println("Total test suite run:" + totalTestSuite);

		driverHandler.quitAppiumDriver();
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("Total.Cases", String.valueOf(totalExecution));
		properties.put("Total.Pass.Cases", String.valueOf(totalExecution - numFailCases));
		properties.put("Total.Fail.Cases", String.valueOf(numFailCases));
		System.out.println("saving result to " + Config.getInstance().getProperty("test.result"));
		PropertiesFileUtils.SaveAsPropertiesFile(Config.getInstance().getProperty("test.result"), properties);
	}
}
