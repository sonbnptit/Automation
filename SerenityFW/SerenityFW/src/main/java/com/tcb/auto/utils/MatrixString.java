package com.tcb.auto.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatrixString {

    /**
     * Processes the assignment of matrix of variables to matrix of values. Base
     * on the variables' relative positions, the corresponding values are
     * granted.
     * <p>
     * <b><i>e.g.</i></b>
     * <table><tbody>
     * <tr>
     * <td>
     * <table><tbody><tr><td>var_11,</td><td>var_12,</td><td>var_13;</td></tr><tr><td>var_21,</td><td>var_22,</td><td>var_23;</td></tr><tr><td>var_31,</td><td>var_32,</td><td>var_33</td></tr></tbody></table>
     * </td>
     * <td>:=</td>
     * <td>
     * <table><tbody><tr><td>val_11,</td><td>val_12,</td><td>val_13;</td></tr><tr><td>val_21,</td><td>val_22,</td><td>val_23;</td></tr><tr><td>val_31,</td><td>val_32,</td><td>val_33</td></tr></tbody></table>
     * </td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param matxVariable
     *            the matrix of variables
     * @param matxValue
     *            the matrix of values which representing a <b>Data Table</b>
     * @param sep1 the row separator in matxVariable
     * @param sep2 the column separator in matxVariable
     * @param sepRow the row separator in matxValue
     * @param sepCol the column separator in matxValue
     * @return map of variables and their corresponding values
     */
    public Map<String, String> key2ValueMap(String matxVariable, String matxValue, String sep1, String sep2,
                                            String sepRow, String sepCol) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        // a := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) == -1 && matxVariable.indexOf(sep2) == -1) {
            map.put(matxVariable.trim(),
                    matxValue.replace(sepRow, sep1).replace(sepCol, sep2));
        }

        // a,b := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) == -1 && matxVariable.indexOf(sep2) > -1) {
            for (int i = 0; i < matxVariable.split(sep2).length; i++) {
                if (!matxVariable.split(sep2)[i].trim().equals(Constants.NULL_TEXT_INDICATOR))
                    map.put(matxVariable.split(sep2)[i].trim(),
                            getColumn(matxValue, i, sepRow, sepCol, Constants.NULL_TEXT_INDICATOR).replace(sepRow, sep1)
                                    .replace(sepCol, sep2));
            }
        }

        // a;b := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) > -1 && matxVariable.indexOf(sep2) == -1) {
            for (int i = 0; i < matxVariable.split(sep1).length; i++) {
                if (!matxVariable.split(sep1)[i].trim().equals(Constants.NULL_TEXT_INDICATOR))
                    map.put(matxVariable.split(sep1)[i].trim(),
                            getRow(matxValue, i, sepRow, sepCol, Constants.NULL_TEXT_INDICATOR).replace(sepRow, sep1)
                                    .replace(sepCol, sep2));
            }
        }

        // a,b,c;d,e,f := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) > -1 && matxVariable.indexOf(sep2) > -1) {
            for (int i = 0; i < matxVariable.split(sep1).length; i++) {
                String row = matxVariable.split(sep1)[i].trim();
                for (int j = 0; j < row.split(sep2).length; j++) {
                    if (!row.split(sep2)[j].trim().equals(Constants.NULL_TEXT_INDICATOR))
                        map.put(row.split(sep2)[j].trim(),
                                getElement(matxValue, i, j, sepRow, sepCol, Constants.NULL_TEXT_INDICATOR)
                                        .replace(sepRow, sep1).replace(sepCol, sep2));
                }
            }
        }
        return map;
    }

    /**
     * Gets the entire row of a matrix by its row index
     * @param matrix
     * @param idx row index
     * @param sepRow row separator
     * @param sepCol column separator
     * @param asNull text to represent NULL_CHARACTER
     * @return the row as plain string. Elements in this row are separated by separators. A <b>Dummy Row</b> with NULL_CHARACTER is returned if the value of idx exceeds the total number of rows in the matrix.
     */
    public String getRow(String matrix, int idx, String sepRow, String sepCol, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
        String[] rowsArr = matrix.split(sepRow);
        String dummyRow = "";
        for (int i = 0; i < rowsArr[0].split(sepCol).length; i++) {
            dummyRow += (dummyRow.isEmpty() ? "" : sepCol) + asNull;
        }
        return idx < rowsArr.length ? rowsArr[idx] : dummyRow;
    }

    /**
     * Gets the entire column of a matrix by its column index
     * @param matrix
     * @param idx column index
     * @param sepRow row separator
     * @param sepCol column separator
     * @param asNull text to represent NULL_CHARACTER
     * @return the column as plain string. Elements in this column are separated by separators. A <b>Dummy Column</b> with NULL_CHARACTER is returned if the value of idx exceeds the total number of columns in the matrix.
     */
    public String getColumn(String matrix, int idx, String sepRow, String sepCol, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
        String[] rowsArr = matrix.split(sepRow);
        String column = "";
        String dummyColumn = "";
        for (int i = 0; i < rowsArr.length; i++) {
            dummyColumn += (dummyColumn.isEmpty() ? "" : sepRow) + asNull;
            if (idx < rowsArr[i].split(sepCol).length)
                column += (column.isEmpty() ? "" : sepRow) + rowsArr[i].split(sepCol)[idx].trim();
        }
        return idx < rowsArr[0].split(sepCol).length ? column : dummyColumn;
    }

    /**
     * Gets a element of a matrix by its row index and column index
     * @param matrix
     * @param idxRow row index
     * @param idxCol column index
     * @param sepRow row separator
     * @param sepCol column separator
     * @param asNull text to represent NULL_CHARACTER
     * @return the element located by the point <code><b>matrix[idxRow,idxCol]</b></code>. NULL_CHARACTER is returned if the value of idxRow or idxCol exceeds the size of the matrix.
     */
    public String getElement(String matrix, int idxRow, int idxCol, String sepRow, String sepCol, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
        if (idxRow > matrix.split(sepRow).length)
            return asNull;
        String row = matrix.split(sepRow)[idxRow].trim();
        if (idxCol > row.split(sepCol).length)
            return asNull;
        return row.split(sepCol)[idxCol].trim();
    }

    /**
     * Combines two matrixes into one, expanding one matrix by the columns of the other matrix.
     * <p>
     * <b><i>e.g.</i></b>
     * <table><tbody>
     * <tr>
     * <td><code>[a]=</code><table><tbody><tr><td>a_11,</td><td>a_12,</td><td>a_13;</td></tr><tr><td>a_21,</td><td>a_22,</td><td>a_23;</td></tr><tr><td>a_31,</td><td>a_32,</td><td>a_33</td></tr></tbody></table></td>
     * <td>and</td>
     * <td><code>[b]=</code><table><tbody><tr><td>b_11,</td><td>b_12;</td></tr><tr><td>b_21,</td><td>b_22;</td></tr><tr><td>b_31,</td><td>b_32</td></tr></tbody></table></td>
     * </tr>
     * </tbody></table>
     * then
     * <code>[a,b]=</code><table><tbody><tr><td>a_11,</td><td>a_12,</td><td>a_13,</td><td>b_11,</td><td>b_12;</td></tr><tr><td>a_21,</td><td>a_22,</td><td>a_23,</td><td>b_21,</td><td>b_22;</td></tr><tr><td>a_31,</td><td>a_32,</td><td>a_33,</td><td>b_31,</td><td>b_32</td></tr></tbody></table>
     * @param matrix1 matrix to combine
     * @param matrix2 matrix to combine
     * @param sepRow row separator
     * @param sepCol column separator
     * @return the combined matrix as plain string. Elements in this matrix are separated by separators.
     * If the numbers of rows of the two matrixes do not equal, string "Dimension mismatch" is returned.
     */
    public String expandColumns(String matrix1, String matrix2, String sepRow, String sepCol) {
        if (matrix1 == null || matrix1.isEmpty()) {
            return matrix2;
        }

        if (matrix2 == null || matrix2.isEmpty()) {
            return matrix1;
        }

        String[] rowList1 = matrix1.split(sepRow);
        String[] rowList2 = matrix2.split(sepRow);
        if (rowList1.length == rowList2.length) {
            String matrix = "";
            for (int i = 0; i < rowList1.length; i++) {
                String row = rowList1[i] + sepCol + rowList2[i];
                matrix += (matrix.isEmpty() ? "" : sepRow) + row;
            }
            return matrix;

        }
        return "Dimension mismatch: " + rowList1.length + "!=" + rowList2.length + " (matrix.rows.length)";
    }

    /**
     * Combines two matrixes into one, expanding one matrix by the rows of the other matrix.
     * <p>
     * <b><i>e.g.</i></b>
     * <table><tbody>
     * <tr>
     * <td><code>[a]=</code><table><tbody><tr><td>a_11,</td><td>a_12,</td><td>a_13;</td></tr><tr><td>a_21,</td><td>a_22,</td><td>a_23;</td></tr><tr><td>a_31,</td><td>a_32,</td><td>a_33</td></tr></tbody></table></td>
     * <td>and</td>
     * <td><code>[b]=</code><table><tbody><tr><td>b_11,</td><td>b_12,</td><td>b_13;</td></tr><tr><td>b_21,</td><td>b_22,</td><td>b_23</td></tr></tbody></table></td>
     * </tr>
     * </tbody></table>
     * then
     * <code>[a;b]=</code><table><tbody><tr><td>a_11,</td><td>a_12,</td><td>a_13;</td></tr><tr><td>a_21,</td><td>a_22,</td><td>a_23;</td></tr><tr><td>a_31,</td><td>a_32,</td><td>a_33;</td></tr><tr><td>b_11,</td><td>b_12,</td><td>b_13;</td></tr><tr><td>b_21,</td><td>b_22,</td><td>b_23</td></tr></tbody></table>
     * @param matrix1 matrix to combine
     * @param matrix2 matrix to combine
     * @param sepRow row separator
     * @param sepCol column separator
     * @return the combined matrix as plain string. Elements in this matrix are separated by separators.
     * If the numbers of columns of the two matrixes do not equal, string "Dimension mismatch" is returned.
     */
    public String expandRows(String matrix1, String matrix2, String sepRow, String sepCol) {
        if (matrix1 == null || matrix1.isEmpty()) {
            return matrix2;
        }

        if (matrix2 == null || matrix2.isEmpty()) {
            return matrix1;
        }
        String[] colList1 = matrix1.split(sepRow);
        String[] colList2 = matrix2.split(sepRow);
        if (colList1.length == colList2.length) {
            return matrix1 + sepRow + matrix2;
        }
        return "Dimension mismatch: " + colList1.length + "!=" + colList2.length + " (matrix.columns.length)";
    }

    /**
     * Locates all matrixes in a string by the pair of marks <b>[</b> and <b>]</b>, e.g. "This is a matrix [a,b;c,d]".
     * All those matrixes will be combined into one by expanding columns of the first matrix in this string.
     * <p>
     * e.g. the string <b>[a,b;c,d][e,f;g,h]</b> will become <b>a,b,c,d;e,f,g,h</b>.
     * Note that the <b>[</b> and <b>]</b> marks at the beginning and the end of the returned string will be removed.
     * @param text the string which contains matrixes to combine
     * @param sepRow row separator
     * @param sepCol column separator
     * @return the combined matrix as plain string. Elements in this matrix are separated by separators.
     * If the numbers of rows of all the matrixes do not equal, string "Dimension mismatch" is returned.
     */
    public String combine(String text, String sepRow, String sepCol) {
        Pattern patt_expandColumns = Pattern.compile("(?s)\\[([^\\[\\]]+)\\]\\s*,?\\s*\\[([^\\[\\]]+)\\]");
        Matcher matcher = patt_expandColumns.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
            String matrix = expandColumns(matcher.group(1), matcher.group(2), sepRow, sepCol);
            if (matrix.indexOf("Dimension mismatch") > -1) {
                return matrix + " while paring(" + count + ") " + matcher.group(1) + " and " + matcher.group(2);
            }
            text = text.replace(matcher.group(0), "[" + matrix + "]");

            matcher = patt_expandColumns.matcher(text);
        }
        return text.replaceAll("^\\[+|\\]+$", "");
    }

    /**
     * Converts a <b>Data Table</b> structure into a plain string.
     * Headers of this table will be ignored and values will be converted into a matrix.
     * Elements are separated by separators.
     * @param table the table to convert
     * @param sepRow row separator
     * @param sepCol column separator
     * @return the matrix of values as plain text.
     */
    public String flatten(List<Map<String, String>> table, String sepRow, String sepCol) {
        String flatObjects = "";
        for (Map<String, String> map : table) {
            String flatObject = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                flatObject += (flatObject.isEmpty() ? "" : sepCol)
                        + (entry.getValue() == null || entry.getValue().trim().isEmpty() ? Constants.NULL_TEXT_INDICATOR : entry.getValue());
            }
            flatObjects += (flatObjects.isEmpty() ? "" : sepRow) + flatObject;
        }
        return flatObjects;
    }

    /**
     * Compares two matrixes regardless the order of rows.
     * <p>
     * i.e. the comparison of <b>[a,b;c,d]</b> with <b>[c,d;b,a]</b> will return <code>true</code> as the order of rows is different but the order of columns in each row is the same.
     * <p>
     * However, the comparison of <b>[a,b;c,d]</b> with <b>[b,a;d,c]</b> returns <code>false</code> for the order of columns is different, even though the order of row is the same.
     * @param matrix1 matrix to compare
     * @param matrix2 matrix to compare
     * @param sepRow row separator
     * @param sepCol column separator
     *  casesentive
     * @return <code>true</code> if the two matrixes equal and <code>false</code> if the two matrixes do not equal
     */
    public boolean compare(String matrix1, String matrix2, String sepRow, String sepCol) {
        if(matrix2.contains("(.*")&&matrix1.matches(matrix2))
            return true;
        List<String> rowList1 = new ArrayList<String>();
        List<String> rowList2 = new ArrayList<String>();
        for (String row : matrix1.split(sepRow)) {
            rowList1.add(row.toUpperCase().trim());
        }
        for (String row : matrix2.split(sepRow)) {
            rowList2.add(row.toUpperCase().trim());
        }

        if (rowList1.size() != rowList2.size()) {
            return false;
        }

        Collections.sort(rowList1);
        Collections.sort(rowList2);
        return rowList1.equals(rowList2);
    }

    /**
     * Checks if a matrix contain a specific row or specific matrix. This row can be located in any position, but the order of columns must be the same as of the matrix.
     * @param matrix
     * @param rowOrMatrix
     * @param sepRow row separator
     * @param sepCol column separator
     * @return <code>true</code> if the matrix contains this row and <code>false</code> if the matrix does not.
     */
    public boolean contain(String matrix, String rowOrMatrix, String sepRow, String sepCol) {
        //check matrix1 contain matrix2
        if(matrix.contains(sepRow)&&!matrix.contains("&lt;") &&
                rowOrMatrix.contains(sepRow)&&!rowOrMatrix.contains("&lt;")){
            List<String> rowList1 = Arrays.asList(matrix.split(sepRow));
            List<String> rowList2 = Arrays.asList(rowOrMatrix.split(sepRow));

            if(rowList1.size() < rowList2.size()) return false;
            return rowList1.containsAll(rowList2);
        }

        //check matrix1 contain row
        if(matrix.contains(sepRow)&&!matrix.contains("&lt;")){
            List<String> rowList = Arrays.asList(matrix.split(sepRow));
            return rowList.contains(rowOrMatrix);
        }
        return matrix.contains(rowOrMatrix);
    }

    public Map<String, String> key2ValueMap(String matxVariable, List<Map<String, String>> matrix, String sep1, String sep2) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        // a := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) == -1 && matxVariable.indexOf(sep2) == -1) {
            map.put(matxVariable.trim(),
                    flatten(matrix, sep1, sep2));
        }

        // a,b := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) == -1 && matxVariable.indexOf(sep2) > -1) {
            for (int i = 0; i < matxVariable.split(sep2).length; i++) {
                if (!matxVariable.split(sep2)[i].trim().equals(Constants.NULL_TEXT_INDICATOR))
                    map.put(matxVariable.split(sep2)[i].trim(),
                            getColumn(matrix, i, sep2, Constants.NULL_TEXT_INDICATOR));
            }
        }

        // a;b := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) > -1 && matxVariable.indexOf(sep2) == -1) {
            for (int i = 0; i < matxVariable.split(sep1).length; i++) {
                if (!matxVariable.split(sep1)[i].trim().equals(Constants.NULL_TEXT_INDICATOR))
                    map.put(matxVariable.split(sep1)[i].trim(),
                            getRow(matrix, i, sep1, Constants.NULL_TEXT_INDICATOR));
            }
        }

        // a,b,c;d,e,f := val1,val2,val3;val4,val5,val6
        if (matxVariable.indexOf(sep1) > -1 && matxVariable.indexOf(sep2) > -1) {
            for (int i = 0; i < matxVariable.split(sep1).length; i++) {
                String row = matxVariable.split(sep1)[i].trim();
                for (int j = 0; j < row.split(sep2).length; j++) {
                    if (!row.split(sep2)[j].trim().equals(Constants.NULL_TEXT_INDICATOR))
                        map.put(row.split(sep2)[j].trim(),
                                getElement(matrix, i, j, Constants.NULL_TEXT_INDICATOR));
                }
            }
        }
        return map;
    }

    public String getRow(List<Map<String, String>> matrix, int idx, String sepCol, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
        if(matrix.size() <= idx) return asNull;
        Map<String, String> map = matrix.get(idx);
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry: map.entrySet()){
            builder.append(builder.length() == 0? "" : sepCol).append(entry.getValue());
        }
        return builder.toString();
    }

    public String getColumn(List<Map<String, String>> matrix, int idx, String sepRow, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;

        StringBuilder builder = new StringBuilder();
        for(Map<String, String> map: matrix){
            if(map.size() <= idx) return asNull;
            int count = 0;
            for(Map.Entry<String, String> entry: map.entrySet()){
                if(count == idx){
                    builder.append(builder.length() == 0 ? "" : sepRow).append(entry.getValue());
                }
                count++;
            }
        }
        return builder.toString();
    }

    public String getElement(List<Map<String, String>> matrix, int idxRow, int idxCol, String asNull) {
        asNull = asNull != null ? asNull : Constants.NULL_TEXT_INDICATOR;
        if(matrix.size() <= idxRow) return asNull;
        Map<String, String> map = matrix.get(idxRow);
        if(map.size() <= idxCol) return asNull;
        int count = 0;
        for(Map.Entry<String, String> entry: map.entrySet()){
            if(count == idxCol){
                return entry.getValue();
            }
        }
        return asNull;
    }

}
