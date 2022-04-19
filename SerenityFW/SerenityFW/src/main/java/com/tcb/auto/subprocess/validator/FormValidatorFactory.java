package com.tcb.auto.subprocess.validator;

import com.tcb.auto.utils.Compiler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class FormValidatorFactory{
    private Compiler compiler;
    private WebDriver driver;

    public FormValidatorFactory(WebDriver driver) {
        compiler = new Compiler();
        this.driver = driver;
    }

    public FormValidatorFactory withCompiler(Compiler compiler){
        this.compiler = compiler;
        return this;
    }

    /**
     * Form Validator for Browser Popup
     * @param excelFile
     * @param sheetName
     * @param checkBy   : element need check validation
     * @param submitBy  : submit form button
     * @return
     * @throws IOException
     */
    public FormValidator getValidatorBrowserPopup(String excelFile, String sheetName, By checkBy, By submitBy) throws IOException{
        FormValidator.ImpBrowserPopupMessage popupMessage = new FormValidator.ImpBrowserPopupMessage(this.driver);
        FormValidator validator = new FormValidator(excelFile, sheetName, compiler, driver, checkBy, submitBy, popupMessage);
        return validator;
    }

    /**
     * Form Validator for same window Popup
     * @param excelFile
     * @param sheetName
     * @param checkBy   : element need check validation
     * @param submitBy  : submit form button
     * @param messagePopupText  : element contain message
     * @param closeMessageBtn   : element do close of popup
     * @return
     * @throws IOException
     */
    public FormValidator getValidatorPopup(String excelFile, String sheetName, By checkBy, By submitBy, By messagePopupText, By closeMessageBtn) throws IOException{
        FormValidator.ImpPopupMessage popupMessage = new FormValidator.ImpPopupMessage(this.driver, messagePopupText, closeMessageBtn);
        FormValidator validator = new FormValidator(excelFile, sheetName, compiler, driver, checkBy, submitBy, popupMessage);
        return validator;
    }

    /**
     * Form Validator for new window Popup
     * @param excelFile
     * @param sheetName
     * @param checkBy   : element need check validation
     * @param submitBy  : submit form button
     * @param messageInWindowPopup  : element contain message
     * @return
     * @throws IOException
     */
    public FormValidator getValidatorWindowPopup(String excelFile, String sheetName, By checkBy, By submitBy, By messageInWindowPopup) throws IOException{
        FormValidator.ImpWindowPopupMessage popupMessage = new FormValidator.ImpWindowPopupMessage(this.driver, messageInWindowPopup);
        FormValidator validator = new FormValidator(excelFile, sheetName, compiler, driver, checkBy, submitBy, popupMessage);
        return validator;
    }

    /**
     * Form Validator for hover icon Popup
     * @param excelFile
     * @param sheetName
     * @param checkBy       : element need check validation
     * @param submitBy      : submit form button
     * @param hoverIcon     : icon need hover to show popup
     * @param messageText   : element contain message
     * @return
     * @throws IOException
     */
    public FormValidator getValidatorHoverMessage(String excelFile, String sheetName, By checkBy, By submitBy, By hoverIcon, By messageText) throws IOException{
        FormValidator.ImpHoverMessage hoverMessage = new FormValidator.ImpHoverMessage(this.driver, hoverIcon, messageText);
        FormValidator validator = new FormValidator(excelFile, sheetName, compiler, driver, checkBy, submitBy, hoverMessage);
        return validator;
    }

}
