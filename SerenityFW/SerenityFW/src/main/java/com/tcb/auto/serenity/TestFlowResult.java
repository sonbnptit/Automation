package com.tcb.auto.serenity;

import com.google.common.collect.Table;
import com.tcb.auto.utils.Commons;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestFlowResult {
    private static Map<String, Map<String, String>> flowResult = new LinkedHashMap<>();

    private static String currentFlow;

    /**
     * Start a test flow
     * @param testcaseDb
     * @param flowName
     */
    public static void startFlow(String testcaseDb, String flowName){
        //Clear ol flow test if exists
        if(flowResult.containsKey(flowName) && !Commons.isBlankOrEmpty(flowResult.get(flowName))){
            flowResult.get(flowName).clear();
        }
        //Add all test case in flow from db
        try {
            TestcaseDAO dao = new TestcaseDAO(testcaseDb);
            Map<String, String> mapTestcase = dao.loadTestcaseInFlow(flowName);
            flowResult.put(flowName, mapTestcase);
        }catch (Exception e){
            flowResult.put(flowName, null);
        }

        currentFlow = flowName;
    }

    public static void saveFlowResult(String testcaseDb, String releaseId, String flowName){
        if(!flowResult.containsKey(flowName) || Commons.isBlankOrEmpty(flowResult.get(flowName))) return;
        Map<String, String> mapTestcase = flowResult.get(flowName);

        TestcaseDAO dao = new TestcaseDAO(testcaseDb);
        mapTestcase.forEach((caseId, status) -> {
            dao.saveTestcaseResult(flowName, caseId, releaseId, status, "");
        });

    }
}
