package com.tcb.scf.payments.page;

import com.tcb.auto.serenity.CommonPageObject;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.TimeController;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Resource(name = "scf.bank")
public class BankRepaymentPage extends CommonPageObject {

    ConfigController cc = new ConfigController();
    WebElementController wc = new WebElementController();

    By bank_select_language = mapPgObject.get("bank_select_language").get();
    By bank_supply_chain_finance_btn = mapPgObject.get("bank_supply_chain_finance_btn").get();
    By bank_data_admin_btn = mapPgObject.get("bank_data_admin_btn").get();

    By bank_process_btn =  mapPgObject.get("bank_process_btn").get();
    By bank_approve_btn =  mapPgObject.get("bank_approve_btn").get();
    By bank_submit_btn = mapPgObject.get("bank_submit_btn").get();
    By bank_status_actual = mapPgObject.get("bank_status_actual").get();
    By bank_OK_btn = mapPgObject.get("bank_OK_btn").get();
    By bank_start_btn = mapPgObject.get("bank_start_btn").get();
    By bank_View_btn = mapPgObject.get("bank_View_btn").get();
    By bank_Back_btn = mapPgObject.get("bank_Back_btn").get();

        //Data admin
    By bank_worflow_func = mapPgObject.get("bank_worflow_func").get();
    By workflow_setup_queue_function = mapPgObject.get("workflow_setup_queue_function").get();
    By bank_tools_func = mapPgObject.get("bank_tools_func").get();
    By tool_schedule_manager_func = mapPgObject.get("tool_schedule_manager_func").get();
    By bank_admin_func = mapPgObject.get("bank_admin_func").get();
    By admin_bank_func = mapPgObject.get("admin_bank_func").get();
    // bank
    By tcb_select = mapPgObject.get("tcb_select").get();
    //By bank_name_txt = mapPgObject.get("bank_name_txt").get();
    By open_btn = mapPgObject.get("open_btn").get();
    By business_date = mapPgObject.get("business_date").get();
    By eod_date = mapPgObject.get("eod_date").get();
    //setup queue
    By select_pipay_schedule = mapPgObject.get("select_pipay_schedule").get();
    By popup_frame = mapPgObject.get("popup_frame").get();


    //Supply Chain Finance
    By supply_tools_func = mapPgObject.get("supply_tools_func").get();
    By subpply_exteral_mes_monitor_func = mapPgObject.get("subpply_exteral_mes_monitor_func").get();
    By close_popup_btn =mapPgObject.get("close_popup_btn").get();
    By bank_Search_btn = mapPgObject.get("bank_Search_btn").get();
    By message_type_Select =mapPgObject.get("message_type_Select").get();
    By status_select = mapPgObject.get("status_select").get();
    By business_date_input = mapPgObject.get("business_date_input").get();
    By EOD_date_input = mapPgObject.get("EOD_date_input").get();

    public void select_language(String language){
        Commons.waitAction(2000);
        element(bank_select_language).waitUntilEnabled().selectByValue(language);
    }

    public void click_supply_chain_finance_btn(){
        element(bank_supply_chain_finance_btn).waitUntilEnabled().click();
    }

    public void click_Data_Admin_func(){
        Commons.waitAction(1000);
        element(bank_data_admin_btn).waitUntilEnabled().click();
    }

    public void click_Admin_func(){
        element(bank_admin_func).waitUntilEnabled().click();
    }

    public void click_Bank_func(){
        element(admin_bank_func).waitUntilEnabled().click();
    }

    public void choose_tcb_Bank(){
        element(tcb_select).waitUntilEnabled().click();
    }

