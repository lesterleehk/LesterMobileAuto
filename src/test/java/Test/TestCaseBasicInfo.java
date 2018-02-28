package Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import AppiumSupport.Config;
import JunitTest.TestRunner;
import PageObjects.ContactPageAndroid;
import PageObjects.ContactPageService;
import PageObjects.LoginPageAndroid;
import PageObjects.LoginPageService;
import Utils.POIUtils;
import io.appium.java_client.AppiumDriver;

public abstract class TestCaseBasicInfo {
	
	protected String UID = "", PWD = "", FuncType = "", ExpectedResult = "", ID = "", TestSuite = "", 
			ObjectInputs = "", Label = "", TestCaseType="";
	
	private TestRunner testRunner;

	// for test suite
	protected ArrayList<TestCaseBasicInfo> testCaseArray = new ArrayList<TestCaseBasicInfo>();
	
	// for input object params
	protected ArrayList<TestCaseBasicInfo> objectParams = new ArrayList<TestCaseBasicInfo>();
	
	public TestRunner getTestRunner() {
		return testRunner;
	}
	
	public abstract void intiElements(AppiumDriver driver);
	
	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getPWD() {
		return PWD;
	}

	public void setPWD(String pWD) {
		PWD = pWD;
	}

	public String getFuncType() {
		return FuncType;
	}

	public void setFuncType(String funcType) {
		FuncType = funcType;
	}

	public String getExpectedResult() {
		return ExpectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		ExpectedResult = expectedResult;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTestSuite() {
		return TestSuite;
	}

	public void setTestSuite(String testSuite) {
		TestSuite = testSuite;
		testCaseArray = loadTestCases(testSuite);
	}

	public String getObjectInputs() {
		return ObjectInputs;
	}

	public void setObjectInputs(String objectInputs) {
		ObjectInputs = objectInputs;
		setObjectParams(loadTestCases(ObjectInputs));
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTestCaseType() {
		return TestCaseType;
	}

	public void setTestCaseType(String testCaseType) {
		TestCaseType = testCaseType;
	}

	public ArrayList<TestCaseBasicInfo> getTestCaseArray() {
		return testCaseArray;
	}

	public void setTestCaseArray(ArrayList<TestCaseBasicInfo> testCaseArray) {
		this.testCaseArray = testCaseArray;
	}

	public ArrayList<TestCaseBasicInfo> getObjectParams() {
		return objectParams;
	}

	public void setObjectParams(ArrayList<TestCaseBasicInfo> objectParams) {
		this.objectParams = objectParams;
	}

	/**
	 * 
	 * @param sheetName
	 * @param funcType
	 * @param rowIndex 
	 *        excel row is starting from 0, so +1 to represent the actual row for human readable
	 * @return
	 */
	public static String genObjectID(String sheetName, String funcType, int rowIndex) {
		return new StringBuilder().
				append(sheetName).
				append("_").
				append(funcType).
				append("_").
				append(rowIndex+1).
				toString();
	}
	
	public String toString(){
		return new StringBuilder().
				append(FuncType).
				toString();
	}
	
	/**
	 * 
	 * @param testCasesStr eg: CDC:runDeployGoal_CDC:2\nCDC:runDeployGoal_CDC:3
	 * seperated by "\n" between cases
	 * Chained TestDataObject creation will happen eg. runCheckGoalLock(Goal)->runDeployGoal_CDC:2(CDC)->PerformanceGoal:1(PerformanceGoal)
	 * @return
	 */
	
	public ArrayList<TestCaseBasicInfo> loadTestCases(String testCasesStr){
		ArrayList<TestCaseBasicInfo> testCaseArray = new ArrayList<TestCaseBasicInfo>();
		String[] testCases = testCasesStr.split("\n");
		try {
			HSSFWorkbook wb = new HSSFWorkbook(Config.getInstance().getTestFileInputStream());

			for (String testCase : testCases) {
				String[] testCase_array = testCase.split(":");
				String sheetName = testCase_array[0].trim();
				String funcType = testCase_array[1].trim();
				int rowNum = Integer.parseInt(testCase_array[2].trim());
				// try to load the testcase from here for testsuite or objectInputs
				TestCaseBasicInfo obj = POIUtils.loadTestCaseFromExcelRow(sheetName,
						funcType, rowNum, wb);
				if (obj == null){
					throw new RuntimeException("<b>ERROR: loadTestSuite In "+this.getFuncType()+"-->CAN NOT Find "+ funcType + " in " +sheetName +" row "+rowNum+"<b/>");
				}else{
					obj.setTestRunner(this.getTestRunner());
					testCaseArray.add(obj);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e){
			testCaseArray= null;
			e.printStackTrace();
		}

		return testCaseArray;
	}	

	public void setTestRunner(TestRunner testRunner) {
		this.testRunner = testRunner;
	}
	

}
	
