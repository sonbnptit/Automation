package com.tcb.auto.utils;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

/**
 * This class provides methods to process <b>Data Table</b> structures by
 * simplifying them into plain text with special row and column separators. Data
 * Table structure is treated as a <b>MatrixMap</b> of values.
 * 
 * @author bachtx2
 * @version 1.0.170418
 * @since JDK1.7
 *
 */
public class MatrixMap {

	public static final String KEY_ID = "__KEY_ID";
	public static final char KEY_SEPARATE = '|';

	public boolean matrixEqual(List<Map<String, String>> matrix1, List<Map<String, String>> matrix2){
		if(Commons.isBlankOrEmpty(matrix1) || Commons.isBlankOrEmpty(matrix2)) return false;
		if(matrix1.size() != matrix2.size()) return false;
		return containMatrix(matrix1, matrix2, "");
	}

	public boolean matrixEqual(List<Map<String, String>> matrix1, List<Map<String, String>> matrix2, List<String> matrix1Cols, List<String> matrix2Cols){
		if(Commons.isBlankOrEmpty(matrix1) || Commons.isBlankOrEmpty(matrix2)) return false;
		if(matrix1.size() != matrix2.size()) return false;
		return containMatrix(matrix1, matrix2, matrix1Cols, matrix2Cols, "");
	}

	public boolean containMatrix(List<Map<String, String>> matrix, List<Map<String, String>> subMatrix, String asNull) {
		if(matrix == null || matrix.isEmpty()) return false;
		if(subMatrix == null || subMatrix.isEmpty()) return true;

		if(matrix.size() < subMatrix.size()) return false;

		//check matrix contain subMatrix
		for(Map<String, String> row: subMatrix){
			//check matrix contain row
			if(!containRow(matrix, row, asNull))
				return false;
		}
		//matrix contain all row
		return true;
	}

	public boolean containMatrix(List<Map<String, String>> matrix, List<Map<String, String>> subMatrix, List<String> matrixCols, List<String> subMatrixCols, String asNull) {
		List<Map<String, String>> exMatrix = extractSubMatrix(matrix, matrixCols);
		List<Map<String, String>> exSubMatrix = extractSubMatrix(subMatrix, subMatrixCols);
		return containMatrix(exMatrix, exSubMatrix, asNull);
	}

	public boolean containRow(List<Map<String, String>> matrix, Map<String, String> row, String asNull) {
		if(matrix == null || matrix.isEmpty()) return false;
		if(row == null || row.isEmpty()) return true;
		for(Map<String, String> mtRow: matrix){
			if(rowEqual(mtRow, row, asNull))
				return true;
		}
		//no match row
		return false;
	}

	protected boolean rowEqual(Map<String, String> row1, Map<String, String> row2, String asNull){
		if(Commons.isBlankOrEmpty(row1) || Commons.isBlankOrEmpty(row2)) return false;
		if(row1.size() != row2.size()) return false;

		List<String> keyR1 = new ArrayList<String>(row1.keySet());
		List<String> keyR2 = new ArrayList<String>(row2.keySet());

		//check 2 row equal
		for(int i = 0; i < keyR1.size(); i++){
			String valR1 = Commons.isBlankOrEmpty(row1.get(keyR1.get(i))) ? asNull : row1.get(keyR1.get(i));
			String valR2 = Commons.isBlankOrEmpty(row2.get(keyR2.get(i))) ? asNull : row2.get(keyR2.get(i));
			valR1 = valR1.replaceAll("(?i)<*null>*", "");
			valR2 = valR2.replaceAll("(?i)<*null>*", "");
			if(NumberUtils.isCreatable(valR1.replaceAll("\\,", "")) && NumberUtils.isCreatable(valR2.replaceAll("\\,", ""))){
				if(!(NumberUtils.createNumber(valR1.replaceAll("\\,", "")).doubleValue() == NumberUtils.createNumber(valR2.replaceAll("\\,", "")).doubleValue())){
					Commons.log4jAndReport("Match fail key of row 1 : " + row1.get(keyR1.get(0)) + " with key of row 2 " + row2.get(keyR2.get(0)));
					Commons.log4jAndReport("Match fail value1 : " + valR1 + " with value2 " + valR2);
					return false;
				}
			}else{
				if(!valR1.trim().equals(valR2.trim())){
					Commons.log4jAndReport("Match fail key of row 1 : " + row1.get(keyR1.get(0)) + " with key of row 2 " + row2.get(keyR2.get(0)));
					Commons.log4jAndReport("Match fail value1 : " + valR1 + " with value2 " + valR2);
					return false;
				}
			}
		}
		//two row equal
		return true;
	}

