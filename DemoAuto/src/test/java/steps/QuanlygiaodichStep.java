package steps;

import Webservice.WebserviceClient;
import com.vin3s.auto.dataobject.SearchingKeywordObject;
import com.vin3s.auto.serenity.CommonScenarioSteps;
import com.vin3s.auto.utils.CaseInsensitiveMapObjWrapper;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import org.junit.Assert;
import page.QuanlygiaodichPage;

public class QuanlygiaodichStep extends CommonScenarioSteps {

    @Page
    QuanlygiaodichPage qlgdPage;

    @Steps
    EmspLoginStep emspLoginStep;


    public void SearchingFunction(CaseInsensitiveMapObjWrapper<SearchingKeywordObject> data){
        emspLoginStep.LoginFunction(data.dt().getRole());
        Click_QuanlyGiaodich_Button();
        Click_QuanlyGiaodichSac_Button();
        Input_SearchString(data.dt().getKeyword());
        Click_Search_Button();
        String result = getValueResult();
        boolean check = false;
        if(result.equals("Tìm thấy 1 kết quả")){
            check = true;
        }
        Assert.assertTrue("Assert True: ",check) ;
        //Commons.log4jAndReport(result);
    }

    @Step("Click button Quan ly giao dich: ")
    public void Click_QuanlyGiaodich_Button(){
        qlgdPage.click_Quanlygiaodich_btn();

    }

    @Step("Click button Quan ly giao dich sac: ")
    public void Click_QuanlyGiaodichSac_Button(){
        qlgdPage.click_Quanlygiaodichsac_btn();
    }

    @Step("Click button Tim kiem: ")
    public void Click_Search_Button(){
        qlgdPage.click_Search_btn();
    }

    @Step("Click button Fillter")
    public void Click_Fillter_Button(){
        qlgdPage.click_Fillter_btn();
    }

    @Step("Nhap gia tri tim kiem: {0}")
    public void Input_SearchString(String search){
        qlgdPage.input_search_textfield(search);
    }

    @Step("Lấy giá trị kết quả")
    public String getValueResult(){
        return qlgdPage.get_Text_result();
    }



}
