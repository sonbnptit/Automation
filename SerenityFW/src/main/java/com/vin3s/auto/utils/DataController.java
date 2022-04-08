package com.vin3s.auto.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.reflect.FieldUtils;

public class DataController {

    private static final String DATA_VARIABLE = "{{";
    private static final String DATATION = "func";

    public String getValue(String dataReadFromFramework) {
        if (dataReadFromFramework.contains(DATATION) || dataReadFromFramework.contains(DATA_VARIABLE))
            return null;
        return dataReadFromFramework;
    }

    /**
     * get data from a object class (used to get data after Serenity render data
     * from csv file )
     *
     * @author sonbn
     * @param obj: Object of test class. Note. field of class should be public to
     *        access from outsite
     * @return Case Insensitive map <Key: filed name, Value: value of data >
     */
    public CaseInsensitiveMap<String, String> getDataFromObjectClass(Object obj) {
        CaseInsensitiveMap<String, String> data = new CaseInsensitiveMap<String, String>();
        return (CaseInsensitiveMap) getDataFromObjectClass(obj, CaseInsensitiveMap.class);
    }

    public Map<String, String> getDataFromObjectClass(Object obj, Class mapType) {
        Map<String, String> data;
        if (mapType.equals(LinkedCaseInsensitiveMap.class)) {
            data = new LinkedCaseInsensitiveMap<>();
        } else {
            data = new CaseInsensitiveMap<>();
        }

        if (!obj.getClass().getName().toLowerCase().contains("tcb"))
            return data;

        Field[] fields = FieldUtils.getAllFields(obj.getClass());
        for (Field field : fields) {
            /**
             * @author sonbn Chỉ load du lieu khi type la kieu primitive va String: int,
             *         long, double, String
             */
            if (!(field.getType().getName().contains("int") || field.getType().getName().contains("String")
                    || field.getType().getName().contains("double") || field.getType().getName().contains("long"))) {
                continue; // skip class != int, String, long, double
            }
            if (Modifier.isStatic(field.getModifiers()))
                continue; // skip static
            try {
                Object objValue = FieldUtils.readField(field, obj, true);
                String fieldValue = Commons.isBlankOrEmpty(objValue) ? ""
                        : String.valueOf(FieldUtils.readField(field, obj, true));
                data.put(field.getName(), fieldValue);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * Compile data CaseInsensitiveMap
     *
     * @param data
     * @param jsFile
     * @return
     */
    public CaseInsensitiveMap<String, String> compileData(CaseInsensitiveMap<String, String> data, String jsFile) {
        Compiler compiler = new Compiler(data, jsFile);
        CaseInsensitiveMap<String, String> outData = new CaseInsensitiveMap<String, String>();
        for (String key : data.keySet()) {
            outData.put(key, compiler.compileWithProperties(data.get(key)));
        }
        return outData;

    }


}

