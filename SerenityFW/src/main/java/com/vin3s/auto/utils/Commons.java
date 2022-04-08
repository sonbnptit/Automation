package com.vin3s.auto.utils;


import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.vin3s.auto.serenity.CommonScenarioSteps;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Commons {

	private static org.apache.logging.log4j.Logger _log = null;
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

	public static void waitAction(long milisecond) {
		try {
//			System.out.println("Wait for " + milisecond  + " milisecond");
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	public static String removeSpecialChar(String text) {
		if (Commons.isBlankOrEmpty(text))
			return "";
		return text.replaceAll("[^a-zA-Z0-9]", "");
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

	public static Logger getLogger() {
		if (_log == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String logFileName = "auto_" + dateFormat.format(new Date()) + ".log";
			System.setProperty("logFile", logFileName);
			_log = LogManager.getLogger();
		}
		return _log;
	}



	public static String getContentResourceFile(String fileName) {
		try {
			InputStream is = Commons.class.getClassLoader().getResourceAsStream(fileName);
			InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
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
