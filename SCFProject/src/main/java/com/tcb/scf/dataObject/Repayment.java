package com.tcb.scf.dataObject;

public class Repayment {


    private String run;
    private String caseId;
    private String siteBank;
    private String Description;
    private String BusinessDate;
    private String EODDate;
    private String UpdateBankStatusExpect;
    private String ApproveBankStatusExpect;

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getSiteBank() {
        return siteBank;
    }

    public void setSiteBank(String siteBank) {
        this.siteBank = siteBank;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getBusinessDate() {
        return BusinessDate;
    }

    public void setBusinessDate(String businessDate) {
        BusinessDate = businessDate;
    }

    public String getEODDate() {
        return EODDate;
    }

    public void setEODDate(String EODDate) {
        this.EODDate = EODDate;
    }

    public String getUpdateBankStatusExpect() {
        return UpdateBankStatusExpect;
    }

    public void setUpdateBankStatusExpect(String updateBankStatusExpect) {
        UpdateBankStatusExpect = updateBankStatusExpect;
    }

    public String getApproveBankStatusExpect() {
        return ApproveBankStatusExpect;
    }

    public void setApproveBankStatusExpect(String approveBankStatusExpect) {
        ApproveBankStatusExpect = approveBankStatusExpect;
    }
}
