package com.tcb.auto.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Console;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashMatrixMap extends MatrixMap {
    public static final String HASH_KEY = "__HASH_KEY";

    private void prepareHashMatrix(List<Map<String, String>> matrix){
        matrix.forEach(rowMap -> {
            StringBuilder builder = new StringBuilder();
            for(String value: rowMap.values()){
                builder.append(value);
            }
            String hashCode = "";
            try {
                hashCode = toHexString(getSHA(builder.toString()));
            } catch (NoSuchAlgorithmException e) {
                Commons.getLogger().warn(e.getMessage());
            }
            rowMap.put(HASH_KEY, hashCode);

        });
    }

    private void removeHashKeyMatrix(List<Map<String, String>> matrix){
        matrix.forEach(rowMap -> {
            rowMap.remove(HASH_KEY);
        });
    }

    private byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    @Override
    public boolean containMatrix(List<Map<String, String>> matrix, List<Map<String, String>> subMatrix, String asNull) {
        if(matrix == null || matrix.isEmpty()) return false;
        if(subMatrix == null || subMatrix.isEmpty()) return true;

        if(matrix.size() < subMatrix.size()) return false;

        prepareHashMatrix(matrix);
        prepareHashMatrix(subMatrix);

        boolean result = true;
        //check matrix contain subMatrix
        for(Map<String, String> row: subMatrix){
            //check matrix contain row
            if(!containRow(matrix, row, asNull)){
                result = false;
                break;
            }
        }
        removeHashKeyMatrix(matrix);
        removeHashKeyMatrix(subMatrix);
        return result;
    }

    @Override
    protected boolean rowEqual(Map<String, String> row1, Map<String, String> row2, String asNull){
        if(Commons.isBlankOrEmpty(row1) || Commons.isBlankOrEmpty(row2)) return false;
        if(row1.size() != row2.size()) return false;

        String hashR1 = row1.get(HASH_KEY);
        String hashR2 = row2.get(HASH_KEY);
        if(Commons.isBlankOrEmpty(hashR1) || Commons.isBlankOrEmpty(hashR2)) return false;
        if(hashR1.equals(hashR2)) return true;  //two row equal
        return false;
    }
}
