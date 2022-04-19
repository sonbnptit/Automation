package com.tcb.auto.subprocess.validator;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.Compiler;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.List;

public class FormValidatorSteps extends CommonScenarioSteps {
    protected FormValidator validator;
    private Compiler compiler = null;
    public FormValidatorSteps withCompiler(Compiler compiler){
        this.compiler = compiler;
        return this;
    }

    public FormValidatorSteps withUseSendKeyJs(boolean useSendKeyJs) {
        if(validator != null) {
            validator.setUseSendKeyJs(useSendKeyJs);
        }
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
    public FormValidatorSteps forValidatorBrowserPopup(String excelFile, String sheetName, By checkBy, By submitBy) throws IOException{
        if(compiler == null){
            compiler = new Compiler(runData, "");
        }
        FormValidator.ImpBrowserPopupMessage popupMessage = new FormValidator.ImpBrowserPopupMessage(getDriver());
        validator = new FormValidator(excelFile, sheetName, compiler, getDriver(), checkBy, submitBy, popupMessage);
        return this;
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
    public FormValidatorSteps forValidatorPopup(String excelFile, String sheetName, By checkBy, By submitBy, By messagePopupText, By closeMessageBtn) throws IOException{
        if(compiler == null){
            compiler = new Compiler(runData, "");
        }
        FormValidator.ImpPopupMessage popupMessage = new FormValidator.ImpPopupMessage(getDriver(), messagePopupText, closeMessageBtn);
        validator = new FormValidator(excelFile, sheetName, compiler, getDriver(), checkBy, submitBy, popupMessage);
        return this;
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
    public FormValidatorSteps forValidatorWindowPopup(String excelFile, String sheetName, By checkBy, By submitBy, By messageInWindowPopup) throws IOException{
        if(compiler == null){
            compiler = new Compiler(runData, "");
        }
        FormValidator.ImpWindowPopupMessage popupMessage = new FormValidator.ImpWindowPopupMessage(getDriver(), messageInWindowPopup);
        validator = new FormValidator(excelFile, sheetName, compiler, getDriver(), checkBy, submitBy, popupMessage);
        return this;
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
    public FormValidatorSteps forValidatorHoverMessage(String excelFile, String sheetName, By checkBy, By submitBy, By hoverIcon, By messageText) throws IOException{
        if(compiler == null){
            compiler = new Compiler(runData, "");
        }
        FormValidator.ImpHoverMessage hoverMessage = new FormValidator.ImpHoverMessage(getDriver(), hoverIcon, messageText);
        validator = new FormValidator(excelFile, sheetName, compiler, getDriver(), checkBy, submitBy, hoverMessage);
        return this;
    }

    @Step
    public List<ValidationItem> do_all_validation(){
        if(validator == null) return null;
        validator.getValidationData().forEach(validationItem -> {
            do_one_validation(validationItem);
        });
        return validator.getValidationData();
    }

    @Step
    public List<ValidationItem> do_all_validation_select_box(){
        if(validator == null) return null;
        validator.getValidationData().forEach(validationItem -> {
            do_one_validation_select_box(validationItem);
        });
        return validator.getValidationData();
    }

    @Step
    public List<ValidationItem> do_all_validation_tab_item(){
        if(validator == null) return null;
        validator.getValidationData().forEach(validationItem -> {
            do_one_validation_tab_item(validationItem);
        });
        return validator.getValidationData();
    }

    @Step
    public void do_one_validation(ValidationItem validationItem){
        validator.doValidationItem(validationItem);
    }

    @Step
    public void do_one_validation_select_box(ValidationItem validationItem){
        validator.doValidationItemSelectBox(validationItem);
    }

    @Step
    public void do_one_validation_tab_item(ValidationItem validationItem){
        validator.doValidationTabItem(validationItem);
    }

    @Step
    public int get_test_status_count(String status){
        return validator.getTestStatusCount(status);
    }

    @Step
    public void print_validation_result(){
        String summaryStr = validator.getSummaryReport();
        Serenity.recordReportData().withTitle("Test validate summary").andContents(summaryStr);
    }
}
