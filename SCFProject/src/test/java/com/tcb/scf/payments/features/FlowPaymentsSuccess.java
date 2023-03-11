package com.tcb.scf.payments.features;

import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.WebServiceClient;
import com.tcb.scf.dataObject.Payment;
import com.tcb.scf.payments.step.ApprovePaymentStep;
import com.tcb.scf.payments.step.LoginStep;
import com.tcb.scf.payments.step.CreatePaymentStep;
import com.tcb.scf.payments.step.WorkingT24Step;
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

import java.util.Map;
import java.util.Set;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "CreatePayments" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/CreatePayment.xlsx", sheet = "Payments")
public class FlowPaymentsSuccess extends Payment {
    @Managed
    WebDriver driver;

    @Steps
    LoginStep loginStep;

    @Steps
    CreatePaymentStep cr_pay_Step;

    @Steps
    ApprovePaymentStep appr_pay_Step;


    @Steps
    WorkingT24Step t24Step;


    public static CaseInsensitiveMapObjWrapper<Payment> data;

    @Before
    public void logIn_and_getData() {

        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void Flow_Create_Payment_Success(){
        _01_Create_Payment_Transaction();
        _02_Approve_Payment_Transaction();

    }


    public void _01_Create_Payment_Transaction(){
        cr_pay_Step.maker_Create_Payment_Transaction(loginStep,data);
    }

    public void _02_Approve_Payment_Transaction(){
        appr_pay_Step.cheker_Approve_Payment_Transaction(loginStep,data);
        appr_pay_Step.bank_Approve_Transaction(loginStep,data);


    }
    @After
    public void tearDown() {
        //close DB connection
        ConnectDB.closeAllConnection();
    }


}
