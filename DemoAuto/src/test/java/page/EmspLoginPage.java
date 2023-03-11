package page;

import com.vin3s.auto.serenity.CommonPageObject;
import com.vin3s.auto.subprocess.web.WebElementController;
import com.vin3s.auto.utils.Commons;
import javax.annotation.Resource;

@Resource(name = "resource.login")
public class EmspLoginPage extends CommonPageObject {

    com.vin3s.auto.subprocess.web.WebElementController wc = new WebElementController();
    public static String LOGIN_FILE_PATH = System.getProperty("user.dir") + "\\data\\Login_data.xlsx";


    public void click_Login_Button(String username,String password){
        element(mapPgObject.get("btn_Login").get()).waitUntilClickable().click();
        //Commons.waitAction(5000);
        wc.switchWindow(getDriver(),mapPgObject.get("username_txt").get());
        element(mapPgObject.get("username_txt").get()).waitUntilEnabled().sendKeys(username);
        element(mapPgObject.get("next_button").get()).waitUntilClickable().click();
        //Commons.waitAction(2000);
        wc.switchWindow(getDriver(),mapPgObject.get("password_txt").get());
        element(mapPgObject.get("password_txt").get()).waitUntilEnabled().sendKeys(password);
        element(mapPgObject.get("next_button").get()).waitUntilClickable().click();
        //Commons.waitAction(2000);
        wc.switchWindow(getDriver(),mapPgObject.get("next_button").get());
        element(mapPgObject.get("next_button").get()).waitUntilClickable().click();
    }

}


