package com.tcb.scf.dataObject;

public class Payment {

    private String run;
    private String caseId;
    private String KHmaker;
    private String KHchecker;
    private String siteBank;
    private String buyAccount;
    private String supplierAccount;
    private String paymentRef;
    private String maturityDate;
    private String remitType;
    private String remitNo;
    private String amount;
    private String poNo;
    private String comment;
    private String txnRef;
    private String expectStatus;
    private String ExpectApproveStatus;



    public String getKHmaker() {
        return KHmaker;
    }

    public void setKHmaker(String KHmaker) {
        this.KHmaker = KHmaker;
    }

    public String getKHchecker() {
        return KHchecker;
    }

    public void setKHchecker(String KHchecker) {
        this.KHchecker = KHchecker;
    }

    public String getRun() {
        return run;
    }

    public String getCaseId() {
        return caseId;
    }


    public String getSiteBank() {
        return siteBank;
    }

    public void setSiteBank(String siteBank) {
        this.siteBank = siteBank;
    }


    public void setRun(String run) {
        this.run = run;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }


    public String getBuyAccount() {
        return buyAccount;
    }

    public void setBuyAccount(String buyAccount) {
        this.buyAccount = buyAccount;
    }

    public String getSupplierAccount() {
        return supplierAccount;
    }

    public void setSupplierAccount(String supplierAccount) {
        this.supplierAccount = supplierAccount;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTxnRef() {
        return txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public String getExpectStatus() {
        return expectStatus;
    }

    public void setExpectStatus(String expectStatus) {
        this.expectStatus = expectStatus;
    }

    public String getExpectApproveStatus() {
        return ExpectApproveStatus;
    }

    public void setExpectApproveStatus(String expectApproveStatus) {
        ExpectApproveStatus = expectApproveStatus;
    }

}
