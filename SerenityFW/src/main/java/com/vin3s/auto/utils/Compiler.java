package com.vin3s.auto.utils;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vin3s.auto.subprocess.web.WebElementController;

public class Compiler {
    private Map<String, String> valueData;
    private String jscriptFile;
    private WebElementController webElementController;

    public Compiler(Map<String, String> valueData, String jscriptFile) {
        this.valueData = valueData;
        this.jscriptFile = jscriptFile;
        this.webElementController = new WebElementController();
    }
    public Compiler() {
        // TODO Auto-generated constructor stub
    }

    public String compileWithProperties(String text){
        Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
        Matcher matcher = pattern.matcher(text);
        boolean condition = matcher.find();
        while (condition) {
            String propName = matcher.group(1);
            String key = propName.replace(" ", "");
            String propValue = valueData.get(key);
            if (propValue == null) {
                propValue = "";
            }
            text = text.replace("{{" + propName + "}}", propValue);
            matcher = pattern.matcher(text);
            condition = matcher.find();
        }
        return text;
    }


}
