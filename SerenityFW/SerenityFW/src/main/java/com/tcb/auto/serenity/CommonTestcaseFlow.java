package com.tcb.auto.serenity;

import com.tcb.auto.subprocess.db.ConnectDB;
import com.tcb.auto.subprocess.db.DbConnection;
import com.tcb.auto.subprocess.web.WebElementController;
import com.tcb.auto.utils.*;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CommonTestcaseFlow {
    public static final String PRODUCT_RELEASE = "Release";
    public static final String TESTCASEDB_ENABLE = "testcasedb.enable";
    protected String flowId;
    protected String testcaseDb;
    protected String release;
    protected boolean testcaseDbEnable = false;

    private static Map<String, String> tcExecuteMap;
    private TestcaseDAO dao;

    public CommonTestcaseFlow(){
        //get default value from config
        testcaseDb = "TESTCASE.DB";
        //get release
        ConfigController cc = new ConfigController();

        testcaseDbEnable = Commons.isBlankOrEmpty(cc.getProperty(TESTCASEDB_ENABLE)) ? false : Boolean.parseBoolean(cc.getProperty(TESTCASEDB_ENABLE));
        release = cc.getProperty(PRODUCT_RELEASE);

        dao = new TestcaseDAO(testcaseDb);
    }

    public void startNewTestFlow() {
        if(Commons.isBlankOrEmpty(flowId) || !testcaseDbEnable) return;
        tcExecuteMap = null;
        tcExecuteMap = dao.loadTestcaseInFlow(flowId);
        //save to global
        GlobalVariable.runFlowTestcase = flowId;
        GlobalVariable.mapFlowTestcaseExecute = tcExecuteMap;
    }

    private void initTestcaseExecuteList(){
        if(!testcaseDbEnable) return;
        if(Commons.isBlankOrEmpty(flowId)){
            flowId = GlobalVariable.runFlowTestcase;
        }
        if(Commons.isBlankOrEmpty(tcExecuteMap)){
            if(!Commons.isBlankOrEmpty(GlobalVariable.mapFlowTestcaseExecute)){
                //load tcExecuteMap from global
                tcExecuteMap = GlobalVariable.mapFlowTestcaseExecute;
            }else{
                //load from db
                if(!Commons.isBlankOrEmpty(flowId)){
                    tcExecuteMap = dao.loadTestcaseInFlow(flowId);
                }
            }
        }
    }

    /**
     *
     * @param testcaseId
     * @param result    : DB_CASE_PASS, DB_CASE_FAILED, DB_CASE_UNEXECUTE
     * @return
     */
    public boolean updateTestcaseResult(String testcaseId, String result){
        if(!testcaseDbEnable) return false;
        initTestcaseExecuteList();
        if(Commons.isBlankOrEmpty(tcExecuteMap) || !tcExecuteMap.containsKey(testcaseId)) return false;
        tcExecuteMap.put(testcaseId, result);
        return true;
    }

    public String getFlowId() {
        if(Commons.isBlankOrEmpty(flowId)){
            flowId = GlobalVariable.runFlowTestcase;
        }
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public CommonTestcaseFlow withFlowId(String flowId) {
        this.flowId = flowId;
        return this;
    }

    /**
     * update execute status for remain testcase in flow
     * @param result
     */
    public void updateRemainTestcaseResult(String result){
        if(!testcaseDbEnable) return;
        for (String tcId: tcExecuteMap.keySet()){
            if(tcExecuteMap.get(tcId).equals(Constants.DB_CASE_UNEXECUTE)){
                tcExecuteMap.put(tcId, result);
            }
        }
    }

    public void updateListTestcaseResult(List<String> testcaseIds, String result){
        if(!testcaseDbEnable) return;
        for (String id: testcaseIds) {
            updateTestcaseResult(id, result);
        }
    }

    public void updateAnnotationTestcaseResult(String result){
        if(!testcaseDbEnable) return;
        //get current method
        //get mine simple name
        String thisName = this.getClass().getName();
        System.out.println(thisName);
        //get stack trace
        //StackTraceElement[] traceArr = new Throwable().getStackTrace();
        StackTraceElement[] traceArr = Thread.currentThread().getStackTrace();

        //find class @Steps and method @Step
        int maxDep = Math.min(15, traceArr.length);
        for(int idx=0;idx<maxDep;idx++){
            StackTraceElement trace = traceArr[idx];
            System.out.println(trace.getFileName() + " : " +  trace.getClassName());
            if(thisName.startsWith(trace.getClassName())){
                //found @Steps class
                //get caller method
                String methodName = trace.getMethodName();
                Method stepMethod = Commons.findFirstMethodByName(this.getClass(), methodName);
                if(stepMethod == null) return;
                //check method has and @Testcase
                if(!Commons.checkMethodHasAnnotation(stepMethod, Testcase.class)
                    && !Commons.checkMethodHasAnnotation(stepMethod, Testcase.List.class)) return;   //has not @Testcase
                //get @Testcase
                Testcase[] arrTcAnno = stepMethod.getAnnotationsByType(Testcase.class);
                //update math flow
                for(Testcase tcAnno : arrTcAnno){
                    if(Commons.isBlankOrEmpty(tcAnno.flow())
                        || getFlowId().toLowerCase().equals(tcAnno.flow().toLowerCase())
                        || getFlowId().matches(tcAnno.flow())){
                        List<String> testCaseList = Arrays.asList(tcAnno.listId());
                        //update testcase result
                        updateListTestcaseResult(testCaseList, result);
                    }
                }

            }
        }
    }

    public void passAnnotationTestcase(){
        if(!testcaseDbEnable) return;
        updateAnnotationTestcaseResult(Constants.DB_CASE_PASS);
    }

    public void saveResultDB(){
        if(!testcaseDbEnable) return;
        if(Commons.isBlankOrEmpty(tcExecuteMap)) return;
        tcExecuteMap.forEach((tcId, result) -> {
            dao.saveTestcaseResult(flowId, tcId, release, result, "");
        });
        tcExecuteMap.clear();
    }

    public void updateSingleCaseResult(String caseId, String result, String noteData){
        dao.saveTestcaseResult(flowId, caseId, release, result, noteData);
    }
}
