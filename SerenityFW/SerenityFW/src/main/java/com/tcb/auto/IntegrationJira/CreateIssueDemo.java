package com.tcb.auto.IntegrationJira;


import com.tcb.auto.subprocess.webservice.SOAPClient;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.GlobalVariable;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Base64;
import java.util.Properties;

public class CreateIssueDemo {

    private String endPointCreateIssue;
    private String projectName;
    private String usernameJira;
    private String passwordJira;
    private String endPointCreateIssueByXray;
    private String jiraURL;
    private String hostJira;
    private String folderReportpath;
    private String getListProjectURL;
    public CreateIssueDemo() {
        ConfigController cc = new ConfigController();
        projectName = cc.getProperty("IntegrationJira.projectNameJira");
        usernameJira = cc.getProperty("IntegrationJira.userLoginJira");
        passwordJira = cc.getProperty("IntegrationJira.passwordLoginJira");
        endPointCreateIssueByXray = cc.getProperty("IntegrationJira.apiJiraUrlbyXray");
        endPointCreateIssue = cc.getProperty("IntegrationJira.apiJiraIssueUrl");
        jiraURL=cc.getProperty("IntegrationJira.apiJiraUrl");
        hostJira = cc.getProperty("IntegrationJira.hostJira");
        folderReportpath = cc.getProperty("Common.folderReport");
        getListProjectURL=cc.getProperty("IntegrationJira.project");
    }

    public String getFolderReportpath(){
        return folderReportpath;
    }

    // function create Issue,Testcase,Bug using rest apo jira

    public void CreateIssue(String summary,String description,String issueType){

        String request ="{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \""+projectName+"\"\n" +
                "       },\n" +
                "       \"summary\": \""+summary+"\",\n" +
                "       \"description\": \""+description+"\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \""+issueType+"\"\n" +
                "       },\n" +
                "       \"assignee\":{\"name\":\"Tester\"},\n" +
                "       \"environment\":\"SIT\",\n" +
                "       \"labels\":[\"AUTO\"]\n" +
                "   }\n" +
                "}\n";

        try {
            SOAPClient soapClient = new SOAPClient();
            String response = soapClient.requestHTTP(endPointCreateIssue,"POST",request,"json",usernameJira,passwordJira);
            System.out.println("response: "+response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateAttachment(String url,String path){
        String userpass = usernameJira + ":" + passwordJira;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("X-Atlassian-Token", "no-check");
        httppost.setHeader("Authorization",  basicAuth);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        File fileToUpload = new File(path);
        if(fileToUpload.exists()){
            FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
            entity.addPart("file", fileBody);
            httppost.setEntity(entity);
            try{
                HttpResponse response = httpclient.execute(httppost);
                System.out.println("Response: "+response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Commons.getLogger().info("File "+fileToUpload+"is not exit!!!");
        }


    }

    // function create execution test and update Execution Test to Testcase
    public String CreateExecutionAndAddToTestcase(String key, String result, String summary, String description) {
        String id = "";
        String key1 = "";
        String url = "";
        String rs="";
        String comment ="";
        if(result.equalsIgnoreCase("ERROR") | result.equalsIgnoreCase("FAILURE")) {
            //CreateIssue("Bug");
            rs = "FAIL";
            comment = "Execution failed!!";
        }else if(result.equalsIgnoreCase("SUCCESS"))  {
            rs = "PASS";
            comment ="Successful execution";
        }

        String request = "{\n" +
                    "\t\"info\" : {\n" +
                    "\t\t\"summary\" : \""+summary +"\",\n" +
                    "\t\t\"description\" : \""+description+"\",\n" +
                    "\t\t\"user\" : \"Tester\",\n" +
                    "\t\t\"testEnvironments\": [\"PC\"]\n" +
                    "\t\t },\n" +
                    "\t\"tests\" : [{\n" +
                    "\t\t\"testKey\" : \""+key+"\",\n" +
                    "\t\t\"status\" : \""+rs+"\",\n" +
                    "\t\t\"comment\" : \""+comment+"\"\n" +
                    "\t}]\n" +
                    "}";

        String newEndPoint ="";
        try {
            SOAPClient soapClient = new SOAPClient();
            String response = soapClient.requestHTTP(endPointCreateIssueByXray, "POST", request, "json", usernameJira, passwordJira);
            System.out.println("Response: "+response);
            Commons.getLogger().debug("Response call ws: " + response);
            JSONObject json = new JSONObject(response);
            JSONObject js = json.getJSONObject("testExecIssue");
            id = js.getString("id");
            key1 = js.getString("key");
            url = js.getString("self");

            Commons.log4jAndReport("Link test execution: ",getLinkExecution(key1) );

            newEndPoint = endPointCreateIssue +key1+"/attachments";
            System.out.println("Link attachment: "+newEndPoint);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return newEndPoint;
    }

    public String getListTestcase(String key) throws Exception{

         JSONObject response;
         String id="";
         String endpoint =jiraURL+"search?jql=project=%22"+projectName+"%22and%20summary~%27"+key+"*%27%20and%20issuetype=%22Test%22";
         SOAPClient soapClient = new SOAPClient();
         response = soapClient.requestHTTP2(endpoint,"GET",usernameJira,passwordJira);
            //System.out.println(response);
        int totalResult = response.getInt("total");
        String keyIssue ="",url = "";
        if(totalResult ==0){
            Commons.log4jAndReport("","Cannot find result of key "+ key);
        }else {
            Commons.log4jAndReport("","Find "+totalResult+" result of key "+ key);
            JSONArray loudScreaming =  response.getJSONArray("issues");
            JSONObject jsonObject = new JSONObject(loudScreaming.get(0).toString());
            id = jsonObject.getString("id");
            keyIssue = jsonObject.getString("key");
            url = jsonObject.getString("self");
            //System.out.println(keyIssue);
            //System.out.println(url);

        }

        return keyIssue;

    }
    public void getListIssueProject(String project) throws Exception{

        JSONObject response;
        String id="";
        String endpoint =jiraURL+"search?jql=project=%22"+project+"%22and%20issuetype=%22Bug%22$maxIssue";
        SOAPClient soapClient = new SOAPClient();
        response = soapClient.requestHTTP2(endpoint,"GET",usernameJira,passwordJira);
        System.out.println(response);
        int totalResult = response.getInt("total");
        String keyIssue ="",url = "";
        if(totalResult ==0){
            Commons.getLogger().info("Cannot find result ");
        }else {
            Commons.getLogger().info("Find "+totalResult+" result");
            JSONArray loudScreaming =  response.getJSONArray("issues");
            for(int i = 0;i<loudScreaming.length();i++){
                JSONObject jsonObject = new JSONObject(loudScreaming.get(i).toString());
                id = jsonObject.getString("id");
                keyIssue = jsonObject.getString("key");
                url = jsonObject.getString("self");
                System.out.println(id);
                System.out.println(keyIssue);
                System.out.println(url);
            }


        }

        //return keyIssue;

    }

    public String getLinkExecution(String key){
        return hostJira+"/project"+projectName+"/issue/"+key;
    }

}
