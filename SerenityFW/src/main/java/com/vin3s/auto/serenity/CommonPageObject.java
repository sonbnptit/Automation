package com.vin3s.auto.serenity;

import com.vin3s.auto.subprocess.web.WebElementController;
import com.vin3s.auto.utils.LazyBy;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import javax.annotation.Resource;
import java.util.Map;

public class CommonPageObject extends PageObject {

    protected CaseInsensitiveMap<String, String> runData = null;
    protected Map<String, LazyBy> mapPgObject;

    public Map<String, LazyBy> getMapPgObject() {
        return mapPgObject;
    }

    public CommonPageObject() {
        super();
        if(this.getClass().getAnnotations() == null || this.getClass().getAnnotations().length == 0) return;
        //init with null data
        setRunData(null);
    }

    public void setRunData(CaseInsensitiveMap<String, String> runData) {
        this.runData = runData;
        Resource annotation = this.getClass().getAnnotation(Resource.class);
        if (annotation != null) {
            String propertiesFile = annotation.name();
            mapPgObject = WebElementController.getLazyMapByFromProperties(runData, propertiesFile);
        }
    }

}
