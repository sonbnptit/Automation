package com.tcb.auto.serenity.driver;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class CustomChromeDriver implements DriverSource {
    protected final static String sourceDes = System.getProperty("user.dir");
    protected static ConfigController cc = new ConfigController();

    protected ChromeOptions chromeOptions = null;
    protected DesiredCapabilities capability = null;

    public CustomChromeDriver(){

    }

//    public CustomChromeDriver(ChromeOptions customChromeOptions){
//        chromeOptions = customChromeOptions;
//    }

    @Override
    public WebDriver newDriver() {
        System.setProperty("webdriver.chrome.driver", Commons.getAbsolutePath(sourceDes + File.separator + cc.getProperty("WebConfig.pathDriver")));
        if(chromeOptions == null){
            chromeOptions = initDefaultChromeOptions();
        }

        capability = DesiredCapabilities.chrome();
        capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        org.openqa.selenium.WebDriver driver = new ChromeDriver(chromeOptions);
        return driver;
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }

    public static ChromeOptions initDefaultChromeOptions(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-file-access-from-files");
        options.addArguments("--disable-web-security");
        return options;
    }
    public static WebDriver iEDriver() {
        System.setProperty("webdriver.ie.driver", sourceDes + cc.getProperty("Env.IE.Path"));
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
        caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);

        WebDriver driver = new InternetExplorerDriver(caps);
        driver.manage().window().maximize();
        return driver;
    }
    public static WebDriver mobileWeb(){
        Map<String, String> mobileEmulation = new LinkedHashMap<>();
        String deviceName = cc.getProperty("Mobile.Web.deviceName");
        mobileEmulation.put("deviceName", deviceName); //Galaxy S5
        //mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3329.0 Mobile Safari/537.36");
        System.setProperty(cc.getProperty("WebConfig.chromeDriver"), sourceDes + cc.getProperty("WebConfig.pathDriver"));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        chromeOptions.addArguments("--allow-file-access-from-files");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--disable-notifications");
        //turn off message chrome is being controlled by automate test software
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        //disable save password notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        chromeOptions.setExperimentalOption("prefs", prefs);
        DesiredCapabilities capability;
        capability = DesiredCapabilities.chrome();
        capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        org.openqa.selenium.WebDriver driver = new ChromeDriver(chromeOptions);
        return driver;
    }
}
