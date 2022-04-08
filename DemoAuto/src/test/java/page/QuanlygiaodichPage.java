package page;

import com.vin3s.auto.serenity.CommonPageObject;
import com.vin3s.auto.subprocess.web.WebElementController;
import com.vin3s.auto.utils.Commons;
import org.openqa.selenium.Keys;

import javax.annotation.Resource;

@Resource(name = "resource.qlgd")
public class QuanlygiaodichPage extends CommonPageObject {
    WebElementController wc = new WebElementController();

    public void click_Quanlygiaodich_btn(){
        wc.switchWindowNative(getDriver());
        element(mapPgObject.get("quanlygiaodich_btn").get()).waitUntilClickable().click();
    }

    public void click_Quanlygiaodichsac_btn(){
        element(mapPgObject.get("quanlygiaodichsac_btn").get()).waitUntilClickable().click();

    }

    public void input_search_textfield(String search){
        element(mapPgObject.get("search_txt").get()).waitUntilClickable().sendKeys(search);
    }

    public void click_Search_btn(){
        try{
            wc.sendFunctionKey(getDriver(), Keys.ENTER);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String get_Text_result(){
        return element(mapPgObject.get("result_txt").get()).waitUntilVisible().getText();
    }

    public String get_Text_result_inTable(){
        return element(mapPgObject.get("result_in_table_txt").get()).waitUntilVisible().getText();
    }


    public void click_Fillter_btn(){
        element(mapPgObject.get("fillter_btn").get()).waitUntilClickable().click();
    }

}
