package com.tcb.scf.payments.page;


import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.serenity.CommonPageObject;
import org.openqa.selenium.By;

import javax.annotation.Resource;

@Resource(name = "scf.login")
public class LoginPage extends CommonPageObject {
    ConfigController cc = new ConfigController();
    WebElementController wc = new WebElementController();

    private By T24Username = mapPgObject.get("T24Username").get();
    private By T24Password = mapPgObject.get("T24Password").get();
    private By T24SignInBtn = mapPgObject.get("T24SignInBtn").get();

    public static String LOGIN_FILE_PATH = System.getProperty("user.dir") + "\\data\\Login_data.xlsx";

    public void enter_companyName(String companyName) {
        element(mapPgObject.get("scf_companyName").get()).type(companyName);
    }

    public void enter_Username(String username) {

        element(mapPgObject.get("scf_username_txt").get()).type(username);
    }

    public void enter_Password(String password) {

        element(mapPgObject.get("scf_password_txt").get()).type(password);
    }

    public void submit_loginCredential() {
        element(mapPgObject.get("scf_Logginbtn").get()).click();

    }
    public void closeDriver(){
        wc.closeDriver(getDriver());
    }

	public void enter_ECMUsername(String userName) {
		element(mapPgObject.get("user_name_ecm").get()).type(userName);
	}

	public void enter_ECMPassword(String password) {
		element(mapPgObject.get("pass_word_ecm").get()).type(password);
		
	}

    public void enter_T24Username(String userName) {
        element(T24Username).type(userName);
    }

    public void enter_T24Password(String password) {
        element(T24Password).type(password);
    }

	public void click_submitbtn_ecm() {
        wc.clickJS(getDriver(),mapPgObject.get("submit_btn_ecm").get());
		 //element(mapPgObject.get("submit_btn_ecm").get()).click();
	}

    public void click_SubmitBtn_T24() {

        element(T24SignInBtn).waitUntilEnabled().click();
    }


}