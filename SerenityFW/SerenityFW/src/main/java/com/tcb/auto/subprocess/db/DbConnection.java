package com.tcb.auto.subprocess.db;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.LinkedCaseInsensitiveMap;
import net.serenitybdd.core.Serenity;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class uses java native library JDBC to connect and query a relational
 * database management system (RDBMS).
 *  
 * @version 1.0.170418
 * @since JDK1.7
 *
 */
public class DbConnection {

	private List<Map<String, String>> getResultList(ResultSet resultSet,String... title) throws SQLException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		ResultSetMetaData metaData = resultSet.getMetaData();
		while (resultSet!=null&&resultSet.next()) {
			Map<String, String> map = new CaseInsensitiveMap<String, String>();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String colName = metaData.getColumnName(i);
				String colValue = resultSet.getString(colName);
				map.put(colName, colValue);
			}
			result.add(map);
		}
		return result;
	}

	private List<Map<String, String>> getLinkedResultList(ResultSet resultSet,String... title) throws SQLException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		ResultSetMetaData metaData = resultSet.getMetaData();
		while (resultSet!=null&&resultSet.next()) {
			Map<String, String> map = new LinkedCaseInsensitiveMap<>();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String colName = metaData.getColumnName(i);
				String colValue = resultSet.getString(colName);
				map.put(colName, colValue == null ? "" : colValue);
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * Update rows
	 *
     * @param dbType
     * @param sql
     * @param title
	 * @return number of rows updated
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int update(String dbType, String sql, String... title) throws Exception {
		Commons.getLogger().debug("DB SQL: " + sql);
        if (title.length > 0) {
            Serenity.recordReportData().withTitle(title[0]).andContents(sql);
        }

        Connection connection = ConnectDB.getConnection(dbType);
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int updatedRow = statement.executeUpdate();
		return updatedRow;
	}

	/**
	 * Insert a row
	 * 
	 * @param dbType
	 * @param sql
     * @param title
	 * @return new generated id of row inserted
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int insert(String dbType, String sql, String... title) throws Exception {
		Commons.getLogger().debug("DB SQL: " + sql);
        Connection connection = ConnectDB.getConnection(dbType);
		//List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int updatedRow = statement.executeUpdate();

		//ResultSet resultSet = statement.getGeneratedKeys();
	    if (title.length > 0) {
            Serenity.recordReportData().withTitle(title[0]).andContents(sql);
            //result = getResultList(resultSet,title);
        }
	    //else
		//result = getResultList(resultSet);
		
		return updatedRow;
	}

	/**
	 * Execute Query from DB
	 * 
	 * @author anhptn14
	 * @param dbType:
	 *            DB to execute
	 * @param query
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<Map<String, String>> query(String dbType, String query, String... title) throws Exception {

		Commons.getLogger().debug("DB SQL: " + query);

		Connection connection = ConnectDB.getConnection(dbType);
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        Statement statement = null;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (title.length > 0) {
//            Serenity.recordReportData().withTitle(title[0]).andContents(query);
            Commons.getLogger().debug(dbType + ": " + title[0]);
            result = getResultList(resultSet,title); 
        }else
        	result = getResultList(resultSet);

        if(!statement.isClosed()){
        	statement.close();
		}
        Commons.getLogger().debug("Data sql: "+result.toString());

        return result;
	}

	public List<Map<String, String>> queryLinked(String dbType, String query, String... title) throws Exception {

		Commons.getLogger().debug("DB SQL: " + query);

		Connection connection = ConnectDB.getConnection(dbType);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		Statement statement = null;
		statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		if (title.length > 0) {
//            Serenity.recordReportData().withTitle(title[0]).andContents(query);
			Commons.getLogger().debug(dbType + ": " + title[0]);
			result = getLinkedResultList(resultSet,title);
		}else
			result = getLinkedResultList(resultSet);

		Commons.getLogger().debug("Data sql: "+result.toString());

		return result;
	}

	/**
	 * Return the first record of the result by column return null if cannot
	 * find anything
	 * 
	 * @author anhptn14
	 * @param dbType
	 * @param query
	 * @param columnName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String queryFirstRecord(String dbType, String query, String columnName) throws Exception {
		Commons.getLogger().debug("DB SQL: " + query);
		List<Map<String, String>> result;
		result = query(dbType, query/*, "get " + columnName*/);
		if (result.size() > 0)
			return result.get(0).get(columnName.toUpperCase());

		return null;
	}

    public List<Map<String, String>> executeQuery(String connKey, String query, String... input) throws Exception {
		Commons.getLogger().debug("DB SQL: " + query);
		Commons.getLogger().debug("And parameter: " + String.join(",", input));
		Connection con = ConnectDB.getConnection(connKey);
        PreparedStatement statement = con.prepareStatement(query);
        if (input != null) {
            for (int i = 0; i < input.length; i++) {
                statement.setString(i + 1, input[i]);
            }
        }
        List<Map<String, String>> results = buildResults(statement.executeQuery());
        statement.close();
        con.close();
        return results;
    }

    public List<Map<String, String>> executeQueryWithIn(String connKey, String query, String[] inputs,
            String... paramsIn) throws Exception {
		Commons.getLogger().debug("DB SQL: " + query);
		Commons.getLogger().debug("And parameter: " + String.join(",", inputs));
		Commons.getLogger().debug("And parameter IN: " + String.join(",", paramsIn));
        Connection con = ConnectDB.getConnection(connKey);
        if (paramsIn != null) {
            for (int i = 0; i < paramsIn.length; i++) {
                query = query.replace("(:paramsIn" + (i + 1) + ")", "('" + paramsIn[i] + "')");
            }
        }
        PreparedStatement statement = con.prepareStatement(query);
        if (inputs != null) {
            for (int i = 0; i < inputs.length; i++) {
                statement.setString(i + 1, inputs[i]);
            }
        }

        List<Map<String, String>> results = buildResults(statement.executeQuery());
        statement.close();
        return results;
    }

    private List<Map<String, String>> buildResults(ResultSet results) throws SQLException {
        List<Map<String, String>> lisstResult = new ArrayList<Map<String, String>>();

        ResultSetMetaData metaData = results.getMetaData();
        Map<String, String> map;
        while (results.next()) {
            map = new CaseInsensitiveMap<String, String>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String colName = metaData.getColumnName(i);                          
                String colValue = results.getString(colName);
                map.put(colName, colValue);              
            }
            lisstResult.add(map);
        }
        results.close();
		Commons.getLogger().debug("Data sql: "+lisstResult.toString());
        // Serenity.recordReportData().withTitle("Result
        // sql").andContents(lisstResult.toString());
        return lisstResult;
    }
}