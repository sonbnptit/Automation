package features;

import com.vin3s.auto.dataobject.SearchingKeywordObject;
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
import steps.QuanlygiaodichStep;

@RunWith(SerenityParameterizedExcelRunner.class)
@WithTagValuesOf({ "SearchingKeyword" })
@UseTestDataFrom(value = "data/Blank.csv")
@UseTestDataFromExcel(value = "data/SearchingData.xlsx", sheet = "SearchingKeyword")
public class SearchingKeywordFeature extends SearchingKeywordObject {

    @Managed
    WebDriver driver;

    @Steps
    QuanlygiaodichStep qlgdStep;

    public static CaseInsensitiveMapObjWrapper<SearchingKeywordObject> data;

    @Before
    public void logIn_and_getData() {
        data = new CaseInsensitiveMapObjWrapper<>(this);
        Commons.setDataAllSteps(this, data);
    }

    @Test
    public void SeachingKeywordFeatureSuscess(){
        qlgdStep.SearchingFunction(data);

    }

    @After
    public void tearDown() {
        //close DB connection
        driver.quit();
    }
}




