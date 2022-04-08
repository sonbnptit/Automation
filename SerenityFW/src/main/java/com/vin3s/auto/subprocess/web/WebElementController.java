package com.vin3s.auto.subprocess.web;

import com.vin3s.auto.utils.Compiler;
import com.vin3s.auto.utils.LazyBy;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.*;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.io.IOException;
import java.util.*;
import com.vin3s.auto.utils.ConfigController;
import com.vin3s.auto.utils.Commons;

public class WebElementController extends PageObject {

	/**
	 * Should use waitForWindowPresent (wait for the windows present then switch to
	 * the window) . Switch to current window from list windows case 1: switch to
	 * window via value param is name of window Case 2: switch to window that it
	 * contains locator Case 3: switch to last window
	 *
	 * @param driver
	 */
	@Deprecated 
	public void switchWindow(WebDriver driver, By byLocator, String... previousWindow) {
		// switch to window has name
		if (previousWindow.length != 0) {
			driver.switchTo().window(previousWindow[0]);
			return;
		}
		Set<String> allWindows = driver.getWindowHandles();
		// switch window contain locator
		driver.switchTo().defaultContent();
		Boolean check = false;
		if (byLocator != null) {

			List<Set> list = new ArrayList(allWindows);
			Collections.sort(list, Collections.reverseOrder());
			Set<String> resultSet = new LinkedHashSet(list);

			for (String currentWindow : resultSet) {
				driver.switchTo().window(currentWindow);

				try {
					if(element(byLocator).isCurrentlyVisible()){
						check = true;
						System.out.println(String.format("Window with locator %s is found: %s ", byLocator.toString(),
								driver.getTitle()));
						break;
					}
				} catch (Exception e) {
					continue;
				} finally {
					resetImplicitTimeout();
				}
			}
			if (!check) {
				System.out.println(String.format("Window with locator %s is not found", byLocator.toString()));
			}
		}
		// switch latest window
		else {
			for (String currentWindow : allWindows) {
				driver.switchTo().window(currentWindow);
			}
			driver.manage().window().maximize();
		}

	}

	public void switchWindowNative(WebDriver driver) {
		Set<String> allWindows = driver.getWindowHandles();
		for (String currentWindow : allWindows) {
			driver.switchTo().window(currentWindow);
		}
	}

	public void sendFunctionKey(WebDriver driver, Keys key) throws Exception {
		driver.switchTo().activeElement().sendKeys(key);
	}

	public static By getBy(String stringLocator, String... locatorParam) {
		if (stringLocator.contains("$")) {
			String[] infoLocator = stringLocator.split("\\$");
			return getLocator(infoLocator[0], infoLocator[1], locatorParam);
		} else {
			String locatorType = "";
			return getLocator(locatorType, stringLocator, locatorParam);
		}
	}

	public static By getLocator(String locatorType, String locatorValue, String... locatorParam) {
		By locator;
		if (locatorParam.length != 0) {
			locatorValue = String.format(locatorValue, locatorParam);
		}
		if (locatorType.equalsIgnoreCase("id"))
			locator = By.id(locatorValue);
		else if (locatorType.equalsIgnoreCase("name"))
			locator = By.name(locatorValue);
		else if ((locatorType.equalsIgnoreCase("classname")) || (locatorType.equalsIgnoreCase("class")))
			locator = By.className(locatorValue);
		else if ((locatorType.equalsIgnoreCase("tagname")) || (locatorType.equalsIgnoreCase("tag")))
			locator = By.tagName(locatorValue);
		else if ((locatorType.equalsIgnoreCase("linktext")) || (locatorType.equalsIgnoreCase("link")))
			locator = By.linkText(locatorValue);
		else if (locatorType.equalsIgnoreCase("partiallinktext"))
			locator = By.partialLinkText(locatorValue);
		else if ((locatorType.equalsIgnoreCase("cssselector")) || (locatorType.equalsIgnoreCase("css")))
			locator = By.cssSelector(locatorValue);
		else if (locatorType.equalsIgnoreCase("xpath"))
			locator = By.xpath(locatorValue);
		else if (locatorType.equalsIgnoreCase(""))
			locator = By.cssSelector(locatorValue);
		else
			locator = By.cssSelector(locatorValue);
		return locator;
	}

	/**
	 * switch to a frame or iFrame
	 *
	 * @param driver
	 * @param        byLocator: frame locator
	 */
	public void switchFrame(WebDriver driver, By byLocator) {
		WebElement iframe = driver.findElement(byLocator);
		driver.switchTo().frame(iframe);
	}

	public static Map<String, LazyBy> getLazyMapByFromProperties(CaseInsensitiveMap<String, String> valueData,
																 String propFileNames) {
		Map<String, LazyBy> mapBy = new HashMap<>();
		ConfigController cc = new ConfigController();

		//if(Commons.isBlankOrEmpty(propFileNames)){ return mapBy; }	//blank mapBy
		List<String> propFileList = Arrays.asList(propFileNames.split(";"));
		for(String propFile: propFileList){
			String fileName = cc.LOCATOR_DIRECTORY_PATH + cc.getSpecificProperty(propFile);
			try {
				Compiler com = new Compiler(valueData, "");
				Properties allProps = cc.getAllPropertiesFromPropertiesFile(fileName);
				allProps.forEach((key, value) -> {
					LazyBy lzBy = new LazyBy(com, value.toString().trim());
					mapBy.put(key.toString(), lzBy);
				});

			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		return mapBy;
	}

}
