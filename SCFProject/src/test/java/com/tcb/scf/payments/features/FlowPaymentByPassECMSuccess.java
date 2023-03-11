package com.tcb.scf.payments.features;

import com.tcb.auto.serenity.JiraTestcase;
import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.ApprovePayment;
import com.tcb.scf.payments.step.ApprovePaymentByPassECMStep;
import com.tcb.scf.payments.step.LoginStep;
import com.tcb.scf.payments.step.WorkingT24Step;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "ApprovePaymentsByPassECM" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/ApprovePayment.xlsx", sheet = "Payments")
public class FlowPaymentByPassECMSuccess extends ApprovePayment {
    @Managed
    WebDriver driver;

    @Steps
    LoginStep loginStep;

    @Steps
    ApprovePaymentByPassECMStep byPassECMStep;


    @Steps
    WorkingT24Step t24Step;

    public static CaseInsensitiveMapObjWrapper<ApprovePayment> data;

    @Before
    public void logIn_and_getData() {

        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }


    @Test
    @JiraTestcase(testID ="TES-4801")
    public void Approve_ByPass_ECM(){
        _01_GetInforPayment();
        _02_Approve_Payment_BypassECM_Transaction();

    }

    public void _01_GetInforPayment(){


        byPassECMStep.View_Payment_InBank_Transaction(loginStep,data);

    }

    public void _02_Approve_Payment_BypassECM_Transaction(){
        //byPassECMStep.Approve_Payment_Transaction(loginStep,t24Step,data);
        byPassECMStep.Approve_Payment_Transaction(loginStep,data);

    }


    @After
    public void tearDown() {


        //close DB connection
        ConnectDB.closeAllConnection();

    }




}
