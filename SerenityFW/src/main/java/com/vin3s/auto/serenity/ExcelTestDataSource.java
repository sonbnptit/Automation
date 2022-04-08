package com.vin3s.auto.serenity;

import com.vin3s.auto.subprocess.excel.ExcelDriver;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.LinkedCaseInsensitiveMap;
import net.thucydides.core.csv.FailedToInitializeTestData;
import net.thucydides.core.csv.FieldName;
import net.thucydides.core.csv.InstanceBuilder;
import net.thucydides.core.steps.StepFactory;
import net.thucydides.core.steps.stepdata.TestDataSource;

import java.util.*;

public class ExcelTestDataSource extends AbsCustomTestDataSource {
    private String excelFile;
    private String sheet;
    private boolean runAllTest;

    public ExcelTestDataSource(String excelFile, String sheet, boolean runAllTest) {
        this.excelFile = excelFile;
        this.sheet = sheet;
        this.runAllTest = runAllTest;
    }

    List<Map<String, String>> excelData;

    @Override
    public List<String> getHeaders() {
        if(Commons.isBlankOrEmpty(excelData)) return new LinkedList<>();
        Set<String> excelHeader = excelData.get(0).keySet();
        //copy header
        LinkedList<String> testHeader = new LinkedList<>();
        excelHeader.forEach(s -> {
            testHeader.add(Commons.removeSpecialChar(s));
        });
        return testHeader;
    }

    @Override
    public List<Map<String, String>> getData() {
        ExcelDriver excelDriver = new ExcelDriver();
        excelData = excelDriver.getCustomMapDataFromSheetName(excelFile, sheet, LinkedCaseInsensitiveMap.class);

        List<Map<String, String>> testDataList = new ArrayList<>();

        //copy excel
        Iterator<Map<String, String>> row = excelData.iterator();
        while (row.hasNext()) {
            Map<String, String> mapRow = row.next();
            if(Commons.isBlankOrEmpty(mapRow)) continue;
            if(runAllTest || (mapRow.containsKey("Run") && mapRow.get("Run").equalsIgnoreCase("x"))){
                //copy row
                LinkedCaseInsensitiveMap copyRow = new LinkedCaseInsensitiveMap();
                mapRow.forEach((key, value) -> {
                    copyRow.put(Commons.removeSpecialChar(key), value);
                });
                testDataList.add(copyRow);
            }
        }

        return testDataList;

        /*if(!runAllTest){
            Iterator<Map<String, String>> row = excelData.iterator();
            while (row.hasNext()) {
                Map<String, String> mapRow = row.next();
                if(!mapRow.get("Run").equalsIgnoreCase("x")){
                    //don't run => need remove it
                    row.remove();
                }
            }
        }

        return excelData;*/
    }

    @Override
    public TestDataSource separatedBy(char c) {
        return null;
    }

}