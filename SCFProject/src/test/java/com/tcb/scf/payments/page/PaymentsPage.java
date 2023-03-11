package com.tcb.scf.payments.page;

import com.tcb.auto.serenity.CommonPageObject;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.subprocess.xml.XmlDriver;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import org.jruby.RubyProcess;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.openqa.selenium.By;
import com.tcb.auto.utils.TimeController;
import org.openqa.selenium.By;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.Map;

@Resource(name = "scf.payments")
public class PaymentsPage extends CommonPageObject {

    ConfigController cc = new ConfigController();
    WebElementController wc = new WebElementController();
    private  XmlDriver xmlDriver = new XmlDriver();

    By select_language = mapPgObject.get("select_language").get();
    By supply_chain_finance_btn = mapPgObject.get("supply_chain_finance_btn").get();
    By workflow_btn = mapPgObject.get("workflow_btn").get();
    By logout_btn = mapPgObject.get("logout_btn").get();
    By payable_btn = mapPgObject.get("payable_btn").get();
    By create_payment_func = mapPgObject.get("create_payment_func").get();
    By maturity_date = mapPgObject.get("maturity_date").get();
    By select_buyer_account = mapPgObject.get("select_buyer_account").get();
    By supplier_account_lookup_btn=mapPgObject.get("supplier_account_lookup_btn").get();
    By supplier_select_button = mapPgObject.get("supplier_select_button").get();
    By popup_frame = mapPgObject.get("popup_frame").get();
    By supplier_search_button = mapPgObject.get("supplier_search_button").get();
    By supplier_account_select = mapPgObject.get("supplier_account").get();
    By account_txt = mapPgObject.get("account_txt").get();
    By payment_ref = mapPgObject.get("payment_ref").get();
    By continue_btn = mapPgObject.get("continue_btn").get();
    By addRow_btn = mapPgObject.get("addRow_btn").get();
    By remittances_type = mapPgObject.get("remittances_type").get();
    By remittances_no_txt = mapPgObject.get("remittances_no").get();
    By net_amount_txt = mapPgObject.get("net_amount").get();
    By Po_No_txt = mapPgObject.get("Po_No").get();
    By comment_txt = mapPgObject.get("comment").get();
    By scf_submit_btn = mapPgObject.get("scf_submit_btn").get();
    By scf_status_actual = mapPgObject.get("scf_status_actual").get();
    By scf_OK_btn = mapPgObject.get("scf_OK_btn").get();
    By user_queue_func = mapPgObject.get("user_queue_func").get();
    By process_btn =mapPgObject.get("process_btn").get();
    By approve_btn =mapPgObject.get("approve_btn").get();

    //checker
    By Receivables_btn = mapPgObject.get("Receivables_btn").get();
    By Discount_by_btn = mapPgObject.get("Discount_by_btn").get();
    By Individual_Payments_btn = mapPgObject.get("Individual_Payments_btn").get();
    By select_account = mapPgObject.get("select_account").get();
    By account_checkbox = mapPgObject.get("account_checkbox").get();
    By txn_Ref_select = mapPgObject.get("txn_Ref_select").get();
    By txn_Ref = mapPgObject.get("txn_Ref").get();

    //bank check

    By tools_func = mapPgObject.get("tools_func").get();
    By exteral_mes_monitor_func = mapPgObject.get("exteral_mes_monitor_func").get();
    By View_btn =mapPgObject.get("View_btn").get();
    By view_Response_btn = mapPgObject.get("view_Response_btn").get();
    By view_Request_btn = mapPgObject.get("view_Request_btn").get();
    By close_popup = mapPgObject.get("close_popup").get();

    public void select_language(String language){
        Commons.waitAction(500);
        element(select_language).waitUntilEnabled().selectByValue(language);
    }

    public void click_Supply_Chane_Finance_Button(){
        Commons.waitAction(2000);
        element(supply_chain_finance_btn).waitUntilVisible().click();


    }

