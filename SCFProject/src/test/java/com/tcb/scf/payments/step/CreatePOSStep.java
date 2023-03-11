package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.scf.dataObject.POB;
import com.tcb.scf.dataObject.POS;
import com.tcb.scf.payments.page.CreatePOB_POSPaymentPage;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Step;
import org.fluentlenium.core.annotation.Page;

public class CreatePOSStep extends CommonScenarioSteps {

    @Page
    CreatePOB_POSPaymentPage pobPaymentPage;


    public void Buyer_Create_POSPayment_Transaction(LoginStep loginStep,CreatePOBStep createPOBStep,CreatePaymentStep createPaymentStep, CaseInsensitiveMapObjWrapper<POS> data){
        loginStep.login(data.dt().getBuyerAcc());
        createPOBStep.select_Language("en_US");
        createPOBStep.click_POManager_Function();
        createPOBStep.click_POCreateFunction();

        createPOBStep.click_HeadInforTab();
        createPOBStep.input_PONo(data.dt().getPoNo());
        createPOBStep.select_SupplierID(data.dt().getSupplier());
        createPOBStep.select_POType(data.dt().getPoType());
        createPOBStep.select_POCurrency(data.dt().getCurrency());
        createPOBStep.input_ShipToAddress(data.dt().getShiptToAddress());
        createPOBStep.input_ShipToCity(data.dt().getShipToCity());
        createPOBStep.select_ShipToCountry(data.dt().getShipToCountry());

        createPOBStep.click_LineInforTab();
        createPOBStep.input_POLineNo(data.dt().getPoLineNo());
        createPOBStep.input_PartNo(data.dt().getPartNo());
        createPOBStep.input_UOM(data.dt().getUOM());
        createPOBStep.input_PartDescription(data.dt().getPartDescription());
        createPOBStep.input_Quantity(data.dt().getQuantity());
        createPOBStep.input_Unit_Price(data.dt().getUnitPrice());
        createPOBStep.click_AddButton();

        createPOBStep.click_HeadInforTab();
        createPOBStep.input_LastestShipDate(data.dt().getLastestShipDate());
        createPOBStep.input_BillToAddress(data.dt().getBillToAddress());
        createPOBStep.input_BillToCity(data.dt().getBillToCity());
        createPOBStep.select_BillToCountry(data.dt().getBillToCountry());

        createPOBStep.click_SubmitButton();
        createPOBStep.click_OKButton();

        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPOBStep.click_ReleaseButton();
        createPOBStep.click_OKButton();

        loginStep.logout_with_scf();
    }

    @Step
    public void Seller_Approve_POSPayment_Transaction(LoginStep loginStep,CreatePOBStep createPOBStep, CreatePaymentStep createPaymentStep,CaseInsensitiveMapObjWrapper<POS> data){

        loginStep.login(data.dt().getSellerAcc());
        createPOBStep.select_Language("en_US");
        createPOBStep.click_POManager_Function();

        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPaymentStep.click_Approve_Button();
        createPaymentStep.click_OK_Button();

        createPaymentStep.choose_SupplyChainFinance_Func();
        createPOBStep.click_PO_Asset_Function();
        createPOBStep.click_PO_Asset_Create_Function();
        createPOBStep.select_PoNo(data.dt().getPoNo());
        createPOBStep.click_FinanceButton();
        createPOBStep.input_FinanceReference(data.dt().getFinanceReference());
        createPOBStep.click_SaveButton();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPOBStep.click_NextButton();
        createPOBStep.click_OKButton();


        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPaymentStep.click_Approve_Button();
        createPaymentStep.click_OK_Button();

        loginStep.logout_with_scf();

    }

    @Step
    public void Bank_Approve_POSPayment_Transaction(LoginStep loginStep,CreatePaymentStep createPaymentStep,ApprovePaymentStep approvePaymentStep,CaseInsensitiveMapObjWrapper<POS> data){
        loginStep.loginBank(data.dt().getSiteBank());

        createPaymentStep.choose_SupplyChainFinance_Func();
        createPaymentStep.click_Workflow_Button();
        createPaymentStep.click_User_Queue_Button();
        createPaymentStep.choose_Payment_Ref(data.dt().getPoNo());
        createPaymentStep.click_Process_Button();
        createPaymentStep.click_Approve_Button();
        createPaymentStep.click_OK_Button();

        approvePaymentStep.click_Tools_Button();
        approvePaymentStep.click_External_Mes_Monitor_Button();
        approvePaymentStep.select_External_Message_Monitor(data.dt().getPoNo());
        approvePaymentStep.click_View_Button();

        loginStep.logout_with_scf();

    }
}
