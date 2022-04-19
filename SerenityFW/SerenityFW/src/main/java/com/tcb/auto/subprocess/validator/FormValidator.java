package com.tcb.auto.subprocess.validator;

import com.tcb.auto.subprocess.excel.ExcelDriver;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.AssertionEvaluator;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.Compiler;
import com.tcb.auto.utils.Constants;
import net.thucydides.core.annotations.Step;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FormValidator {
    By checkBy;
    By submitBy;

    String excelFile;
    String sheetName;
    Compiler compiler;
    WebDriver driver;
    IMessageValidation messageValidation;

    boolean useSendKeyJs;
    boolean checkAssert;

    List<ValidationItem> validationData;

    Logger log = Commons.getLogger();

    /**
     * Validate input element by data in excel file, with message validation as alert, popup, window popup or custom function
     * @param excelFile
     * @param sheetName
     * @param compiler
     * @param driver
     * @param checkBy
     * @param submitBy
     * @param messageValidation
     * @throws IOException
     */
    public FormValidator(String excelFile, String sheetName, Compiler compiler, WebDriver driver, By checkBy, By submitBy, IMessageValidation messageValidation) throws IOException {
        this.excelFile = excelFile;
        this.sheetName = sheetName;
        this.compiler = compiler;
        this.driver = driver;
        this.checkBy = checkBy;
        this.submitBy = submitBy;
        this.messageValidation = messageValidation;

        //get validationData from excel file
        validationData = new LinkedList<>();
        ExcelDriver excelDriver = new ExcelDriver();
        List<Map<String, String>> lstDataValidate = excelDriver.getDataFromSheetName(excelFile, sheetName, "");
        lstDataValidate.forEach(map -> {
            ValidationItem item = new ValidationItem(
                    map.get("No"),
                    map.get("Description"),
                    compiler.compileWithProperties(map.get("Value Validate")),
                    map.get("Expected Result")
            );
            validationData.add(item);
        });
    }

    public boolean isUseSendKeyJs() {
        return useSendKeyJs;
    }

    public void setUseSendKeyJs(boolean useSendKeyJs) {
        this.useSendKeyJs = useSendKeyJs;
    }

    public boolean isCheckAssert() {
        return checkAssert;
    }

    public void setCheckAssert(boolean checkAssert) {
        this.checkAssert = checkAssert;
    }

    public List<ValidationItem> getValidationData() {
        return validationData;
    }

    public List<ValidationItem> doAllValidation(){

        validationData.forEach(validationItem -> {
            doValidationItem(validationItem);
        });

        return validationData;
    }

    public void doValidationItem(ValidationItem validationItem){
        WebElementController wc = new WebElementController();
        AssertionEvaluator ss = new AssertionEvaluator();

        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
        if (wait.until(ExpectedConditions.presenceOfElementLocated(checkBy)) == null) {
            log.warn("Not found check element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        }else if (wait.until(ExpectedConditions.presenceOfElementLocated(submitBy)) == null){
            log.warn("Not found submit element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        } else{

            WebElement checkElement = driver.findElement(checkBy);
            WebElement submitElement = driver.findElement(submitBy);
            //input text
            checkElement.clear();
            if(useSendKeyJs){
                StringBuilder jsCode = new StringBuilder().append(wc.getObjectFromJS(checkBy)).append(".value='").append(validationItem.getValueValidate()).append("'");
                JavascriptExecutor js =(JavascriptExecutor)driver;
                js.executeScript(jsCode.toString());
            }else{
                checkElement.sendKeys(validationItem.getValueValidate());
            }
            //press submit button
            submitElement.click();
            wc.waitForMultiLoadingDone(By.xpath("//div[contains(@class,'x-mask-loading')]"));

            //get error message
            String message = messageValidation.getMessage();
            log.warn("Validation message: " + message);

            boolean check = ss.assertEqual(message, validationItem.getExpectedResult());
            if(checkAssert) {
                Assert.assertTrue(check);
            }
            validationItem.setResult(check ? Constants.TEST_PASS : Constants.TEST_FAILED);
        }
    }

    public void doValidationTabItem(ValidationItem validationItem){
        WebElementController wc = new WebElementController();
        AssertionEvaluator ss = new AssertionEvaluator();

        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
        if (wait.until(ExpectedConditions.presenceOfElementLocated(checkBy)) == null) {
            log.warn("Not found check element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        }else if (wait.until(ExpectedConditions.presenceOfElementLocated(submitBy)) == null){
            log.warn("Not found submit element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        } else{

            WebElement checkElement = driver.findElement(checkBy);
            WebElement submitElement = driver.findElement(submitBy);
            //input text
            checkElement.clear();
            if(useSendKeyJs){
                StringBuilder jsCode = new StringBuilder().append(wc.getObjectFromJS(checkBy)).append(".value='").append(validationItem.getValueValidate()).append("'");
                JavascriptExecutor js =(JavascriptExecutor)driver;
                js.executeScript(jsCode.toString());
            }else{
                checkElement.sendKeys(validationItem.getValueValidate());
            }

            try {
                wc.sendFunctionKey(driver, Keys.TAB);
            } catch (Exception e) {
                e.printStackTrace();
            }
            wc.waitForMultiLoadingDone(By.xpath("//div[contains(@class,'x-mask-loading')]"));

            //get error message
            String message = messageValidation.getMessage();
            log.warn("Validation message: " + message);

            boolean check = ss.assertEqual(message, validationItem.getExpectedResult());
            if(checkAssert) {
                Assert.assertTrue(check);
            }
            validationItem.setResult(check ? Constants.TEST_PASS : Constants.TEST_FAILED);
        }
    }

    public void doValidationItemSelectBox(ValidationItem validationItem){
        WebElementController wc = new WebElementController();
        AssertionEvaluator ss = new AssertionEvaluator();

        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
        if (wait.until(ExpectedConditions.presenceOfElementLocated(checkBy)) == null) {
            log.warn("Not found check element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        }else if (wait.until(ExpectedConditions.presenceOfElementLocated(submitBy)) == null){
            log.warn("Not found submit element");
            validationItem.setResult(Constants.TEST_FAILED);
            return;
        } else{

            WebElement checkElement = driver.findElement(checkBy);
            WebElement submitElement = driver.findElement(submitBy);

            if(useSendKeyJs){
                StringBuilder jsCode = new StringBuilder().append(wc.getObjectFromJS(checkBy)).append(".value='").append(validationItem.getValueValidate()).append("'");
                JavascriptExecutor js =(JavascriptExecutor)driver;
                js.executeScript(jsCode.toString());
            }else{
                checkElement.sendKeys(validationItem.getValueValidate());
            }

            checkElement.click();
            try {
                wc.sendFunctionKey(driver, Keys.TAB);
            } catch (Exception e) {
                e.printStackTrace();
            }
            wc.waitForMultiLoadingDone(By.xpath("//div[contains(@class,'x-mask-loading')]"));

            //get error message
            String message = messageValidation.getMessage();
            log.warn("Validation message: " + message);

            boolean check = ss.assertEqual(message, validationItem.getExpectedResult());
            if(checkAssert) {
                Assert.assertTrue(check);
            }
            validationItem.setResult(check ? Constants.TEST_PASS : Constants.TEST_FAILED);
        }
    }

    public int getTestStatusCount(String status){
        if(Commons.isBlankOrEmpty(validationData)) return 0;
        int count = 0;
        for(ValidationItem item: validationData){
            if(item.getResult().equals(status)){
                count++;
            }
        }
        return count;
    }

    public String getSummaryReport(){
        StringBuilder builder = new StringBuilder();
        if(Commons.isBlankOrEmpty(validationData)){
            builder.append("No Data");
            return builder.toString();
        }
        //get pass case count
        int testPassCount = getTestStatusCount(Constants.TEST_PASS);
        //get failed case count
        int testFailedCount = getTestStatusCount(Constants.TEST_FAILED);

        builder.append("Test pass: ").append(testPassCount).append('\n')
                .append("Test failed: ").append(testFailedCount).append('\n')
                .append("DETAIL:").append('\n');
        //get detail list
        validationData.forEach(item ->{
            builder.append("Case: ").append(item.getNo()).append(" - ").append(item.getDescription())
                    .append(": ").append(item.getResult()).append('\n');
        });

        return builder.toString();
    }

    public static class ImpBrowserPopupMessage implements IMessageValidation{
        WebDriver driver;

        public ImpBrowserPopupMessage(WebDriver driver) {
            this.driver = driver;
        }

        @Override
        public String getMessage() {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
            if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
                return "";
            } else{
                Alert alert = driver.switchTo().alert();
                String message = alert.getText();
                alert.dismiss();
                return message;
            }
        }
    }

    public static class ImpPopupMessage implements IMessageValidation{
        By messageBy;
        By closeBy;
        WebDriver driver;

        public ImpPopupMessage(WebDriver driver, By messageBy, By closeBy) {
            this.driver = driver;
            this.messageBy = messageBy;
            this.closeBy = closeBy;
        }

        @Override
        public String getMessage() {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
            if (wait.until(ExpectedConditions.presenceOfElementLocated(messageBy)) == null) {
                return "";
            }else {
                WebElement messageElement = driver.findElement(messageBy);
                String message = messageElement.getText();
                if(closeBy != null){
                    try {
                        WebElement closeElement = driver.findElement(closeBy);
                        closeElement.click();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                return message;
            }
        }
    }

    public static class ImpWindowPopupMessage implements IMessageValidation{
        By messageBy;
        WebDriver driver;

        public ImpWindowPopupMessage(WebDriver driver, By messageBy) {
            this.driver = driver;
            this.messageBy = messageBy;
        }

        @Override
        public String getMessage() {
            //get current windows
            String currentWdHandle = driver.getWindowHandle();
            WebElementController wc = new WebElementController();
            boolean check = wc.waitForWindowPresent(driver, messageBy, 5);
            if(!check){
                driver.switchTo().window(currentWdHandle);
                return "";
            }
            WebElement messageElement = driver.findElement(messageBy);
            String message = messageElement.getText();
            driver.close();
            driver.switchTo().window(currentWdHandle);
            return message;
        }
    }

    public static class ImpHoverMessage implements IMessageValidation{
        By messageBy;
        By hoverBy;
        WebDriver driver;

        public ImpHoverMessage(WebDriver driver, By hoverBy, By messageBy) {
            this.messageBy = messageBy;
            this.hoverBy = hoverBy;
            this.driver = driver;
        }

        @Override
        public String getMessage() {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(1000)).ignoring(NoSuchElementException.class);
            if (wait.until(ExpectedConditions.presenceOfElementLocated(hoverBy)) == null) {
                return "";
            }else {
                WebElement hoverElement = driver.findElement(hoverBy);
                Actions actions = new Actions(driver);
                actions.moveToElement(hoverElement).pause(Duration.ofMillis(1000)).perform();

                WebElement messageElement = driver.findElement(messageBy);
                String message = messageElement.getText();
                return message;
            }
        }
    }
}
