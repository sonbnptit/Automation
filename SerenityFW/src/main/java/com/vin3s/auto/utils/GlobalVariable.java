package com.vin3s.auto.utils;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalVariable {
	public static ConcurrentHashMap<String,String> ListData =new ConcurrentHashMap<>();
	public static Properties listConfigEnv = null;
	public static Map<String, Connection> mapConnection = new HashMap<String, Connection>();

	public static Map<String, String> mapFlowTestcaseExecute = new HashMap<>();
	public static String runFlowTestcase = null;
	public static boolean isBootstrapFinished = false;
	
	static {
	    listConfigEnv = loadEnvConfigs();
	}
	
	public static Properties loadEnvConfigs() {
	    if (listConfigEnv != null) {
	        return listConfigEnv;
	    }
	    ConfigController cc = new ConfigController();
        //listConfigEnv = LoadingConfigs.loadConfigs(System.getProperty("user.dir") + ProcessConstants.FILE_NAME_CONFIG_ENV);
		if(!Commons.isBlankOrEmpty(cc.getProperty("Common.DBConnect"))) {
			listConfigEnv = LoadingConfigs.loadConfigs(Commons.getAbsolutePath(cc.getProperty("Common.DBConnect")));
		}
		//listConfigEnv = LoadingConfigs.loadConfigs(Commons.getAbsolutePath(cc.getProperty("AppPath.app")));
		if(!Commons.isBlankOrEmpty(cc.getProperty("Common.JiraConnect"))) {
			listConfigEnv = LoadingConfigs.loadConfigs(Commons.getAbsolutePath(cc.getProperty("Common.JiraConnect")));
		}
        return listConfigEnv;
	}
}
