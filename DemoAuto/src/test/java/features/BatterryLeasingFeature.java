package features;

import Webservice.WebserviceClient;
import com.vin3s.auto.dataobject.BaterryLeasingCreateObject;
import com.vin3s.auto.serenity.UseTestDataFromExcel;
import com.vin3s.auto.utils.CaseInsensitiveMapObjWrapper;
import com.vin3s.auto.utils.Commons;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "Send Request Baterry Leasing" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/API Data.xlsx", sheet = "API Data")
public class BatterryLeasingFeature extends BaterryLeasingCreateObject {

    @Managed
    WebDriver driver;

    WebserviceClient client = new WebserviceClient();

    public static CaseInsensitiveMapObjWrapper<BaterryLeasingCreateObject> data;

    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void SendAPIBaterryLeasingCreateSuccess(){
        client.sendAPIBaterryLeasingCreate(data);

    }

    @After
    public void tearDown() {
        //close DB connection
        driver.quit();
    }
}
