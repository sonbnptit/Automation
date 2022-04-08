package com.tcb.auto.subprocess.db;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.GlobalVariable;
import net.serenitybdd.core.Serenity;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectDB {

    private static final int MAX_RETRIES = 3;

    public static Connection getConnection(String connKey) throws Exception {
        Connection connection = GlobalVariable.mapConnection.get(connKey);
        if (connection == null || connection.isClosed()) {
            Properties properties = GlobalVariable.listConfigEnv;
            if (properties == null) {
                properties = GlobalVariable.loadEnvConfigs();
            }
            String driver = properties.getProperty(String.format("%s.DRIVER", connKey));
            String url = properties.getProperty(String.format("%s.URL", connKey));
            String userName = properties.getProperty(String.format("%s.USERNAME", connKey));
            String pass = properties.getProperty(String.format("%s.PASSWORD", connKey));
            connection = createConnectDB(driver, url, userName, pass);

            //switch schema
            String schema = properties.getProperty(String.format("%s.SCHEMA", connKey));
            if(!Commons.isBlankOrEmpty(schema)){
                String schemaSwitchSql = "ALTER SESSION SET CURRENT_SCHEMA = %s";
                PreparedStatement statement = connection.prepareStatement(String.format(schemaSwitchSql, schema));
                statement.execute();
            }
            GlobalVariable.mapConnection.put(connKey, connection); 
        }
        return connection;
    }

    private static Connection createConnectDB(String driver, String url, String userName, String password)
            throws Exception {
        Connection connection = null;
        String errMsg;
        try {
            Class.forName(driver);

            int retry = -1;
            while (retry <= MAX_RETRIES) {
                try {
                    retry++;
                    if (retry > 0) {
                        Thread.sleep(1000);
                    }
                    connection = DriverManager.getConnection(url, userName, password);
                    break;
                } catch (Exception ex) {
                    if (retry > MAX_RETRIES)
                        throw ex;
                    Serenity.recordReportData().withTitle("Create Connect to DB")
                            .andContents("Connect to DB failed => Retry: " + retry);
                    System.err.println("Connect to DB failed => Retry: " + retry);
                }
            }
            //errMsg = "You made it, take control your database now!";
            //System.out.println(errMsg);
        } catch (ClassNotFoundException e) {
            errMsg = "Where is your JDBC Driver?";
            System.out.println(errMsg);
        }

        // Serenity.recordReportData().withTitle("Create Connect to
        // DB").andContents(errMsg);
        return connection;
    }

    public static void closeAllConnection(){
        try {
            for(Connection connection : GlobalVariable.mapConnection.values()){
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
