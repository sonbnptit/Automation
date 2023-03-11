package com.tcb.scf.payments.page;

import com.tcb.auto.serenity.CommonPageObject;
import com.tcb.auto.serenity.Testcase;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.TimeController;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import javax.annotation.Resource;
import java.util.List;

@Resource(name = "scf.t24")
public class WorkingT24Page extends CommonPageObject {

    WebElementController wc = new WebElementController();

    By key_command_input = mapPgObject.get("key_command_input").get();
    By banner_frame = mapPgObject.get("banner_frame").get();
    By menu_frame = mapPgObject.get("menu_frame").get();
    By menu_LoanAndPastDue_btn = mapPgObject.get("menu_LoanAndPastDue_btn").get();
    By menu_LoanAndPastDue_Input_btn = mapPgObject.get("menu_LoanAndPastDue_Input_btn").get();
    By menu_LoanAndPastDue_Authorise_btn = mapPgObject.get("menu_LoanAndPastDue_Authorise_btn").get();
    By menu_Input_LoanAndPastDue_btn = mapPgObject.get("menu_Input_LoanAndPastDue_btn").get();
    By menu_Loans_Btn = mapPgObject.get("menu_Loans_Btn").get();
    By menu_Input_Techcombank_Loan_btn = mapPgObject.get("menu_Input_Techcombank_Loan_btn").get();
    By input_transactionID = mapPgObject.get("input_transactionID").get();
    By commit_btn = mapPgObject.get("commit_btn").get();
    By value_date_btn = mapPgObject.get("value_date_btn").get();
    By select_month = mapPgObject.get("select_month").get();
    By select_year = mapPgObject.get("select_year").get();
    By interest_spread_input = mapPgObject.get("interest_spread_input").get();
    By accept_message = mapPgObject.get("accept_message").get();
    By menu_Author_AuthorLoansAndPastDue_btn = mapPgObject.get("menu_Author_AuthorLoansAndPastDue_btn").get();
    By menu_AuthorLoansAndPastDue_Loan_btn = mapPgObject.get("menu_AuthorLoansAndPastDue_Loan_btn").get();
    By menu_DuyetVaChinhSuaBienDoLaisuat1buoc_btn = mapPgObject.get("menu_DuyetVaChinhSuaBienDoLaisuat1buoc_btn").get();
    By authorise_deal_btn = mapPgObject.get("authorise_deal_btn").get();
    By Perform_Btn = mapPgObject.get("Perform_Btn").get();

    public void input_Key_Command_Input(String keyInput){
        element(key_command_input).type(keyInput);
        Commons.waitAction(200);
    }

    public void switch_To_Banner_Frame(){
        Commons.waitAction(1000);
        wc.switchFrame(getDriver(),banner_frame);
    }

    public void switch_To_Menu_Frame(){
        wc.switchFrame(getDriver(),menu_frame);
    }

    public void switch_To_ChinhanhHN(){
        Commons.waitAction(1000);
        wc.waitForWindowPresentByUrl(getDriver(),"https://t24dev2.techcombank.com.vn/servlet/BrowserServlet",3);
    }

    public void goto_TechComBankLoan(){
        element(menu_LoanAndPastDue_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_LoanAndPastDue_Input_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_Input_LoanAndPastDue_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_Loans_Btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_Input_Techcombank_Loan_btn).waitUntilEnabled().click();
        Commons.waitAction(1000);
    }

    public void goto_DuyetVaChinhSuaLaiSuat(){
        element(menu_LoanAndPastDue_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_LoanAndPastDue_Authorise_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_Author_AuthorLoansAndPastDue_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(menu_AuthorLoansAndPastDue_Loan_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
        withAction().moveToElement(element(menu_DuyetVaChinhSuaBienDoLaisuat1buoc_btn)).build().perform();
        Commons.waitAction(500);
        element(menu_DuyetVaChinhSuaBienDoLaisuat1buoc_btn).waitUntilEnabled().click();
        Commons.waitAction(500);
    }

    public void enter_key(){
        try{
            wc.sendFunctionKey(getDriver(), Keys.ENTER);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void switch_To_LoanAndDeposit_Page(){
        wc.switchWindow(getDriver(),input_transactionID);
    }

    public void input_LD(String LD){
        element(input_transactionID).type(LD);
        Commons.waitAction(1000);
        enter_key();
        Commons.waitAction(1000);
    }

    public void input_LD_Authorise(String LD){
        element(input_transactionID).type(LD);
        Commons.waitAction(1000);

    }


    public void valueDate_month(String month) {
        Commons.waitAction(1000);
        element(select_month).selectByValue(month);
    }

    public void valueDate_year(String year) {
        Commons.waitAction(1000);
        element(select_year).selectByVisibleText(year);
    }

    public By valueDate_date(String date) {
        if (date.startsWith("0"))
            date = date.replaceFirst("0", "");
        return mapPgObject.get("select_day").by(date);
    }

    public void input_MaturityDate(String date){
        try{
            wc.sendkeyJS(getDriver(),date,mapPgObject.get("t24_maturity_date").get());
        }catch(Exception e){
            Commons.getLogger().info(e.getMessage());
        }

    }

    public void click_ChooseValueDate(){
        Commons.waitAction(1000);
        element(value_date_btn).waitUntilPresent().click();
    }

    public void choose_Month(String month){
        valueDate_month(month);
    }

    public void choose_Year(String year){
        valueDate_year(year);
    }

    public void choose_Date(String date){
        //Commons.getLogger().info(valueDate_date(date));
        Commons.waitAction(1000);
        element(valueDate_date(date)).waitUntilPresent().click();
        Commons.waitAction(1000);
    }

    public void click_Accept_Override(){
        element(accept_message).waitUntilEnabled().click();
    }



    public By put_VayMonOption(String value){
        return mapPgObject.get("vay_mon").by(value);
    }

    public void select_VaymonOption(String vaymon){
        element(put_VayMonOption(vaymon)).click();
    }

    public void click_DealBtn(){
        Commons.waitAction(2000);
        element(commit_btn).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public void click_Perform_Btn(){
        Commons.waitAction(1000);
        element(Perform_Btn).waitUntilEnabled().click();
    }
    public void click_Authorise_Btn(){
        Commons.waitAction(2000);
        element(authorise_deal_btn).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public void input_InterestSpread(String intSpread){
        element(interest_spread_input).type(intSpread);
    }

    public boolean checking_T24(){
        boolean check;
        if(element(mapPgObject.get("error_field").get()).isCurrentlyVisible()){


            List<WebElement> list = getDriver().findElements(By.xpath(String.format("//td[@class='errors']/a/span")));
            List<WebElement> list2 = getDriver().findElements(By.xpath(String.format("//td[@class='errorText']/span")));
            Commons.getLogger().info("Fail in T24");
            for (int i = 0;i<list.size();i++){

                for(int j = 0;j<list2.size();j++){
                    if(i==j){
                        Commons.log4jAndReport("Error field",list.get(i).getText() +"\t"+ list2.get(j).getText());
                    }
                }
            }
            check = true;
        }else{
            Commons.log4jAndReport("Error field","Cannot find Error field");
            check = false;
        }
        return check;
    }



}
