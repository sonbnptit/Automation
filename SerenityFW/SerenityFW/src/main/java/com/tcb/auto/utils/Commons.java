package com.tcb.auto.utils;

import com.jbase.jremote.JRemoteException;
import com.tcb.auto.serenity.CommonPageObject;
import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.serenity.driver.MultiChromeDriver;
import com.tcb.auto.subprocess.t24.remote.RemoteCommand;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {
	public static final String RANDOM_NUMBER = "RANDOM_NUMBER";
	public static final String RANDOM_CHARACTER = "RANDOM_CHARACTER";
	public static final String RANDOM_MIX = "RANDOM_MIX";

	private static ScriptEngineManager scptManager = null;
	private static ScriptEngine jsEngine = null;

	private static Logger _log = null;

	private static ScriptEngine getJSEngine() {
		if (jsEngine == null) {
			scptManager = new ScriptEngineManager();
			jsEngine = scptManager.getEngineByName("js");
		}
		return jsEngine;
	}

	// get value between "(" and ")"
	public static String getKeyFromContent(String content) {
		int firstIndex = content.indexOf("(");
		int lastindex = content.lastIndexOf(")");
		return content.substring(firstIndex + 1, lastindex);
	}

	/**
	 * Get data has contain variable
	 * 
	 * @param valueData
	 * @param originValue
	 * @return
	 */
	public static String replaceValue(Map<String, String> valueData, String originValue) {
		int begin = originValue.indexOf("{{");
		int end = originValue.indexOf("}}");
		String variable = originValue.substring((begin), end + 2);
		String value = valueData.get(variable);
		originValue = originValue.replace(variable, value);
		return originValue;
	}

	/**
	 * Get SubString from startChar to endChar
	 * 
	 * @param originValue
	 * @param startCharAt
	 * @param endCharAt
	 * @return
	 */
	public static String subStringText(String originValue, String startCharAt, String endCharAt) {
		int firstIndex = originValue.indexOf(startCharAt);
		int lastindex = originValue.indexOf(endCharAt);
		String desValue = originValue.substring(firstIndex + 1, lastindex);
		return desValue;
	}

	/*
	 * get subString from first ( to last )
	 */
	public static String getKeyFromAction(String action) {
		int firstIndex = action.indexOf("(");
		int lastindex = action.lastIndexOf(")");
		return action.substring(firstIndex + 1, lastindex);
	}

	/**
	 * return a random string which is only number, only alphabet character or mix
	 * type
	 * 
	 * @param numberOfCharacter: number of character of the output
	 * @param randomType: RANDOM_NUMBER | RANDOM_CHARACTER | RANDOM_MIX
	 * @return
	 */
	public static String random(int numberOfCharacter, String... randomType) {
		StringBuilder sb = new StringBuilder(numberOfCharacter);
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		String onlyNumberString = "0123456789";
		String onlyAlpabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
		String template = null;
		if (randomType.length == 0) {
			template = AlphaNumericString;
		} else {
			if (randomType[0].equals(RANDOM_MIX))
				template = AlphaNumericString;
			if (randomType[0].equals(RANDOM_CHARACTER))
				template = onlyAlpabetString;
			if (randomType[0].equals(RANDOM_NUMBER))
				template = onlyNumberString;
		}

		for (int i = 0; i < numberOfCharacter; i++) {

			// generate a random indext of Template string
			int index = (int) (template.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(template.charAt(index));
		}

		return sb.toString();
	}

	/**
	 * Get list mao value via key ==> convert to fomat string 'a','b'...;
	 * 
	 * @param listData
	 * @param key
	 * @return
	 */
	public static String convertListToParamSQL(List<Map<String, String>> listData, String key) {
		String resultConvert = "";
		for (int i = 0; i < listData.size(); i++) {
			Map<String, String> map = listData.get(i);
			if (i == 0)
				resultConvert = resultConvert + "'" + map.get(key) + "'";
			else
				resultConvert = resultConvert + ",'" + map.get(key) + "'";

		}
		if (listData.size() == 0)
			resultConvert = "'null'";
		return resultConvert;
	}

	public static String convertListToParamSQL(List<String> listData) {
		String resultConvert = "";
		for (int i = 0; i < listData.size(); i++) {
			if (i == 0)
				resultConvert = resultConvert + "'" + listData.get(i) + "'";
			else
				resultConvert = resultConvert + ",'" + listData.get(i) + "'";
		}
		if (listData.size() == 0)
			resultConvert = "'null'";
		return resultConvert;
	}

	public static String getValueViaKey(List<Map<String, String>> listData, String keyCondition, String valueCondition,
			String keyTarget) {
		for (Map<String, String> map : listData) {
			if (map.get(keyCondition).equalsIgnoreCase(valueCondition))
				return map.get(keyTarget);

		}
		return null;
	}

	public static boolean isBlankOrEmpty(Object value) {
		if (value == null)
			return true;
		if (value instanceof String) {
			return value.toString().trim().isEmpty() || value.toString().trim().equals(Constants.NULL_TEXT_INDICATOR);
		}
		if (value instanceof List) {
			List lst = (List) value;
			return lst.size() == 0;
		}

		if (value instanceof Map) {
			Map map = (Map) value;
			return map.size() == 0;
		}

		return false;
	}

	public static String getTableCellValue(List<Map<String, String>> listResult, int rowIdx, String colKey) {
		if (isBlankOrEmpty(listResult))
			return Constants.NULL_TEXT_INDICATOR;
		if (rowIdx < 0 || listResult.size() <= rowIdx || !listResult.get(rowIdx).containsKey(colKey))
			return Constants.NULL_TEXT_INDICATOR;
		return listResult.get(rowIdx).get(colKey);
	}

	public static List<String> getTableColList(List<Map<String, String>> listResult, String colKey) {
		if (isBlankOrEmpty(listResult) || !listResult.get(0).containsKey(colKey))
			return null;

		List<String> colDataList = new ArrayList<>();
		for (Map<String, String> mapItem : listResult) {
			colDataList.add(mapItem.get(colKey));
		}
		return colDataList;
	}

	public static String funcFormatList(List<String> inputList, String sepFormat, String preItem, String sufItem) {
		if (isBlankOrEmpty(inputList))
			return Constants.NULL_TEXT_INDICATOR;
		StringBuilder builder = new StringBuilder();
		for (String item : inputList) {
			if (builder.length() > 0) {
				builder.append(sepFormat);
			}
			builder.append(preItem).append(item).append(sufItem);
		}
		return builder.toString();
	}

	public static String funcGetGroupRegex(String str, String regex, int groupIdx) {
		Pattern ptm = Pattern.compile(regex);
		Matcher match = ptm.matcher(str);
		if (match.find())
			return match.group(groupIdx);
		return Constants.NULL_TEXT_INDICATOR;
	}

	public static List<String> funcGetAllGroupRegex(String str, String regex, int groupIdx) {
		Pattern ptm = Pattern.compile(regex);
		Matcher match = ptm.matcher(str);
		List<String> listResult = new LinkedList<>();
		while (match.find()) {
			listResult.add(match.group(groupIdx));
		}
		return listResult;
	}

	/**
	 * Format string date to other format
	 * 
	 * @param date
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static String funcFormatDate(String date, String inFormat, String outFormat) {
		DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern(inFormat);
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(outFormat);
		LocalDate jdate = LocalDate.parse(date, formatterIn);
		return formatterOut.format(jdate);
	}

	public static String funcFormatDate(LocalDate date, String outFormat) {
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(outFormat);
		return formatterOut.format(date);
	}

	public static LocalDate funcGetLocalDate(String date, String inFormat) {
		DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern(inFormat);
		LocalDate jdate = LocalDate.parse(date, formatterIn);
		return jdate;
	}

	/**
	 * Get current data in format Get date today with format
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static LocalDate funcGetToday(String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate jdate = LocalDate.now();
		return jdate;
	}

	/**
	 *
	 * @param date1    ex: 2019-07-04
	 * @param date2    ex: 2019-07-15
	 * @param diffType ex: day, month, year or d, m, y
	 * @return => 11
	 */
	public static long funcGetDiffDates(LocalDate date1, LocalDate date2, String diffType) {

		if (diffType.toLowerCase().equals("day") || diffType.toLowerCase().equals("d")) {
			long days = ChronoUnit.DAYS.between(date1, date2);
			return days;
		}
		if (diffType.toLowerCase().equals("month") || diffType.toLowerCase().equals("m")) {
			long months = ChronoUnit.MONTHS.between(date1, date2);
			return months;
		}
		if (diffType.toLowerCase().equals("year") || diffType.toLowerCase().equals("y")) {
			long years = ChronoUnit.MONTHS.between(date1, date2);
			return years;
		}
		return 0;
	}

	public static LocalDate funcPlusDate(LocalDate date, int iPlusAmt, String plusType) {
		LocalDate jdate = date;
		if (plusType.toLowerCase().equals("day") || plusType.toLowerCase().equals("d")) {
			jdate = date.plusDays(iPlusAmt);
		}
		if (plusType.toLowerCase().equals("month") || plusType.toLowerCase().equals("m")) {
			jdate = date.plusMonths(iPlusAmt);
		}
		if (plusType.toLowerCase().equals("year") || plusType.toLowerCase().equals("y")) {
			jdate = date.plusYears(iPlusAmt);
		}
		return jdate;
	}

	/**
	 * Get last day of month
	 * 
	 * @param date
	 * @param dayofmt : day in month
	 * @return
	 */
	public static LocalDate funcGetDayOfMonth(LocalDate date, String dayofmt) {

		if (dayofmt.toLowerCase().equals("eom")) {
			LocalDate lastDay = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
			return lastDay;
		}
		if (dayofmt.matches("\\d+")) {
			int dayVal = Integer.parseInt(dayofmt);
			LocalDate dayOfMonth = date.withDayOfMonth(dayVal);
			return dayOfMonth;
		}
		// invalid dayofmt => return input date
		return date;
	}

	/**
	 * return curernt date or time in a defined format
	 * 
	 * @author anhptn14
	 * @param format
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static double convertToDouble(String value) {
		return Double.parseDouble(value);
	}

	public static double compareWithMinMax(double value, double min, double max) {
		if (min > value) {
			return min;
		} else if (max > 0 && value > max) {
			return max;
		} else {
			return value;
		}
	}

	public static double convertToNumber(String value, int munRoundUp) {
		BigDecimal roundfinal = new BigDecimal(value).setScale(munRoundUp, BigDecimal.ROUND_HALF_UP);
		return roundfinal.doubleValue();
	}

	public static void waitAction(long milisecond) {
		try {
//			System.out.println("Wait for " + milisecond  + " milisecond");
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert amount to double
	 * 
	 * @param lstAmt
	 * @return
	 */
	public static List<String> convertAmount(List<String> lstAmt) {
		List<String> lstDoubleAmt = new ArrayList<>();
		if (lstAmt != null && lstAmt.size() > 0) {
			for (String amount : lstAmt) {
				double doubleAmount = Double.parseDouble(amount);
				lstDoubleAmt.add(String.valueOf(doubleAmount));
			}
		}
		return lstDoubleAmt;
	}

	public static String getCusIdFromAccount(String acc) {
		if (isBlankOrEmpty(acc) || acc.length() != 14)
			return "";
		return acc.substring(3, 11);
	}

	public static Logger getLogger() {
		if (_log == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String logFileName = "auto_" + dateFormat.format(new Date()) + ".log";
			System.setProperty("logFile", logFileName);
			_log = LogManager.getLogger();
		}
		return _log;
	}

	public static String getAbsolutePath(String filePath) {
		filePath = filePath.replace('/', File.separatorChar);
		filePath = filePath.replace('\\', File.separatorChar);

		//remove .\ and ./
		if (filePath.startsWith("." + File.separatorChar)) {
			filePath = filePath.substring(2);
		}
		Path p = Paths.get(filePath);
		if (p.isAbsolute())
			return filePath;

		String currentDir = System.getProperty("user.dir");
		if(!currentDir.endsWith(File.separator)){
			currentDir += File.separator;
		}
		return currentDir + filePath;
	}

	/**
	 * Check if targetCls in child of parentCls
	 * 
	 * @param targetCls
	 * @param parentCls
	 * @return
	 */
	public static boolean isSubClassOf(Class<?> targetCls, Class<?> parentCls) {
		if (targetCls.equals(parentCls))
			return true;
		Class<?> superCls = targetCls.getSuperclass();
		while (superCls != null) {
			if (superCls.equals(parentCls))
				return true;
			superCls = superCls.getSuperclass();
		}
		return false;
	}

	/**
	 * Find first method in class by Name
	 * 
	 * @param objCls
	 * @param methodName
	 * @return
	 */
	public static Method findFirstMethodByName(Class<?> objCls, String methodName) {
		// get all method in class
		Method[] methods = objCls.getMethods();
		// find method
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName))
				return methods[i];
		}
		return null;
	}

	/**
	 * Check a method has annotation
	 * 
	 * @param method
	 * @param        annotationList: ex: @Step
	 * @return
	 */
	public static boolean checkMethodHasAnnotation(Method method, Class... annotationList) {
		if (annotationList == null || annotationList.length == 0)
			return true;
		for (int i = 0; i < annotationList.length; i++) {
			if (!method.isAnnotationPresent(annotationList[i]))
				return false;
		}
		return true;
	}

	/**
	 * remove currencyAmount, "," in list amount, Number fix: total number decimal
	 * example: working balance is VND 1,000,000,000;1,000.8 return:
	 * 1000000000.00;1000.80
	 * 
	 * @param listAmount
	 * @param numberfix
	 * @return
	 */
	public static String convertListAmountToFormat(String listAmount, int numberfix) {
		listAmount = listAmount.replaceAll("[^0-9;.]", "");
		String arrayAmounts[] = listAmount.split(";");
		StringBuilder builder = new StringBuilder();
		for (String perAmount : arrayAmounts) {
			perAmount = String.format("%." + numberfix + "f", Double.parseDouble(perAmount));
			if (builder.length() > 0) {
				builder.append(";");
			}
			builder.append(perAmount);
		}
		return builder.toString();
	}

	public static String uniqChar(String str) {
		StringBuilder sb = new StringBuilder();
		str.chars().distinct().forEach(c -> sb.append((char) c));
		return sb.toString();
	}

	public static String getSysUserDir() {
		return System.getProperty("user.dir");
	}

	public static boolean writeTextFile(String filePath, String content, Charset charset) {
		try {
			Files.write(Paths.get(getAbsolutePath(filePath)), content.getBytes(charset));
			return true;
		} catch (IOException ex) {
			getLogger().warn(ex.getMessage());
			return false;
		}
	}

	public static String pretyDisplayListMap(List<Map<String, String>> list) {
		String output = "";
		for (Map<String, String> map : list) {
			for (String key : map.keySet()) {
				output = output + key + ":" + map.get(key) + "\n";
			}
			output = output + "\n\n";
		}
		return output;
	}

	/**
	 * Print the <b>data</b> to the <b>outputFile</b> with <b>seperator</b> Can add
	 * logging time or not. The content of the file is expandable
	 * 
	 * @param data       : CaseInsensitiveMap
	 * @param outputFile direct path to the output file
	 * @param seperator  seperator between pair of "key=value"
	 * @param logTime    true/fasle to add the logging time or not
	 * @author anhptn14
	 */
	public void printOutDataMap(CaseInsensitiveMap<String, String> data, String outputFile, String seperator,
			boolean logTime) {
		FileOutputStream f;
		try {
			f = new FileOutputStream(outputFile, true);
			PrintStream p = new PrintStream(f);

			for (String key : data.keySet()) {
				p.print(key + "=" + data.get(key) + seperator);
			}
			if (logTime) {
				LocalDateTime current = LocalDateTime.now();
				DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' hh:mm a");
				p.print(current.format(FOMATTER));
			}
			p.println("");
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reportLog(String title) {
		Serenity.recordReportData().withTitle(title);
	}

	public static void reportLog(String title, String content) {
		Serenity.recordReportData().withTitle(title).andContents(content);
	}

	public static void log4jAndReport(String title) {
		getLogger().debug(title);
		Serenity.recordReportData().withTitle(title).andContents("");
	}

	public static void log4jAndReport(String title, String content) {
		getLogger().debug(title);
		getLogger().debug(content);
		Serenity.recordReportData().withTitle(title).andContents(content);
	}

	public static void createDirectoryIfNotExists(String dirPath) {
		String fullPath = Commons.getAbsolutePath(dirPath);
		File directory = new File(String.valueOf(fullPath));
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	public static String getLatestFile(String folderPath, long downloadTime, String... extFilter) {
		File dir = new File(folderPath);
		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				for (String ext : extFilter) {
					if (name.endsWith(ext)) {
						return true;
					}
				}
				return false;
			}
		};
		File[] listFile = dir.listFiles(fileFilter);
		if (listFile == null || listFile.length == 0)
			return "";
		Arrays.sort(listFile, Comparator.comparingLong(File::lastModified).reversed());
		File firstFile = listFile[0];
		if (firstFile.lastModified() < downloadTime)
			return ""; // no new file download
		return listFile[0].getAbsolutePath();
	}

//	@Test
	public void Test() throws JRemoteException, UnsupportedEncodingException {
		String filePath = "\\a2a\\data\\testFile.txt";
		String fullPath = Commons.getAbsolutePath(filePath);
		Assert.assertEquals("D:\\Project\\iGTS\\automationframework\\MavenAutoTest_iGTB\\test\\a2a\\data\\testFile.txt",
				fullPath);
		filePath = "D:/Project/iGTS\\automationframework\\MavenAutoTest_iGTB\\test\\a2a/data/testFile.txt";
		fullPath = Commons.getAbsolutePath(filePath);
		Assert.assertEquals("D:\\Project\\iGTS\\automationframework\\MavenAutoTest_iGTB\\test\\a2a\\data\\testFile.txt",
				fullPath);
		System.out.println(fullPath);

		RemoteCommand t24Remote = RemoteCommand.getDefaultConnect();
		t24Remote.connect();
		String commnad = "LIST F.AI.PER.LOCAL.BEN.CONCAT.TCB WITH @ID EQ HIENNX02 BENEFICIARY.ID";
		List<Map<String, String>> results = t24Remote.jqlListSubroutine(commnad, "*");
		System.out.println(results);

		String ctCommand = "CT F.AI.PER.LOCAL.BEN.CONCAT.TCB HIENNX02";
		String resultStr = t24Remote.command(ctCommand);
		System.out.println(resultStr);
	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	public static String randCharMapWithRegx(int length, String regexExpression) {
		// String AlphaNumericString =
		// "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+-{}[]',.?\\\"/*";
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+-[]',.?\\\"/*";
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		String strs = sb.toString().replaceAll(regexExpression, "");
		if (strs.length() == 0) {
			strs = randCharMapWithRegx(length, regexExpression);
		} else {
			strs = sb.toString().replaceAll(regexExpression, "");
		}
		return strs;
	}

	public static void compilerDataMap(CaseInsensitiveMap<String, String> data, String jsScript) {
		Compiler compiler = new Compiler(data, jsScript);

		data.forEach((key, value) -> {
			if (value.contains("func") || value.contains("{{")) {
				data.put(key, compiler.compileWithJscript(value));
			}
		});
	}

	public static Map<String, String> getMapRowInList(List<Map<String, String>> valueData, String keyColum,
			String keyValue) {
		if (Commons.isBlankOrEmpty(valueData) || !valueData.get(0).keySet().contains(keyColum))
			return null;
		for (Map<String, String> rowMap : valueData) {
			if (keyValue.equals(rowMap.get(keyColum)))
				return rowMap;
		}
		return null;
	}

	public static List<String> split(String str, String regex) {
		List<String> results = new ArrayList<>();
		if (isBlankOrEmpty(str))
			return results;
		str = str.trim();
		return Arrays.asList(str.split(regex));
	}

	public static String removeSpecialChar(String text) {
		if (Commons.isBlankOrEmpty(text))
			return "";
		return text.replaceAll("[^a-zA-Z0-9]", "");
	}

	public static String removeSpecialChar(String text, String allowChar) {
		if (Commons.isBlankOrEmpty(text))
			return "";
		if (allowChar == null)
			allowChar = "";
		return text.replaceAll("[^a-zA-Z0-9" + allowChar + "]", "");
	}

	public static <E> E getStepClass(Object testFlowObj, Class<E> stepClazz) {
		if (!ScenarioSteps.class.isAssignableFrom(stepClazz)) {
			Commons.getLogger().warn("Not found step class");
			return null;
		}
		// find all Steps Field in testObj
		List<Field> listStepField = FieldUtils.getFieldsListWithAnnotation(testFlowObj.getClass(), Steps.class);
		for (Field stepField : listStepField) {
			// check if type of file is stepClazz
			if (stepField.getType().equals(stepClazz)) {
				try {
					Object stepObj = FieldUtils.readField(stepField, testFlowObj, true);
					return (E) stepObj;
				} catch (IllegalAccessException e) {
					Commons.getLogger().warn("Not found step class");
					return null;
				}
			}

		}
		Commons.getLogger().warn("Not found step class");
		return null;
	}

	public static void setDataAllSteps(Object testFlowObj, CaseInsensitiveMap<String, String> data) {
		// find all Steps Field in testObj
		List<Field> fieldList = FieldUtils.getAllFieldsList(testFlowObj.getClass());
		for (Field stepField : fieldList) {
			// check if type of field is CommonScenarioSteps
			if (CommonScenarioSteps.class.isAssignableFrom(stepField.getType())) {
				try {
					CommonScenarioSteps steps = (CommonScenarioSteps) FieldUtils.readField(stepField, testFlowObj,
							true);
					steps.setPageData(data);
				} catch (Exception e) {
					getLogger().warn(e.getMessage());
				}
			}
		}
	}

	public static void switchWebDriver(String session, Object flow) {
		if (flow == null)
			return;
		WebDriver webDriver = MultiChromeDriver.getInstance().switchOrInitDriver(session);
		List<Field> fieldList = FieldUtils.getAllFieldsList(flow.getClass());
		int count = 0;
		for (Field field : fieldList) {
			count++;
			// if (Commons.isSubClassOf(field.getType(), CommonPageObject.class)) {
			if (CommonScenarioSteps.class.isAssignableFrom(field.getType())) {
				try {
					CommonScenarioSteps steps = (CommonScenarioSteps) FieldUtils.readField(field, flow, true);
					steps.setPageDriver(webDriver);
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				}

			}
		}
	}

	public static String funcGetAccountType(String category) {
		if ("1252".equals(category) || "21051".equals(category) || "21052".equals(category) || "21053".equals(category)
				|| "21054".equals(category) || "21055".equals(category) || "21056".equals(category)
				|| "21061".equals(category) || "21003".equals(category) || "2001".equals(category)
				|| "1411".equals(category))
			return "Loan payment";
		else if ("3005".equals(category) || "3040".equals(category) || "3050".equals(category)
				|| "3025".equals(category) || "3032".equals(category) || "21004".equals(category)
				|| "21006".equals(category) || "21005".equals(category))
			return "Term deposit";
		else if ("1001".equals(category) || "1004".equals(category) || "1012".equals(category)
				|| "1020".equals(category) || "1021".equals(category) || "1023".equals(category)
				|| "1306".equals(category) || "3006".equals(category))
			return "Current account";
		else if ("1002".equals(category) || "1005".equals(category) || "1009".equals(category)
				|| "1010".equals(category) || "1015".equals(category) || "1016".equals(category)
				|| "1024".equals(category) || "1025".equals(category) || "1031".equals(category)
				|| "1032".equals(category) || "1050".equals(category) || "1061".equals(category)
				|| "1062".equals(category) || "1063".equals(category) || "1064".equals(category)
				|| "1065".equals(category) || "1066".equals(category) || "1067".equals(category)
				|| "1068".equals(category) || "1069".equals(category) || "1070".equals(category))
			return "Capital account";
		else if ("1008".equals(category) || "1018".equals(category))
			return "Overdraft account";
		else if ("3021".equals(category))
			return "Savings account";
		return "";
	}

	public static String removeNumberFormat(String number) {
		if (number == null)
			return number;
		return number.replaceAll("[^0-9\\.+-]", "");
	}

	public static String getContentResourceFile(String fileName) {
		try {
			InputStream is = Commons.class.getClassLoader().getResourceAsStream(fileName);
			InputStreamReader streamReader =
					new InputStreamReader(is, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			StringBuilder fileContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				fileContent.append(line).append(System.lineSeparator());
			}
			return fileContent.toString();
		} catch (IOException ex) {
			return "";
		}
	}
}