	/*public Map<String, String> getDiffRow(List<Map<String, String>> matrix, Map<String, String> row, String asNull){
		if(matrix == null || matrix.isEmpty()) return null;
		if(row == null || row.isEmpty()) return null;
		for(Map<String, String> mtRow: matrix){
			//get diff 2 row
			if(mtRow.size() != row.size()) continue;

			List<String> keyMtRow = new ArrayList<String>(mtRow.keySet());
			List<String> keyRow = new ArrayList<String>(row.keySet());

			boolean checkDiff = false;
			for(int i = 0; i < keyMtRow.size(); i++){
				String valMtRow = Utils.isBlankOrEmpty(mtRow.get(keyMtRow.get(i))) ? asNull : mtRow.get(keyMtRow.get(i));
				String valRow = Utils.isBlankOrEmpty(row.get(keyRow.get(i))) ? asNull : row.get(keyRow.get(i));

				if(NumberUtils.isCreatable(valMtRow) && NumberUtils.isCreatable(valRow)){
					if(!NumberUtils.createNumber(valMtRow).equals(NumberUtils.createNumber(valRow))) checkDiff = true;
				}else{
					if(!valMtRow.trim().equals(valRow.trim())) checkDiff = true;
				}
				if(checkDiff){
					//save diff
					StringBuilder builder = new StringBuilder();
					builder.append(keyRow.get(i)).append(":").append(valRow).append("<>").append(keyMtRow.get(i)).append(":").append(valMtRow);
					row.put(keyRow.get(i), builder.toString());
				}
			}

			if(checkDiff) return row;
		}
		return null;
	}*/

	public List<Map<String, String>> getDiffMatrix(List<Map<String, String>> matrix, List<Map<String, String>> subMatrix, String asNull){
		List<Map<String, String>> diffMatrix = new LinkedList<>();
		if(Commons.isBlankOrEmpty(matrix) || Commons.isBlankOrEmpty(subMatrix)) return diffMatrix;

		//if(matrix.size() < subMatrix.size()) return subMatrix;

		//check matrix contain subMatrix
		for(Map<String, String> row: subMatrix){
			//check matrix contain row
			/*if(getDetail){
				Map<String, String> diffRow = getDiffRow(matrix, row, asNull);
				if(diffRow != null) diffMatrix.add(diffRow);
			}*/
			if(!containRow(matrix, row, asNull)){
				diffMatrix.add(row);
			}
		}
		//return diff matrix
		return diffMatrix;
	}

	public List<Map<String, String>> getDiffMatrix(List<Map<String, String>> matrix, List<Map<String, String>> subMatrix, List<String> matrixCols, List<String> subMatrixCols, String asNull){
		List<Map<String, String>> exMatrix = extractSubMatrix(matrix, matrixCols);
		List<Map<String, String>> exSubMatrix = extractSubMatrix(subMatrix, subMatrixCols);
		return getDiffMatrix(exMatrix, exSubMatrix, asNull);
	}

	public List<Map<String, String>> extractSubMatrix(List<Map<String, String>> matrix, List<String> matrixCols){
		if(Commons.isBlankOrEmpty(matrix) || Commons.isBlankOrEmpty(matrixCols)) return null;
		Map<String, String> mapMatrixColKey = new CaseInsensitiveMap<>();  //map matrixCols with matrix key

        matrixCols.replaceAll(String::trim);
		//check matrix contain keys
        for(String colKey: matrixCols){
            System.out.println(colKey);
            boolean checkKey = false;

            for(String mtKey: matrix.get(0).keySet()){
                mtKey = mtKey.trim().toUpperCase();
                if(mtKey.equals(colKey.toUpperCase()) || mtKey.contains(colKey.toUpperCase()) || mtKey.matches(colKey)){
                    //matrix contains key
                    checkKey = true;
                    mapMatrixColKey.put(colKey, mtKey);
                    break;
                }
            }
            if(!checkKey) return null;  //matrix not contains key
        }

		List<Map<String, String>> subMatrix = new LinkedList<>();
		matrix.forEach(rowMap -> {
			Map<String, String> subRowMap = new LinkedCaseInsensitiveMap<>();
			for(String key: matrixCols){
			    String mtKey = mapMatrixColKey.get(key);
                subRowMap.put(mtKey, rowMap.get(mtKey));
            }
			subMatrix.add(subRowMap);
		});

		return subMatrix;
	}


