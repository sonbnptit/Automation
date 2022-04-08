package com.tcb.auto.subprocess.jscript;

import com.tcb.auto.utils.Constants;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author bachtx2
 * @since JDK1.7
 * @version 1.0.170418
 */
public class JScriptInvoker {

	// final static Logger log = LogManager.getLogger(JScriptInvoker.class);

	private static HashMap<String, ScriptEngine> mapJsEngine = new HashMap<>();
	private static HashMap<String, String> mapFunctionFile = new HashMap<>();

	/**
	 *
	 * @param scriptList
	 *            the full file path to the JavaScript file where functions are
	 *            defined
	 */
	public JScriptInvoker(String scriptList, Map<String, String> bindingMap) {
		if (mapJsEngine.size() == 0) {
			String[] scriptFileArr = scriptList.split(";");
			for (String scriptFile : scriptFileArr) {
				// init ScriptEngine
				getJSEngine(scriptFile, bindingMap);
			}
		}
	}

	public static void clearScriptEngine() {
		mapFunctionFile.clear();
		mapJsEngine.clear();
	}

	private static ScriptEngine getJSEngine(String jscriptFile, Map<String, String> bindingMap) {
		if (mapJsEngine.size() == 0 || !mapJsEngine.containsKey(jscriptFile)) {
			ScriptEngineManager scptManager = new ScriptEngineManager();

			ScriptEngine jsEngine = null;

			if (jscriptFile.endsWith("js")) {
				jsEngine = scptManager.getEngineByName("js");
			} else if (jscriptFile.endsWith("groovy")) {
				jsEngine = scptManager.getEngineByName("groovy");
				((GroovyScriptEngineImpl) jsEngine).getBindings(ScriptContext.ENGINE_SCOPE)
						.put(Constants.SCRIPT_BINDING_MAP, bindingMap);
			} else
				return null;

			try {
				jsEngine.eval(new FileReader(jscriptFile));
				Set<String> lstFunc = getInvokableList(jsEngine);
				for (String funcName : lstFunc) {
					mapFunctionFile.put(funcName, jscriptFile);
				}
			} catch (FileNotFoundException | ScriptException e) {
				// do nothing
				e.printStackTrace();
			}
			mapJsEngine.put(jscriptFile, jsEngine);
		}
		return mapJsEngine.get(jscriptFile);
	}

	/**
	 * Evaluates a script written in JavaScript language.
	 * 
	 * @param script
	 *            the script to evaluate
	 * @return value returned from this piece of script as a String or the
	 *         Exception Message if any exception occurs.
	 */
	public String eval(String script, String asNull) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			Object obj = engine.eval(script);

