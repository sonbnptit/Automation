package com.tcb.auto.utils;

import com.tcb.auto.subprocess.excel.ExcelDriver;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConvertExcelToCSV {

	public static final String MANAGE_SHEET = "MANAGE";
	public static final String COL_TEST_DATA_FILE = "TEST DATA FILE";
	public static final String COL_TEST_DATA_SHEET = "TEST DATA SHEET";
	public static final String COL_CVS_FILE = "CVS FILE";
	public static final String COL_RUN = "Run";

	/**
	 * convert a excel sheet to csv file
	 * 
	 * @param excelFile
	 * @param csvFile
	 * @param sheetName
	 * @throws Exception
	 */
	public void convertSelectedSheetInXLXSFileToCSV(String excelFile, String csvFile, String sheetName) throws Exception {
		System.out.println("Start convert Excel file \"" + excelFile + "\", Sheet [" + sheetName + "] to CSV file \"" + csvFile + "\".");

		FileInputStream in = new FileInputStream(excelFile);
		FileWriter csvWriter = new FileWriter(csvFile);


		CSVPrinter csvPrinter = new CSVPrinter(csvWriter, CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL));
		// Open the excel file and get the requested sheet from the workbook

		ExcelDriver excelDriver = new ExcelDriver();
		List<Map<String,String>> sheetData = excelDriver.getDataFromSheetName(excelFile, sheetName);

		List<String> excelHeaderRows = new LinkedList<>();
		List<String> csvHeaderRows = new LinkedList<>();
		for (int idx=0;idx<sheetData.size();idx++) {
			Map<String,String> rowData = sheetData.get(idx);
			List<String> csvRows = new LinkedList<>();
			if(idx==0){
				//get header row
				for(String headerCol: rowData.keySet()){
					excelHeaderRows.add(headerCol);
					csvHeaderRows.add(headerCol.replace("{{","").replace("}}",""));
				}
				//save header row
				csvPrinter.printRecord(csvHeaderRows);
			}
			//get row data
			//get run column data
			String runValue = rowData.get("Run");
			if(runValue.equalsIgnoreCase("x")){
				for(String keys: excelHeaderRows){
					String value = rowData.get(keys);
					csvRows.add(value);
				}
				//save row data
				csvPrinter.printRecord(csvRows);
			}
		}

		csvPrinter.close();
		csvWriter.close();
		in.close();

		System.out.println("Convert Excel to CSV successfully.");
	}

	/**
	 * convert excel file using management config file
	 *
	 * @param excelMngFile
	 */
	public void convertExcelManagementToCSV(String excelMngFile){
		String baseDir = System.getProperty("user.dir");

		ExcelDriver excelDriver = new ExcelDriver();

		List<Map<String,String>> manageData = excelDriver.getDataFromSheetName(excelMngFile, MANAGE_SHEET);
		for(Map<String, String> rowMap: manageData){
			//If check run => generate csv file
			if(!rowMap.get(COL_RUN).equalsIgnoreCase("x")) continue;	//skip
			//generate csv file
			String excelDataFile = baseDir + rowMap.get(COL_TEST_DATA_FILE);
			String excelDataSheet = rowMap.get(COL_TEST_DATA_SHEET);
			String csvOutput = baseDir + rowMap.get(COL_CVS_FILE);

			try {
				convertSelectedSheetInXLXSFileToCSV(excelDataFile, csvOutput, excelDataSheet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void convertToCSV() {
		try {
//			convertSelectedSheetInXLXSFileToCSV(System.getProperty("user.dir") + "/data/CBXFO/A2A_SelfPayment.xlsx",
//			convertSelectedSheetInXLXSFileToCSV(System.getProperty("user.dir") + "/data/CBXFO/DaoA2ASelfPayment.xlsx",
//					System.getProperty("user.dir") + "/data/CBXFO/A2A_SelfPayment.csv", "Data_A2A_SelfPayment");
//					System.getProperty("user.dir") + "/data/CBXFO/DaoA2ASelfPayment.csv", "Data_A2A_SelfPayment");
//			convertSelectedSheetInXLXSFileToCSV(System.getProperty("user.dir") + "/data/CBXFO/Payment_Data.xlsx", System.getProperty("user.dir") + "/data/CBXFO/Payment_BulkUpload.csv","Data_BulkUpload");
			convertSelectedSheetInXLXSFileToCSV("D:\\AutoT24R\\FIB\\Fib_20190725\\Fast-iBank\\data\\test\\Account\\FIB_Account_Detail.xlsx", "D:\\AutoT24R\\T24Refresh\\T24Refresh_serenity\\FIB\\fib\\data\\FIB\\Account\\Data_Account.csv","Data_AC_account");
//			convertSelectedSheetInXLXSFileToCSV("D:\\workspace\\T24Refresh\\T24Refresh_serenity\\FIB\\fib\\data\\FIB\\ChuyenKhoan\\ChuyenKhoan.xlsx", "D:\\workspace\\T24Refresh\\T24Refresh_serenity\\FIB\\fib\\data\\FIB\\ChuyenKhoan\\CK_Trong_TCB.csv","bwTCB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void convertAllExcelToCSV(){
		convertExcelManagementToCSV(System.getProperty("user.dir") + "/data/config/Data_Config.xlsx");
	}

}
