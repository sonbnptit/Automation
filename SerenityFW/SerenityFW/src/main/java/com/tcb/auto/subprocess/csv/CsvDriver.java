package com.tcb.auto.subprocess.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import com.tcb.auto.utils.Commons;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class CsvDriver {
    public static List<Map<String, String>> loadCSVData(String filePath) throws IOException {
        List<Map<String, String>> lstRow = new LinkedList<>();
        File csvFile = new File(filePath);
        CSVParser csvParser = CSVParser.parse(csvFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord());
        List<String> keys = csvParser.getHeaderNames();

        for (CSVRecord csvRecord : csvParser) {
            //add row value
            CaseInsensitiveMap<String, String> mapRow = new CaseInsensitiveMap<>();
            for(String key:keys){
                mapRow.put(key, csvRecord.get(key));
            }
            lstRow.add(mapRow);
        }
        csvParser.close();

        return lstRow;
    }

    public static boolean saveCSVFile(String filePath, List<Map<String, String>> lstRow) throws IOException {
        if(Commons.isBlankOrEmpty(lstRow)) return false;

        //get keyMap
        Set<String> keys = lstRow.get(0).keySet();

        FileWriter csvWriter = new FileWriter(filePath);
        CSVPrinter csvPrinter = new CSVPrinter(csvWriter, CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL));

        csvPrinter.printRecord(keys);

        for(Map<String, String> mapRow: lstRow){
            List<String> csvRows = new LinkedList<>();
            //get row data
            for(String key:keys){
                csvRows.add(mapRow.get(key));
            }
            csvPrinter.printRecord(csvRows);
        }
        csvPrinter.close();
        csvWriter.close();
        return true;
    }

    @Test
    public void testCSV() throws IOException {
        List<Map<String, String>> allRow = loadCSVData("D:/LyPT4/ENQ_TXN (5).csv");
        allRow.forEach(map -> {
            map.keySet().remove("");
        });
        System.out.println(allRow);
//        Assert.assertTrue(allRow.size() > 0);
//        boolean result = saveCSVFile("D:/Project/iGTS/automationframework/MavenAutoTest_iGTB/iGTSMobile/data/CBXFO/Common/login_data_2.csv", allRow);
//        Assert.assertTrue(result);
    }
}
