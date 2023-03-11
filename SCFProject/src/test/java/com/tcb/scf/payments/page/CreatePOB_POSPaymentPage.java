package com.tcb.scf.payments.page;

import com.tcb.auto.serenity.CommonPageObject;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import javax.annotation.Resource;

@Resource(name = "scf.payments")
public class CreatePOB_POSPaymentPage extends CommonPageObject {
    ConfigController cc = new ConfigController();
    WebElementController wc = new WebElementController();

    By select_language = mapPgObject.get("select_language").get();

    private void switchToPopupFrame(){
        By popupframe = mapPgObject.get("popup_frame").get();
        wc.switchFrame(getDriver(),popupframe);
        Commons.waitAction(1000);
    }

    public void click_PO_Manager_Func(){

        element(mapPgObject.get("po_manager_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(1000);
    }

    public void click_PO_Create_Func(){
        element(mapPgObject.get("po_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(mapPgObject.get("po_create_function").get()).waitUntilEnabled().click();
        Commons.waitAction(2000);
    }

    public void select_language(String language){
        Commons.waitAction(500);
        element(select_language).waitUntilEnabled().selectByValue(language);
        Commons.waitAction(1000);
    }

    public void click_HeadInforTab(){
        element(mapPgObject.get("head_infor_btn").get()).waitUntilEnabled().click();
    }

    public void click_LineInforTab(){
        element(mapPgObject.get("line_infor_btn").get()).waitUntilEnabled().click();
    }

    public void input_PoNo(String poNo){
        element(mapPgObject.get("po_no_txt").get()).type(poNo);
    }

    private By select_Supplier_ID_Option(String supplier){
        return mapPgObject.get("suppiler_id_option").by(supplier);
    }

    public void select_SupplierID(String supplier){
        element(mapPgObject.get("suppiler_id_btn").get()).waitUntilEnabled().click();
        switchToPopupFrame();
        element(select_Supplier_ID_Option(supplier)).waitUntilEnabled().click();
        Commons.waitAction(500);
        element(mapPgObject.get("po_select_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(1000);
    }

    public void select_PoType(String poType){
        element(mapPgObject.get("po_type").get()).selectByValue(poType);
    }

    public void select_Currency(String currency){
        element(mapPgObject.get("po_currency").get()).selectByValue(currency);
    }

    public void input_Ship_To_Address(String address){
        element(mapPgObject.get("po_ship_to_address_1").get()).type(address);
    }

    public void input_Ship_To_City(String city){
        element(mapPgObject.get("po_ship_to_city").get()).type(city);
    }

    public void select_Ship_To_Country(String country){
        element(mapPgObject.get("po_ship_to_country").get()).selectByValue(country);
    }

    public void input_Lastest_Ship_Date(String date){
        try{
            wc.sendkeyJS(getDriver(),date,mapPgObject.get("po_lastest_ship_date").get(),"");
        }catch (Exception e){
            Commons.getLogger().info(e.getMessage());
        }

        //element(mapPgObject.get("po_lastest_ship_date").get()).type(date);
    }

    public void input_Bill_To_Address(String address){
        element(mapPgObject.get("po_bill_to_address_1").get()).type(address);
    }

    public void input_Bill_To_City(String city){
        element(mapPgObject.get("po_bill_to_city").get()).type(city);
    }

    public void select_Bill_To_Country(String country){
        element(mapPgObject.get("po_bill_to_country").get()).selectByValue(country);
    }

    public void input_PO_Line_No(String lineNo){
        element(mapPgObject.get("po_line_no").get()).type(lineNo);
    }

    public void input_Part_No(String partNo){
        element(mapPgObject.get("part_no").get()).type(partNo);
    }

    public void input_UOM(String uom){
        element(mapPgObject.get("uom").get()).type(uom);
    }

    public void input_Part_Description(String description){
        element(mapPgObject.get("part_description").get()).type(description);
    }

    public void input_Quantity(String quantity){
        element(mapPgObject.get("quantity").get()).type(quantity);
    }

    public void input_Unit_Price(String price){
        try{
            element(mapPgObject.get("unit_price").get()).type(price);
            wc.sendFunctionKey(getDriver(), Keys.ENTER);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void click_Add_btn(){
        element(mapPgObject.get("po_add_btn").get()).waitUntilEnabled().click();
    }

    public void click_Submit_btn(){
        element(mapPgObject.get("po_submit_btn").get()).waitUntilEnabled().click();
    }

    public void click_OK_btn(){
        element(mapPgObject.get("po_OK_btn").get()).waitUntilEnabled().click();
    }

    public void click_Realse_btn(){
        element(mapPgObject.get("po_Release_btn").get()).waitUntilEnabled().click();
    }

    public void click_PO_Asset_func(){
        element(mapPgObject.get("po_asset_btn").get()).waitUntilEnabled().click();
    }

    public void click_PO_Asset_Create_func(){
        element(mapPgObject.get("po_asset_create_function").get()).waitUntilEnabled().click();
    }

    private By po_no_option(String poNo){
        return mapPgObject.get("po_no_option").by(poNo);
    }

    public void select_PoNo(String poNo){
        element(mapPgObject.get("po_search_btn").get()).waitUntilEnabled().click();
        switchToPopupFrame();
        element(mapPgObject.get("search_po_no").get()).type(poNo);
        element(mapPgObject.get("po_search_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(1000);
        element(po_no_option(poNo)).waitUntilEnabled().click();
    }

    public void click_Finance_btn(){
        element(mapPgObject.get("po_finance_btn").get()).waitUntilEnabled().click();
    }

    public void input_Finance_Reference(String reference){
        switchToPopupFrame();
        element(mapPgObject.get("finance_reference").get()).type(reference);
        Commons.waitAction(1000);
    }

    public void click_Save_Btn(){
        element(mapPgObject.get("po_save_btn").get()).waitUntilEnabled().click();
    }

    public void click_Next_Btn(){
        element(mapPgObject.get("po_next_btn").get()).waitUntilEnabled().click();
        switchToPopupFrame();
        Commons.waitAction(1000);
        element(mapPgObject.get("po_OK_btn").get()).waitUntilEnabled().click();
        Commons.waitAction(1000);
    }


}
