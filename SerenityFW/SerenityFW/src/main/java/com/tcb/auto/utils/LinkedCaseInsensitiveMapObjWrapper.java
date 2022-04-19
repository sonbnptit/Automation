package com.tcb.auto.utils;

import java.lang.reflect.Method;
import java.util.Map;

public class LinkedCaseInsensitiveMapObjWrapper<K> extends LinkedCaseInsensitiveMap<String, String> {
    private K dtObject;

    public LinkedCaseInsensitiveMapObjWrapper() {
        super();
        this.dtObject = null;
    }

    public LinkedCaseInsensitiveMapObjWrapper(K dtObject) {
        super();
        setDtObject(dtObject);
    }

    public LinkedCaseInsensitiveMapObjWrapper(K dtObject, int initialCapacity) {
        super(initialCapacity);
        setDtObject(dtObject);
    }

    public LinkedCaseInsensitiveMapObjWrapper(K dtObject, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        setDtObject(dtObject);
    }

    public LinkedCaseInsensitiveMapObjWrapper(K dtObject, Map<? extends String, ? extends String> map) {
        super(map);
        setDtObject(dtObject);
    }

    public K getDtObject() {
        return dtObject;
    }

    public K dt() {
        return dtObject;
    }

    private void setDtObject(K dtObject) {
        DataController dtc = new DataController();
        Map<String, String> mapDt = dtc.getDataFromObjectClass(dtObject, LinkedCaseInsensitiveMap.class);
        mapDt.forEach((key, value) -> {
            super.put(key, value);
        });
        this.dtObject = dtObject;
    }

    private String getDtObjectProperty(String propName){
        if(dtObject == null) return "";
        String getPropFunc = "get" + propName;
        Method[] methods = dtObject.getClass().getMethods();
        for(Method method: methods){
            if(method.getName().toLowerCase().equalsIgnoreCase(getPropFunc)
                    && method.getParameterCount() == 0 && method.getReturnType().equals(String.class)){
                try {
                    Object objValue = method.invoke(dtObject);
                    return (String) objValue;
                } catch (Exception e){
                    return "";
                }
            }
        }
        return "";
    }

    private void setDtObjectProperty(String propName, String value){
        if(dtObject == null) return;
        String setPropFunc = "set" + propName;
        Method[] methods = dtObject.getClass().getMethods();
        for(Method method: methods){
            if(method.getName().toLowerCase().equalsIgnoreCase(setPropFunc)
                    && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(String.class)){
                try {
                    method.invoke(dtObject, value);
                }finally {
                    return;
                }
            }
        }
    }

    public String get(String key) {
        //check if K object has key property?
        String propValue = getDtObjectProperty(key);
        return Commons.isBlankOrEmpty(propValue) ? super.get(key) : propValue;
    }

    @Override
    public String put(String key, String value) {
        //update object both
        setDtObjectProperty(key, value);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        super.putAll(map);
        if(map == null) return;
        map.forEach((key, value) -> {
            setDtObjectProperty(key, value);
        });
    }
}
