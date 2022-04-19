package com.tcb.auto.serenity;

import com.tcb.auto.subprocess.db.DbConnection;
import com.tcb.auto.utils.Commons;
import net.thucydides.core.steps.stepdata.TestDataSource;

import java.util.*;

public class SqlTestDataSource extends AbsCustomTestDataSource {
    private String connection;
    private String table;
    private String query;
    private boolean runAllTest;

    public SqlTestDataSource(String connection, String table, String query, boolean runAllTest) {
        this.connection = connection;
        this.table = table;
        this.query = query;
        this.runAllTest = runAllTest;
    }

    List<Map<String, String>> sqlData;

    @Override
    public List<String> getHeaders() {
        if(Commons.isBlankOrEmpty(sqlData)) return new LinkedList<>();
        return new LinkedList<>(sqlData.get(0).keySet());
    }

    @Override
    public List<Map<String, String>> getData() {
        DbConnection dbConnection = new DbConnection();
        if(Commons.isBlankOrEmpty(query)){
            query = String.format("SELECT * FROM %s", table);
        }
        try {
            sqlData = dbConnection.queryLinked(connection, query);
            if(Commons.isBlankOrEmpty(sqlData)) return null;
            if(!runAllTest && sqlData.get(0).keySet().contains("Run")){
                Iterator<Map<String, String>> row = sqlData.iterator();
                while (row.hasNext()) {
                    Map<String, String> mapRow = row.next();
                    if(!mapRow.get("Run").equalsIgnoreCase("x")){
                        //don't run => need remove it
                        row.remove();
                    }
                }
            }
        } catch (Exception e) {
            sqlData = null;
            Commons.getLogger().warn(e.getMessage());
        }

        return sqlData;
    }

    @Override
    public TestDataSource separatedBy(char c) {
        return null;
    }
}
