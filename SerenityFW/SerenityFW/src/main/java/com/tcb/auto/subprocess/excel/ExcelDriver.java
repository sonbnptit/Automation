package com.tcb.auto.subprocess.excel;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.Compiler;
import com.tcb.auto.utils.Constants;
import com.tcb.auto.utils.LinkedCaseInsensitiveMap;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * This class provides methods to read from and write into an excel file.
 * 
 * @author autoteam
 * @version 1.0.170418
 * @since JDK1.7
 *
 */
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

	/**
	 * Loads data from a sheet in an excel file into <b>Data Table</b>
	 * structure. For each BLANK or NULL cell, the NULL_TEXT_INDICATOR will be
	 * returned.
	 * <p>
	 * Note: the method getCellTypeEnum() provided by <b>org.apache.poi</b> is
	 * deprecated and will be removed in future versions.
	 * 
	 * @param filename
	 *            full path to the excel file to load data from
	 * @param sheetName
	 *            the name of the sheet to load data from
	 * @param asNull
	 *            NULL_TEXT_INDICATOR to represent BLANK and NULL cell
	 * @return data from the excel file in the form of <b>Data Table</b>
	 *         structure
	 * @throws IOException
	 */
	public List<Map<String, String>> getDataFromSheetName(String filename, String sheetName, String asNull)
			throws IOException {
		asNull = asNull != null ? asNull : "";
		List<Map<String, String>> keyMaps = new ArrayList<Map<String, String>>();
		Workbook workbook = getWorkbook(filename);
		if(workbook == null) return null;
		keyMaps = readDataSheetByName(workbook, sheetName, asNull, CaseInsensitiveMap.class);

		workbook.close();
		return keyMaps;
	}
	
	/**
	 *
	 * @param filename
	 * @param sheetName
	 * @param colList
	 * @param offsetRow
	 * @param offsetCol
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> extractDataFromSheetName(String filename, String sheetName, List<String> colList, int offsetRow, int offsetCol) throws IOException {
		return extractDataFromSheetName(filename, sheetName, colList, offsetRow, offsetCol, Constants.NULL_TEXT_INDICATOR, "", "", 0, 0);
	}

	/**
	 *
	 * @param filename
	 * @param sheetName
	 * @param colList
	 * @param offsetRow
	 * @param offsetCol
	 * @param asNull
	 * @param datetimeFormat
	 * @param numberFormat
	 * @param readRow
	 * @param skipRow
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> extractDataFromSheetName(String filename, String sheetName, List<String> colList, int offsetRow, int offsetCol, String asNull, String datetimeFormat, String numberFormat, int readRow, int skipRow) throws IOException {
		asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
		List<Map<String, String>> keyMaps = new ArrayList<Map<String, String>>();
		FileInputStream in = new FileInputStream(new File(filename));
		Workbook workbook = getWorkbook(filename);
		if(workbook == null) return null;
		keyMaps = extractDataSheetByName(workbook, sheetName, colList, offsetRow, offsetCol, asNull, datetimeFormat, numberFormat, readRow, skipRow);

		workbook.close();
		return keyMaps;
	}

	/**
	 * get data from a sheet of excel file is under the folder test so don't
	 * need to put HeaderConstants.FOLDERPATH_TEST as input parameter
	 */
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
				keyMaps = readDataSheetByName(workbook, sheetName, "", LinkedCaseInsensitiveMap.class);
			else
				keyMaps = readDataSheetByName(workbook, sheetName, "", LinkedHashMap.class);
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyMaps;
	}
	
	/**
	 * read data of a sheet in excel file
	 * 
	 * @param filename
	 * @param sheetName
	 * @param validateName
	 * @return
	 */
	
	//#region function map data in excel with column name
	//guildline
	/* 1. Config there cases invalid in excel file
	 * 2. The first row defines the names of the columns Ex. field name, type, max length, min length
	 * 3. The returned data is a json string containing the key value
	 * 4. To retrieve data, do the following: data returned get("column name") Ex. max length:  data.get(max_length) 
	 */
	public Map<String, Map<String, Map<String, String>>> getDataFromSheetMapWithColumnName(String filename, String sheetName, String validateName) throws IOException {
		Map<String, Map<String, Map<String, String>>> keyMaps = new HashMap<String, Map<String,Map<String,String>>>();
		FileInputStream in = new FileInputStream(new File(filename));
		Workbook workbook = getWorkbook(filename);
		if(workbook == null) return null;
		keyMaps = readDataMapColumnName(workbook, sheetName, validateName);
		workbook.close();
		return keyMaps;
	}
	
	public Map<String, Map<String, Map<String, String>>> readDataMapColumnName(Workbook workbook, String sheetName, String validateName) {
		List<String> dataRows = new ArrayList<>();
		List<String> colName = new ArrayList<String>();
		Map<String, Map<String, String>> fieldsMapColNames = new HashMap<String, Map<String,String>>();
		Map<String, String> fields = new HashMap<String, String>();
		Map<String, Map<String, Map<String, String>>> mapData = new HashMap<String, Map<String,Map<String,String>>>();
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
		while(rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				Iterator<Cell> cells = row.cellIterator();
				while(cells.hasNext()) {
					Cell cell = cells.next();
					colName.add(cell.getStringCellValue().trim().replaceAll(" ", "_").toLowerCase());
				}
				continue;
			}
			
			String valueColumn = "";
			for(int j = 0; j < colName.size(); j++) {
				if(j == 0) {
					valueColumn = row.getCell(j).getStringCellValue().trim().replaceAll(" ",  "_").toLowerCase();
				}else {
					if(row.getCell(j) == null || row.getCell(j).getCellType() == CellType.BLANK) {
						valueColumn = "null";
					}else if(row.getCell(j).getCellType() == CellType.STRING) {
						valueColumn = row.getCell(j).getStringCellValue();
					}else if(row.getCell(j).getCellType() == CellType.NUMERIC) {
						valueColumn = Integer.toString((int)row.getCell(j).getNumericCellValue());
					}else if(row.getCell(j).getCellType() == CellType.BOOLEAN) {
						valueColumn = row.getCell(j).getStringCellValue();
					}
				}
				dataRows.add(valueColumn);
			}
			fields = zipToMap(colName, dataRows);
			
			String fieldKey = "";
			TreeMap<String, String>  limitFields = new TreeMap<String,String>();
			for(String key: fields.keySet()) {
				if(key.equals("fields")) {
					fieldKey = fields.get(key);
				}else {
					limitFields.put(key, fields.get(key));
				}
			}
			fieldsMapColNames.put(fieldKey, limitFields);
			dataRows.removeAll(dataRows);
		}
		validateName = validateName.trim().contains(" ") ? validateName.replaceAll(" ",  "_").toLowerCase(): validateName.toLowerCase();
		mapData.put(validateName, fieldsMapColNames);

		return mapData;
	}
	
	public Map<String, String> zipToMap(List<String> keys, List<String> values) {
	    return IntStream.range(0, keys.size()).boxed().collect(Collectors.toMap(keys::get, values::get));
	}
	//#endregion function map data in excel with column name
	
	@SuppressWarnings("deprecation")
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
			else if(LinkedCaseInsensitiveMap.class == mapType)
				keyMap = new LinkedCaseInsensitiveMap<>();
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

	/**
	 *
	 * @param workbook
	 * @param sheetName
	 * @param colList
	 * @param offsetRow: Start header row index
	 * @param offsetCol: Start header column index
	 * @param asNull
	 * @param datetimeFormat
	 * @param numberFormat
	 * @param readRow: Amount data row read
	 * @param skipRow: Start data row index
	 * @return
	 * @throws IOException
	 */
	private List<Map<String, String>> extractDataSheetByName(Workbook workbook, String sheetName, List<String> colList, int offsetRow, int offsetCol, String asNull, String datetimeFormat, String numberFormat, int readRow, int skipRow) throws IOException {

		//readAllCol = true: Read data of all column
		//readAllCol = false: Read only data of column in colList
		boolean readAllCol = false;
		datetimeFormat = !Commons.isBlankOrEmpty(datetimeFormat) ? datetimeFormat : Constants.EXCEL_DATE_TIME_FORMAT;
		//colMapIndex: map column name with column index
		Map<String, Integer> colMapIndex = new CaseInsensitiveMap<String, Integer>();
		//Check colList is null => Read data of all column
		if(Commons.isBlankOrEmpty(colList)) readAllCol = true;
		if(offsetRow<0) offsetRow = 0;
		if(offsetCol<0) offsetCol = 0;
		skipRow = Math.max(skipRow, offsetRow);

		List<Map<String, String>> keyMaps = new ArrayList<Map<String, String>>();

		Sheet datasheet = workbook.getSheet(sheetName);

		DateFormat dtFormat = new SimpleDateFormat(datetimeFormat);

		//get header row
		Row row = datasheet.getRow(offsetRow);
		int rowCount = datasheet.getLastRowNum() + 1;   //0-base index
		int readEndIndex = rowCount;
		if(readRow > 0){
			readEndIndex = Math.min(rowCount,  skipRow + readRow + 1);
		}
		int cellCount = row.getLastCellNum();   // 1-base index
		for(int cIdx = offsetCol; cIdx < cellCount; cIdx++){
			//Check and skip col < offsetCol
			Cell cell = row.getCell(cIdx, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String cellValue = cell.getStringCellValue().trim();
			if(cellValue.isEmpty()) continue;
			//keyCols.add(cell.getColumnIndex());
			if(readAllCol){
				//add col and index to map
				colMapIndex.put(cellValue, cell.getColumnIndex());
			}else{
				if(colList.contains(cellValue)){
					//add col and index to map
					colMapIndex.put(cellValue, cell.getColumnIndex());
				}else{
					//check cellValue match col list
					for(String colValue: colList){
						if(cellValue.matches(colValue)){
							colMapIndex.put(cellValue, cell.getColumnIndex());
							break;
						}
					}
				}
			}
		}

		if(!readAllCol && colList.size() > colMapIndex.size()){
			//sheet doesn't contain col list => return
			throw new IOException("Sheet doesn't contain col list");
		}

		//read row
		for(int rIdx = skipRow + 1; rIdx < readEndIndex; rIdx++){
			row = datasheet.getRow(rIdx);
			Map<String, String> keyMap = new CaseInsensitiveMap<String, String>();

			for (Map.Entry<String, Integer> entry : colMapIndex.entrySet()) {
				int i = entry.getValue();
				Cell cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				String cellValue;
				if (cell.getCellTypeEnum() == CellType.NUMERIC){
					if(HSSFDateUtil.isCellDateFormatted(cell))
						cellValue = dtFormat.format(cell.getDateCellValue());
					else {
						double numVal = cell.getNumericCellValue();
						if(!Commons.isBlankOrEmpty(numberFormat))
							cellValue = (new DecimalFormat(numberFormat)).format(numVal);
						else{
							if(Math.floor(numVal) == numVal){
								cellValue = String.valueOf((long)numVal);
							}else{
								cellValue = String.valueOf(numVal);
							}
						}
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
				keyMap.put(entry.getKey(), cellValue);
			}

			//check keyMap is null?
			boolean allNull = true;
			for(String strVal : keyMap.values()){
				if(strVal.length() > 0 && !strVal.equals(asNull)){
					allNull = false;
					break;
				}
			}
			if(!allNull) keyMaps.add(keyMap);

		}

		return keyMaps;
	}

	/**
	 * Return value from name_valueColumn if the value in name_keyColumn equal
	 * value_keyColumn (Used in log in via Role. If Role equal "checker" then
	 * return username / password of checker)
	 * 
	 * @param login_dataFile
	 * @param sheetName
	 * @param name_keyColumn
	 * @param value_keyColumn
	 * @param name_valueColumn
	 * @return value in name_valueColumn
	 */

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

	private void updateTemplate(CaseInsensitiveMap<String, String> valueData, String filename, String sheetName,
			String tagetFileName, String jsFileObjectPath) throws IOException {
		Workbook workbook = null;
		// Sheet sheet;
		Compiler compiler = new Compiler(valueData, jsFileObjectPath);
		/**
		 * If not full path
		 */
		if (!filename.contains(":"))
			filename = System.getProperty("user.dir") + filename;
		File file = new File(filename);

		// if file dose exist, load its content
		if (file.exists() && file.isFile()) {
			workbook = getWorkbook(filename);
			Sheet datasheet = workbook.getSheet(sheetName);
			Iterator<Row> rows = datasheet.rowIterator();
			// list of table headers
			List<String> keys = new ArrayList<String>();

			// list of column indices of table headers
			List<Integer> keyCols = new ArrayList<Integer>();
			while (rows.hasNext()) {
				Map<String, String> keyMap = new CaseInsensitiveMap<>();
				Row row = (Row) rows.next();

				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = (Cell) cells.next();
					String cellValue;
					if (cell.getCellTypeEnum() == CellType.NUMERIC)
					{
						double numValue = cell.getNumericCellValue();
						if(Math.floor(numValue) == numValue){
							cellValue = String.valueOf((long)numValue);
						}else{
							cellValue = String.valueOf(numValue);
						}
					}
					else if (cell.getCellTypeEnum() == CellType.BLANK)
						cellValue = "";
					else
					    cellValue = cell.getStringCellValue().trim();
					if(cellValue.contains("{{")||cellValue.contains("fun")) {
                        cellValue = compiler.compileWithProperties(cellValue);
                        cell.setCellValue(cellValue);
                    }
				}

			}
		}

		write(workbook, tagetFileName);
		workbook.close();
	}

	/**
	 * YenNTh28
	 * Update row has value = key
	 * @param valueData
	 * keyName: list column need to update
	 * @throws IOException
	 */
	public static void writeResultToFileSheet(String fileName, String sheet,
											  CaseInsensitiveMap<String, String> valueData, int row, String listKeyName) throws IOException {
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet spreadsheet = workbook.getSheet(sheet);
			Iterator<Row> rowIterator = spreadsheet.iterator();

			// Lay thong tin header cua file excel
			XSSFRow header_row = spreadsheet.getRow(0);
			XSSFRow rowInsert = (XSSFRow) spreadsheet.getRow(row);
			String [] listKeyNameUpdateList = listKeyName.split(";");
			for (int i = 0; i < header_row.getLastCellNum(); i++) {
				XSSFCell header_cell = header_row.getCell(i);
				String header = header_cell.getStringCellValue().toString();

				for(int j=0;j<listKeyNameUpdateList.length;j++){
					if (header.equalsIgnoreCase(listKeyNameUpdateList[j])) {
						Cell cell = rowInsert.createCell(i);
						String value = (String) valueData.get(header);
						XSSFCellStyle style = (XSSFCellStyle) workbook
								.createCellStyle();
						cell.setCellValue(value);
					}
				}

			}
			FileOutputStream fos = new FileOutputStream(fileName);
			workbook.write(fos);
			fos.close();
			System.out.println(fileName + "is written successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writes a <b>org.apache.poi.ss.usermodel.Workbook</b> object into a file.
	 * <p>
	 * Note: This method will overwrite existing file.
	 * 
	 * @param workbook
	 *            the object represent the whole excel content
	 * @param filename
	 *            the full path to the file to write.
	 * @throws IOException
	 *             when this file is not accessible, is blocked or is opened by
	 *             other application or process.
	 */
	public void write(Workbook workbook, String filename) throws IOException {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		FileOutputStream outputStream = new FileOutputStream(filename);
		workbook.write(outputStream);
		outputStream.close();
	}

	/**
	 * Make upload file from a template return new file path
	 * 
	 * @author anhptn14
	 * @param data:
	 *            data object, type: CaseInsensitiveMap<String, String>
	 * @param pathOfTemplateFile:
	 *            full path or relative path
	 * @param jsFileObjectPath:
	 *            js file to compile template content
	 * @return
	 * @throws IOException
	 */
	public String updateTemplate(CaseInsensitiveMap<String, String> data, String pathOfTemplateFile,
			String jsFileObjectPath) throws IOException {
		if (!pathOfTemplateFile.contains(":")) {
			pathOfTemplateFile = System.getProperty("user.dir") + pathOfTemplateFile;
		}

		Compiler com = new Compiler(data, jsFileObjectPath);

		String newFilePath = com.compileWithProperties(pathOfTemplateFile);
		if(newFilePath.contains(".xls")) {
			ExcelDriver exdri = new ExcelDriver();
			exdri.updateTemplate(data, pathOfTemplateFile, "Sheet1", newFilePath, jsFileObjectPath);
		}
		return newFilePath;
	}

	/**
	 * Get value in value list by excelKeyColumn and update to excel file via updateColumnList
	 * @param valueData: Value list data to update excel file
	 * @param filename : Excel file need update
	 * @param sheetName : Sheet name
	 * @param excelKeyColumn : key column in excel file for search in valueData
	 * @param updateColumnList : excel column list need update value
	 * @return : true if update success
	 */
	public boolean saveAsExcelFile(List<Map<String, String>> valueData, String filename, String sheetName,
								  String excelKeyColumn, List<String> updateColumnList) {
		if(Commons.isBlankOrEmpty(valueData)) return false;
		String valueKeyColumn = Commons.removeSpecialChar(excelKeyColumn, "_");

		Workbook workbook = null;
		filename = Commons.getAbsolutePath(filename);
		File file = new File(filename);

		//get keys
		//Set<String> keys = valueData.get(0).keySet();
		if (!file.exists() || !file.isFile()){
			Commons.getLogger().error(String.format("File s%s doesn't exists", file));
			return false;
		}

		// if file dose exist, load its content
		try{
			workbook = getWorkbook(filename);
			Sheet datasheet = workbook.getSheet(sheetName);
			Iterator<Row> rows = datasheet.rowIterator();
			// list of table headers
			List<String> excelKeys = new ArrayList<String>();

			int exKeyColumnIndex = -1;
			int exRunColumnIndex = -1;
			//excel col map Index - Name
			Map<Integer, String> exUpdateColMap = new LinkedHashMap<>();

			// list of column indices of table headers
			List<Integer> keyCols = new ArrayList<Integer>();
			while (rows.hasNext()) {
				Map<String, String> keyMap = new CaseInsensitiveMap<>();
				Row row = (Row) rows.next();
				Iterator<Cell> cells = row.cellIterator();

				// table headers
				if (row.getRowNum() == 0) {
					while (cells.hasNext()) {
						Cell cell = (Cell) cells.next();
						String cellValue = getCellStringValue(cell);

						excelKeys.add(cellValue);
						keyCols.add(cell.getColumnIndex());
						if(excelKeyColumn.equalsIgnoreCase(cellValue)){
							exKeyColumnIndex = cell.getColumnIndex();
						}
						if("run".equals(cellValue.toLowerCase())){
							exRunColumnIndex = cell.getColumnIndex();
						}
						if(updateColumnList.stream().anyMatch(cellValue::equalsIgnoreCase)){
							exUpdateColMap.put(cell.getColumnIndex(), cellValue);
						}
					}
					continue;
				}
				//check run column
				if(exRunColumnIndex >= 0){
					Cell runCell = row.getCell(exRunColumnIndex);
					if(!"x".equalsIgnoreCase(getCellStringValue(runCell).trim())) continue;	//skip
				}
				//get key value
				Cell keyCell = row.getCell(exKeyColumnIndex);
				//find Map row in list data
				Map<String, String> mapRow = Commons.getMapRowInList(valueData, valueKeyColumn, getCellStringValue(keyCell));
				if(Commons.isBlankOrEmpty(mapRow)) continue;	//row not need update
				exUpdateColMap.forEach((exColIndex, exColName) -> {
					if(exColIndex >= 0){
						String dtColName = Commons.removeSpecialChar(exColName);
						String updateValue = mapRow.keySet().contains(dtColName) ? mapRow.get(dtColName) : "";
						if(row.getCell(exColIndex) == null){
							row.createCell(exColIndex, CellType.STRING);
						}
						row.getCell(exColIndex).setCellType(CellType.STRING);
						row.getCell(exColIndex).setCellValue(updateValue);
					}

				});
			}

			//save to current file
			write(workbook, filename);
			workbook.close();
			return true;

		}catch (Exception e){
			Commons.getLogger().warn(e.getMessage());
			return false;
		}
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

	public List<String> getListSheetName(String fileName) {
		Workbook workbook = null;
		try {
			workbook = getWorkbook(fileName);
			if(workbook == null) return null;
			List<String> sheetNameLst = new LinkedList<>();
			Iterator<Sheet> iterator = workbook.sheetIterator();
			while (iterator.hasNext()) {
				Sheet dataSheet = iterator.next();
				sheetNameLst.add(dataSheet.getSheetName());
			}
			workbook.close();
			return sheetNameLst;
		} catch (IOException e) {
			return null;
		}
	}

	private String getCellStringValue(Cell cell){
		if(cell == null) return "";
		String cellValue;
		if (cell.getCellTypeEnum() == CellType.NUMERIC){
			if(cell.getNumericCellValue() == (long)cell.getNumericCellValue())
				cellValue = String.valueOf((long) cell.getNumericCellValue());
			else
				cellValue = String.valueOf(cell.getNumericCellValue());
		}
		else if (cell.getCellTypeEnum() == CellType.BLANK || cell.getStringCellValue() == null)
			cellValue = "";
		else
			cellValue = cell.getStringCellValue().trim();

		return cellValue;
	}

}