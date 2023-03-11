package com.tcb.scf.payments.features;


import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.POB;
import com.tcb.scf.payments.step.ApprovePaymentStep;
import com.tcb.scf.payments.step.CreatePOBStep;
import com.tcb.scf.payments.step.CreatePaymentStep;
import com.tcb.scf.payments.step.LoginStep;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "CreatePOBPayment" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/CreateARF_POB_POS.xlsx", sheet = "POB")
public class FlowCreatePOBPaymentSuccess extends POB {
    @Managed
    WebDriver driver;

    @Steps
    CreatePOBStep pobStep;

    @Steps
    LoginStep loginStep;

    @Steps
    CreatePaymentStep createPaymentStep;

    @Steps
    ApprovePaymentStep approvePaymentStep;

    CaseInsensitiveMapObjWrapper<POB> data;

    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void CreatePOBPayment_Transaction(){

        _01_Buyer_Create_POBPayment_Transaction();
        _02_Seller_Approve_POBPayment_Transaction();
        _03_Buyer_Create_PO_Asset_Transaction();
        _04_Bank_Approve_POBPayment_Transaction();
    }

    public void _01_Buyer_Create_POBPayment_Transaction(){
        pobStep.Buyer_Create_POBPayment_Transaction(loginStep,createPaymentStep,data);
    }
    public void _02_Seller_Approve_POBPayment_Transaction(){
        pobStep.Seller_Approve_POBPayment_Transaction(loginStep,createPaymentStep,data);
    }

    public void _03_Buyer_Create_PO_Asset_Transaction(){
        pobStep.Buyer_Create_PO_Asset_Transaction(loginStep,createPaymentStep,data);
    }

    public void _04_Bank_Approve_POBPayment_Transaction(){
        pobStep.Bank_Approve_POBPayment_Transaction(loginStep,approvePaymentStep,data);
    }

    @After
    public void tearDown() {
        //close DB connection
        ConnectDB.closeAllConnection();
    }
}