    public void click_Open_button(){
        element(open_btn).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public String get_Bank_code(){
        return element(mapPgObject.get("bank_name_txt").get()).getTextContent();
    }

//    private String selectMonth(String month){
//        String select_month ="";
//        switch (month){
//            case "01":
//                select_month = "January";
//                break;
//            case "02":
//                select_month = "February";
//                break;
//            case "03":
//                select_month = "March";
//                break;
//            case "04":
//                select_month = "April";
//                break;
//            case "05":
//                select_month = "May";
//                break;
//            case "06":
//                select_month = "June";
//                break;
//            case "07":
//                select_month = "July";
//                break;
//            case "08":
//                select_month = "August";
//                break;
//            case "09":
//                select_month = "September";
//                break;
//            case "10":
//                select_month = "Octorber";
//                break;
//            case "11":
//                select_month = "November";
//                break;
//            case "12":
//                select_month = "December";
//                break;
//        }
//        return select_month;
//    }
//    private By valueDate_dateToday(String date) {
//        if (date.startsWith("0"))
//            date = date.replaceFirst("0", "");
//        //return By.xpath(String.format("//td/a[@class='date-today']/b[text()='%s']", date));
//        return By.xpath(String.format("//td/a[@class='date-weekday' and text()='%s']", date));
//    }
//
//    private By valueDate_dateAnotherDay(String date) {
//        if (date.startsWith("0"))
//            date = date.replaceFirst("0", "");
//        return By.xpath(String.format("//td/a[@class='date-weekday' and text()='%s']", date));
//    }

//    public By valueDate(String date) {
//        if (date.startsWith("0"))
//            date = date.replaceFirst("0", "");
//       // return By.xpath(String.format("//td/a[@class='date-weekday' and text()='%s']", date));
//        return  mapPgObject.get("day_popup").by(date);
//    }
//
//    public void valueDate_month(String month) {
//        String s = selectMonth(month);
//        element(mapPgObject.get("month_popup").get()).selectByVisibleText(selectMonth(month));
//    }
//
//    public void valueDate_year(String year) {
//        element(mapPgObject.get("year_popup").get()).type(year);
//    }

    public void choose_ValueDate(By locator, String valuedate) {

        try{
            withAction().moveToElement(element(locator)).build().perform();
            wc.sendkeyJS(getDriver(),valuedate,locator);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        Commons.waitAction(2000);

    }


    public void choose_Businessdate(String valueDate){
        choose_ValueDate(business_date_input,valueDate);
    }

    public void choose_EODdate(String valueDate){
        choose_ValueDate(EOD_date_input,valueDate);
    }

    public void click_Submit_Btn(){
        withAction().moveToElement(element(bank_submit_btn)).build().perform();
        element(bank_submit_btn).waitUntilEnabled().click();
    }

    public void click_OK_Btn(){
        element(bank_OK_btn).waitUntilEnabled().click();
    }

    public void click_Workflow_function(){
        element(bank_worflow_func).waitUntilEnabled().click();
    }

    public void click_Setup_Queue_function(){
        element(workflow_setup_queue_function).waitUntilEnabled().click();
    }

    private By put_Bankname(String bankname){
        return mapPgObject.get("queue_bank_select").by(bankname);
    }

    public void choose_Queue_Bank(String bankName){
        element(put_Bankname(bankName)).waitUntilEnabled().click();
    }

    public void click_Process_btn(){
        element(bank_process_btn).waitUntilEnabled().click();
    }

    public void click_Approve_btn(){
        element(bank_approve_btn).waitUntilEnabled().click();
    }

    public void click_Tools_func(){
        element(bank_tools_func).waitUntilEnabled().click();
    }

    public void click_Schedule_Manager_func(){
        element(tool_schedule_manager_func).waitUntilEnabled().click();
    }

    public void choose_PIPAY_schedule(){
        element(select_pipay_schedule).waitUntilEnabled().click();
    }

    public void click_Start_btn(){
        element(bank_start_btn).waitUntilEnabled().click();
    }

    public void Click_supply_Tools_Func(){
        element(supply_tools_func).waitUntilEnabled().click();
    }

    public void Click_Tools_External_Message_Monitor_Func(){
        element(subpply_exteral_mes_monitor_func).waitUntilEnabled().click();
    }


    public void click_View_btn(){
        element(bank_View_btn).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public void click_Back_Btn(){
        Commons.waitAction(1000);
        element(bank_Back_btn).waitUntilEnabled().click();
        Commons.waitAction(1000);
    }

    public void click_ClosePopup_btn(){
        wc.switchToTheRootFrame(getDriver());
        element(close_popup_btn).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public By put_Value(String locator,String value){
        return mapPgObject.get(locator).by(value);

    }


    public void select_element(By locator){
        element(locator).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public List<WebElementFacade> getListElement(String stringLocator,String put_Value) {
         By newLocator = put_Value(stringLocator,put_Value);
        return findAll(newLocator);

    }

    public void click_Search_Btn(){
        element(bank_Search_btn).waitUntilEnabled().click();
    }


    public void select_Message_Type(String type){
        element(message_type_Select).selectByValue(type);
        Commons.waitAction(500);
    }

    public void select_Status_Type(String type){
        element(status_select).selectByValue(type);
        Commons.waitAction(500);
    }

    public void input_MonitorID(String monitorID){
        element(mapPgObject.get("monitor_ID_txt").get()).type(monitorID);
        Commons.waitAction(1000);
    }

    public void switch_PopupFrame(){
        wc.switchFrame(getDriver(),popup_frame);
    }


    public String click_View_Response_Message(By locator){
        element(locator).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(3000);
        String s = element(mapPgObject.get("view_response_message").get()).getText();
        System.out.println(s);
        String AckId = "";
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(s.toString().getBytes("UTF-8"));
            Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("LoanRepayResponse");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                Element eElement = (Element) nNode;
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    AckId = eElement.getElementsByTagName("AcknowledgementId").item(0).getTextContent();
                    //System.out.println(AckId);
                    break;
                }
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return AckId;

    }




}
