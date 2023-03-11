package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.Payment;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import org.jruby.RubyProcess;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApprovePaymentStep extends CommonScenarioSteps {

    @Page
    PaymentsPage payPage;

    @Steps
    CreatePaymentStep createSteps;

    @Step("Checker approve_payment")
    public void cheker_Approve_Payment_Transaction(LoginStep loginStep, CaseInsensitiveMapObjWrapper<Payment> data){
        loginStep.login(data.dt().getKHchecker());
        createSteps.choose_SupplyChainFinance_Func();
        click_Receivable_Button();
        click_DiscountBy_Function();
        click_Individual_Payment_Function();
        payPage.choose_Supplyer_Account(data.dt().getSupplierAccount());

        click_Continue_Button();
        click_payment_checkbox();
        click_Continue_Button();
        click_Submit_Button();
        click_Popup_OK_Button();
        click_OK_Button();


        click_Workflow_Button();
        click_User_Queue_Button();
        String txnRef = payPage.get_Txn_Ref();

        data.put("txnRef",txnRef);
        select_Txn_Ref();
        click_Process_Button();
        click_Approve_Button();
        loginStep.logout_with_scf();


    }

    public void bank_Approve_Transaction(LoginStep loginStep, CaseInsensitiveMapObjWrapper<Payment> data){

        loginStep.loginBank(data.dt().getSiteBank());
        createSteps.choose_SupplyChainFinance_Func();
        click_Tools_Button();
        click_External_Mes_Monitor_Button();
        select_External_Message_Monitor(data.dt().getTxnRef());
        click_View_Button();
    }



    @Step("Click Receivable Button")
    public void click_Receivable_Button(){
        payPage.click_Receivable_btn();
    }

    @Step("Choose function Discount By")
    public void click_DiscountBy_Function(){
        payPage.click_DiscountBy_btn();
    }

    @Step("Choose Individual Payment")
    public void click_Individual_Payment_Function(){
        payPage.click_Individual_Payment_btn();
    }

    @Step("Click Continue Button")
    public void click_Continue_Button(){

        payPage.click_Continue_Btn();
    }

    @Step("Click payment checkbox")
    public void click_payment_checkbox(){

        payPage.checkBox_Payment();
    }

    @Step("Click Submit Button")
    public void click_Submit_Button(){

        payPage.click_Submit_btn();
    }

    @Step("Click OK Confirm Button")
    public void click_Popup_OK_Button(){

        payPage.click_OK_Btn();
    }

    @Step("Click OK Button")
    public void click_OK_Button(){

        payPage.click_OK_Btn();
    }

    @Step("Click Workflow button")
    public void click_Workflow_Button(){
        payPage.click_Workflow_Button();
    }

    @Step("Click User Queue button")
    public void click_User_Queue_Button(){
        payPage.click_User_Queue_Function();
    }

    public void select_Txn_Ref(){
        payPage.select_Txn_Ref();
    }

    @Step("Click Process button")
    public void click_Process_Button(){
        payPage.click_Process_btn();
    }

    @Step("Click Appprove button")
    public void click_Approve_Button(){
        payPage.click_Approve_btn();
    }


    @Step("Click Tools button")
    public void click_Tools_Button(){
        payPage.click_Tool_btn();
    }

    @Step("Click External Message Monitor button")
    public void click_External_Mes_Monitor_Button(){
        payPage.click_External_Mes_Monitor();
    }

    @Step("Select txn Ref")
    public void select_External_Message_Monitor(String ref){
        payPage.txnRef_Select(ref);

    }

    @Step("Select Monitor ID")
    public void select_MonitorID(String monitorID){
        payPage.MonitorID_Select(monitorID);
    }

    @Step("Click View button")
    public void click_View_Button(){
        payPage.click_View_btn();

    }

    @Step("Click View Request Button")
    public Map<String,String> click_View_Request_Button(){
        return payPage.click_View_Request_Btn();
    }

    @Step("Click View Response button")
    public String  click_View_Response_Button(){
        return payPage.click_View_Response_btn();

    }

    @Step
    public String get_MornitorID(String txnRef){
        return payPage.getMornitorID(txnRef);
    }

    @Step("Get status {0}")
    public String get_Status(String txnRef){
        return payPage.getStatus_AfterCheckingT24(txnRef);
    }


}
