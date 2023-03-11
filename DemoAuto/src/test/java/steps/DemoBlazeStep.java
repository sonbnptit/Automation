package steps;

import com.vin3s.auto.serenity.CommonScenarioSteps;
import net.thucydides.core.annotations.Step;
import org.fluentlenium.core.annotation.Page;
import org.junit.Assert;
import page.DemoBlazePage;

public class DemoBlazeStep extends CommonScenarioSteps {
    @Page
    DemoBlazePage blazePage;

    @Step("Go to Demo Blaze Page")
    public void gotoDemoBlazePage(){
        blazePage.GotoDemoBlazeHomePage();
    }

    @Step("Go to Details Page of product {0}")
    public void ClickGotoDetailsPageOfProductname(String deviceName){
        blazePage.click_gotoDetailspage(deviceName);
    }

    @Step("Compare Information of Product {0}: name")
    public void CompareNameProduct(String deviceName){
        String name = blazePage.getInformationDeviceName();
        Assert.assertEquals(name,deviceName);

    }

    @Step("Compare Information of description: {0}")
    public void CompareDescriptionProduct(String description){
        String name = blazePage.getInformationDescription();
        Assert.assertEquals(name,description);

    }
    @Step("Compare Information of  price: {0}")
    public void ComparePriceProduct(String price){
        String name = blazePage.getInformationPrice();
        Assert.assertEquals(name,price);

    }
}
