package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.scf.dataObject.Payment;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Step;
import org.junit.Assert;

public class CreatePaymentStep extends CommonScenarioSteps {

    PaymentsPage payPage;

    @Step("Maker create payments")
    public void maker_Create_Payment_Transaction(LoginStep loginStep, CaseInsensitiveMapObjWrapper<Payment> data){
        loginStep.login(data.dt().getKHmaker());
        choose_SupplyChainFinance_Func();
        choose_Payable_Func();
        choose_Create_Payment_Func();

        select_BuyerAccount(data.dt().getBuyAccount());
        select_SupplyAccount(data.dt().getSupplierAccount());
        input_PaymentRef(data.dt().getPaymentRef());
        input_Maturity_Date(data.dt().getMaturityDate(), "dd/MM/yyyy");

        click_Continue_Button();
        click_Addrow_Button();
        select_Remmittances_Type(data.dt().getRemitType());
        input_RemitNo(data.dt().getRemitNo());
        input_NetAmount(data.dt().getAmount());
        input_PoNo(data.dt().getPoNo());
        input_Comment(data.dt().getComment());


        click_Submit_Button();

        String actual = payPage.getStatusMessage();
        Assert.assertEquals(actual,data.dt().getExpectStatus());
        click_OK_Button();

        click_Workflow_Button();
        click_User_Queue_Button();
        choose_Payment_Ref(data.dt().getPaymentRef());
        click_Process_Button();
        click_Approve_Button();

        String actualApprove = payPage.getStatusMessage();
        Assert.assertEquals(actualApprove,data.dt().getExpectApproveStatus());

        click_OK_Button();

        loginStep.logout_with_scf();
    }



    @Step("Choose Supply Chain Finance")
    public void choose_SupplyChainFinance_Func(){
        payPage.select_language("en_US");
        payPage.click_Supply_Chane_Finance_Button();
    }

    @Step("Choose Payables ")
    public void choose_Payable_Func(){
        payPage.click_Payable_Button();
    }

    @Step("Choose Create Payment Function ")
    public void choose_Create_Payment_Func(){
        payPage.choose_Create_Function();
    }

    @Step("Select buyer account")
    public void select_BuyerAccount(String buyAccount){
        payPage.choose_Buyer_Account(buyAccount);
    }

    @Step("Seelect supply account")
    public void select_SupplyAccount(String supAccount){
        payPage.select_Supplier_Account(supAccount);
    }

    @Step("Input Payment Ref")
    public void input_PaymentRef(String ref){
        payPage.input_Payment_Ref(ref);
    }

    @Step("Input Maturity Date")
    public void input_Maturity_Date(String date, String formatDate){
        payPage.chooseMatutiryDate(date,formatDate);
    }

    @Step("Click continue button")
    public void click_Continue_Button(){
        payPage.click_Continue_Btn();
    }

    @Step("Click Add row button")
    public void click_Addrow_Button(){
        payPage.click_Addrow_Btn();
    }

    @Step("Select Remittance Type")
    public void select_Remmittances_Type(String type){
        payPage.select_Remittype(type);
    }

    @Step("input Remmitance No")
    public void input_RemitNo(String remitNo){
        payPage.input_RemmitNo(remitNo);
    }

    @Step("Input amount")
    public void input_NetAmount(String amount){
        payPage.input_Netamount(amount);
    }

    @Step("Input Po No")
    public void input_PoNo(String po){
        payPage.input_Po_No(po);
    }

    @Step("Input comment")
    public void input_Comment(String comment){
        payPage.input_Comment(comment);
    }

    @Step("Click Submit button")
    public void click_Submit_Button(){
        payPage.click_Submit_btn();
    }

    @Step("Click Workflow button")
    public void click_Workflow_Button(){
        payPage.click_Workflow_Button();
    }

    @Step("Click User Queue button")
    public void click_User_Queue_Button(){
        payPage.click_User_Queue_Function();
    }

    @Step("Click OK button")
    public void click_OK_Button(){
        payPage.click_OK_Btn();
    }

    @Step("Click Logout button")
    public void click_Logout_Button(){
        payPage.click_Logout_Btn();

    }

    @Step("Click Process button")
    public void click_Process_Button(){
        payPage.click_Process_btn();
    }

    @Step("Choose Payment Ref")
    public void choose_Payment_Ref(String ref){
        payPage.select_Payment_To_Approve(ref);
    }

    @Step("Click Appprove button")
    public void click_Approve_Button(){
        payPage.click_Approve_btn();
    }


}
