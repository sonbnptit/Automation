package com.vin3s.auto.utils;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Lớp wrapper của CaseInsensitiveMap<String, String>, chứa đối tượng ObjectData
 * @param <K>
 */
public class CaseInsensitiveMapObjWrapper<K> extends CaseInsensitiveMap<String, String> {
    private K dtObject;

    /**
     * Khởi tạo với object data NULL
     */
    public CaseInsensitiveMapObjWrapper() {
        super();
        this.dtObject = null;
    }

    /**
     * Khỏi tạo và thêm tất cả thuộc tính object data vào map
     * @param dtObject
     */
    public CaseInsensitiveMapObjWrapper(K dtObject) {
        super();
        setDtObject(dtObject);
    }

    /**
     * Lấy đối tượng object data
     * @return
     */
    public K getDtObject() {
        return dtObject;
    }

    public K dt() {
        return dtObject;
    }

    /**
     * Gán và thêm tất cả thuộc tính object data vào map
     * @param dtObject
     */
    private void setDtObject(K dtObject) {
        setDtObject(dtObject, true);
    }

    private void setDtObject(K dtObject, boolean nullable) {
        DataController dtc = new DataController();
        CaseInsensitiveMap<String, String> mapDt = dtc.getDataFromObjectClass(dtObject);
        mapDt.forEach((key, value) -> {
            if(nullable || !Commons.isBlankOrEmpty(value)) {
                super.put(key, value);
            }
        });
        this.dtObject = dtObject;
    }

    /**
     * Tìm hàm get trong Object data và thực hiện gọi để lấy ra thuộc tính
     * @param propName
     * @return
     */
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

    /**
     * Tìm hàm set trong Object data và thực hiện gọi để gán thuộc tính
     * @param propName
     * @param value
     */
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

