package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.scf.dataObject.POB;
import com.tcb.scf.payments.page.CreatePOB_POSPaymentPage;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Step;
import org.fluentlenium.core.annotation.Page;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;

public class CreatePOBStep extends CommonScenarioSteps {

    @Page
    CreatePOB_POSPaymentPage pobPaymentPage;

    @Page
    PaymentsPage paymentsPage;

    @Step
    public void Buyer_Create_POBPayment_Transaction(LoginStep loginStep,CreatePaymentStep createPaymentStep, CaseInsensitiveMapObjWrapper<POB> data){

        loginStep.login(data.dt().getBuyerAcc());
        select_Language("en_US");
        click_POManager_Function();
        click_POCreateFunction();

        click_HeadInforTab();
        input_PONo(data.dt().getPoNo());
        select_SupplierID(data.dt().getSupplier());
        select_POType(data.dt().getPoType());
        select_POCurrency(data.dt().getCurrency());
        input_ShipToAddress(data.dt().getShiptToAddress());
        input_ShipToCity(data.dt().getShipToCity());
        select_ShipToCountry(data.dt().getShipToCountry());

        click_LineInforTab();
        input_POLineNo(data.dt().getPoLineNo());
        input_PartNo(data.dt().getPartNo());
        input_UOM(data.dt().getUOM());
        input_PartDescription(data.dt().getPartDescription());
        input_Quantity(data.dt().getQuantity());
        input_Unit_Price(data.dt().getUnitPrice());
        click_AddButton();

        click_HeadInforTab();
        input_LastestShipDate(data.dt().getLastestShipDate());
        input_BillToAddress(data.dt().getBillToAddress());
        input_BillToCity(data.dt().getBillToCity());
        select_BillToCountry(data.dt().getBillToCountry());

        click_SubmitButton();
        click_OKButton();

        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        click_ReleaseButton();
        click_OKButton();

        loginStep.logout_with_scf();

    }

    @Step
    public void Seller_Approve_POBPayment_Transaction(LoginStep loginStep, CreatePaymentStep createPaymentStep,CaseInsensitiveMapObjWrapper<POB> data){

        loginStep.login(data.dt().getSellerAcc());
        select_Language("en_US");
        click_POManager_Function();

        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPaymentStep.click_Approve_Button();
        createPaymentStep.click_OK_Button();
        loginStep.logout_with_scf();

    }

    @Step
    public void Buyer_Create_PO_Asset_Transaction(LoginStep loginStep,  CreatePaymentStep createPaymentStep,CaseInsensitiveMapObjWrapper<POB> data){
        loginStep.login(data.dt().getBuyerAcc());
        createPaymentStep.choose_SupplyChainFinance_Func();

        click_PO_Asset_Function();
        click_PO_Asset_Create_Function();
        select_PoNo(data.dt().getPoNo());
        click_FinanceButton();
        input_FinanceReference(data.dt().getFinanceReference());
        click_SaveButton();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        click_NextButton();
        click_OKButton();
        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPaymentStep.click_Approve_Button();
        createPaymentStep.click_OK_Button();
        loginStep.logout_with_scf();
    }

    @Step
    public void Bank_Approve_POBPayment_Transaction(LoginStep loginStep,ApprovePaymentStep approvePaymentStep,CaseInsensitiveMapObjWrapper<POB> data){
        loginStep.loginBank(data.dt().getSiteBank());

        choose_SupplyChainFinance_Func();
        approvePaymentStep.click_Tools_Button();
        approvePaymentStep.click_External_Mes_Monitor_Button();
        approvePaymentStep.select_External_Message_Monitor(data.dt().getPoNo());
        approvePaymentStep.click_View_Button();
        loginStep.logout_with_scf();

    }


    @Step
    public void select_Language(String language){
        pobPaymentPage.select_language(language);
    }

    @Step
    public void click_POManager_Function(){
        pobPaymentPage.click_PO_Manager_Func();
    }

    @Step
    public void click_POCreateFunction(){
        pobPaymentPage.click_PO_Create_Func();
    }

    @Step
    public void click_HeadInforTab(){
        pobPaymentPage.click_HeadInforTab();
    }

    @Step
    public void click_LineInforTab(){
        pobPaymentPage.click_LineInforTab();
    }

    @Step
    public void input_PONo(String poNo){
        pobPaymentPage.input_PoNo(poNo);
    }

    @Step
    public void select_SupplierID(String supplier){
        pobPaymentPage.select_SupplierID(supplier);
    }

    @Step
    public void select_POType(String poType){
        pobPaymentPage.select_PoType(poType);
    }

    @Step
    public void select_POCurrency(String currency){
        pobPaymentPage.select_Currency(currency);
    }

    @Step
    public void input_ShipToAddress(String address){
        pobPaymentPage.input_Ship_To_Address(address);
    }

    @Step
    public void input_ShipToCity(String city){
        pobPaymentPage.input_Ship_To_City(city);
    }

    @Step
    public void select_ShipToCountry(String country){
        pobPaymentPage.select_Ship_To_Country(country);
    }

    @Step
    public void input_LastestShipDate(String date){
        pobPaymentPage.input_Lastest_Ship_Date(date);
    }

    @Step
    public void input_BillToAddress(String address){
        pobPaymentPage.input_Bill_To_Address(address);
    }

    @Step
    public void input_BillToCity(String city){
        pobPaymentPage.input_Bill_To_City(city);
    }

    @Step
    public void select_BillToCountry(String country){
        pobPaymentPage.select_Bill_To_Country(country);
    }

    @Step
    public void input_POLineNo(String lineNo){
        pobPaymentPage.input_PO_Line_No(lineNo);
    }

    @Step
    public void input_PartNo(String partNo){
        pobPaymentPage.input_Part_No(partNo);
    }

    @Step
    public void input_UOM(String uom){
        pobPaymentPage.input_UOM(uom);
    }

    @Step
    public void input_PartDescription(String description){
        pobPaymentPage.input_Part_Description(description);
    }

    @Step
    public void input_Quantity(String quantity){
        pobPaymentPage.input_Quantity(quantity);
    }

    @Step
    public void input_Unit_Price(String price){
        pobPaymentPage.input_Unit_Price(price);
    }
    @Step
    public void click_AddButton(){
        pobPaymentPage.click_Add_btn();
    }

    @Step
    public void click_SubmitButton(){
        pobPaymentPage.click_Submit_btn();
    }

    @Step
    public void click_OKButton(){
        pobPaymentPage.click_OK_btn();
    }

    @Step
    public void click_ReleaseButton(){
        pobPaymentPage.click_Realse_btn();
    }

    @Step
    public void click_PO_Asset_Function(){
        pobPaymentPage.click_PO_Asset_func();
    }

    @Step
    public void click_PO_Asset_Create_Function(){
        pobPaymentPage.click_PO_Asset_Create_func();
    }

    @Step
    public void select_PoNo(String poNo){
        pobPaymentPage.select_PoNo(poNo);
    }

    @Step
    public void click_FinanceButton(){
        pobPaymentPage.click_Finance_btn();
    }

    @Step
    public void input_FinanceReference(String reference){
        pobPaymentPage.input_Finance_Reference(reference);
    }

    @Step
    public void click_SaveButton(){
        pobPaymentPage.click_Save_Btn();
    }

    @Step
    public void click_NextButton(){
        pobPaymentPage.click_Next_Btn();
    }
    @Step
    public void choose_SupplyChainFinance_Func(){
        paymentsPage.select_language("en_US");
        paymentsPage.click_Supply_Chane_Finance_Button();

    }
}
