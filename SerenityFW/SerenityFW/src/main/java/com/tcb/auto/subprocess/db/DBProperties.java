package com.tcb.auto.subprocess.db;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

/**
 * 
 * @author anhptn14
 *
 */
public class DBProperties {
	EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();

	public String getDriverClassName(String dbType) {
		return variables.getProperty(String.format("%s.DRIVER", dbType));
	}

	public String getURL(String dbType) {
		return variables.getProperty(String.format("%s.URL", dbType));
	}

	public String getUsername(String dbType) {
		return variables.getProperty(String.format("%s.USERNAME", dbType));
	}

	public String getPassword(String dbType) {
		return variables.getProperty(String.format("%s.PASSWORD", dbType));
	}

}
