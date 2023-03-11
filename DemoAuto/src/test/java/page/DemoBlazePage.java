package page;

import com.vin3s.auto.serenity.CommonPageObject;
import com.vin3s.auto.subprocess.web.WebElementController;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import org.openqa.selenium.By;

import javax.annotation.Resource;

@Resource(name = "resource.demoblaze")
public class DemoBlazePage extends CommonPageObject {
    WebElementController wc = new WebElementController();
    ConfigController cc = new ConfigController();

   public void GotoDemoBlazeHomePage(){
       openUrl(cc.getProperty("url.demo_blaze"));
   }

    private By Device_Put_option(String deviceName){
        runData.put("deviceName",deviceName);
        return mapPgObject.get("device_name").by(deviceName);
    }
    public void click_gotoDetailspage(String nameDevice){

        element(Device_Put_option(nameDevice)).waitUntilClickable().click();
    }

    public String getInformationDeviceName(){

        return element(mapPgObject.get("product_text").get()).getTextValue();
    }

    public String getInformationPrice(){
        return element(mapPgObject.get("product_price").get()).getTextValue();
    }
    public String getInformationDescription(){
        return element(mapPgObject.get("product_description").get()).getTextValue();
    }
}
