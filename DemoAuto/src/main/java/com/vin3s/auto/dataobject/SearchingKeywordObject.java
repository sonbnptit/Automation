package com.vin3s.auto.dataobject;

public class SearchingKeywordObject {

    private String run;
    private String FlowID;
    private String CaseID;
    private String role;
    private String Keyword;
    private String ExpectResult;

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getFlowID() {
        return FlowID;
    }

    public void setFlowID(String flowID) {
        FlowID = flowID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public String getExpectResult() {
        return ExpectResult;
    }

    public void setExpectResult(String expectResult) {
        ExpectResult = expectResult;
    }

    public String getCaseID() {
        return CaseID;
    }

    public void setCaseID(String caseID) {
        CaseID = caseID;
    }
}
