package com.tcb.scf.payments.features;

import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.ARF;
import com.tcb.scf.dataObject.Repayment;
import com.tcb.scf.payments.step.*;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "CreateARFPayment" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/CreateARF_POB_POS.xlsx", sheet = "ARF")
public class FlowCreateARFPaymentSuccess extends ARF {

    @Managed
    WebDriver driver;
    @Steps
    CreateARFStep arfStep;

    @Steps
    CreatePaymentStep createPaymentStep;

    @Steps
    LoginStep loginStep;

    @Steps
    ApprovePaymentStep approvePaymentStep;


    public static CaseInsensitiveMapObjWrapper<ARF> data;

    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void CreateARFPayment_Transaction(){
        _01_Seller_Create_ARFPayment_Transaction();
        _02_Bank_Approve_ARFPayment_Transaction();
    }

    public void _01_Seller_Create_ARFPayment_Transaction(){
        arfStep.Seller_Create_ARF_Transcstion(loginStep,createPaymentStep,data);
    }
    public void _02_Bank_Approve_ARFPayment_Transaction(){
        arfStep.Bank_Approve_ARF_Transcstion(loginStep,createPaymentStep,approvePaymentStep,data);
    }
    @After
    public void tearDown() {
        //close DB connection
        ConnectDB.closeAllConnection();
    }

}
