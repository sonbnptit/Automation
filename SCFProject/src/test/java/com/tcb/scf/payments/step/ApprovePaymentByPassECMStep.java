package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.CaseInsensitiveMapObjWrapper;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.TimeController;
import com.tcb.scf.WebServiceClient;
import com.tcb.scf.dataObject.ApprovePayment;
import com.tcb.scf.payments.features.BranchCode;
import com.tcb.scf.payments.page.PaymentsPage;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.util.Map;
import java.util.Set;

public class ApprovePaymentByPassECMStep extends CommonScenarioSteps {

    PaymentsPage payPage;

    @Steps
    CreatePaymentStep createSteps;

    @Steps
    ApprovePaymentStep approvePaymentStep;

    @Steps
    BankRepaymentStep repaymentStep;

    BranchCode branchCode = new BranchCode();

    public void View_Payment_InBank_Transaction(LoginStep loginStep, CaseInsensitiveMapObjWrapper<ApprovePayment> data){

//        if(Commons.isBlankOrEmpty(data.dt().getTestCase())){
//            CreateIssueDemo demo = new CreateIssueDemo();
//            demo.CreateTestCase();
//        }
        loginStep.loginBank(data.dt().getSiteBank());
        createSteps.choose_SupplyChainFinance_Func();
        approvePaymentStep.click_Tools_Button();
        approvePaymentStep.click_External_Mes_Monitor_Button();
        repaymentStep.select_Payment_With_MonitorID(data.dt().getMonitorID());
        approvePaymentStep.select_MonitorID(data.dt().getMonitorID());
        approvePaymentStep.click_View_Button();
        Map<String, String> mapList = approvePaymentStep.click_View_Request_Button();
        String AckID = approvePaymentStep.click_View_Response_Button();
        mapList.put("ackID",AckID);

        Set<String> set = mapList.keySet();
        for (String key : set) {
            data.put(key,mapList.get(key));

        }

        Map<String,String> listBranchCode = branchCode.getBranchCodeList();
        Set<String> set1 = listBranchCode.keySet();
        for (String key : set1) {
            if(data.dt().getBranchCode().equalsIgnoreCase(key)){
                data.put("branchName",listBranchCode.get(key));
            }
        }
        for (String key : set) {
            Commons.log4jAndReport("",key+"\t"+mapList.get(key));
        }


        String maturityDate = convertDatetime(data.dt().getMaturityDate());
        String valueDate = convertDatetime(data.dt().getValueDate());
        String loanAmount = data.dt().getLoanAmount().replace(",","");
        System.out.println(maturityDate);
        WebServiceClient client = new WebServiceClient();
        String LD = client.createNewLoan(data.dt().getAckID(),maturityDate, data.dt().getBranchCode(),data.dt().getBranchName(),
                data.dt().getCustomerID(),loanAmount,valueDate,data.dt().getLiqAcct(),data.dt().getDrawdownAccount(),data.dt().getLoanCurrency());

        //String LD="";
        if(Commons.isBlankOrEmpty(LD)){
            Assert.assertFalse(true);
        }
        data.put("LD",LD);
        System.out.println("LD: "+data.dt().getLD());
        loginStep.logout_with_scf();
    }
    public void Approve_Payment_Transaction(LoginStep loginStep,CaseInsensitiveMapObjWrapper<ApprovePayment> data){
        WebServiceClient client = new WebServiceClient();
        client.approvalLoan(data.dt().getLD());
        After_Checking_T24_Transaction(loginStep,data);
    }
//    public void Approve_Payment_Transaction(LoginStep loginStep,WorkingT24Step t24Step,CaseInsensitiveMapObjWrapper<ApprovePayment> data){
//        //data.put("LD","LD2010080600");
//        //String command = "CN BIZ";
//        if(data.dt().getLD() ==null || data.dt().getLD() ==""){
//            Commons.log4jAndReport("Cannot create LD");
//
//        }else{
//            loginStep.loginT24(data.dt().getT24maker());
//            String command = "CN "+data.dt().getBranchName();
//            t24Step.input_Command_Key(command);
//            t24Step.switch_To_Working_Page();
//            t24Step.switch_To_Menu_Frame();
//            t24Step.goto_Techcombank_Loan();
//            t24Step.switch_To_LoanAndDeposit_Page();
//            t24Step.input_LD(data.dt().getLD());
//            t24Step.input_Interest_Spread(data.dt().getInterestSpread());
//            t24Step.select_VayMon_Option(data.dt().getVaymon());
//
//            t24Step.ClickDealButton();
//            boolean check = t24Step.checkingT24();
//            //System.out.println("check :::"+check);
//            //Commons.waitAction(1000000);
//            if(check){
//                Assert.assertFalse(check);
//
//            }else{
//                t24Step.click_AcceptOverride();
//                loginStep.logout_with_scf();
//                checking_T24_Checker_Transaction(loginStep,t24Step,data);
//                After_Checking_T24_Transaction(loginStep,data);
//            }
//        }
//    }
//
//    public void checking_T24_Checker_Transaction(LoginStep loginStep,WorkingT24Step t24Step, CaseInsensitiveMapObjWrapper<ApprovePayment> data){
//        loginStep.loginT24(data.dt().getT24checker());
//        String command = "CN "+data.dt().getBranchName();
//        t24Step.input_Command_Key(command);
//        t24Step.switch_To_Working_Page();
//        t24Step.switch_To_Menu_Frame();
//        t24Step.goto_DuyetVaChinhSuaLaiSuat1Buoc();
//        t24Step.switch_To_LoanAndDeposit_Page();
//        t24Step.input_LD_Authorise(data.dt().getLD());
//        t24Step.click_Perform_Button();
//        t24Step.click_Authorise_Deal_Button();
//        loginStep.logout_with_scf();
//
//    }