    public void click_Workflow_Button(){
        Commons.waitAction(1000);
        element(workflow_btn).click();
    }

    public void click_User_Queue_Function(){
        Commons.waitAction(1000);
        element(user_queue_func).waitUntilEnabled().click();
    }

    public void click_Logout_Button(){
        element(logout_btn).click();
    }

    public void click_Payable_Button(){
        Commons.waitAction(500);
        element(payable_btn).waitUntilEnabled().click();
    }

    public void choose_Create_Function(){
        Commons.waitAction(500);
        element(create_payment_func).waitUntilVisible().click();
    }

    public void select_BuyerAccount(String account){
        element(select_buyer_account).waitUntilEnabled().selectByValue(account);
    }



    public void select_Supplier_Account(String account){
        Commons.waitAction(1000);
        element(supplier_account_lookup_btn).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(2000);
        element(account_txt).type(account);
        Commons.waitAction(500);
        element(supplier_search_button).waitUntilEnabled().click();
        Commons.waitAction(1000);
        element(supplier_account_select).waitUntilEnabled().click();
        Commons.waitAction(2000);
        element(supplier_select_button).waitUntilVisible().click();
        Commons.waitAction(2000);
    }

    public void input_Payment_Ref(String ref){
        element(payment_ref).type(ref);
    }

    private By valueDate_date(String date) {
        if (date.startsWith("0"))
            date = date.replaceFirst("0", "");
        return By.xpath(String.format("//td/a[@class='date-weekday' and text()='%s']", date));
    }

    private String selectMonth(String month){
        String select_month ="";
        switch (month){
            case "01":
                select_month = "January";
                break;
            case "02":
                select_month = "February";
                break;
            case "03":
                select_month = "March";
                break;
            case "04":
                select_month = "April";
                break;
            case "05":
                select_month = "May";
                break;
            case "06":
                select_month = "June";
                break;
            case "07":
                select_month = "July";
                break;
            case "08":
                select_month = "August";
                break;
            case "09":
                select_month = "September";
                break;
            case "10":
                select_month = "Octorber";
                break;
            case "11":
                select_month = "November";
                break;
            case "12":
                select_month = "December";
                break;
        }
        return select_month;
    }

    private void valueDate_month(String month) {
        element(mapPgObject.get("maturi_month").get()).selectByVisibleText(selectMonth(month));
    }

    private void valueDate_year(String year) {
        element(mapPgObject.get("maturi_year").get()).type(year);
        //return By.xpath(String.format("//td[contains(@class,'date-monthyear')]/a[text()='%s']", year));
    }

    public void chooseMatutiryDate(String valuedate, String currentDateFormat){
        choose_ValueDate(maturity_date,valuedate,currentDateFormat);
    }

    public void choose_ValueDate(By locator,String valuedate, String currentDateFormat) {

        String dateConvert[] = TimeController.funcFormatDate(valuedate, currentDateFormat, "dd/MM/yyyy").split("/");
        String year = dateConvert[2];
        String month = dateConvert[1];
        String date = dateConvert[0];
        Commons.getLogger().info(year);
        Commons.getLogger().info(month);
        Commons.getLogger().info(date);
        element(locator).waitUntilPresent().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(100);
        valueDate_month(month);
        Commons.waitAction(100);
        valueDate_year(year);
        Commons.waitAction(200);
        element(valueDate_date(date)).waitUntilPresent().click();
        Commons.waitAction(2000);

    }

    public void click_Continue_Btn(){
        Commons.waitAction(1000);
        element(continue_btn).waitUntilEnabled().click();
    }

    public void click_Addrow_Btn(){
        Commons.waitAction(1000);
        element(addRow_btn).waitUntilEnabled().click();
    }

    private By remittances_type_option(String remit_type){
        runData.put("RemitType",remit_type);
        return mapPgObject.get("remittances_type_option").by(remit_type);
    }

