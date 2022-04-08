package com.vin3s.auto.subprocess.webservice;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.vin3s.auto.utils.Constants;
import org.json.JSONObject;


public class SoapClient {

    public HttpsURLConnection addSSLforHTTPS(String URL) throws Exception{
        // create URL object

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        URL url = new URL(URL);
        return (HttpsURLConnection) url.openConnection();
    }

    // Add infor for connect
    public HttpsURLConnection AddInforMessageConnect(String linkWS,String requestMethod,String typeMessage,String requestMessage,String authorize) throws Exception{
        HttpsURLConnection con = addSSLforHTTPS(linkWS);
        con.setRequestMethod(requestMethod);
        //con.setDoInput(true);
        con.setDoOutput(true);
        if (typeMessage.contains("xml")) {
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        } else if (typeMessage.contains("json")) {
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "*/*");
        }
        else{
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            con.setRequestProperty("User-Agent", "Apache-HttpClient/4.5.12 (Java/1.8.0_261)");
        }
        int len = requestMessage.length();
        con.setRequestProperty("Content-Length", Integer.toString(len));
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Authorization", authorize);
        con.connect();
        // write XML to the server
        OutputStreamWriter outStr = new OutputStreamWriter(con.getOutputStream());
        outStr.write(requestMessage, 0, len);
        outStr.flush();
        return con;
    }

    public JSONObject AccessTokenforeMSP(String URL,String requestMessage,String requestMethod,String typeMessage) throws Exception {
        HttpsURLConnection con = AddInforMessageConnect(URL,requestMethod,typeMessage,requestMessage,"");
        InputStreamReader read;
        try {
            read = new InputStreamReader(con.getInputStream());
        } catch (Exception exception) {
            read = new InputStreamReader(con.getErrorStream());
        }
        // read server response
        StringBuilder sb = new StringBuilder();
        int ch = read.read();
        while (ch != -1) {
            sb.append((char) ch);
            ch = read.read();
        }
        String responseTr = sb.toString();
        System.out.println(responseTr);
        return new JSONObject(responseTr);
    }

    /**
     * send request message to server via HTTPS
     *
     * @param linkWS
     * @param requestMessage
     * @param typeMessage
     *
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject requestHTTPS(String linkWS, String requestMethod,String requestMessage, String typeMessage, String token  ) throws Exception {
        HttpsURLConnection con = AddInforMessageConnect(linkWS,requestMethod,typeMessage,requestMessage,token);
        InputStreamReader read;
        try {
            read = new InputStreamReader(con.getInputStream());
        } catch (Exception exception) {
            read = new InputStreamReader(con.getErrorStream());
        }
        // read server response
        StringBuilder sb = new StringBuilder();
        int ch = read.read();
        while (ch != -1) {
            sb.append((char) ch);
            ch = read.read();
        }
        String responseTr = sb.toString();
        return new JSONObject(responseTr);
    }

    /**
     * send request message to server via HTTPS
     *
     * @param linkWS
     * @param requestMessage
     * @param typeMessage
     *
     * @return String
     * @throws Exception
     */

    public String requestHTTPS(String linkWS, String requestMessage,String requestMethod, String typeMessage, String username, String password) throws Exception {
        String userpass = username + ":" + password;
        String basicAuth="";
        if (!username.trim().isEmpty() && !username.equals(Constants.NULL_TEXT_INDICATOR) && !password.trim().isEmpty()) {
            basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes(StandardCharsets.UTF_8)));
        }
        HttpsURLConnection con = AddInforMessageConnect(linkWS,requestMethod,typeMessage,requestMessage,basicAuth);
        InputStreamReader read;
        try {
            read = new InputStreamReader(con.getInputStream());
        } catch (Exception exception) {
            read = new InputStreamReader(con.getErrorStream());
        }
        // read server response
        StringBuilder sb = new StringBuilder();
        int ch = read.read();
        while (ch != -1) {
            sb.append((char) ch);
            ch = read.read();
        }
        String responseTr = sb.toString();
        System.out.println(responseTr);
        return responseTr;
    }
}