	/* Check 2 matrix equal with Keys */
	public Comparator<Map<String, String>> matrixKeyComparator = new Comparator<Map<String, String>>() {
		public int compare(Map<String, String> m1, Map<String, String> m2) {
			return m1.get(KEY_ID).compareTo(m2.get(KEY_ID));
		}
	};

	public boolean matrixEqualWithKey(List<Map<String, String>> matrix1, List<Map<String, String>> matrix2, List<String> matrix1Keys, List<String> matrix2Keys){
		if(Commons.isBlankOrEmpty(matrix1) || Commons.isBlankOrEmpty(matrix2)) return false;
		if(matrix1.size() != matrix2.size()) return false;
		createMatrixKey(matrix1, matrix1Keys);
		createMatrixKey(matrix2, matrix2Keys);

		Collections.sort(matrix1, matrixKeyComparator.reversed());
		Collections.sort(matrix2, matrixKeyComparator.reversed());

		for(Map<String, String> row1: matrix1){
			Map<String, String> row2 = findRowByKey(matrix2, row1.get(KEY_ID));
			if(row2 == null){
				Commons.getLogger().warn("Can't found row with key: " + row1.get(KEY_ID));
				return false;
			}
			if(!rowEqual(row1, row2, "")){
				Commons.getLogger().warn("Row1 not equal Row2");
				Commons.getLogger().warn("Row1 data: " + row1.toString());
				Commons.getLogger().warn("Row2 data: " + row2.toString());
				return false;
			}
		}
		return true;
	}

	protected void createMatrixKey(List<Map<String, String>> matrix, List<String> matrixKeys){
		for(Map<String, String> row: matrix){
			StringBuilder builder = new StringBuilder();
			matrixKeys.forEach(col -> {
				if(builder.length() > 0){
					builder.append(KEY_SEPARATE);
				}
				builder.append(row.get(col));
			});
			row.put(KEY_ID, builder.toString());
		}
	}

	protected Map<String, String> findRowByKey(List<Map<String, String>> matrix, String key){
		for(Map<String, String> row: matrix){
			if(key.equals(row.get(KEY_ID)))
				return row;
		}
		return null;
	}


	@Test
	public void Test_MatrixEqual(){
		List<Map<String, String>> matrixA = new LinkedList<>();
		List<Map<String, String>> matrixB = new LinkedList<>();

		Map<String, String> rowA1 = new CaseInsensitiveMap<>();
		rowA1.put("name", "Nguyen van A");
		rowA1.put("account", "12345");
		rowA1.put("amount", "500000");
		Map<String, String> rowA2 = new CaseInsensitiveMap<>();
		rowA2.put("name", "Tran van N");
		rowA2.put("account", "56789");
		rowA2.put("amount", "800000");
		matrixA.add(rowA1);
		matrixA.add(rowA2);

		Map<String, String> rowB1 = new CaseInsensitiveMap<>();
		rowB1.put("customer", "Nguyen van A");
		rowB1.put("payment", "500000.00");
		Map<String, String> rowB2 = new CaseInsensitiveMap<>();
		rowB2.put("customer", "Tran van N");
		rowB2.put("payment", "800000.0000");
		matrixB.add(rowB1);
		matrixB.add(rowB2);

		List<String> mtACols = Arrays.asList(new String[] {"name", "amount"});
		List<String> mtBCols = Arrays.asList(new String[] {"(?si)(.*)cuS(.*)", "Payment"});

		//boolean check = matrixEqual(matrixA, matrixB);	//=> FAILED
		boolean check = matrixEqual(matrixA, matrixB, mtACols, mtBCols);	//=> PASS

		Assert.assertTrue(check);
	}
}
