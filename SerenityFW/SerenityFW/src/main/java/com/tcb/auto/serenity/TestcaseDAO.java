package com.tcb.auto.serenity;

import com.tcb.auto.subprocess.db.DbConnection;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.Constants;
import org.junit.Test;

import java.util.*;

public class TestcaseDAO {
    public static final String SQL_SELECT_FLOW_TESTCASE = "SELECT * FROM at_FLOW WHERE FLOWID = '%s'";
    public static final String SQL_INSERT_TEST_RESULT = "INSERT INTO at_RUNRESULT(TESTCASEID, FLOWID, RELEASEID, STATUS, NOTE) VALUES ('%s','%s','%s','%s', '%s')";
    public static final String SQL_SEARCH_TEST_RESULT = "SELECT ID FROM at_RUNRESULT WHERE TESTCASEID = '%s' AND FLOWID = '%s' AND RELEASEID = '%s'";
    public static final String SQL_UPDATE_TEST_RESULT = "UPDATE at_RUNRESULT SET RUNTIME = now(), STATUS = '%s', NOTE = '%s' WHERE ID = '%s'";
    private String testcaseDb;
    DbConnection connection;

    public TestcaseDAO(String testcaseDb){
        this.testcaseDb = testcaseDb;
        this.connection = new DbConnection();
    }

    public Map<String, String> loadTestcaseInFlow(String flowName) {
        List<Map<String, String>> flowList = null;
        try {
            flowList = connection.query(testcaseDb, String.format(SQL_SELECT_FLOW_TESTCASE, flowName));
        } catch (Exception e) {
            return null;
        }
        if(Commons.isBlankOrEmpty(flowList)) return null;
        Map<String, String> flowRow = flowList.get(0);
        //get all case Id
        Map<String, String> mapCaseId = new HashMap<>();

        //Add all case in flow to map
        if(!Commons.isBlankOrEmpty(flowRow.get(Constants.DB_LISTCASEID))){
            List<String> caseIdList = Arrays.asList(flowRow.get(Constants.DB_LISTCASEID).split(";"));
            caseIdList.forEach(s -> mapCaseId.put(s, Constants.DB_CASE_UNEXECUTE));
        }

        //check has parent flow
        if(!Commons.isBlankOrEmpty(flowRow.get(Constants.DB_BASEFLOWID))){
            String baseFlow = flowRow.get(Constants.DB_BASEFLOWID);
            Map<String, String> parentCaseId = loadTestcaseInFlow(baseFlow);
            if(!Commons.isBlankOrEmpty(parentCaseId)) {
                mapCaseId.putAll(parentCaseId);
            }
        }
        return mapCaseId;
    }

    public boolean saveTestcaseResult(String flowName, String testcaseId, String releaseId, String status, String noteData) {
        if(Constants.DB_CASE_UNEXECUTE.equals(status)) return false;    //skip unexecute status
        try {
            //search has testcaseId in test results
            String id = connection.queryFirstRecord(testcaseDb, String.format(SQL_SEARCH_TEST_RESULT, testcaseId, flowName, releaseId), "ID");
            if(!Commons.isBlankOrEmpty(id)){
                //update test result
                connection.update(testcaseDb, String.format(SQL_UPDATE_TEST_RESULT, status, noteData, id));
            }else{
                //insert new test result
                connection.insert(testcaseDb, String.format(SQL_INSERT_TEST_RESULT, testcaseId, flowName, releaseId, status, noteData));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
