package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.TimeController;
import com.tcb.scf.WebServiceClient;
import com.tcb.scf.dataObject.ARF;
import com.tcb.scf.dataObject.Payment;
import com.tcb.scf.payments.features.BranchCode;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import net.thucydides.core.annotations.Step;
import org.junit.Assert;

import java.util.Map;
import java.util.Set;

public class CreateARFStep extends CommonScenarioSteps {
    @Page
    PaymentsPage paymentsPage;


    public void Seller_Create_ARF_Transcstion(LoginStep loginStep,CreatePaymentStep paymentStep,CaseInsensitiveMapObjWrapper<ARF> data){

        loginStep.login(data.dt().getSeller());
        choose_SupplyChainFinance_Func();
        //create new invoice payment
        select_Invoice_Function();
        click_Addrow_Button();
        input_InvoiceNo(data.dt().getInvoiceNo());
        select_InvoiceBuyer(data.dt().getBuyer());
        select_InvoiceCurrency(data.dt().getCurrency());
        input_InvoiceAmount(data.dt().getAmount());
        select_OriginalMaturityDate(data.dt().getMaturityDate(),"dd/MM/yyyy");
        click_Submit_Button(data.dt().getInvoiceNo());

        //approve invoice payment
        paymentStep.click_Workflow_Button();
        paymentStep.click_User_Queue_Button();
        paymentStep.choose_Payment_Ref(data.dt().getInvoiceNo());
        paymentStep.click_Process_Button();
        select_InvoiceItemVerify(data.dt().getInvoiceNo());
        paymentStep.click_Approve_Button();
        paymentStep.click_OK_Button();

        //Create new asset
        click_Asset_Function();
        select_Asset_Item(data.dt().getBuyer());
        paymentStep.click_Process_Button();
        select_Asset_Invoice_Item(data.dt().getInvoiceNo());
        click_CreateNewAsset_Button(data.dt().getInvoiceNo());
        paymentStep.click_OK_Button();
        paymentStep.click_OK_Button();

        //approve new asset
        paymentStep.click_Workflow_Button();
        paymentStep.click_User_Queue_Button();
        paymentStep.choose_Payment_Ref(data.dt().getInvoiceNo());
        paymentStep.click_Process_Button();
        select_InvoiceItemVerify(data.dt().getInvoiceNo());
        paymentStep.click_Submit_Button();
        switch_ToPopupFrame();
        paymentStep.click_OK_Button();
        paymentStep.click_OK_Button();

        //approve new invoice
        paymentStep.click_Workflow_Button();
        paymentStep.click_User_Queue_Button();
        paymentStep.choose_Payment_Ref(data.dt().getInvoiceNo());
        paymentStep.click_Process_Button();
        paymentStep.click_Approve_Button();
        paymentStep.click_OK_Button();

        loginStep.logout_with_scf();

    }

    public void Bank_Approve_ARF_Transcstion(LoginStep loginStep,CreatePaymentStep paymentStep,ApprovePaymentStep approvePaymentStep,CaseInsensitiveMapObjWrapper<ARF> data) {

        loginStep.loginBank(data.dt().getSiteBank());
        choose_SupplyChainFinance_Func();
        paymentStep.click_Workflow_Button();
        paymentStep.click_User_Queue_Button();
        paymentStep.choose_Payment_Ref(data.dt().getInvoiceNo());
        paymentStep.click_Process_Button();
        paymentStep.click_Approve_Button();
        paymentStep.click_OK_Button();

        approvePaymentStep.click_Tools_Button();
        approvePaymentStep.click_External_Mes_Monitor_Button();
        approvePaymentStep.select_External_Message_Monitor(data.dt().getInvoiceNo());
        approvePaymentStep.click_View_Button();


    }


    @Step
    public void choose_SupplyChainFinance_Func(){
        paymentsPage.select_language("en_US");
        paymentsPage.click_Supply_Chane_Finance_Button();

    }

    @Step
    public void select_Invoice_Function(){
        paymentsPage.click_Invoice_Func();
    }

    @Step
    public void click_Addrow_Button(){
        paymentsPage.click_Addrow_Btn();
    }

    @Step
    public void input_InvoiceNo(String invoiceNo){
        paymentsPage.input_InvoiceNo(invoiceNo);
    }

    @Step
    public void select_InvoiceBuyer(String buyer){
        paymentsPage.select_Invoice_Buyer(buyer);
    }

    @Step
    public void select_InvoiceCurrency(String currency){
        paymentsPage.select_Invoice_Currency(currency);
    }
    @Step
    public void input_InvoiceAmount(String amount){
        paymentsPage.input_Invoice_Amount(amount);
    }
    @Step
    public void select_OriginalMaturityDate(String valuedate, String currentDateFormat){
        paymentsPage.chooseInvoiceMatutiryDate(valuedate,currentDateFormat);
    }
    @Step
    public void click_Submit_Button(String loadRef){
        paymentsPage.click_InvoiceSubmit_btn(loadRef);
    }

    @Step
    public void select_InvoiceItemVerify(String invoiceNo){
        paymentsPage.select_invoice_item_verify(invoiceNo);
    }

    @Step
    public void click_Asset_Function(){
        paymentsPage.click_Asset_func();
    }
    @Step
    public void select_Asset_Item(String buyer){
        paymentsPage.select_Asset_Item(buyer);
    }

    @Step
    public void select_Asset_Invoice_Item(String invoiceNo){

        paymentsPage.select_Asset_Invoice_Item(invoiceNo);
    }

    @Step
    public void click_CreateNewAsset_Button(String invoiceNo){
        paymentsPage.click_CreateNewAsset_btn(invoiceNo);
    }

    @Step
    public void switch_ToPopupFrame(){
        paymentsPage.switch_To_PopupFrame();
    }


}
