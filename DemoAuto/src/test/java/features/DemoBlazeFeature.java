package features;

import com.vin3s.auto.dataobject.DemoBlazeObject;
import com.vin3s.auto.serenity.UseTestDataFromExcel;
import com.vin3s.auto.utils.CaseInsensitiveMapObjWrapper;
import com.vin3s.auto.utils.Commons;
import net.serenitybdd.junit.runners.SerenityParameterizedExcelRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import steps.DemoBlazeStep;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "DemoBlaze" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/demoblaze.xlsx", sheet = "Data")
public class DemoBlazeFeature extends DemoBlazeObject {
    @Managed
    WebDriver driver;

    @Steps
    DemoBlazeStep blazeStep;

    public static CaseInsensitiveMapObjWrapper<DemoBlazeObject> data;

    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void TestDevice5(){
        CompareInformationDevice(data);
    }

    public void CompareInformationDevice(CaseInsensitiveMapObjWrapper<DemoBlazeObject> data){
        blazeStep.gotoDemoBlazePage();
        blazeStep.ClickGotoDetailsPageOfProductname(data.dt().getDeviceName());
        blazeStep.CompareNameProduct(data.dt().getDeviceName());
        blazeStep.CompareDescriptionProduct(data.dt().getDescription());
        blazeStep.ComparePriceProduct(data.dt().getPrice());
    }

    @After
    public void tearDown() {
        //close DB connection

        driver.quit();
    }
}