			return getObjectValueAsString(obj, asNull);
		} catch (ScriptException e) {
			return e.getClass() + ":" + e.getMessage();
		}

	}

	/**
	 * Invokes a function defined in a JavaScript file by the its name, parses
	 * necessary arguments and evaluates this function by JavaScript engine.
	 * 
	 * @param funcName
	 *            the name of function to invoke and evaluate
	 * @param params
	 *            the array of arguments to parse into this JavaScript function
	 * @param jscriptFile
	 *            the full path to JavaScript file
	 * @param asNull
	 *            NULL_TEXT_INDICATOR representing null value
	 * @return the value returned after evaluating the function as string.
	 *         NULL_TEXT_INDICATOR will be returned if the JavaScript function
	 *         returns <code>null</code>. In case any exception occurs, the
	 *         Exception Message is returned instead.
	 */
	public String invoke(String funcName, String[] params, String jscriptFile, String asNull) {
		try {
			ScriptEngine engine = getJSEngine(jscriptFile, null);
			Invocable inv = (Invocable) engine;
			String val = null;

			Object obj = inv.invokeFunction(funcName, (Object[]) params);
			val = getObjectValueAsString(obj, asNull);
			return val;
		} catch (ScriptException | NoSuchMethodException e) {
			return e.getClass() + ":" + e.getMessage();
		}
	}

	/**
	 * Compiles an expression by invoking and evaluating all JavaScript
	 * functions inside. JavaScript functions in this expression are recognized
	 * <b>only if</b> their names start with <code>func...</code>, following by
	 * an array of arguments and a pair of marks <b>(</b> and <b>)</b>. e.g.
	 * <code>"funcToUpperCase(abcde)"</code>.
	 * <p>
	 * Parameters are put inside a pair of marks <b>(</b> and <b>)</b>. If there
	 * are more than one parameter, they must be separated by a comma <b>,</b>
	 * <p>
	 * The part of the expression which is outside of JavaScript function will
	 * be preserved. e.g. The expression
	 * <code>"this is a funcToUpperCase(house)"</code> will be compiled into the
	 * <code>"this is a HOUSE"</code>, as long as the function
	 * <code>funcToUpperCase(str){...}</code> is already defined.
	 * 
	 * @param expression
	 *            the input expression to compile
	 * @param jsInjectionPattern
	 *            the regex pattern to distinguish a JavaScript function from
	 *            normal text
	 * @param jsContainerPattern
	 *            the regex pattern to check if the input expression contains
	 *            JavaScript function
	 * @param jsParamSepPattern
	 *            the regex pattern to define the separator between each
	 *            argument
	 * @param asNull
	 *            the NULL_TEXT_INDICATOR
	 * @return the output string after compiling the input expression.
	 *         NULL_TEXT_INDICATOR will be returned if JavaScript functions
	 *         return <code>null</code>. In case any exception occurs, the
	 *         Exception Message is returned instead.
	 */
	public String compile(String expression, String jsInjectionPattern, String jsContainerPattern,
			String jsParamSepPattern, String asNull) {
		try {

			jsInjectionPattern = jsInjectionPattern != null ? jsInjectionPattern : Constants.REGEX_JS_INJECTION;
			jsContainerPattern = jsContainerPattern != null ? jsContainerPattern : Constants.REGEX_JS_CONTAIN;
			jsParamSepPattern = jsParamSepPattern != null ? jsParamSepPattern : Constants.REGEX_JS_PARAMS_SEP;
			asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;

			while (expression.matches(jsContainerPattern)) {
				Pattern pattern = Pattern.compile(jsInjectionPattern);
				Matcher matcher = pattern.matcher(expression);
				while (matcher.find()) {
					// get function name
					String funcName = matcher.group(1);// tim functionName va
														// Function (có the dung
														// first ( va last )
					String params = matcher.group(2);
					String val = null;
					String[] paramsArr = params.split(jsParamSepPattern);// cach nhau dau , giưa cac param

					// get ScriptEngine by function name
					if (!mapFunctionFile.containsKey(funcName)) {
						throw new ScriptException("Not found function: '" + funcName + "'");
					}
					String scriptFile = mapFunctionFile.get(funcName);
					ScriptEngine engine = getJSEngine(scriptFile, null);
					Invocable inv = (Invocable) engine;

					Object obj = inv.invokeFunction(funcName, (Object[]) paramsArr);
					val = getObjectValueAsString(obj, asNull);
					expression = expression.replace(funcName + "(" + params + ")", val);

				}
			}
			expression = expression.replace(Constants.SYMB_COMMA, ",").replace(Constants.SYMB_COMMA_CAP, ",");
			expression = expression.replace(Constants.SYMB_LEFT_PARENTHESIS, "(")
					.replace(Constants.SYMB_LEFT_PARENTHESIS_CAP, "(");
			expression = expression.replace(Constants.SYMB_RIGHT_PARENTHESIS, ")")
					.replace(Constants.SYMB_RIGHT_PARENTHESIS_CAP, ")");
			return expression;
		} catch (Exception e) {
			return e.getClass() + ":" + e.getMessage();
		}
	}

	private String getObjectValueAsString(Object obj, String asNull) {
		String val;
		if (obj instanceof Boolean || obj instanceof Double || obj instanceof Integer || obj instanceof String)
			val = obj.toString();
		else if (obj != null)
			val = obj.getClass().getName();
		else
			val = asNull;

		return val;
	}

	public static Set<String> getInvokableList(ScriptEngine engine) {
		Set<String> lstFunc = new HashSet<>();

		if (engine instanceof GroovyScriptEngineImpl) {
			// groovy
			Class[] arrClass = ((GroovyScriptEngineImpl) engine).getClassLoader().getLoadedClasses();
			for (Class gvClsLoaded : arrClass) {
				Method[] arrMethod = gvClsLoaded.getDeclaredMethods();
				for (Method gvMethod : arrMethod) {
					lstFunc.add(gvMethod.getName());
				}
			}
		} else {
			// js
			Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
			for (String attr : bindings.keySet()) {
				try {
					if ("function".equals(engine.eval("typeof " + attr))) {
						lstFunc.add(attr);
					}
				} catch (ScriptException e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		return lstFunc;
	}

}
