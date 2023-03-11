package com.tcb.scf.payments.step;

import com.tcb.auto.serenity.driver.CustomChromeDriver;
import com.tcb.auto.utils.Commons;
import com.tcb.scf.dataObject.Login;
import com.tcb.auto.serenity.CommonScenarioSteps;
import com.tcb.auto.utils.ConfigController;
import com.tcb.scf.driver.CustomProfileChromeDriver;
import com.tcb.scf.payments.page.LoginPage;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.openqa.selenium.WebDriver;

public class LoginStep extends CommonScenarioSteps {

    LoginPage login;

    ConfigController cc = new ConfigController();

    CustomProfileChromeDriver cusDriver   = new CustomProfileChromeDriver();


    static String companyName,browser, userName, Password;

    public void setUpLogingData(String role) {
        Login login = new Login();

        this.companyName = login.getData(LoginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_COMPANY_NAME);
        this.browser = login.getData(LoginPage.LOGIN_FILE_PATH,login.SHEETNAME,role,login.COLUMN_BROWSER);
        this.userName = login.getData(LoginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_USERNAME);
        this.Password = login.getData(LoginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_PASSWORD);
    }

    public void login(String role){
        setUpLogingData(role);
//        switch_browser_name(browser);
//        cusDriver.newDriver();
        onThe_KHPage();
        input_CompanyName(companyName);
        input_userName(userName);
        input_Password(Password);
        click_Loginbtn();
    }

    public void loginBank(String role){
        setUpLogingData(role);
//        switch_browser_name(browser);
//        cusDriver.newDriver();
        onThe_BankPage();
        input_CompanyName(companyName);
        input_userName(userName);
        input_Password(Password);
        click_Loginbtn();
    }


    public void onThe_KHPage() {
        login.openUrl(cc.getProperty("url.kh"));
    }

    public void onThe_BankPage() {
        login.openUrl(cc.getProperty("url.bank"));
    }

    public void onThe_ECMPage() {
        login.openUrl(cc.getProperty("url.ecm"));
    }

    public void onThe_T24Page() {
        login.openUrl(cc.getProperty("url.t24"));
    }

    @Step("Input company Name")
    public void input_CompanyName(String companyName){

        login.enter_companyName(companyName);
    }

    @Step("Input Username")
    public void input_userName(String userName){
        login.enter_Username(userName);
    }

    @Step("Input password")
    public void input_Password(String password){
        login.enter_Password(password);
    }

    @Step("Click button Login")
    public void click_Loginbtn(){
        login.submit_loginCredential();
    }

    @Step
    public void logout_with_scf(){
        login.closeDriver();
    }


    @Step
    public void click_submitbtn_ecm() {
        login.click_submitbtn_ecm();
    }




    @Step("Input Username")
    public void input_ECMName(String userName){
        login.enter_ECMUsername(userName);
    }

    @Step("Input password")
    public void input_ECMPass(String password){
        login.enter_ECMPassword(password);
    }

    @Step
    public void input_T24UserName(String username){
        login.enter_T24Username(username);
    }
    @Step
    public void input_T24UserPassword(String password){
        login.enter_T24Password(password);
    }

    @Step
    public void click_Submit_Button_T24() {
        login.click_SubmitBtn_T24();
    }

    @Step
    public void loginT24(String role) {
        setUpLogingData(role);
//        switch_browser_name(browser);
//        cusDriver.newDriver();
        onThe_T24Page();
        input_T24UserName(userName);
        input_T24UserPassword(Password);
        click_Submit_Button_T24();

    }

    public void switch_browser_name(String browser){
        if(getDriver() != null){
            getDriver().quit();
        }
        cc.setEnvironmentVariables("Env.Web.browser", browser);

    }

}
