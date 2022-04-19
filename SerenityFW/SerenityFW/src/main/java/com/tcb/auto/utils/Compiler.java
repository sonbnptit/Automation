package com.tcb.auto.utils;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcb.auto.subprocess.jscript.JScriptInvoker;
import com.tcb.auto.subprocess.web.WebElementController;

public class Compiler {
	private Map<String, String> valueData;
	private String jscriptFile;
	private WebElementController webElementController;
/**
 * 
 * @param valueData anhptn14 đổi sang dạng Map thay vì CaseInsensitiveMap để sử dụng cho các case cần Header case sensitive
 * @param jscriptFile
 */
	public Compiler(Map<String, String> valueData, String jscriptFile) {
		this.valueData = valueData;
		this.jscriptFile = jscriptFile;
		this.webElementController = new WebElementController();
	}

	public void setJscriptFile(String jscriptFile) {
		this.jscriptFile = jscriptFile;
	}

	public Compiler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Detect all JavaScript functions and evaluate them to get the values
	 * 
	 * @param text
	 * @param jscriptFile
	 * @return
	 * @throws IOException
	 */
	public String compileWithJscript(String text, String jscriptFile) {
		JScriptInvoker invoker = new JScriptInvoker(jscriptFile, valueData);
		text = convertParamToJS(text, jscriptFile);
		// if(text.length() <= Constants.MAX_LOG_LENGTH){
		System.out.println("JScriptInvoker: " + text);
		// }
		return invoker.compile(text, Constants.REGEX_JS_INJECTION, Constants.REGEX_JS_CONTAIN,
				Constants.REGEX_JS_PARAMS_SEP, Constants.NULL_TEXT_INDICATOR);
	}

	public String compileWithJscript(String text) {
		JScriptInvoker invoker = new JScriptInvoker(jscriptFile, valueData);
		text = convertParamToJS(text, jscriptFile);
		// if(text.length() <= Constants.MAX_LOG_LENGTH){
		System.out.println("JScriptInvoker: " + text);
		// }
		return invoker.compile(text, Constants.REGEX_JS_INJECTION, Constants.REGEX_JS_CONTAIN,
				Constants.REGEX_JS_PARAMS_SEP, Constants.NULL_TEXT_INDICATOR);
	}

	/**
	 * convert input text for js function with Detect variable names, which are
	 * placed inside a pair of double curly brackets <b>{{...}}</b>, and get
	 * their values from the list valueData and replace some special characters
	 * as ',', '(' and ')'
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public String convertParamToJS(String text, String jscriptFile) {
		/*
		 * if(text.contains("func")){ text=compileWithJscript(text,jscriptFile);
		 * }else
		 */ if (text.contains("{{")) {
			Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String propName = matcher.group(1);
				String propValue = valueData.get( propName.replaceAll(" ",""));
				if (propValue.contains(Constants.SEP_1)) {
					propValue = "[" + propValue + "]";
				}
				text = text.replace("{{"+propName+"}}", propValue.replace(",", Constants.SYMB_COMMA)
						.replace("(", Constants.SYMB_LEFT_PARENTHESIS).replace(")", Constants.SYMB_RIGHT_PARENTHESIS));
				/* TH xml */
				text = text.replaceAll("&amp;", "&");
				text = text.replaceAll("&gt;", ">");
				text = text.replaceAll("&lt;", "<");
			}
		}
		return text;
	}

	/**
	 * Detect variable names, which are placed inside a pair of double curly
	 * brackets <b>{{...}}</b>, and get their values from the list valueData
	 * where all variables of this test case are saved. Special characters which
	 * interrupt JavaScript function, e.g. <b>,</b> and <b>(</b> and <b>)</b>,
	 * are encoded.
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public String compileWithProperties(String text){
		Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
		Matcher matcher = pattern.matcher(text);
		boolean condition = matcher.find();
		while (condition) {
			String propName = matcher.group(1);
		String key = propName.replace(" ", "");
		String propValue = valueData.get(key);
		if (propValue == null) {
			propValue = "";
		}
		text = text.replace("{{" + propName + "}}", propValue);
		matcher = pattern.matcher(text);
		condition = matcher.find();
	}
		/**
		 * If value contain func in javascript, need to execute func before
		 * return value
		 */

		if (text.contains("func")) {
			text = compileWithJscript(text);
		}

		return text;
	}

	public String compileWithPropertiesParams(String text, String... params){
		Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
		Matcher matcher = pattern.matcher(text);
		boolean condition = matcher.find();
		int index = 0;
		while (condition) {
			String propName = matcher.group(1);
			String key = propName.replace(" ", "");
			String propValue = params[index];
			if (propValue == null) {
				propValue = "";
			}
			text = text.replace("{{" + propName + "}}", propValue);
			matcher = pattern.matcher(text);
			condition = matcher.find();
			index += 1;
		}
		/**
		 * If value contain func in javascript, need to execute func before
		 * return value
		 */

		if (text.contains("func")) {
			text = compileWithJscript(text);
		}

		return text;
	}


}
