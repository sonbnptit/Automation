package com.vin3s.auto.utils;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

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

	public String getProperty(String prop) {
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
					Properties properties = new Properties();
					if(properties == null) return null;
					value = properties.getProperty(prop);
				}
			}
		}
		return value;
	}


	public String getSpecificProperty(String prop) {
		//EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		EnvironmentSpecificConfiguration specificConfiguration = EnvironmentSpecificConfiguration
				.from(environmentVariables);

		return specificConfiguration.getProperty(prop);
	}

	public Properties getAllPropertiesFromPropertiesFile(String propertiesFile) throws IOException {
		Properties prop = new Properties();
		Reader reader = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8");
		prop.load(reader);
		reader.close();
		return prop;
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

}
