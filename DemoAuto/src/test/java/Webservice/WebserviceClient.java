package Webservice;

import com.vin3s.auto.dataobject.Constant;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import org.json.JSONObject;
import com.vin3s.auto.subprocess.webservice.SoapClient;


public class WebserviceClient {
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

    public void SendRequestAndGetResponseForeMSP(String contractNo, String fullName, String email){
        String endPointVFGateway = cc.getProperty("endpoint.battery_leasing_create");
        String messsageRequest = "{\n" +
                "    \"command\": 0,\n" +
                "    \"contractNo\": \"+contractNo+\",\n" +
                "    \"fullName\": \"CAPP UAT11\",\n" +
                "    \"email\": \"capp.uat11@gmail.com\",\n" +
                "    \"phoneNumber\": \"8367201776\",\n" +
                "    \"idNumber\": \"8367201776\",\n" +
                "    \"address\": \"Ocean Park_nhimt\",\n" +
                "    \"customerId\": \"ad86f9c2-312f-4ef3-a4b2-a826c93d560c\",\n" +
                "    \"sapCustomerId\": \"2600166001\",\n" +
                "    \"vehicleId\": \"RLLV5AFA5NV000911\",\n" +
                "    \"vehicleType\": \"Car\",\n" +
                "    \"vehicleModel\": \"VF e34\",\n" +
                "    \"batteries\": [\n" +
                "        {\n" +
                "            \"serial\": \"RLLV5AFA5NV000911\",\n" +
                "            \"stock\": \"RLLV5AFA5NV000911\",\n" +
                "            \"capacity\": \"500\",\n" +
                "            \"type\": \"0\",\n" +
                "            \"status\": \"1\",\n" +
                "            \"enabled\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"taxRegistrationNumber\": \"RLLV5AFA5NV000911\",\n" +
                "    \"taxRegistrationName\": \"RLLV5AFA5NV000911\",\n" +
                "    \"enterpriseCustomer\": false,\n" +
                "    \"status\": 1,\n" +
                "    \"contractType\": 0\n" +
                "}";

        System.out.println("Send request: " + messsageRequest);
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