    public void select_Remittype(String type){
        element(remittances_type).waitUntilEnabled().click();
        element(remittances_type_option(type)).waitUntilEnabled().click();
    }

    public void input_RemmitNo(String remmitNo){
        element(remittances_no_txt).type(remmitNo);
    }
     public void input_Netamount(String amount){
        element(net_amount_txt).type(amount);
     }

     public  void input_Po_No(String po){
        element(Po_No_txt).type(po);
     }

     public void input_Comment(String com){
         element(comment_txt).type(com);
     }

     public void click_Submit_btn(){
         Commons.waitAction(1000);
        element(scf_submit_btn).waitUntilEnabled().click();
     }

     public void click_OK_Btn(){
        Commons.waitAction(1000);
        element(scf_OK_btn).waitUntilEnabled().click();
     }

     public void click_Logout_Btn(){
         Commons.waitAction(1000);
        element(logout_btn).waitUntilEnabled().click();
     }
     public String getStatusMessage(){
        return element(scf_status_actual).getTextContent();
     }

    public void click_Receivable_btn(){
        Commons.waitAction(500);
        element(Receivables_btn).waitUntilEnabled().click();
    }
    public void click_DiscountBy_btn(){
        Commons.waitAction(500);
        element(Discount_by_btn).waitUntilEnabled().click();
    }
    public void click_Individual_Payment_btn(){
        Commons.waitAction(500);
        element(Individual_Payments_btn).waitUntilEnabled().click();
    }

    public void selectAccount(By locator, String account){
        List<String> select = element(locator).getSelectOptions();
        for(int i = 0;i<select.size();i++){
            Commons.getLogger().info(select.get(i).trim().toString());
            if(select.get(i).trim().contains(account)){
                element(locator).selectByVisibleText(select.get(i));
                break;
            }
        }
    }

    public void choose_Buyer_Account(String account){
        Commons.waitAction(500);
        selectAccount(select_buyer_account,account);
    }

    public void choose_Supplyer_Account(String account){
        selectAccount(select_account,account);
    }

    public void checkBox_Payment(){
        Commons.waitAction(1000);
        element(account_checkbox).click();
    }

    private By payment_txn_ref_option(String paymentRef){
        runData.put("PaymentRef",paymentRef);
        return mapPgObject.get("payment_txn_ref").by(paymentRef);
    }

    public void select_Payment_To_Approve(String paymentRef){
        Commons.waitAction(1000);
        element(payment_txn_ref_option(paymentRef)).waitUntilEnabled().click();
    }

    public void click_Process_btn(){
        Commons.waitAction(1000);
        element(process_btn).waitUntilEnabled().click();
    }

    public void click_Approve_btn(){
        Commons.waitAction(1000);
        element(approve_btn).waitUntilEnabled().click();
    }

    public void select_Txn_Ref(){
        element(txn_Ref_select).waitUntilEnabled().click();
    }

    public String get_Txn_Ref(){
        return element(txn_Ref).getTextContent().trim();
    }

    public void click_Tool_btn(){
        element(tools_func).waitUntilEnabled().click();
    }

    public void click_External_Mes_Monitor(){
        element(exteral_mes_monitor_func).waitUntilEnabled().click();
    }

    private By txnRef_Select_Option(String txnRef){
        return mapPgObject.get("txnRef_Select").by(txnRef);
    }

    public void txnRef_Select(String ref){
        element(txnRef_Select_Option(ref)).waitUntilEnabled().click();
    }

    private By MonitorID_SelectOption(String monitorID){
        return mapPgObject.get("MonitorID_Select").by(monitorID);
    }

    public void MonitorID_Select(String monitorID){
        element(MonitorID_SelectOption(monitorID)).waitUntilEnabled().click();
    }

    public void click_View_btn(){
        element(View_btn).waitUntilEnabled().click();
    }


    private By txnRef_MonitorID_Select_Option(String txnRef){
        return mapPgObject.get("txnRef_MonitorID").by(txnRef);
    }

