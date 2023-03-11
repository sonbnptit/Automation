package com.vin3s.auto.dataobject;

import com.tcb..subprocess.excel.ExcelDriver;

public class Login {
    public static final String COLUMN_ROLE = "Role";
    public static final String COLUMN_COMPANY_NAME = "CompanyName";
    public static final String COLUMN_BROWSER = "Browser";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String SHEETNAME = "Login";
    ExcelDriver excelDriver = new ExcelDriver();

    public String getData(String login_dataFile, String sheetName, String role, String datacolumn) {
        return excelDriver.getDataFromColunm(login_dataFile, sheetName, COLUMN_ROLE, role, datacolumn);

    }

}
