package com.tcb.auto.subprocess.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.Constants;

/**
 * 
 * @author yennth28
 *
 */
public class SOAPClient {

	/**
	 * send request message to server via HTTP
	 * 
	 * @param linkWS
	 * @param requestMessage
	 * @param typeMessage
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String requestHTTP(String linkWS,String requestMethod, String requestMessage, String typeMessage, String username,
			String password, Map... conProp) throws Exception {
		// Check if Https webservice
		if (linkWS.trim().toLowerCase().startsWith("https")) {
			return requestHTTPS(linkWS, requestMessage, typeMessage, username, password);
		}

		// create URL object
		URL url = new URL(linkWS);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(requestMethod);
		con.setDoOutput(true);
		con.setDoInput(true);
		if (typeMessage.contains("xml"))
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		else
			con.setRequestProperty("Content-Type", "application/json");
		int len = requestMessage.length();
		con.setRequestProperty("Content-Length", Integer.toString(len));
		con.setRequestProperty("Connection", "Keep-Alive");

		
		if (conProp.length > 0) {
			conProp[0].keySet().forEach((key) -> {
				con.setRequestProperty(key.toString(), conProp[0].get(key).toString());
			});

		}
		
		if (!Commons.isBlankOrEmpty(username) && !Commons.isBlankOrEmpty(password)) {
			// String userpass = userPassword[0] + ":" + userPassword[1];
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
			con.setRequestProperty("Authorization", basicAuth);
		}

		con.connect();
		// write XML to the server
		OutputStreamWriter outStr = new OutputStreamWriter(con.getOutputStream());
		outStr.write(requestMessage, 0, len);
		outStr.flush();

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
		// return parserRespone(responseTr);
		return responseTr;
	}

	/**
	 * send request message to server via HTTPS
	 * 
	 * @param linkWS
	 * @param requestMessage
	 * @param typeMessage
	 * @param username
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public String requestHTTPS(String linkWS, String requestMessage, String typeMessage, String username,
			String password, String... RequestMethod) throws Exception {
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
		URL url = new URL(linkWS);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		String requestMethod = "";
		if (RequestMethod.length == 0) {
			requestMethod = "POST";
		} else {
			requestMethod = RequestMethod[0];
		}
		con.setRequestMethod(requestMethod);
		con.setDoInput(true);
		con.setDoOutput(true);

		int len = requestMessage.length();
		if (typeMessage.contains("xml")) {
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

		} else {
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
		}
		con.setRequestProperty("Content-Length", Integer.toString(len));
		con.setRequestProperty("Connection", "Keep-Alive");
		if (!username.trim().isEmpty() && !username.equals(Constants.NULL_TEXT_INDICATOR)
				&& !password.trim().isEmpty()) {
			// String userpass = userPassword[0] + ":" + userPassword[1];
			String userpass = username + ":" + password;
			String basicAuth = "Basic "
					+ new String(Base64.getEncoder().encode(userpass.getBytes(StandardCharsets.UTF_8)));
			con.setRequestProperty("Authorization", basicAuth);
		}

		con.connect();

		// write XML to the server
		OutputStreamWriter outStr = new OutputStreamWriter(con.getOutputStream());
		outStr.write(requestMessage, 0, len);
		outStr.flush();

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

	/**
	 * Get String include node and value of node in respone message
	 * 
	 * @param respone
	 * @return
	 */
	public String parserRespone(String respone) {
		String result = "";
		Map<String, String> listNode = new LinkedHashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource is;
		try {
			builder = factory.newDocumentBuilder();
			is = new InputSource(new StringReader(respone));
			Document doc = builder.parse(is);
			NodeList list = doc.getElementsByTagName("*");
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				String nodeName = element.getNodeName();
				String nodeValue = element.getTextContent();
				listNode.put(nodeName, nodeValue);
				result += nodeName + ":\"" + nodeValue + "\",";
				System.out.println("Name: " + nodeName + "Value: " + nodeValue);
			}
		} catch (Exception e) {
			result = respone;
		}

		System.out.println("value: " + result);
		return result;
	}



	public String requestHTTP1(String GET_URL, String requestMethod, String username, String password) throws Exception  {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(requestMethod);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (!Commons.isBlankOrEmpty(username) && !Commons.isBlankOrEmpty(password)) {
			// String userpass = userPassword[0] + ":" + userPassword[1];
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
			con.setRequestProperty("Authorization", basicAuth);
		}
		con.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			int ch = in.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = in.read();
			}
			String responseTr = sb.toString();
			//JSONObject json = new JSONObject(responseTr);
			return responseTr;
			// print result
	}
	public JSONObject requestHTTP2(String GET_URL, String requestMethod, String username, String password) throws Exception  {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(requestMethod);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (!Commons.isBlankOrEmpty(username) && !Commons.isBlankOrEmpty(password)) {
			// String userpass = userPassword[0] + ":" + userPassword[1];
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
			con.setRequestProperty("Authorization", basicAuth);
		}
		con.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		int ch = in.read();
		while (ch != -1) {
			sb.append((char) ch);
			ch = in.read();
		}
		String responseTr = sb.toString();
		JSONObject json = new JSONObject(responseTr);
		return json;
		// print result
	}
}
