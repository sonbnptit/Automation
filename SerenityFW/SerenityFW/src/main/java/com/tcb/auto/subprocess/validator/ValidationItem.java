package com.tcb.auto.subprocess.validator;

public class ValidationItem {
    private String no;
    private String description;
    private String valueValidate;
    private String expectedResult;
    private String result;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueValidate() {
        return valueValidate;
    }

    public void setValueValidate(String valueValidate) {
        this.valueValidate = valueValidate;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ValidationItem(String no, String description, String valueValidate, String expectedResult) {
        this.no = no;
        this.description = description;
        this.valueValidate = valueValidate;
        this.expectedResult = expectedResult;
    }
}
