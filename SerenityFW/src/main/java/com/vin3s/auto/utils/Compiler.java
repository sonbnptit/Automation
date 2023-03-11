package com.vin3s.auto.utils;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vin3s.auto.subprocess.jscript.JScriptInvoker;
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

    public String compileWithPropertiesParams(String text, String... params){
        Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
        Matcher matcher = pattern.matcher(text);
        boolean condition = matcher.find();
        int index = 0;
        while (condition) {
            String propName = matcher.group(1);
            String key = propName.replace(" ", "");
            String propValue = params[index];
            if (propValue == null) {
                propValue = "";
            }
            text = text.replace("{{" + propName + "}}", propValue);
            matcher = pattern.matcher(text);
            condition = matcher.find();
            index += 1;
        }
        /**
         * If value contain func in javascript, need to execute func before
         * return value
         */

        if (text.contains("func")) {
            text = compileWithJscript(text);
        }

        return text;
    }

    public String convertParamToJS(String text, String jscriptFile) {
        /*
         * if(text.contains("func")){ text=compileWithJscript(text,jscriptFile);
         * }else
         */ if (text.contains("{{")) {
            Pattern pattern = Pattern.compile(Constants.REGEX_PROP_INJECTION);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String propName = matcher.group(1);
                String propValue = valueData.get( propName.replaceAll(" ",""));
                if (propValue.contains(Constants.SEP_1)) {
                    propValue = "[" + propValue + "]";
                }
                text = text.replace("{{"+propName+"}}", propValue.replace(",", Constants.SYMB_COMMA)
                        .replace("(", Constants.SYMB_LEFT_PARENTHESIS).replace(")", Constants.SYMB_RIGHT_PARENTHESIS));
                /* TH xml */
                text = text.replaceAll("&amp;", "&");
                text = text.replaceAll("&gt;", ">");
                text = text.replaceAll("&lt;", "<");
            }
        }
        return text;
    }



    public String compileWithJscript(String text) {
        JScriptInvoker invoker = new JScriptInvoker(jscriptFile, valueData);
        text = convertParamToJS(text, jscriptFile);
        // if(text.length() <= Constants.MAX_LOG_LENGTH){
        System.out.println("JScriptInvoker: " + text);
        // }
        return invoker.compile(text, Constants.REGEX_JS_INJECTION, Constants.REGEX_JS_CONTAIN,
                Constants.REGEX_JS_PARAMS_SEP, Constants.NULL_TEXT_INDICATOR);
    }


}
