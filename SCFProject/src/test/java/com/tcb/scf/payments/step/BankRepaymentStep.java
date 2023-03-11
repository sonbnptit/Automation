package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.TimeController;
import com.tcb.scf.dataObject.Repayment;
import com.tcb.scf.payments.page.BankRepaymentPage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import org.openqa.selenium.By;

import java.util.List;

public class BankRepaymentStep extends CommonScenarioSteps {


    BankRepaymentPage repayPage;

    public void check_Repayment(LoginStep loginStep, CaseInsensitiveMapObjWrapper<Repayment> data){
        loginStep.loginBank(data.dt().getSiteBank());


        choose_Language("en_US");

        click_DataAdmin_Function();
        click_Admin_Function();
        click_Bank_Function();

        select_Bank();
        click_Open_Button();
        String bankName = repayPage.get_Bank_code();
        edit_BusinessDate(data.dt().getBusinessDate());
        edit_EODDate(data.dt().getEODDate());

        click_Submit_Button();
        click_OK_Button();

        click_Workflow_Function();
        click_Setup_Queue_Function();
        choose_Queue_BankName(bankName);
        click_Process_Button();
        click_Approve_Button();
        click_OK_Button();

        click_Tools_Function();
        click_ScheduleManager_Function();
        choose_PIPAY_Schedule();
        click_Start_Button();
        click_OK_Button();

        click_Supply_Chain_Finance();
        click_Supply_Tools_Function();
        click_Tools_External_Message_Monitor();
        SelectListWebElement(data.dt().getBusinessDate());


    }
    @Step
    public void choose_Language(String language){
        repayPage.select_language(language);
    }

    @Step
    public void click_DataAdmin_Function(){
        repayPage.click_Data_Admin_func();
    }

    @Step
    public void click_Admin_Function(){
        repayPage.click_Admin_func();
    }

    @Step
    public void click_Bank_Function(){
        repayPage.click_Bank_func();
    }

    @Step
    public void select_Bank(){
        repayPage.choose_tcb_Bank();
    }

    @Step
    public void click_Open_Button(){
        repayPage.click_Open_button();
    }

    @Step
    public void edit_BusinessDate(String valueDate){
        repayPage.choose_Businessdate(valueDate);
    }

    @Step
    public void edit_EODDate(String valueDate){
        repayPage.choose_EODdate(valueDate);
    }


    @Step
    public void click_Submit_Button(){
        repayPage.click_Submit_Btn();
    }

    @Step
    public void click_OK_Button(){
        repayPage.click_OK_Btn();
    }

    @Step
    public void click_Workflow_Function(){
        repayPage.click_Workflow_function();
    }

    @Step
    public void click_Setup_Queue_Function(){
        repayPage.click_Setup_Queue_function();
    }

    @Step
    public void choose_Queue_BankName(String bankName){
        repayPage.choose_Queue_Bank(bankName);
    }

    @Step
    public void click_Process_Button(){
        repayPage.click_Process_btn();
    }

    @Step
    public void click_Approve_Button(){
        repayPage.click_Approve_btn();
    }

    @Step
    public void click_Tools_Function(){
        repayPage.click_Tools_func();
    }

    @Step
    public void click_ScheduleManager_Function(){
        repayPage.click_Schedule_Manager_func();
    }

    @Step
    public void choose_PIPAY_Schedule(){
        repayPage.choose_PIPAY_schedule();
    }

    @Step
    public void click_Start_Button(){
        repayPage.click_Start_btn();
    }

    @Step
    public void click_Supply_Chain_Finance(){
        repayPage.click_supply_chain_finance_btn();
    }

    @Step
    public void click_Supply_Tools_Function(){
        repayPage.Click_supply_Tools_Func();
    }

    @Step
    public void click_Tools_External_Message_Monitor(){
        repayPage.Click_Tools_External_Message_Monitor_Func();
    }

    @Step
    public void click_View_Button(){
        repayPage.click_View_btn();
    }

    @Step
    public void click_Back_Button(){
        repayPage.click_Back_Btn();
    }

    @Step
    public void click_Close_popup_Button(){
        repayPage.click_ClosePopup_btn();
    }


    @Step("Select {0}")
    public void choose_Element(By element){
        repayPage.select_element(element);
    }

    @Step("Click button Search")
    public void click_Search_Button(){
        repayPage.click_Search_Btn();
    }


    @Step("Choose Mes Type: {0}")
    public void choose_Message_Type(String type){
        repayPage.select_Message_Type(type);
    }

    @Step("Choose Status: {0}")
    public void choose_Status_Type(String type){
        repayPage.select_Status_Type(type);
    }

    public void select_Item_Matching_Condition( String messageType, String statusType){
        click_Search_Button();
        repayPage.switch_PopupFrame();
        choose_Message_Type(messageType);
        choose_Status_Type(statusType);
        click_Search_Button();
    }

    public void select_Payment_With_MonitorID(String monitorID){
        click_Search_Button();
        repayPage.switch_PopupFrame();
        input_MonitorID(monitorID);
        click_Search_Button();

    }

    public void input_MonitorID(String monitorID){
        repayPage.input_MonitorID(monitorID);
    }

    public String  check_Message_Of_Item(By locator, By mess_Response_Locator){

        choose_Element(locator);
        click_View_Button();
        String ackID = repayPage.click_View_Response_Message(mess_Response_Locator);
        //Commons.getLogger().info(s);
        click_Close_popup_Button();
        click_Back_Button();
        return  ackID;
    }

    public void SelectListWebElement(String date){

        String list_col_STT_Loan_Repayment = "list_col_STT_Loan_Repayment";
        String select_item_stt_Loan_Repayment = "select_item_stt_Loan_Repayment";
        String view_Response_Message_btn = "view_Response_Message_btn";


            select_Item_Matching_Condition("LOAN-REPAYMENT","S");
            List<WebElementFacade> list2 = repayPage.getListElement(list_col_STT_Loan_Repayment,date);
            By choose_Loan_Repayment = repayPage.put_Value(select_item_stt_Loan_Repayment,date);
            By view_Response_Message_button = repayPage.put_Value(view_Response_Message_btn,date);
        Commons.waitAction(2000);
            String ackID = "";
            if(list2.size()==0){
                Commons.getLogger().info("Khong co Payment can thanh toan");
            }else if(list2.size()!=0) {
                Commons.getLogger().info("Check list thu nợ");

                Commons.waitAction(2000);
                for (int j = 0; j < list2.size(); j++) {
                    ackID = check_Message_Of_Item(choose_Loan_Repayment, view_Response_Message_button);
                    System.out.println(ackID);
                }
            }
    }



}
