package com.vin3s.auto.subprocess.excel;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ExcelDriver {

	private Workbook getWorkbook(String filename) throws IOException {
		FileInputStream in = new FileInputStream(new File(filename));
		Workbook workbook = null;
		switch (FilenameUtils.getExtension(filename)) {
			case "xlsx":
				workbook = new XSSFWorkbook(in);
				break;
			case "xls":
				workbook = new HSSFWorkbook(in);
			default:
				break;
		}
		return workbook;
	}


	public List<Map<String, String>> getDataFromSheetName(String fileName, String sheetName,boolean ... isHeaderCaseInsensitive) {
		List<Map<String, String>> keyMaps = null;
		boolean headerCaseInsensitve = true;
		if(isHeaderCaseInsensitive.length!=0) {
			headerCaseInsensitve = isHeaderCaseInsensitive[0];
		}
		try {
			Path p = Paths.get(fileName);
			String filePath = fileName;// ? fileName :
			// HeaderConstants.FOLDERPATH_TEST +
			// fileName;
			keyMaps = new ArrayList<Map<String, String>>();
			FileInputStream in = new FileInputStream(new File(filePath));
			Workbook workbook = getWorkbook(fileName);
			if(workbook == null) return null;
			if(headerCaseInsensitve)
				keyMaps = readDataSheetByName(workbook, sheetName, "", LinkedMap.class);
			else
				keyMaps = readDataSheetByName(workbook, sheetName, "", LinkedHashMap.class);
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyMaps;
	}

	private List<Map<String, String>> readDataSheetByName(Workbook workbook, String sheetName, String asNull, Class mapType) {
		List<Map<String, String>> keyMaps = new ArrayList<Map<String, String>>();

		Sheet datasheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = datasheet.rowIterator();

		// list of table headers
		List<String> keys = new ArrayList<>();

		// list of column indices of table headers
		List<Integer> keyCols = new ArrayList<>();
		while (rows.hasNext()) {
			Map<String, String> keyMap;
			if(CaseInsensitiveMap.class == mapType)
				keyMap = new CaseInsensitiveMap<>();
			else if(LinkedMap.class == mapType)
				keyMap = new LinkedMap();
			else keyMap = new LinkedHashMap<>();

			Row row = (Row) rows.next();
			// For the first row (row number = 0), cell values will be used as
			// table headers
			if (row.getRowNum() == 0) {
				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = (Cell) cells.next();
					String cellValue;
					if (cell.getCellTypeEnum() == CellType.NUMERIC)
						cellValue = String.valueOf((long) cell.getNumericCellValue());
					else if (cell.getCellTypeEnum() == CellType.BLANK)
						cellValue = asNull;
					else
						cellValue = cell.getStringCellValue().trim();

					keys.add(cellValue);
					keyCols.add(cell.getColumnIndex());
				}
			} else {
				// and for other rows (row number > 0), cell values are saved as
				// table values
				for (int i = 0; i < keyCols.size(); i++) {
					Cell cell = row.getCell(keyCols.get(i), MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String cellValue;
					if (cell.getCellTypeEnum() == CellType.NUMERIC){
						double numValue = cell.getNumericCellValue();
						if(Math.floor(numValue) == numValue){
							cellValue = String.valueOf((long)numValue);
						}else{
							cellValue = String.valueOf(numValue);
						}
					}
					else if (cell.getCellTypeEnum() == CellType.BLANK)
						cellValue = asNull;
					else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
						cellValue = String.valueOf(cell.getBooleanCellValue());
					else
						cellValue = cell.getStringCellValue().trim();

					// check if the excel sheet is in right format. Only cell
					// with header is accepted and saved as table value.
					if (cell.getColumnIndex() < keys.size() && keys.get(cell.getColumnIndex()) != null)
						keyMap.put(keys.get(cell.getColumnIndex()), cellValue);
				}
				keyMaps.add(keyMap);
			}
		}

		return keyMaps;
	}

	public List<Map<String, String>> getCustomMapDataFromSheetName(String fileName, String sheetName, Class mapType) {
		List<Map<String, String>> keyMaps = null;
		try {
			Path p = Paths.get(fileName);
			String filePath = fileName;// ? fileName :
			// HeaderConstants.FOLDERPATH_TEST +
			// fileName;
			Workbook workbook = getWorkbook(fileName);
			if(workbook == null) return null;
			keyMaps = readDataSheetByName(workbook, sheetName, "", mapType);

			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyMaps;
	}


	public String getDataFromColunm(String login_dataFile, String sheetName, String name_keyColumn,
			String value_keyColumn, String name_valueColumn) {
		ExcelDriver excelDriver = new ExcelDriver();
		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		listData = excelDriver.getDataFromSheetName(login_dataFile, sheetName);
		for (Map<String, String> data : listData) {
			if (data.get(name_keyColumn).equals(value_keyColumn)) {
				return (String) data.get(name_valueColumn);
			}
		}
		return null;
	}
}