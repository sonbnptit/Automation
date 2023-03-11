package com.tcb.scf.payments.features;


import com.tcb.auto.serenity.UseTestDataFromExcel;
import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.payments.step.BankRepaymentStep;
import com.tcb.scf.payments.step.LoginStep;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.tcb.scf.dataObject.Repayment;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "CheckRepayments" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/Repayment.xlsx", sheet = "Repayment")
public class FlowCheckBankRepayment extends Repayment {


    @Steps
    LoginStep loginStep;

    @Steps
    BankRepaymentStep repayStep;

    public static CaseInsensitiveMapObjWrapper<Repayment> data;

    @Before
    public void logIn_and_getData() {

        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void check_Repayment_Transaction(){
        repayStep.check_Repayment(loginStep,data);
    }

    @After
    public void tearDown() {
        //close DB connection
        ConnectDB.closeAllConnection();
    }

}
