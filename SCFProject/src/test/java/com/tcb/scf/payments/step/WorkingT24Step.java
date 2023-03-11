package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.TimeController;
import com.tcb.scf.WebServiceClient;
import com.tcb.scf.dataObject.Payment;
import com.tcb.scf.dataObject.WorkingT24;
import com.tcb.scf.payments.page.WorkingT24Page;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import org.junit.Assert;

public class WorkingT24Step extends CommonScenarioSteps {

    @Page
    WorkingT24Page t24Page;

    @Step
    public void input_Command_Key(String keyValue){

        t24Page.switch_To_Banner_Frame();
        t24Page.input_Key_Command_Input(keyValue);
        t24Page.enter_key();

    }
    @Step
    public boolean checkingT24(){
        return t24Page.checking_T24();
    }

    @Step
    public void switch_To_Working_Page(){

        t24Page.switch_To_ChinhanhHN();
    }
    @Step
    public void switch_To_Menu_Frame(){

        t24Page.switch_To_Menu_Frame();
    }

    @Step
    public void goto_Techcombank_Loan(){
        t24Page.goto_TechComBankLoan();
    }
    @Step
    public void goto_DuyetVaChinhSuaLaiSuat1Buoc(){
        t24Page.goto_DuyetVaChinhSuaLaiSuat();
    }

    @Step
    public void switch_To_LoanAndDeposit_Page(){
        t24Page.switch_To_LoanAndDeposit_Page();
    }

    @Step
    public void input_LD(String LD){
        t24Page.input_LD(LD);
    }
    @Step
    public void input_LD_Authorise(String LD){
        t24Page.input_LD_Authorise(LD);
    }

//    @Step
//    public void select_ValueDate(String valueDate, String dateFormat){
//        t24Page.choose_ValueDate(valueDate,dateFormat);
//    }

    @Step
    public void input_Interest_Spread(String value){
        t24Page.input_InterestSpread(value);
    }

    @Step
    public void input_MaturityDate(String date){
        t24Page.input_MaturityDate(date);
    }
    @Step
    public void select_VayMon_Option(String vaymon){
        t24Page.select_VaymonOption(vaymon);
    }

    @Step
    public void ClickDealButton(){
        t24Page.click_DealBtn();
    }

    @Step
    public void choose_Month(String month){
        t24Page.valueDate_month(month);
    }

    @Step
    public void choose_Year(String year){
        t24Page.valueDate_year(year);
    }

    @Step
    public void choose_Date(String date){
        t24Page.choose_Date(date);
    }

    @Step
    public void click_AcceptOverride(){
        t24Page.click_Accept_Override();
    }

    @Step
    public void click_Authorise_Deal_Button(){
        t24Page.click_Authorise_Btn();
    }

    @Step
    public void select_ValueDate(String valuedate, String currentDateFormat) {

        String dateConvert[] = TimeController.funcFormatDate(valuedate, currentDateFormat, "dd/MM/yyyy").split("/");
        String year = dateConvert[2];
        String month = dateConvert[1];
        String date = dateConvert[0];
        Commons.getLogger().info(year);
        Commons.getLogger().info(month);
        Commons.getLogger().info(date);

        t24Page.click_ChooseValueDate();
        //wc.switchFrame(getDriver(),value_date_iframe);
        choose_Month(month);
        t24Page.click_ChooseValueDate();
        choose_Month(month);
        choose_Year(year);
        choose_Date(date);


    }

    @Step
    public void click_Perform_Button(){
        t24Page.click_Perform_Btn();
    }

}
