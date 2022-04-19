package com.tcb.auto.utils;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class ConfigController {
	public static final String LOCATOR_DIRECTORY_PATH = System.getProperty("user.dir") + "/src/test/resources/";
	public static EnvironmentVariables environmentVariables = null;

	public ConfigController(){
		if(environmentVariables == null){
			environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		}
	}

	/**
	 * get properties from serenity.conf file
	 * 
	 * @author anhptn14
	 * @param prop:
	 *            property name
	 * @return: the value of the properties
	 */
	public String getSpecificProperty(String prop) {
		//EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		EnvironmentSpecificConfiguration specificConfiguration = EnvironmentSpecificConfiguration
				.from(environmentVariables);

		return specificConfiguration.getProperty(prop);
	}

	/**
	 * get properties from Serenity.properties
	 * 
	 * @author anhptn14
	 * @param prop
	 * @return
	 */
	public String getProperty(String prop) {
		//EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String value = environmentVariables.getProperty(prop);
		if(value == null){
			//try get in environment
			String key = "environments." + environmentVariables.getProperty("environment") + "." + prop;
			value = environmentVariables.getProperty(key);
			if(value == null){
				//try get in all environment
				key = "environments.all." + prop;
				value = environmentVariables.getProperty(key);
				if(value == null){
					//try get from Global Env
					Properties properties = GlobalVariable.listConfigEnv;
					if(properties == null) return null;
					value = properties.getProperty(prop);
				}
			}
		}
		return value;
	}

	public void clearProperty(String prop){
		//EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String value = environmentVariables.getProperty(prop);
		if(!Commons.isBlankOrEmpty(value)){
			environmentVariables.setProperty(prop, "");
		}else{
			//try get in environment
			String key = "environments." + environmentVariables.getProperty("environment") + "." + prop;
			value = environmentVariables.getProperty(key);
			if(!Commons.isBlankOrEmpty(value)) {
				environmentVariables.setProperty(key, "");
			}else{
				//try get in all environment
				key = "environments.all." + prop;
				value = environmentVariables.getProperty(key);
				if(!Commons.isBlankOrEmpty(value)) {
					environmentVariables.setProperty(key, "");
				}
			}
		}
	}

	/**
	 * Reads a property from a .properties file by the input <b>key</b>. null
	 * will be returned if there is no such property in .properties file.
	 * 
	 * @param propertiesFile
	 *            the file path of the .properties file
	 * @param key
	 *            the input key to search for value
	 * 
	 * @return the value of this property as string null is returned in case no
	 *         property is found.
	 * @throws IOException
	 */
	public String getValueFromKey(String propertiesFile, String key) throws IOException {
		// Properties prop = new Properties();
		// String value = null;
		// Reader reader = new InputStreamReader(new
		// FileInputStream(propertiesFile), "UTF-8");
		// prop.load(reader);
		// value = prop.getProperty(key);
		// reader.close();
		propertiesFile = Commons.getAbsolutePath(propertiesFile);
		Properties prop = getAllPropertiesFromPropertiesFile(propertiesFile);

		return prop.getProperty(key);
	}

	/**
	 * Get value from properties file if know the key from the properties file
	 * and the key indicate the properties file
	 * @author anhptn14
	 * @param keyIndicatePropertiesInConfFile:
	 *            ex: IPSH.AuthorizationBulkUpload
	 * @param keyInPropertiesFile:
	 *            ex: authorize_btn
	 * @return
	 */

	public String getValueFromPropertiesFileViaConfFile(String keyIndicatePropertiesInConfFile,
			String keyInPropertiesFile) {
		String value = null;
		try {
			value = getValueFromKey(getSpecificProperty(keyIndicatePropertiesInConfFile), keyInPropertiesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	public Properties getAllPropertiesFromPropertiesFile(String propertiesFile) throws IOException {
		Properties prop = new Properties();
		Reader reader = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8");
		prop.load(reader);
		reader.close();
		return prop;
	}

	public void setEnvironmentVariables(String prop, String value, String section){
		//EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String key;
		if(Commons.isBlankOrEmpty(section)){
			key = "environments." + environmentVariables.getProperty("environment") + "." + prop;
		}else{
			key = "environments." + section + "." + prop;
		}
		environmentVariables.setProperty(key, value);
	}

	public void setEnvironmentVariables(String prop, String value){
		setEnvironmentVariables(prop, value, "");
	}

	/*@Test
	public void testReadProp() {
		String path = LOCATOR_DIRECTORY_PATH + "IPSH.InitiationTransaction_en.properties";
		try {
			System.out.println(getValueFromKey(path, "OK_btn"));
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		*//*
		 * WebElementController wc = new WebElementController();
		 * System.out.println(wc.getByFromProperties("IPSH.General", "OK_btn"));
		 *//*
		// TODO Auto-generated catch block
	}*/
}
