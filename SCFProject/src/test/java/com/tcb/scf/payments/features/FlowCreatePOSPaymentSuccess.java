package com.tcb.scf.payments.features;

import com.tcb.auto.serenity.JiraTestcase;
import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.POB;
import com.tcb.scf.dataObject.POS;
import com.tcb.scf.payments.step.*;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "CreatePOSPayment" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/CreateARF_POB_POS.xlsx", sheet = "POS")
public class FlowCreatePOSPaymentSuccess extends POS {
    @Managed
    WebDriver driver;

    @Steps
    CreatePOBStep pobStep;


    @Steps
    CreatePOSStep posStep;

    @Steps
    LoginStep loginStep;

    @Steps
    CreatePaymentStep createPaymentStep;

    @Steps
    ApprovePaymentStep approvePaymentStep;

    CaseInsensitiveMapObjWrapper<POS> data;


    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    @JiraTestcase(testID ="TES-4901")
    public void Create_POSPayment_Transaction(){
        _01_Buyer_Create_POSPayment_Transaction();
        _02_Seller_Approve_POSPayment_Transaction();
        _03_Bank_Check_POSPayment_Transaction();
    }

    @Test
    @JiraTestcase(testID ="TES-4901")
    public void Create_POSPayment2_Transaction(){
        _01_Buyer_Create_POSPayment_Transaction();
//        _02_Seller_Approve_POSPayment_Transaction();
//        _03_Bank_Check_POSPayment_Transaction();
    }

    public void _01_Buyer_Create_POSPayment_Transaction(){
        posStep.Buyer_Create_POSPayment_Transaction(loginStep,pobStep,createPaymentStep,data);
    }

    public void _02_Seller_Approve_POSPayment_Transaction(){
        posStep.Seller_Approve_POSPayment_Transaction(loginStep,pobStep,createPaymentStep,data);

    }
    public void _03_Bank_Check_POSPayment_Transaction(){
        posStep.Bank_Approve_POSPayment_Transaction(loginStep,createPaymentStep,approvePaymentStep,data);
    }


    @After
    public void tearDown() {
        //close DB connection
        ConnectDB.closeAllConnection();
    }
}
