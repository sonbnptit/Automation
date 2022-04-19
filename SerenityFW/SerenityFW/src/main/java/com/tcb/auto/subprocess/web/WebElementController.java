package com.tcb.auto.subprocess.web;

import com.tcb.auto.utils.*;
import com.tcb.auto.utils.Compiler;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.webdriver.javascript.JavascriptExecutorFacade;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
			/**
			 * @author anhptn14: make a reverse WindowHandles order, because we often switch
			 *         to the last windows
			 */

			List<Set> list = new ArrayList(allWindows);
			Collections.sort(list, Collections.reverseOrder());
			Set<String> resultSet = new LinkedHashSet(list);

			for (String currentWindow : resultSet) {
				driver.switchTo().window(currentWindow);

				try {
//					setImplicitTimeout(TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR,
//							java.time.temporal.ChronoUnit.SECONDS);
//					WebElement element = driver.findElement(byLocator);
					// driver.manage().window().maximize();
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

	/**
	 * switch to a frame or iFrame
	 *
	 * @param driver
	 * @param        byLocator: frame locator
	 */
	public void switchFrame(WebDriver driver, By byLocator) {
		// waitPresent(driver, byLocator);
		WebElement iframe = driver.findElement(byLocator);
		driver.switchTo().frame(iframe);
	}

	public void switchToFrameFromRoot(WebDriver driver, By byLocator) {
		switchToTheRootFrame(driver);
		switchFrame(driver, byLocator);
	}

	public void switchToParentFrame(WebDriver driver) {
		driver.switchTo().parentFrame();
	}

	public void switchToTheRootFrame(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public String getValueJS(WebDriver driver, By byLocator) {
		return getAttributeJS(driver, byLocator, "value");
	}

	public String getTextJS(WebDriver driver, By byLocator) {
		return getAttributeJS(driver, byLocator, "textContent");
	}

	public String getAttributeColorJs(WebDriver driver, By byLocator) {
		return getAttributeJS(driver, byLocator, "color");
	}
	
	public String getAttributeClassJs(WebDriver driver, By byLocator) {
		return getAttributeJS(driver, byLocator, "class");
	}
	
	public String getAttributeJS(WebDriver driver, By byLocator, String attr) {
		if (attr == null || attr.isEmpty() || attr.equals("")) {
			attr = "value";
		}
		String javascriptCommand = getObjectFromJS(byLocator);
		StringBuilder commandBuilder = new StringBuilder();
		if (attr == "value") {
			commandBuilder.append("return ").append("!").append(javascriptCommand).append(" ? '' : ")
					.append(javascriptCommand).append(".value;");

		} else if (attr == "textContent") {
			commandBuilder.append("return ").append("!").append(javascriptCommand).append(" ? '' : ")
					.append(javascriptCommand).append(".textContent;");
		} else {
			commandBuilder.append("return ").append("!").append(javascriptCommand).append(" ? '' : ")
					.append(javascriptCommand).append(".getAttribute('").append(attr).append("');");
		}
		String commandExecute = commandBuilder.toString();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return (String.valueOf(valJS));
		return "";
	}

	/**
	 * Get object by JS
	 * 
	 * @param byLocator
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getObjectFromJS(By byLocator) {
		// get locatorValue
		String locatorValue = byLocator.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String javascriptCommand = "";

		if (byLocator instanceof By.ById)
			javascriptCommand = "document.getElementById('" + locatorValue + "')";

		else if (byLocator instanceof By.ByName)
			javascriptCommand = "document.getElementsByName('" + locatorValue + "')[0]";
		else if (byLocator instanceof By.ByClassName)
			javascriptCommand = "document.getElementsByClassName('" + locatorValue + "')[0]";
		else if (byLocator instanceof By.ByTagName)
			javascriptCommand = "document.getElementsByTagName('" + locatorValue + "')";

		else if (byLocator instanceof By.ByCssSelector)
			javascriptCommand = "document.querySelector('" + locatorValue + "')";
		else if (byLocator instanceof By.ByXPath)
			javascriptCommand = "document.evaluate(\"" + locatorValue
					+ "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
		else
			javascriptCommand = "document.querySelector('" + locatorValue + "')";
		return javascriptCommand;
	}

	/**
	 * Get by from a properties file name (ex: ipsh.login_vi.properties) in
	 * serenity.conf with key and locator param
	 * 
	 * @author anhptn14
	 * @param cc           to reduce call construction method of ConfigControler
	 *                     many time on the page object class TODO
	 * @param propFileName the key of properties file name
	 * @param key
	 * @param locatorParam
	 * @return
	 * @throws IOException
	 */
	public By getByFromProperties(ConfigController cc, String propFileName, String key, String... locatorParam) {
		String fileName = cc.LOCATOR_DIRECTORY_PATH + cc.getSpecificProperty(propFileName);
		String locatorInPropFile = null;
		try {
			locatorInPropFile = cc.getValueFromKey(fileName, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getBy(locatorInPropFile, locatorParam);
	}

	public List<WebElementFacade> getListElement(ConfigController cc, String propFileName, String stringLocator) {
		String fileName = cc.LOCATOR_DIRECTORY_PATH + cc.getSpecificProperty(propFileName);
		String locatorInPropFile = null;
		try {
			locatorInPropFile = cc.getValueFromKey(fileName, stringLocator);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (locatorInPropFile.contains("$")) {
			String[] infoLocator = locatorInPropFile.split("\\$");
			return findAll(infoLocator[1]);
		} else {
			return findAll(locatorInPropFile);
		}
	}

	/**
	 * Read multi propFileName and put to mapBy
	 * @param valueData
	 * @param propFileNames
	 * @return
	 */
	public static Map<String, LazyBy> getLazyMapByFromProperties(CaseInsensitiveMap<String, String> valueData,
			String propFileNames) {
		Map<String, LazyBy> mapBy = new HashMap<>();
		ConfigController cc = new ConfigController();

		if(Commons.isBlankOrEmpty(propFileNames)){ return mapBy; }	//blank mapBy
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

	/**
	 * Return By object from a string locator
	 * 
	 * @param  stringLocator ex: xpath$//input[@class='%s']
	 * @param locatorParam param of the locator if needed
	 * @return By object
	 */
	public static By getBy(String stringLocator, String... locatorParam) {
		if (stringLocator.contains("$")) {
			String[] infoLocator = stringLocator.split("\\$");
			return getLocator(infoLocator[0], infoLocator[1], locatorParam);
		} else {
			String locatorType = "";
			return getLocator(locatorType, stringLocator, locatorParam);
		}
	}

//	public List<By> getListBy(String stringLocator) {
//		if (stringLocator.contains("$")) {
//			String[] infoLocator = stringLocator.split("\\$");
////			return getLocator(infoLocator[0], infoLocator[1], locatorParam);
//			return findAll(infoLocator[1]);
//		} else {
//			String locatorType = "";
//			return getLocator(locatorType, stringLocator, locatorParam);
//		}
//	}

	/**
	 * return a instance of the By class based on the type of the locator this By
	 * can be used by the browser object in the actual test
	 */

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
	 * check if windows contains a byLocator present or not
	 * 
	 * @param driver
	 * @param byLocator
	 * @return
	 */
	private boolean isWindowPresent(WebDriver driver, By byLocator) {
		String oldWindow = "";
		try {
			oldWindow = driver.getWindowHandle();
		} catch (Exception e) {
			oldWindow = null;
		}

		Set<String> allWindows = driver.getWindowHandles();

		/*
		 * @author anhptn14: make a reverse WindowHandles order, because we often switch
		 * to the last windows
		 */

		List<Set<String>> list = new ArrayList(allWindows);
		Collections.sort(list, Collections.reverseOrder());
		Set<String> resultSet = new LinkedHashSet(list);
		for (String currentWindow : resultSet) {
			driver.switchTo().window(currentWindow);
			driver.switchTo().defaultContent();
			try {
				/*setImplicitTimeout(TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR,
						java.time.temporal.ChronoUnit.SECONDS);
			if (!driver.findElements(byLocator).isEmpty() || driver.findElements(byLocator).size() > 0) {
//				if (driver.findElement(byLocator) != null) {
					Commons.getLogger().debug(String.format("Window with locator %s is FOUND: %s ",
							byLocator.toString(), driver.getTitle()));
					return true;
				}*/
				if(element(byLocator).isCurrentlyVisible()){
					return true;
				}

			} catch (Exception e) {
				continue;
			} finally {
				//resetImplicitTimeout();
			}
		}
		// switch to old window if cannot find need window
		if (oldWindow != null)
			driver.switchTo().window(oldWindow);

		/*
		 * if (oldWindow != "") try {
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		return false;

	}

	private boolean isWindowPresentByUrl(WebDriver driver, String url) {
		String oldWindow = driver.getWindowHandle();

		Set<String> allWindows = driver.getWindowHandles();

		/*
		 * @author anhptn14: make a reverse WindowHandles order, because we often switch
		 * to the last windows
		 */

		List<Set<String>> list = new ArrayList(allWindows);
		Collections.sort(list, Collections.reverseOrder());
		Set<String> resultSet = new LinkedHashSet(list);
		for (String currentWindow : resultSet) {
			driver.switchTo().window(currentWindow);
			driver.switchTo().defaultContent();
			try {
				setImplicitTimeout(TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR,
						java.time.temporal.ChronoUnit.SECONDS);

				if (driver.getCurrentUrl().contains(url)) {
					Commons.getLogger()
							.debug(String.format("Window with url %s is FOUND: %s ", url, driver.getTitle()));
					return true;
				}
			} catch (Exception e) {
				continue;
			} finally {
				resetImplicitTimeout();
			}
		}
		// switch to old window if cannot find need window
		driver.switchTo().window(oldWindow);

		/*
		 * if (oldWindow != "") try {
		 *
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		return false;

	}

	public boolean isWindowPresent2(WebDriver driver, By byLocator) {
		String oldWindow = "";
		try {
			oldWindow = driver.getWindowHandle();
			if (!driver.findElements(byLocator).isEmpty() || driver.findElements(byLocator).size() > 0) {
				System.out.println("Found windows: " + driver.getTitle());
				return true;
			}
		} catch (Exception e) {
			// Nothing
		}
		//driver.switchTo().defaultContent();
		Set<String> allWindows = driver.getWindowHandles();
		for (String currentWindow : allWindows) {
			// if(currentWindow == oldWindow) continue;
			driver.switchTo().window(currentWindow);
			try {
				/*if (!driver.findElements(byLocator).isEmpty() || driver.findElements(byLocator).size() > 0) {
					System.out.println("Found windows: " + driver.getTitle());
					return true;
				}*/
				if(element(byLocator).isCurrentlyVisible()){
					return true;
				}
				Thread.sleep(100);
			} catch (Exception e) {
				continue;
			}
		}
		// switch to old window
		if (!Commons.isBlankOrEmpty(oldWindow))
			try {
				driver.switchTo().window(oldWindow);
			} catch (Exception e) {
				// Nothing
			}
		return false;
	}

	/**
	 * Wait for the window present and switch to the windows
	 * 
	 * @param driver
	 * @param byLocator
	 * @param second
	 * @throws InterruptedException
	 */
	public boolean waitForWindowPresent(WebDriver driver, By byLocator, int... second) {
		int waitingTime;
		if (second.length == 0) {
			waitingTime = TimeController.WAIT_FOR_WINDOWS_PRESENT;
		} else
			waitingTime = second[0];

		for (int i = 0; i < waitingTime; i++) {

			try {
				if (isWindowPresent2(driver, byLocator))
					return true;
				Thread.sleep(500);
			} catch (Exception e) {
				continue;
			}
		}

		Commons.getLogger().debug(
				String.format("Window with %s is NOT found after %s seconds", byLocator.toString(), waitingTime));
		return false;
	}

	public boolean waitForWindowPresentByUrl(WebDriver driver, String url, int... second) {
		int waitingTime;
		if (second.length == 0) {
			waitingTime = TimeController.WAIT_FOR_WINDOWS_PRESENT;
		} else
			waitingTime = second[0];

		for (int i = 0; i < waitingTime; i++) {

			try {
				if (isWindowPresentByUrl(driver, url))
					return true;
				Thread.sleep(1000);
			} catch (Exception e) {
				continue;
			}
		}

		Commons.getLogger().debug(String.format("Window with %s is NOT found after %s seconds", url, waitingTime));
		return false;
	}

	/**
	 * wait until this element is visible then doubleClick
	 *
	 * @param driver
	 * @param byLocator
	 */
	public void doubleClick(WebDriver driver, By byLocator) {
//		waitForRenderedElementsToBePresent(byLocator);
		waitForElementAppear(byLocator,TimeController.WAIT_FOR_WINDOWS_PRESENT);
		Actions builder = new Actions(driver);
		WebElement element = driver.findElement(byLocator);
		for (int i = 0; i < TimeController.WAIT_FOR_WINDOWS_PRESENT; i++) {
			try {
				builder.doubleClick(element).perform();
				break;
			} catch (WebDriverException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue;
			}
		}

	}

	/**
	 * sendFunctionKey without focus
	 *
	 * @param driver
	 * @param        key: TAB, ENTER, F1,....
	 * @throws Exception
	 */
	public void sendFunctionKey(WebDriver driver, Keys key) throws Exception {
		driver.switchTo().activeElement().sendKeys(key);
	}

	public void waitForMultiLoadingDone(By loadingIcon, int timeout) {
		int maxWaitingTime = timeout <= 0 ? TimeController.WAIT_FOR_WINDOWS_PRESENT : timeout;
		int count = 0;
		Commons.waitAction(TimeController.SYSTEM_DELAY);
		System.out.println("Start to wait for multi loading");
		boolean wait = true;
		while (wait) {
			try {
				/*waitForRenderedElementsToDisappear(loadingIcon);
				setImplicitTimeout(TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR,
						java.time.temporal.ChronoUnit.SECONDS);
				wait = element(loadingIcon).isDisplayed();*/
				wait = element(loadingIcon).isCurrentlyVisible();
			} catch (TimeoutException e1) {
				// System.out.println("Timeout -> retry wait");
			} catch (Exception e) {
				System.out.println("Finish loading");
				wait = false;
			} finally {
//				resetImplicitTimeout();
				Commons.waitAction(100);
				count++;
			}
//			if (count > maxWaitingTime / TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR) {
//				System.out.println("Wait to long, stop waiting");
//				break;
//			}

		}
	}

	/**
	 * Start waiting until all loading icons disappear.
	 * 
	 * @author anhptn14
	 * @param loadingIcon
	 */
	public void waitForMultiLoadingDone(By loadingIcon) {
		waitForMultiLoadingDone(loadingIcon, 0);
	}
	public boolean  waitForElementAppear(By element, int ...second) {
		int maxWaitingTime = TimeController.WAIT_FOR_WINDOWS_PRESENT * 2;
		if (second.length > 0) {
			maxWaitingTime = second[0] * 2;
		}
		int count = 0;
		//Commons.waitAction(TimeController.SYSTEM_DELAY);
		System.out.println("Start to wait for multi loading");
		boolean wait = true;
		while (wait && count < maxWaitingTime) {
			try {
    /*           waitForRenderedElementsToBePresent(element);
                setImplicitTimeout(TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR,
                        java.time.temporal.ChronoUnit.SECONDS);*/
				count++;
				Commons.waitAction(500);
				if (element(element).isCurrentlyVisible())
					return true;
//              getDriver().findElement(element);
			} catch (Exception e) {

			} finally {
				if (count > maxWaitingTime) {
					System.out.println("Wait to long, stop waiting");
					break;
				}

			}
		}
		return false;
	}


	 /**
     * select from a "select" tag by "option" value
     * <p>
     * <select><option>--select one option--</option><option>item
     * 1</option><option>item 2</option></select>
     *
     * @param driver
     * @param byLocator
     * @param option
     */
    public void select(WebDriver driver,By byLocator, String option) throws Exception {
    	waitForRenderedElementsToBePresent(byLocator);
         WebElement element = driver.findElement(byLocator);

        List <WebElement> optionsInnerText= element.findElements(By.xpath(".//option"));

         for(WebElement text: optionsInnerText){

        String textContent = text.getAttribute("textContent");
        String textValue = text.getAttribute("value");
        if(textContent.toLowerCase().equalsIgnoreCase(option)||textValue.equals(option)){
        	text.click();
       	   return;
        }
        }
        for(WebElement text: optionsInnerText){

            String  textContent = text.getAttribute("textContent");
            String textValue = text.getAttribute("value");
            if(textContent.toLowerCase().contains(option.toLowerCase())||textValue.equals(option)){

          	   text.click();
          	   return;
          	   }
             }
        System.out.println("No option");
          }

    /**
     * select when use div to selection
     */

    public void select(By byLocator, String option, boolean... isContains) {
    	element(byLocator).click();

    	WebElement div_container = element(byLocator).findElement(By.xpath("following-sibling::div"));

//    	waitFor(div_container);

		List<WebElement> optionsInnerText = element(div_container).findElements(By.xpath(".//div"));

		for (WebElement text : optionsInnerText) {

		   String textContent = text.getText();
			if (textContent.toLowerCase().equalsIgnoreCase(option)) {
				text.click();
				return;
			}
			if (isContains.length > 0 && isContains[0]) {
				if (textContent.toLowerCase().contains(option.toLowerCase())) {
					text.click();
					return;
				}
			}
    	}
    }

    public void select_input(By byLocator, String option) {
    	element(byLocator).sendKeys(option);

		WebElement div_container = element(byLocator).findElement(By.xpath("following-sibling::div"));

		waitFor(div_container);

		List<WebElement> optionsInnerText = element(div_container).findElements(By.xpath(".//div"));

		for (WebElement text : optionsInnerText) {

			String textContent = text.getText();
			boolean i = textContent.toLowerCase().contains(option.toLowerCase());
			if (textContent.toLowerCase().contains(option.toLowerCase())) {
				text.click();
				return;
			}
		}

	}

    /**
     * select from a "select" tag by "option" value
     * <p>
     * <select><option>--select one option--</option><option>item
     * 1</option><option>item 2</option></select>
     *
     * @param driver
     * @param byLocator
     * @param index
     */
    public void selectIndex(WebDriver driver, By byLocator, int index) throws Exception {
		try {
            // Waits for Max timeout seconds
            WebDriverWait wait = new WebDriverWait(driver, TimeController.SYSTEM_DELAY);

			// Wait until expected condition size of the dropdown increases and becomes more
			// than 1
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
                    Select select = new Select(driver.findElement(byLocator));
					return select.getOptions().size() > 1;
                }
            });

			// To select the first option
            Select select = new Select(driver.findElement(byLocator));
            select.selectByIndex(index);
		} catch (Throwable e) {
			System.out.println("Error found: " + e.getMessage());
        }
    }

	/**
	 * sendKey Object from JS
	 * 
	 * @param driver
	 * @param value
	 * @throws Exception Example run in console:
	 *                   document.getElementById("ext-comp-1173").dispatchEvent((new
	 *                   Event("click")))
	 */
	public void sendkeyJS(WebDriver driver, String value, By byLocator, String... nextAction) throws Exception {

        String javascriptCommand =  getObjectFromJS(byLocator);
		String commandExecute = javascriptCommand + ".value='" + value + "'";
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(commandExecute);

		if (nextAction.length > 0) {
            String listAction = nextAction[0];
			String[] lstAction = listAction.split(";");
			for (String action : lstAction) {
				String function = "if(document.createEventObject) {" + javascriptCommand + ".fireEvent(\"" + action
						+ "\");" + " } else {" + "var evt = document.createEvent(\"HTMLEvents\");" + "evt.initEvent(\""
						+ action + "\", false, true);" + javascriptCommand + ".dispatchEvent(evt);  }";
                js.executeScript(function);
            }
        }

		return;
    }

	/**
	 * CLick Object from JS
	 * 
	 * @param driver
	 * @param byElement
	 * @throws Exception
	 */
	public void clickJS(WebDriver driver, By byElement) {
		String javascriptCommand =  getObjectFromJS(byElement);
		String commandExecute = javascriptCommand + ".click()";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(commandExecute);
	}
	
	public int getSizeElementList(WebDriver driver, By itemBy) {
		// get List Elements
		try {
            return driver.findElements(itemBy).size();
		} catch (Exception ex) {
			return -1;
		}
	}

	public int getChildIndex(WebDriver driver, By parentBy, By findBy) {
	    try {
			// get parent node
            WebElement parentNode = driver.findElement(parentBy);
			// get child node
            WebElement findNode = driver.findElement(findBy);
			// get all children node
            List<WebElement> childrenList = parentNode.findElements(By.xpath("*"));
			for (int i = 0; i < childrenList.size(); i++) {
				if (childrenList.get(i).equals(findNode))
					return i + 1; // found
            }
		} catch (WebDriverException ex) {
			// log
        }
		return -1; // not found
	}

	// get Text List <tag>
	public String getTextList(WebDriver driver, By byLocator, String sep1) throws Exception {
		List<String> textList = getTextArrayList(driver, byLocator);
		if(textList.isEmpty()) return "";
		else return String.join(sep1, textList);
	}

	public List<String> getTextArrayList(WebDriver driver, By byLocator){
		List<String> textList = new ArrayList<>();
		List<WebElement> elements = driver.findElements(byLocator);
		for (WebElement element : elements) {
			if (!element.getText().trim().isEmpty()) {
				textList.add(element.getText().trim());
			}else{
				// if element get by text is null so get by value
				String value = Commons.isBlankOrEmpty( element.getAttribute("value")) ? "" :  element.getAttribute("value").trim();
				textList.add(value);
			}
		}
		return textList;
	}

	//add value, title to control select options
	public void setObjectSelectOptions(WebDriver driver, By byElement, String value, String title) {
		String javascriptCommand =  getObjectFromJS(byElement);
		String commandExecute = String.format("var select = %s; var el = document.createElement('option'); el.textContent = '"+ title +"'; el.value = '"+ value +"'; el.selected = '"+ true + "'; select.appendChild(el)", javascriptCommand);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(commandExecute);
	}
	
	//use jQuery execute a few actions not work when using javascript
	public void clickXpathSelector(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; $(handingXpath()).click()");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(commandExecute);
	}
	
	public void typingTextField(WebDriver driver, By byElement, String text) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; $(handingXpath()).val('%s')", text);
		JavascriptExecutorFacade js = new JavascriptExecutorFacade(driver);
		js.executeScript(commandExecute);
	}
	
	public boolean checkFieldDisable(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; function check() {return $(handingXpath()).prop('disabled')}; return check()");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return Boolean.valueOf(valJS.toString());
		return false;
	}
	
	public boolean isCheckedRadioOrCheckbox(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; function check() {return $(handingXpath()).prop('checked')}; return check()");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return Boolean.valueOf(valJS.toString());
		return false;
	}
	
	public void forcusElementInViews(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = "function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; function focus() {handingXpath()[0].focus()}; return focus();";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(commandExecute);
	}
	
	//Comparative input data is an object containing the fields on the display table
	//1. get all collum in table shown in page
	//2. map data get from db then map with 1-1
	//3. compare list object 
	//params: webdriver, xpath not id if id pls add //tagName[@id = 'idname']
	public List<String> getDataTableMapFields(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = "function handingXpath() {\r\n" + 
				"	var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null);\r\n" + 
				"	var xNodes = [];\r\n" + 
				"	var xRes;  \r\n" + 
				"	while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes};\r\n" + 
				"	function _returnDataInTable() {\r\n" + 
				"	var arrThead = [], arrTbody = [], fieldsLowerCase = [], _return = [], allDataRows = []; \r\n" + 
				"	$(handingXpath()).children('thead').children('tr').each(function() {\r\n" + 
				"		var rowThread=\"\"\r\n" + 
				"		$(this).find('th').each(function(){\r\n" + 
				"		var isClass = isClass = $(this).attr(\"class\") != undefined ? ($(this).attr(\"class\").includes('hidden') ? false : true): true\r\n" + 
				"		if(isClass) {\r\n" + 
				"			rowThread= rowThread + $(this).prop(\"innerText\").replace(\" \", \"_\") + \"\\n\";\r\n" + 
				"		}\r\n" + 
				"		});\r\n" + 
				"		arrThead.push(rowThread);\r\n" + 
				"	  });\r\n" + 
				"	  \r\n" + 
				"	  $(handingXpath()).children('tbody').children('tr').each(function() {\r\n" + 
				"		var rowTbody=\"\"\r\n" + 
				"		$(this).find('td').each(function(){\r\n" + 
				"		var isClass = isClass = $(this).attr(\"class\") != undefined ? ($(this).attr(\"class\").includes('hidden') ? false : true): true\r\n" + 
				"		if(isClass) {\r\n" + 
				"			rowTbody= rowTbody + $(this).prop(\"innerText\").replace(\"\\n\", \"/\") + \"\\n\";\r\n" + 
				"		}\r\n" + 
				"		});\r\n" + 
				"		arrTbody.push(rowTbody);\r\n" + 
				"	  });\r\n" + 
				"	  \r\n" + 
				"	  if(arrThead.length > 0) {\r\n" + 
				"			arrTheadFilter = arrThead.toString().split(\"\\n\").filter(function (el) {return el !=\"\";});\r\n" + 
				"			for(i =0; i < arrTheadFilter.length; i++) {\r\n" + 
				"				fieldsLowerCase.push(arrTheadFilter[i].toLowerCase());\r\n" + 
				"			}\r\n" + 
				"			for(it = 0; it < arrTbody.length; it ++ ) {  \r\n" + 
				"			   var test = arrTbody[it].split(\"\\n\").filter(function (el) {return el !=\"\";});\r\n" + 
				"			   allDataRows.push(test);\r\n" + 
				"			}\r\n" + 
				"			\r\n" + 
				"			allDataRows.forEach(row => {\r\n" + 
				"				let result = {};\r\n" + 
				"				row.forEach((row, i) => {result[fieldsLowerCase[i]] = row;});\r\n" + 
				"				_return.push(result);\r\n" + 
				"		  }); \r\n" + 
				"			\r\n" + 
				"	  }else {\r\n" + 
				"		  var filtered = arrTbody.filter(function (el) {\r\n" + 
				"			  return el !=\"\";\r\n" + 
				"			});\r\n" + 
				"		  var fields = filtered.splice(0,1)[0].split(\"\\n\").filter(function (el) {return el !=\"\";});\r\n" + 
				"			for(i =0; i < fields.length; i++) {\r\n" + 
				"				fieldsLowerCase.push(fields[i].replace(\" \", \"_\").toLowerCase());\r\n" + 
				"			}\r\n" + 
				"			console.log(fieldsLowerCase);\r\n" + 
				"		  for(it = 0; it < filtered.length; it ++ ) {  \r\n" + 
				"			  var test = filtered[it].split(\"\\n\").filter(function (el) {return el !=\"\";});\r\n" + 
				"			   allDataRows.push(test);\r\n" + 
				"		  }\r\n" + 
				"		  allDataRows.forEach(row => {\r\n" + 
				"			let result = {};\r\n" + 
				"			row.forEach((row, i) => {result[fieldsLowerCase[i]] = row;});\r\n" + 
				"			_return.push(result);\r\n" + 
				"		  }); \r\n" + 
				"	  }	  \r\n" + 
				"     return _return;\r\n" + 
				"}; return _returnDataInTable()";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return (List) valJS;
		return null;
	}
	
	public List<Map<String, String>> getDataEVNBillNumber(WebDriver driver, By byElement, String fields) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {\r\n" +
				"var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null);\r\n" +
				"var xNodes = [];\r\n" +
				"var xRes;\r\n" +
				"while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes};\r\n" +
				"function _returnDataEVNBill(fieldsLowerCase) {\n" +
				"var arrTbody = [],_return = [], allDataRows = []; \n" +
				"\t$(handingXpath()).children('div').children('table').each(function() {\n" +
				"\t\t$(this).children('tbody').children('tr').each(function() {\n" +
				"\t\t\tvar rowTbody=\"\"\n" +
				"\t\t\t$(this).find('td').each(function(){\n" +
				"\t\t\tvar isClass = isClass = $(this).attr(\"class\") != undefined ? ($(this).attr(\"class\").includes('hidden') ? false : true): true\n" +
				"\t\t\tif(isClass) {\n" +
				"\t\t\t\trowTbody= rowTbody + $(this).prop(\"innerText\").replace(\"\\n\", \"/\") + \"\\n\";\n" +
				"\t\t\t}\n" +
				"\t\t\t});\n" +
				"\t\t\tarrTbody.push(rowTbody);\n" +
				"\t\t  });\n" +
				"\t});\n" +
				"\tfor(it = 0; it < arrTbody.length; it ++ ) {  \n" +
				"\t   var test = arrTbody[it].split(\"\\n\").filter(function (el) {return (el !=\"\" && el !=\" \");});\n" +
				"\t   allDataRows.push(test);\n" +
				"\t}\n" +
				"\tallDataRows.forEach(row => {\n" +
				"\t\tlet result = {};\n" +
				"\t\trow.forEach((row, i) => {result[fieldsLowerCase.split(',')[i]] = row;});\n" +
				"\t\t_return.push(result);\n" +
				"\t }); \n" +
				"\t  \n" +
				"\t  return _return;\n" +
				"};return _returnDataEVNBill('%s')", fields);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return (List<Map<String, String>>) valJS;
		return null;
	}
	
	//params: driver, locator format xpath if id pls pass //tagname[@id ='idname']
	public String getValueHover(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = "function handingXpath() { \r\n" + 
				"	var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null);\r\n" + 
				"	var xNodes = [];\r\n" + 
				"	var xRes;\r\n" + 
				"	while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}\r\n" + 
				"	function getValueHover() {\r\n" + 
				"		return $(handingXpath()).hover(function() {}).prop('qtip');\r\n" + 
				"	};\r\n" + 
				" return getValueHover();";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return  (String) valJS;
		return "";
	}
	
	public boolean checkElDisplayed(WebDriver driver, By byElement) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = [];var xRes;  while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}; return $(handingXpath()).is(':visible')");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return  (boolean) valJS;
		return false;
	}
	
	//check element isInView or if not, scroll to that position
	public void checkElIfInView(WebDriver driver, By locator) {
		String element = getObjectFromJS(locator);
		String commandExecute = String.format("function checkIfInView(element){\r\n" + 
				"    var offset = element.offset().top  - $(window).scrollTop();\r\n" + 
				"    if(offset > window.innerHeight){\r\n" + 
				"        $('html,body').animate({scrollTop: offset}, 1000);\r\n" + 
				"        return false;\r\n" + 
				"    }\r\n" + 
				"   return true;\r\n" + 
				"}; checkIfInView(%s);", element);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(commandExecute);
	}
	
	//params: driver, locator format xpath if id pls pass //tagname[@id ='idname'], valOne, valTwo: values in the column
	//return: index row of table
	public int getIndexRowsTable(WebDriver driver, By byElement, String valOne, String valTwo) {
		String locatorValue = byElement.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
		String commandExecute = String.format("function handingXpath() {\r\n" + 
				"		var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null);var xNodes = [];var xRes;\r\n" + 
				"		while(xRes = xResult.iterateNext()) {xNodes.push(xRes)}; return xNodes}\r\n" + 
				"function getIndexRows() {\r\n" + 
				"	var index = 0;\r\n" + 
				"	var tds;\r\n" + 
				"	$(handingXpath()).children('tbody').children('tr').each(function(i, item){\r\n" + 
				"		tds = $(this).find('td');\r\n" + 
				"		tds.each(function(j, elem1){\r\n" + 
				"			tds.each(function(k, elem2){\r\n" + 
				"				if($(elem1)[0] != $(elem2)[0] && $(elem1).text().trim() == '%s' && $(elem2).text().trim() == '%s'){\r\n" + 
				"					index = i;\r\n" + 
				"				}\r\n" + 
				"			});\r\n" + 
				"		});\r\n" + 
				"	});\r\n" + 
				"	return index + 1;\r\n" + 
				"}; return getIndexRows()", valOne, valTwo);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object valJS = js.executeScript(commandExecute);
		if (valJS != null)
			return  Integer.parseInt(valJS.toString());
		return 1;
	}
	
	public void zoomInShowFullPage(WebDriver driver, By startElement, By endElement) {
	    Dimension initialSize = driver.manage().window().getSize();
	    int height = initialSize.getHeight();
	    int verticalStart = driver.findElement(startElement).getLocation().getY();
	    int verticalEnd = driver.findElement(endElement).getLocation().getY();
	    float percent = (float)(verticalEnd - verticalStart)/height;
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String comandExecute = String.format("document.body.style.zoom = '%s'", percent);
		executor.executeScript(comandExecute);
	}

	/**
	 * wait until this element is visible then send Key all
	 *
	 * @param driver
	 * @param byLocator
	 */
	public void sendKeyAll(WebDriver driver, By byLocator,String key) throws Exception {
		waitForElementAppear(byLocator);
		List<WebElement> elements = driver.findElements(byLocator);
		if(elements.size() == 0) return;
		for (WebElement element : elements) {
			for (int i = 0; i < TimeController.WAIT_FOR_WINDOWS_PRESENT; i++) {
				try {
					element.click();
					element.clear();
					element.sendKeys(key);
					break;
				} catch (WebDriverException e) {
					Thread.sleep(1000);
					continue;
				}
			}
		}
	}

	public void controlDatePikerWebApp(WebDriver driver, String valueDate) {
	    Actions action = new Actions(driver);
		try {
		    DateTimeFormatter dfm = DateTimeFormatter.ofPattern("dd-MMM-yyyy");  
		    Date valDateFormated=new SimpleDateFormat("dd/MM/yyyy").parse(valueDate);
		    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		    String[] valDate = formatter.format(valDateFormated).split("-");
		    By tagetDate = By.xpath("//span[text() = '"+valDate[0]+"']");
			By tagetMounth = By.xpath("(//span[text() = '"+valDate[1]+"'])[1]");
			By tagetYear = By.xpath("//span[text() = '"+valDate[2]+"']");
		    LocalDateTime curDateTime = LocalDateTime.now();  
		    String formattedDate = curDateTime.format(dfm);
		    String[] dmy = formattedDate.split("-");
		    By curDate = By.xpath("//span[text() = '"+dmy[0]+"']");
			By curMounth = By.xpath("(//span[text() = '"+dmy[1]+"'])[1]");
			By curYear = By.xpath("//span[text() = '"+dmy[2]+"']");
		    new Actions(driver).dragAndDrop(driver.findElement(curYear), driver.findElement(tagetYear)).build().perform();
			Commons.waitAction(1000);
			new Actions(driver).dragAndDrop(driver.findElement(curMounth), driver.findElement(tagetMounth)).build().perform();
			Commons.waitAction(1000);
			new Actions(driver).dragAndDrop(driver.findElement(curDate), driver.findElement(tagetDate)).build().perform();
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	}

	public void defaultValueDateBoxWebApp(WebDriver driver, LocalDate date, String id) {
		String js = String.format("var datebox = $('#%s');\n" +
				"var date = [%d, %d, %d];\n" +
				"datebox.datebox({'defaultValue': date});",
				id,
				date.getYear(),
				date.getMonthValue() - 1,
				date.getDayOfMonth());
		((JavascriptExecutor)driver).executeScript(js);
	}

	public void selectLastItem(WebDriver driver, By byLocator) throws Exception {
		WebElement element = driver.findElement(byLocator);
		List<WebElement> elements = element.findElements(By.tagName("option"));
		selectIndex(driver, byLocator, elements.size() - 1);
	}

	public boolean switchFrameRecursion(WebDriver driver, By byLocator) {
		try {
			WebElement element = driver.findElement(byLocator);
			//Found frame
			return true;
		} catch (Exception e) {
			//no element in frame -> try switch to inner frame
		}
		//try switch to inner frame
		try{
			List<WebElement> frameLst = driver.findElements(By.cssSelector("frame,iframe"));
			for(WebElement frame: frameLst){
				//switch to frame
				driver.switchTo().frame(frame);
				if(switchFrameRecursion(driver, byLocator)) return true;
				//no element found => switch parent frame
				driver.switchTo().parentFrame();
			}
		}catch (Exception ex){
		}
		//no element found
		return false;
	}

	public boolean switchFrameRecursionFromRoot(WebDriver driver, By byLocator) {
		switchToTheRootFrame(driver);
		return switchFrameRecursion(driver, byLocator);
	}

	/**
	 * Upload file via AutoIT
	 * @param filePath
	 * @throws Exception
	 */
	public void uploadFileViaAutoIT(String filePath)throws Exception{
		ConfigController cc = new ConfigController();
		String browser = cc.getProperty("webdriver.driver");
		if(browser.equalsIgnoreCase("firefox")) browser = "BROWSER_FIREFOX";
		Runtime.getRuntime().exec("cmd /c start " + Constants.FILE_UPLOAD_AUTOIT+" "+browser+ " "+ filePath);
	}
	
	public void closeDriver(WebDriver driver){
	    driver.quit();
    }

    public boolean isCurrWindowClosed(WebDriver driver){
		try {
			String curr = driver.getWindowHandle();
			Set<String> set = driver.getWindowHandles();
			return !set.contains(curr);
		} catch(NoSuchWindowException e){
			return true;
		}
	}

	// move to element by JS
	public void moveToElementJS(WebDriver driver,By byLocator) throws Exception{
	String locatorValue = byLocator.toString().replaceAll("By\\.[a-zA-Z]+:", "").trim();
	String commandExecute = "function handingXpath() { var xResult = document.evaluate(\""+locatorValue+"\", document, null, XPathResult.ANY_TYPE, null); var xNodes = []; var xRes; while(xRes = xResult.iterateNext()) { xNodes.push(xRes) }; return xNodes }; function test() { var t = handingXpath()[0]; t.focus(); } return test();";
	JavascriptExecutor js = (JavascriptExecutor) driver;
	Object valJS = js.executeScript(commandExecute);
	}
	public void mouseHover(WebDriver driver, By byLocator)throws Exception{
		waitForElementAppear(byLocator);
		WebElement element = driver.findElement(byLocator);
		for (int i = 0; i < TimeController.WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR; i++) {
			try {
				Actions act = new Actions(driver);
				act.moveToElement(element).build().perform();
				break;
			} catch (WebDriverException e) {
				Thread.sleep(1000);
				continue;
			}
		}
	}
}