    public void After_Checking_T24_Transaction(LoginStep loginStep,CaseInsensitiveMapObjWrapper<ApprovePayment> data){
        WebServiceClient client = new WebServiceClient();
        String loanAmount = data.dt().getLoanAmount().replace(",","");
        System.out.println(loanAmount);

        try{
            boolean check = client.updateLoanStatus(data.dt().getMonitorID(),data.dt().getAckID(),
                    data.dt().getLD(),data.dt().getValueDate(),data.dt().getMaturityDate(),loanAmount,data.dt().getCustomerID(),data.dt().getDrawdownAccount(),data.dt().getLoanCurrency());
            System.out.println("check: "+check);
        }catch (Exception e){
            Commons.log4jAndReport("", e.getMessage());
        }

        loginStep.loginBank(data.dt().getSiteBank());
        createSteps.choose_SupplyChainFinance_Func();
        approvePaymentStep.click_Tools_Button();
        approvePaymentStep.click_External_Mes_Monitor_Button();
        approvePaymentStep.select_MonitorID(data.dt().getMonitorID());;
        String status = approvePaymentStep.get_Status(data.dt().getMonitorID());
        Commons.getLogger().info(status);
        Assert.assertEquals(data.dt().getStatus(),status);
        approvePaymentStep.click_View_Button();
        Commons.waitAction(5000);


    }

    public String convertDatetime(String valuedate){
        String dateConvert[] = TimeController.funcFormatDate(valuedate,"yyyy-MM-dd", "yyyy-MM-dd").split("-");
        String year = dateConvert[0];
        String month = dateConvert[1];
        String date = dateConvert[2];
        return year.concat(month).concat(date);
    }

    public String convertDatetime2(String valudate){
        String dateConvert[] = TimeController.funcFormatDate(valudate,"yyyy-MM-dd", "yyyy-MM-dd").split("-");
        String year = dateConvert[0];
        String month = dateConvert[1];
        String date = dateConvert[2];
        return date.concat("/").concat(month).concat("/").concat(year);
    }
}
