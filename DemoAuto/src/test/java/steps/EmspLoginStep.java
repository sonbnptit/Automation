package steps;

import com.vin3s.auto.dataobject.LoginObject;
import com.vin3s.auto.serenity.CommonScenarioSteps;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import net.thucydides.core.annotations.Step;
import org.fluentlenium.core.annotation.Page;
import page.EmspLoginPage;

public class

EmspLoginStep extends CommonScenarioSteps {

    @Page
    EmspLoginPage loginPage;

    static String companyName, browser, userName, Password;

    ConfigController cc = new ConfigController();


    public void LoginFunction(String role) {
        setUpLogingData(role);
        toTheHomePage();
        LoginAction(userName, Password);
        Commons.waitAction(5000);

    }


    public void setUpLogingData(String role) {
        LoginObject login = new LoginObject();

        this.companyName = login.getData(loginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_COMPANY_NAME);
        this.browser = login.getData(loginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_BROWSER);
        this.userName = login.getData(loginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_USERNAME);
        this.Password = login.getData(loginPage.LOGIN_FILE_PATH, login.SHEETNAME, role, login.COLUMN_PASSWORD);
    }

    @Step("Navigate to the home page")
    public void toTheHomePage() {
        loginPage.openUrl(cc.getProperty("url.page"));
    }

    @Step("Input username {0}, password {1}, click Login Button")
    public void LoginAction(String username, String pasword) {
        loginPage.click_Login_Button(username, pasword);

    }
}

