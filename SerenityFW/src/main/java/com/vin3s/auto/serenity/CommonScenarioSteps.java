package com.vin3s.auto.serenity;

import net.thucydides.core.steps.ScenarioSteps;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.WebDriver;
import java.lang.reflect.Field;
import java.util.List;

public class CommonScenarioSteps extends ScenarioSteps {
    protected CaseInsensitiveMap<String, String> runData;

    public void setPageData(CaseInsensitiveMap<String, String> runData) {
        this.runData = runData;
        List<Field> fieldList = FieldUtils.getAllFieldsList(this.getClass());
        int count = 0;
        for (Field field : fieldList) {
            count++;
            //if (Commons.isSubClassOf(field.getType(), CommonPageObject.class)) {
            if (CommonPageObject.class.isAssignableFrom(field.getType())) {
                try {
                    CommonPageObject pageObject = (CommonPageObject) FieldUtils.readField(field, this, true);
                    pageObject.setRunData(runData);
                }catch (IllegalAccessException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    public void setPageDriver(WebDriver webDriver){
        this.pages().setDriver(webDriver);
        List<Field> fieldList = FieldUtils.getAllFieldsList(this.getClass());
        int count = 0;
        for (Field field : fieldList) {
            count++;
            //if (Commons.isSubClassOf(field.getType(), CommonPageObject.class)) {
            if (CommonPageObject.class.isAssignableFrom(field.getType())) {
                try {
                    CommonPageObject pageObject = (CommonPageObject) FieldUtils.readField(field, this, true);
                    pageObject.setDriver(webDriver);
                }catch (IllegalAccessException ex){
                    ex.printStackTrace();
                }

            }
        }
    }
}