    public String getMornitorID(String txnRef){
        return element(txnRef_MonitorID_Select_Option(txnRef)).getTextContent();
    }

    private By txnRef_Status_Option(String txnRef){
        return mapPgObject.get("txnRef_Status").by(txnRef);
    }

    public String getStatus_AfterCheckingT24(String txnRef){
        return element(txnRef_Status_Option(txnRef)).getTextContent().trim();
    }

    public String click_View_Response_btn(){

        Commons.waitAction(2000);
        element(view_Response_btn).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(2000);
        String s = element(mapPgObject.get("response_message").get()).getText();
        String AckId = "";
        try{
            Document doc = xmlDriver.parseXmlString(s);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("LoanInitResponse");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                Element eElement = (Element) nNode;
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    AckId = eElement.getElementsByTagName("AcknowledgementId").item(0).getTextContent();
                    System.out.println("AckID: "+AckId);
                    break;
                }
            }
            wc.switchToTheRootFrame(getDriver());
            element(close_popup).waitUntilEnabled().click();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return AckId;

    }


    public Map<String,String> click_View_Request_Btn(){
        element(view_Request_btn).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(2000);
        String s = element(mapPgObject.get("request_message").get()).getText();
        Map<String,String> mapListReq = new HashMap<>();
        try {
            Document doc = xmlDriver.parseXmlString(s);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("LoanInitRequest");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                Element eElement = (Element) nNode;
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    String loanType = eElement.getElementsByTagName("LoanType").item(0).getTextContent();
                    System.out.println("LoanType: "+loanType);
                    if(loanType.contains("APFS") || loanType.contains("ARF")){

                        NodeList nodeList1 = eElement.getElementsByTagName("DisbursementInfo");
                        for (int j = 0; j < nodeList1.getLength(); j++) {
                            Node nNode1 = nodeList1.item(i);
                            Element eElement1 = (Element) nNode1;
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                String account = eElement1.getElementsByTagName("AccountNumber").item(0).getTextContent();
                                String customerID = eElement1.getElementsByTagName("PartyId").item(0).getTextContent();
                                System.out.println("Account: "+ account);
                                System.out.println("CusomerID: "+ customerID);
                                mapListReq.put("liqAcct",account);
                                mapListReq.put("customerID",customerID);

                            }
                        }

                    }else{
                        NodeList nodeList1 = eElement.getElementsByTagName("ObligorInfo");
                        for (int j = 0; j < nodeList1.getLength(); j++) {
                            Node nNode1 = nodeList1.item(i);
                            Element eElement1 = (Element) nNode1;
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                String account = eElement1.getElementsByTagName("AccountNumber").item(0).getTextContent();
                                String customerID = eElement1.getElementsByTagName("PartyId").item(0).getTextContent();
                                mapListReq.put("liqAcct",account);
                                mapListReq.put("customerID",customerID);
                                System.out.println("Account: "+ account);
                                System.out.println("CusomerID: "+ customerID);
                            }

                        }
                    }

                    String monitorID = eElement.getElementsByTagName("RequestReferenceId").item(0).getTextContent();
                    String loanAmount = eElement.getElementsByTagName("LoanAmount").item(0).getTextContent();
                    String loanCurrency = eElement.getElementsByTagName("LoanCurrency").item(0).getTextContent();
                    String valueDate = eElement.getElementsByTagName("LoanValueDate").item(0).getTextContent();
                    String maturityDate = eElement.getElementsByTagName("LoanMaturityDate").item(0).getTextContent();
                    String code = eElement.getElementsByTagName("CreatedBranch").item(0).getTextContent().trim();
                    String createDate = eElement.getElementsByTagName("CreatedDateTime").item(0).getTextContent().trim();
                    System.out.println("Monitor ID: "+monitorID);
                    System.out.println("Amount: "+loanAmount);
                    System.out.println("Currency: "+ loanCurrency);
                    System.out.println("Value Date: "+valueDate);
                    System.out.println("Maturiry Date: "+maturityDate);
                    System.out.println("Code: "+code);
                    mapListReq.put("monitorID",monitorID);
                    mapListReq.put("loanAmount",loanAmount);
                    mapListReq.put("loanCurrency",loanCurrency);
                    mapListReq.put("valueDate",valueDate);
                    mapListReq.put("maturityDate",maturityDate);
                    mapListReq.put("branchCode",code);
                    mapListReq.put("createDate",createDate);

                    //break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        wc.switchToTheRootFrame(getDriver());
        element(close_popup).waitUntilEnabled().click();
        return mapListReq;
    }

    public void click_Invoice_Func(){
        element(mapPgObject.get("invoice_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(mapPgObject.get("invoice_create_func").get()).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public void input_InvoiceNo(String invoiceNo){
        element(mapPgObject.get("invoiceNo_txt").get()).type(invoiceNo);
    }

    private By invoice_buyer_select(String buyer){
        return mapPgObject.get("invoice_buyer_select").by(buyer);
    }

    public void select_Invoice_Buyer(String buyer){
        System.out.println(invoice_buyer_select(buyer));
        element(mapPgObject.get("invoice_buyer_btn").get()).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(1000);
        element(invoice_buyer_select(buyer)).waitUntilEnabled().click();
        element(mapPgObject.get("invoice_select_btn").get()).click();

    }

    public void select_Invoice_Currency(String currency){
        element(mapPgObject.get("invoice_currency").get()).selectByValue(currency);
    }

    public void input_Invoice_Amount(String amount){
        element(mapPgObject.get("invoice_amount").get()).type(amount);
    }

    public void chooseInvoiceMatutiryDate(String valuedate, String currentDateFormat){
        choose_ValueDate(mapPgObject.get("invoice_original_maturityDate").get(),valuedate,currentDateFormat);
    }

    public void click_InvoiceSubmit_btn(String loadRef){
        element(mapPgObject.get("invoice_submit_btn").get()).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        element(mapPgObject.get("loadRef_txt").get()).type(loadRef);
        element(mapPgObject.get("invoice_OK_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(2000);
        element(mapPgObject.get("invoice_OK_btn").get()).waitUntilEnabled().click();
    }

    private By select_invoice_item_verify_option(String invoiceNo){
        return mapPgObject.get("invoice_item_verify").by(invoiceNo);
    }
    public void select_invoice_item_verify(String invoiceNo){
        Commons.waitAction(1000);
        element(select_invoice_item_verify_option(invoiceNo)).waitUntilEnabled().click();
    }

    public void click_Asset_func(){
        Commons.waitAction(1000);
        element(mapPgObject.get("asset_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(mapPgObject.get("asset_create_func").get()).waitUntilEnabled().click();
    }

    private By select_Asset_option(String buyer){
        return mapPgObject.get("asset_item_select").by(buyer);
    }

    public void select_Asset_Item(String buyer){
        Commons.waitAction(1000);
        element(select_Asset_option(buyer)).waitUntilEnabled().click();
    }

    private By select_Asset__Invoice_Item_Option(String invoiceNo){
        return mapPgObject.get("asset_invoice_item_select").by(invoiceNo);
    }

    public void select_Asset_Invoice_Item(String invoiceNo){
        Commons.waitAction(1000);
        element(select_Asset__Invoice_Item_Option(invoiceNo)).waitUntilEnabled().click();
    }

    public void click_CreateNewAsset_btn(String invoiceNo){
        element(mapPgObject.get("create_new_Asset_btn").get()).waitUntilEnabled().click();
        wc.switchFrame(getDriver(),popup_frame);
        element(mapPgObject.get("asset_name").get()).type(invoiceNo);

    }

    public void switch_To_PopupFrame(){
        wc.switchFrame(getDriver(),popup_frame);
        Commons.waitAction(3000);
    }









}
