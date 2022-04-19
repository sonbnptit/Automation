package com.tcb.auto.serenity.driver;

import com.tcb.auto.utils.Commons;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class MultiChromeDriver extends CustomChromeDriver {
    Map<String, WebDriver> mapDriver = new CaseInsensitiveMap<>();
    WebDriver currentDriver;

    private static MultiChromeDriver instance = null;
    public MultiChromeDriver(){
        instance = this;
    }

    public static MultiChromeDriver getInstance(){
        return instance;
    }

    public static boolean isActive(){
        return instance != null;
    }

    public WebDriver getCurrentDriver(){
        return currentDriver;
    }

    public WebDriver switchDriver(String sessionName){
        if(!mapDriver.containsKey(sessionName)){ return null; }

        currentDriver = mapDriver.get(sessionName);
        return currentDriver;
    }

    public WebDriver switchOrInitDriver(String sessionName){
        if(mapDriver.containsKey(sessionName)){
            currentDriver = mapDriver.get(sessionName);
        }else {
            currentDriver = super.newDriver();
            mapDriver.put(sessionName, currentDriver);
        }
        return currentDriver;
    }

    public boolean removeDriver(String sessionName){
        if(!mapDriver.containsKey(sessionName)){ return false; }
        WebDriver sessionDriver = mapDriver.get(sessionName);
        if(!Commons.isBlankOrEmpty(sessionDriver)){
            sessionDriver.quit();
        }
        mapDriver.remove(sessionName);
        return true;
    }

    @Override
    public WebDriver newDriver() {
        currentDriver = super.newDriver();
        mapDriver.put("default", currentDriver);
        return currentDriver;
    }
}
