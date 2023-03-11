package com.vin3s.auto.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadingConfigs {

    public static Properties loadConfigs(String fileName) {
        
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(fileName);
            if (input == null) {
                return null;
            }

            prop.load(input);
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        
        
        return prop;
    }
}
