package Webservice;

import com.vin3s.auto.dataobject.Constant;
import com.vin3s.auto.serenity.CommonScenarioSteps;
import com.vin3s.auto.utils.CaseInsensitiveMapObjWrapper;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.dataobject.BaterryLeasingCreateObject;
import com.vin3s.auto.utils.ConfigController;
import org.json.JSONObject;
import com.vin3s.auto.subprocess.webservice.SoapClient;


public class WebserviceClient extends CommonScenarioSteps {
    ConfigController cc = new ConfigController();


    public String getAccessToken(){
        String message = Constant.STRING_GET_TOKEN;
        String url =cc.getProperty("getToken.urlGetToken");
        SoapClient soapClient = new SoapClient();
        try {
            JSONObject response = soapClient.AccessTokenforeMSP(url,message, "POST","other");
            //System.out.println("Response token: "+response.toString());
            return response.getString("access_token");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void sendAPIBaterryLeasingCreate(CaseInsensitiveMapObjWrapper<BaterryLeasingCreateObject> data){
        String endPointVFGateway = cc.getProperty("endpoint.battery_leasing_create");
        String messsageRequest = "{\n" +
                "    \"command\": 0,\n" +
                "    \"contractNo\": \""+data.dt().getContractNo()+"\",\n" +
                "    \"fullName\": \""+data.dt().getFullName()+"\",\n" +
                "    \"email\": \""+data.dt().getEmail()+"\",\n" +
                "    \"phoneNumber\": \""+data.dt().getPhoneNumber()+"\",\n" +
                "    \"idNumber\": \""+data.dt().getIdNumber()+"\",\n" +
                "    \"address\": \""+data.dt().getAddress()+"\",\n" +
                "    \"customerId\": \""+data.dt().getCustomerId()+"\",\n" +
                "    \"sapCustomerId\": \""+data.dt().getSapCustomerId()+"\",\n" +
                "    \"vehicleId\": \""+data.dt().getVehicleId()+"\",\n" +
                "    \"vehicleType\": \""+data.dt().getVehicleType()+"\",\n" +
                "    \"vehicleModel\": \""+data.dt().getVehicleModel()+"\",\n" +
                "    \"batteries\": [\n" +
                "        {\n" +
                "            \"serial\": \""+data.dt().getSerialbaterry()+"\",\n" +
                "            \"stock\": \""+data.dt().getStockbaterry()+"\",\n" +
                "            \"capacity\": \""+data.dt().getCapacitybaterry()+"\",\n" +
                "            \"type\": \""+data.dt().getTypebaterry()+"\",\n" +
                "            \"status\": \""+data.dt().getStatusbaterry()+"\",\n" +
                "            \"enabled\": "+data.dt().getEnabledbaterry()+"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"taxRegistrationNumber\": \""+data.dt().getTaxRegistrationNumber()+"\",\n" +
                "    \"taxRegistrationName\": \""+data.dt().getTaxRegistrationName()+"\",\n" +
                "    \"enterpriseCustomer\": "+data.dt().getEnterpriseCustomer()+",\n" +
                "    \"status\": "+data.dt().getStatus()+",\n" +
                "    \"contractType\": "+data.dt().getContractType()+"\n" +
                "}";

        System.out.println("Send request: \n" + messsageRequest);
        String token = getAccessToken();
        SoapClient soapClient = new SoapClient();
        try {
            JSONObject response = soapClient.requestHTTPS(endPointVFGateway,"POST", messsageRequest, "json", token );
            System.out.println(response.toString());
            String message = response.getString("message");
            System.out.println("Message: "+ message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
