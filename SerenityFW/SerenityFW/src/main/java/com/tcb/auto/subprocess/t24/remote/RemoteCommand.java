package com.tcb.auto.subprocess.t24.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.asciidoctor.internal.CaseInsensitiveMap;

import com.jbase.jremote.DefaultJConnectionFactory;
import com.jbase.jremote.JConnection;
import com.jbase.jremote.JDynArray;
import com.jbase.jremote.JExecuteResults;
import com.jbase.jremote.JRemoteException;
import com.jbase.jremote.JResultSet;
import com.jbase.jremote.JStatement;
import com.jbase.jremote.JSubroutineNotFoundException;
import com.jbase.jremote.JSubroutineParameters;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.Constants;
import com.tcb.auto.utils.GlobalVariable;

import net.serenitybdd.core.Serenity;

/**
 * Created by nhanha on 3/15/2019.
 */
public class RemoteCommand {
	final static int RETRIES = 5;

	private JConnection jconn;
	private String hostname;
	private int remotePort;
	private String username;
	private String password;

	private boolean _isConnected = false;

	public boolean isConnected() {
		return _isConnected;
	}

	private Map<String, String> compareFuncMap = null;

	private static RemoteCommand singletonT24;

	public RemoteCommand() {
		// Do not remove this function. It's in used
	}

	private class OFSEnquiryCommand {
		private String ofsMessage;
		private List<String> filterList;

		public OFSEnquiryCommand(String ofsMessage, List<String> filterList) {
			this.ofsMessage = ofsMessage = "";
			this.filterList = filterList = null;
		}

		public String getOfsMessage() {
			return ofsMessage;
		}

		public List<String> getFilterList() {
			return filterList;
		}

		public void setOfsMessage(String ofsMessage) {
			this.ofsMessage = ofsMessage;
		}

		public void setFilterList(List<String> filterList) {
			this.filterList = filterList;
		}
	}

	private RemoteCommand(String hostname, int remotePort, String username, String password) {
		this.hostname = hostname;
		this.remotePort = remotePort;
		this.username = username;
		this.password = password;
	}

	/*
	 * public static RemoteCommand getNewConnect(Map<String, String> configManager)
	 * { RemoteCommand t24RemoteCmd; int port = 0; String portStr =
	 * configManager.get(Constants.T24_JQL_REMOTE_PORT); if (portStr == null ||
	 * portStr.isEmpty() || portStr.equals(Constants.NULL_TEXT_INDICATOR) ||
	 * !portStr.matches("\\d+")) { port = Constants.DEFAULT_T24_JSH_REMOTE_PORT; }
	 * else { port = Integer.parseInt(portStr); } t24RemoteCmd = new
	 * RemoteCommand(configManager.get(Constants.T24_JQL_SERVER), port,
	 * configManager.get(Constants.T24_JQL_AUTH_UID),
	 * configManager.get(Constants.T24_JQL_AUTH_PWD)); return t24RemoteCmd; }
	 */

	public static RemoteCommand getDefaultConnect() {

		Properties variables = GlobalVariable.listConfigEnv;
		int port = 0;
		String portStr = variables.getProperty(Constants.T24_JQL_REMOTE_PORT);
		if (portStr == null || portStr.isEmpty() || portStr.equals(Constants.NULL_TEXT_INDICATOR)
				|| !portStr.matches("\\d+")) {
			port = Constants.DEFAULT_T24_JSH_REMOTE_PORT;
		} else {
			port = Integer.parseInt(portStr);
		}
		if (singletonT24 == null) {
			singletonT24 = new RemoteCommand(variables.getProperty(Constants.T24_JQL_SERVER), port,
					variables.getProperty(Constants.T24_JQL_AUTH_UID),
					variables.getProperty(Constants.T24_JQL_AUTH_PWD));
		}

		return singletonT24;
	}

	/**
	 * Connect to jAgent
	 * 
	 * @return
	 */
	public boolean connect() {
		if (_isConnected)
			return true;
		DefaultJConnectionFactory cxf = new DefaultJConnectionFactory();
		cxf.setHost(hostname);
		cxf.setPort(remotePort); // This port should match the port jAgent is listening on
		cxf.setActionTimeout(0); // no timeout
		// cxf.enableSSL(); //Enable SSL encryption
		// cxf.enableNaiveTrustManager(); //Trust all server credentials
		for (int connectCount = RETRIES; connectCount > 0; connectCount--) {
			try {
				if (connectCount < RETRIES) {
					Thread.sleep(1000);
				}
				Properties prop = new Properties();
				prop.setProperty("allow_input", "true");
				jconn = cxf.getConnection(username, password, prop);
				_isConnected = true;
				return true;
			} catch (Exception e) {
				System.out.println("Connect to T24 failed, retry count: " + connectCount);
				// log.error(e.getMessage());
			}
		}
		System.out.println("Connect to T24 failed with max retry");
		_isConnected = false;
		return false;
	}

	/**
	 * Execute and get result jQL command (only for LIST command) For other command
	 * using JExecuteResults => get output like Telnet
	 * 
	 * @param command
	 * @param sepRow
	 * @param sepCol
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 */
	public String command(String command, String sepRow, String sepCol, String sepValue) throws JRemoteException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");

		if (command.trim().toUpperCase().startsWith("LIST ")) {
			// jql List command
			JStatement statement = jconn.createStatement();
			JResultSet rs = statement.execute(command);

			StringBuilder builder = new StringBuilder();
			int row = 0;
			while (rs.next()) {
				if (row > 0)
					builder.append(sepRow);
				JDynArray jd = rs.getRow();
				for (int i = 1; i <= jd.getNumberOfAttributes(); i++) {
					if (jd.getNumberOfValues(i) == 0)
						continue;
					if (i > 1)
						builder.append(sepCol);
					for (int j = 1; j <= jd.getNumberOfValues(i); j++) {
						if (j > 1)
							builder.append(sepValue);
						builder.append(jd.get(i, j).isEmpty() ? Constants.NULL_TEXT_INDICATOR : jd.get(i, j));
					}
				}
				row++;
			}
			return builder.toString();
		} else {
			// other command

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Writer writer = null;
			try {
				writer = new OutputStreamWriter(bos, "UTF-8");
				jconn.setTerminalOutputWriter(writer);
			} catch (IOException e) {
			}

			JExecuteResults execRs = jconn.execute(command);

			JDynArray jd = execRs.getCapturingVar();
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i <= jd.getNumberOfAttributes(); i++) {
				if (jd.getNumberOfValues(i) == 0)
					continue;
				if (i > 1)
					builder.append(sepCol);
				for (int j = 1; j <= jd.getNumberOfValues(i); j++) {
					if (j > 1)
						builder.append(sepValue);
					builder.append(jd.get(i, j).isEmpty() ? Constants.NULL_TEXT_INDICATOR : jd.get(i, j));
				}
			}

			return builder.toString();
		}

	}

	public String command(String command) throws JRemoteException {
		return command(command, Constants.SEP_ROW, Constants.SEP_COL, Constants.SEP_VALUE);
	}

	/**
	 * Execute jQL list command, anh get results as list map jQL
	 * 
	 * @param command
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 * @deprecated use {@link #jqlListSubroutine(String, String)} instead.
	 */
	@Deprecated
	public List<Map<String, String>> jqlList(String command, String sepValue) throws JRemoteException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();

		JStatement statement = jconn.createStatement();
		JResultSet rs = statement.execute(command);

		// get first row
		rs.next();
		JDynArray jdRow = rs.getRow();

		if (jdRow == null) {
			return results; // no results
		}

		ArrayList<String> keys = new ArrayList<String>();
		keys.add("ID");

		// get number attribute results
		int countAttr = jdRow.getNumberOfAttributes() - 1; // -1 value for @ID
		if (jdRow.get(jdRow.getNumberOfAttributes()).isEmpty())
			countAttr -= 1;

		// get key if command contain keyword ONLY
		if (command.toUpperCase().contains("ONLY")) {

			int index = command.toUpperCase().lastIndexOf("ONLY");
			String colStr = command.toUpperCase().substring(index + 4).trim();
			// split key by space
			String[] keysArr = colStr.split("\\s+");
			for (int i = 0; i < countAttr; i++) {
				keys.add(keysArr[i]);
			}

		} else { // gen col name auto
			for (int i = 1; i <= countAttr; i++) {
				keys.add(String.format(Constants.DEFAULT_T24_JSH_COL_FORMAT, i));
			}
		}

		// get all row

		do {
			jdRow = rs.getRow();
			Map<String, String> mapRow = new CaseInsensitiveMap<>();
			// get all attribute
			for (int i = 1; i <= keys.size(); i++) {
				StringBuilder builder = new StringBuilder();
				for (int j = 1; j <= jdRow.getNumberOfValues(i); j++) {
					if (j > 1)
						builder.append(sepValue);
					builder.append(jdRow.get(i, j).isEmpty() ? Constants.NULL_TEXT_INDICATOR : jdRow.get(i, j));
				}
				// put attribute
				mapRow.put(keys.get(i - 1), builder.toString());
			}
			// put row
			results.add(mapRow);
		} while (rs.next());

		return results;
	}

	/**
	 * Execute jQL list command using Subroutine, anh get results as list map jQL
	 * 
	 * @param command
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 * @throws UnsupportedEncodingException
	 */
	public List<Map<String, String>> jqlListSubroutine(String command, String sepValue)
			throws JRemoteException, UnsupportedEncodingException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");

		Commons.getLogger().debug("T24 JQL: " + command);

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();

		// format command
		command = command.replaceAll("\\s+", " ").trim();
		Map<String, String> mapResult = calljQLSubroutine(null, command);
		if (Commons.isBlankOrEmpty(mapResult))
			return results;

		// get key list
		Set<String> keysSet = mapResult.keySet();
		keysSet.removeAll(Arrays.asList("", null));

		// Get all col map
		for (Map.Entry<String, String> entry : mapResult.entrySet()) {
			String key = entry.getKey();
			String[] valArr = entry.getValue().split(Constants.JQL_SEP_ROW); // all row value of col
			// add rows value of col to map
			// get all row map
			for (int index = 0; index < valArr.length; index++) {
				// get row in result list
				Map<String, String> rowMap;
				if (results.size() <= index) {
					// create new row map clone struct from mapResult
					rowMap = new CaseInsensitiveMap<>();
					for (String cloneKey : keysSet) {
						rowMap.put(cloneKey, "");
					}
					// add new row map to result
					results.add(rowMap);
				} else {
					rowMap = results.get(index);
				}
				// put row value to row map
				String rowValue = valArr[index];
				if (!Commons.isBlankOrEmpty(rowValue)) {
					rowValue.replaceAll(Constants.JQL_SEP_VALUE, sepValue);
				}
				rowMap.put(key, rowValue);
			}
		}

		return results;
	}

	/**
	 * Filter results of jql command using xpath as filter
	 * 
	 * @param          command: jql command
	 * @param          filter: using xPath, Ex: //PROVINCE[text()='HA-NOI']
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 * @throws XPathExpressionException
	 */
	public List<Map<String, String>> jqlListFilter(String command, String filter, String sepValue)
			throws JRemoteException, XPathExpressionException {
		List<Map<String, String>> jqlLst = jqlList(command, sepValue);
		JQLResultListFilter jqlFilter = new JQLResultListFilter(jqlLst, filter, sepValue);
		return jqlFilter.getFilterList();
	}

	/**
	 * 1. Get results of jql command using Subroutine 2. Filter results xpath as
	 * filter
	 * 
	 * @param command
	 * @param filter
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	public List<Map<String, String>> jqlListFilterSubroutine(String command, String filter, String sepValue)
			throws JRemoteException, XPathExpressionException, UnsupportedEncodingException {
		List<Map<String, String>> jqlLst = jqlListSubroutine(command, sepValue);
		JQLResultListFilter jqlFilter = new JQLResultListFilter(jqlLst, filter, sepValue);
		return jqlFilter.getFilterList();
	}

	/**
	 * Call T24 Subroutine & get output parameter
	 * 
	 * @param subr: name of Subroutine
	 * @param params: parameter of Subroutine
	 * @param outIndex: index of output parameter if outIndex == 0 => outIndex =
	 *        last index of params
	 * @return: value of output parameter
	 */
	public String callSubroutine(String subr, String[] params, int outIndex) throws JRemoteException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(bos, "UTF-8");
			jconn.setTerminalOutputWriter(writer);

			JSubroutineParameters subroutineParams = new JSubroutineParameters();
			if (params != null && params.length > 0) {
				for (String prm : params) {
					subroutineParams.add(new JDynArray(prm));
				}
			}

			subroutineParams = jconn.call(subr, subroutineParams);

			if (subroutineParams != null) {
				String res_all = subroutineParams.toString();
				// System.out.println("Results: " + res_all);
				if (subroutineParams.size() > outIndex) {
					return subroutineParams.get(outIndex).toString();
				}
			} else {
				System.out.println("No data found!");
			}

			// jconn.close();
			// jconn = null;
		} catch (JRemoteException e) {
			System.out.println("JRemoteException: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "";
	}

	public String callSubroutine(String subr, String[] params) throws JRemoteException {
		int outIndex = 0;
		if (params != null && params.length > 0) {
			outIndex = params.length - 1;
		}

		return callSubroutine(subr, params, outIndex);
	}

	/**
	 * Do jQL ENQ command using Subroutine
	 * 
	 * @param enquiryCmd
	 * @return
	 * @throws JRemoteException
	 * example:
	 * ENQ SDTK.LCN WITH CUSTOMER EQ 29692453
	 * ENQ CUSTOMER.POSITION WITH CUSTOMER.NO EQ 32532179
	 */
	public List<Map<String, String>> enquiryList(String enquiryCmd) throws JRemoteException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");
		OFSEnquiryCommand enqCommand = buildEnquiryCommand(enquiryCmd);
		if (enqCommand.getOfsMessage().trim().isEmpty()
				|| enqCommand.getOfsMessage().equals(Constants.NULL_TEXT_INDICATOR)) {
			// input enquiry command error => return null
			return null;
		}

		// ****** call subroutine ******
		String[] enqPrms = new String[] { enqCommand.getOfsMessage(), "" }; // AUTO.ENQUIRY.SELECT.SUB(ENQ.COMMAND,
																			// ENQ.RESULT)
		String enqResultStr = callSubroutine(Constants.ENQ_SELECT_SUBRT, enqPrms);
		// check results
		if (enqResultStr.contains(Constants.ENQ_NO_RECORD_MESSAGE))
			return null; // no results
		// get list map from result
		List<Map<String, String>> enqResults = parserEnquiryResult(enqResultStr);
		if (enqResults == null || enqResults.size() == 0)
			return null; // no result

		// ****** check and filter list ******
		if (enqCommand.getFilterList() == null || enqCommand.getFilterList().size() == 0)
			return enqResults; // no filter
		// check valid filter
		if (!enqResults.get(0).keySet().containsAll(enqCommand.getFilterList())) {
			// not valid filter => return result without filter
			return enqResults;
		}
		// filter list
		List<Map<String, String>> filterResults = new LinkedList<Map<String, String>>();
		for (Map<String, String> mapRow : enqResults) {
			Map<String, String> filterRow = new CaseInsensitiveMap<>();
			for (String key : enqCommand.getFilterList()) {
				filterRow.put(key, mapRow.get(key));
			}
			filterResults.add(filterRow);
		}
		return filterResults;
	}

	/**
	 * 1. Get results of enquiry jql command using Subroutine 2. Filter results
	 * xpath as filter
	 * 
	 * @param enquiryCmd
	 * @param filter
	 * @param sepValue
	 * @return
	 * @throws JRemoteException
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	public List<Map<String, String>> enquiryListWithFilter(String enquiryCmd, String filter, String sepValue)
			throws JRemoteException, XPathExpressionException, UnsupportedEncodingException {
		List<Map<String, String>> jqlLst = enquiryList(enquiryCmd);
		JQLResultListFilter jqlFilter = new JQLResultListFilter(jqlLst, filter, sepValue);
		return jqlFilter.getFilterList();
	}

	/**
	 * Create OFS enquiry message from jQL ENQ command
	 * 
	 * @param enquiryCmd Input1: SDTK.LCN,CUSTOMER:EQ=12345,SECTOR:EQ=3456 Output1:
	 *                   ENQUIRY.SELECT,,/,SDTK.LCN,CUSTOMER:EQ=12345,SECTOR:EQ=3456
	 *                   Input2: ENQ SDTK.LCN WITH CUSTOMER EQ 12345 AND SECTOR EQ
	 *                   3456 ONLY STK CUSTOMER Output2:
	 *                   ENQUIRY.SELECT,,/,SDTK.LCN,CUSTOMER:EQ=12345,SECTOR:EQ=3456
	 * @return
	 */
	private OFSEnquiryCommand buildEnquiryCommand(String enquiryCmd) {
		OFSEnquiryCommand enqCommand = new OFSEnquiryCommand(Constants.NULL_TEXT_INDICATOR, null);
		if (enquiryCmd.trim().toUpperCase().startsWith("ENQUIRY.SELECT")) { // Input 1
			if (enquiryCmd.matches(".+:[A-Za-z]+=.+")) {
				// no condition => //wrong format
				enqCommand.setOfsMessage(enquiryCmd);
			}
			return enqCommand;
		}
		if (enquiryCmd.matches(".+:[A-Za-z]+=.+")) {
			enqCommand.setOfsMessage("ENQUIRY.SELECT,,/," + enquiryCmd);
			return enqCommand;
		}
		if (enquiryCmd.trim().toUpperCase().startsWith("ENQ ")) { // Input 2
			// create OFS enquiry message
			StringBuilder ofsEnq = new StringBuilder("ENQUIRY.SELECT,,/,"); // ENQUIRY.SELECT,,/,
			List<String> elementList = Arrays.asList(enquiryCmd.trim().split("\\s+"));
			if (elementList.size() < 3)
				return null; // wrong format
			String enqName = elementList.get(1);
			ofsEnq.append(enqName); // ENQUIRY.SELECT,,/,SDTK.LCN

			// get condition list and filter list
			List<String> conditionList = null;
			int withIdx = 2;
			if (!elementList.get(withIdx).toUpperCase().equals("WITH"))
				return enqCommand; // wrong format
			int onlyIdx = getIndexOfList(elementList, "ONLY");
			if (onlyIdx == elementList.size() - 1)
				return enqCommand; // wrong format (ONLY keyword can't be last index)
			if (onlyIdx > withIdx) {
				conditionList = elementList.subList(withIdx + 1, onlyIdx);
				enqCommand.setFilterList(elementList.subList(onlyIdx + 1, elementList.size()));
			} else {
				int andIdx = getLastIndexOfList(elementList, "AND");
				if (andIdx >= elementList.size() - 3)
					return enqCommand; // wrong format
				if (andIdx > 0) {
					conditionList = elementList.subList(withIdx + 1, andIdx + 4);
					if (andIdx + 4 < elementList.size() - 1) {
						enqCommand.setFilterList(elementList.subList(andIdx + 4, elementList.size()));
					} // else: out index
				} else {
					conditionList = elementList.subList(withIdx + 1, elementList.size());
				}
			}

			// format condition
			for (int idx = 0; idx < conditionList.size(); idx += 4) {
				ofsEnq.append(",").append(conditionList.get(idx)).append(":").append(conditionList.get(idx + 1))
						.append("=").append(conditionList.get(idx + 2)); // ENQUIRY.SELECT,,/,SDTK.LCN,CUSTOMER:EQ=12345,SECTOR:EQ=3456
			}

			enqCommand.setOfsMessage(ofsEnq.toString());
		}
		return enqCommand;
	}

	private int getIndexOfList(List<String> lst, String sKey) {
		for (int idx = 0; idx < lst.size(); idx++) {
			if (lst.get(idx).equalsIgnoreCase(sKey))
				return idx;
		}
		return -1;
	}

	private int getLastIndexOfList(List<String> lst, String sKey) {
		for (int idx = lst.size() - 1; idx >= 0; idx--) {
			if (lst.get(idx).equalsIgnoreCase(sKey))
				return idx;
		}
		return -1;
	}

	private List<Map<String, String>> parserEnquiryResult(String enqResult) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		if (enqResult == null || enqResult.trim().isEmpty() || enqResult.trim().equals(Constants.NULL_TEXT_INDICATOR))
			return results;

		// Split key from list
		Pattern patternKey = Pattern.compile(Constants.REGEX_ENQ_KEY_LIST);
		Matcher matcherKey = patternKey.matcher(enqResult);
		if (!matcherKey.find())
			return results; // not found key row
		String keyRowStr = matcherKey.group(1); // list key (column)
		// remove row key in result
		enqResult = enqResult.substring(matcherKey.end(0));

		// get all key
		patternKey = Pattern.compile(Constants.REGEX_ENQ_KEY);
		matcherKey = patternKey.matcher(keyRowStr);

		List<String> keys = new LinkedList<String>();
		while (matcherKey.find()) {
			keys.add(matcherKey.group(1).trim().replace(' ', '.')); // replace ' ' with '.'
		}
		// get all row
		String[] rowList = enqResult.split(Constants.REGEX_ENQ_SEP_ROW);
		// get value in row

		for (int rowIdx = 0; rowIdx < rowList.length; rowIdx++) {
			String row = rowList[rowIdx];
			String[] valList = row.split("\t");

			// add key value to map
			Map<String, String> mapRow = new CaseInsensitiveMap<>();

			for (int keyIdx = 0; keyIdx < keys.size(); keyIdx++) {
				// get value
				String value = keyIdx < valList.length
						? valList[keyIdx].replaceAll("^\"", "").replaceAll("\"$", "").trim()
						: Constants.NULL_TEXT_INDICATOR;
				if (value.isEmpty()) {
					value = Constants.NULL_TEXT_INDICATOR;
				}
				mapRow.put(keys.get(keyIdx), value);
			}
			// add map to results
			results.add(mapRow);
		}

		return results;
	}

	/**
	 * Call to subroutine
	 * 
	 * @param          subName: AUTO.TEST.SUB.TCB
	 * @param inputJQL
	 * @return Map value with keys are the list column
	 * @throws JSubroutineNotFoundException
	 * @throws JRemoteException
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> calljQLSubroutine(String subName, String inputJQL)
			throws JSubroutineNotFoundException, JRemoteException, UnsupportedEncodingException {
		if (jconn == null && !connect())
			throw new JRemoteException("Not connect T24");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(bos, "UTF-8");
		jconn.setTerminalOutputWriter(writer);

		// build parameter for subroutine
		String inputToSub = buildParamSub(inputJQL);
		JSubroutineParameters subroutineParams = new JSubroutineParameters();
		subroutineParams.add(new JDynArray(inputToSub));
		subroutineParams.add(new JDynArray());// Output parameter

		subroutineParams = jconn.call(subName == null ? Constants.T24_SUBROUTINE_DEFAULT : subName, subroutineParams);

		Map<String, String> results = null;
		if (subroutineParams != null) {
			results = new CaseInsensitiveMap<>();
			// Get output parameter
			JDynArray dynArray = subroutineParams.get(1);
			for (int i = 1; i <= dynArray.getNumberOfAttributes(); i++) {
				String values = dynArray.get(i);
				String value = values.substring(values.indexOf(":") + 1, values.length());
				String key = values.substring(0, values.indexOf(":"));
				results.put(key, value);
			}
			System.out.println("Results: " + results.toString());
		} else {
			// System.out.println("No data found!");
		}

		return results;
	}

	/**
	 * build parameter input to subroutine: Table Name!@List Column
	 * Select!@Condition Query
	 * 
	 * @param input
	 * @return
	 */
	private String buildParamSub(String input) {
		String[] splitInput = input.split("WITH");
		if (splitInput.length == 1) {
			return input;
		}

		String table = splitInput[0].trim();
		String withStr = splitInput[1].trim();

		String[] splitTable = table.split(" ");
		String tableName = splitTable[1];

		String listColumn = "";
		String listWith = "";
		if (splitTable.length > 2) {
			listColumn = table.substring(table.indexOf(" ") + 1 + tableName.length() + 1);
			listWith = withStr;
		} else {
			String lastWithValue = lastWithValue(withStr);
			int lastWithValIndex = withStr.lastIndexOf(lastWithValue);
			// get with statement
			listWith = withStr.substring(0, lastWithValIndex + lastWithValue.length()).trim();
			// get column statement
			listColumn = withStr.substring(lastWithValIndex + lastWithValue.length()).replaceFirst("(?i)ONLY", "")
					.trim();
			// check if have BY keyword?
			if (listColumn.toUpperCase().contains(" BY ")) {
				int byIndex = listColumn.toUpperCase().lastIndexOf(" BY ");
				String byStatement = listColumn.substring(byIndex).trim();
				listColumn = listColumn.substring(0, byIndex).trim();
				listWith = listWith + " " + byStatement;
			}
		}

		return tableName + "!@" + listColumn.trim() + "!@" + listWith;
	}

	private Map<String, String> getCompareFuncMap() {
		if (compareFuncMap == null) {
			compareFuncMap = new HashMap<>();
			compareFuncMap.put("EQ", "Equal");
			compareFuncMap.put("RG", "In Range");
			compareFuncMap.put("LT", "Letter Than");
			compareFuncMap.put("GT", "Greater Than");
			compareFuncMap.put("NE", "Not Equal");
			compareFuncMap.put("LIKE", "Like");
			compareFuncMap.put("UNLIKE", "UnLike");
			compareFuncMap.put("LE", "Letter Or Equal");
			compareFuncMap.put("GE", "Greater Or Equal");
			compareFuncMap.put("NR", "Not In Range");
		}
		return compareFuncMap;
	}

	/**
	 * EQ = equal, RG = in range, LT = letter than, GT = greater than, NE = not
	 * equal LIKE, UNLIKE, LE = letter or equal, GE = greater or equal, NR = not in
	 * range
	 * 
	 * @param withStr
	 * @return
	 */
	private String lastWithValue(String withStr) {
		Map<String, String> compareFuncMap = getCompareFuncMap();

		String lastWithValue = "";
		String[] arrayWithStr = withStr.split("\\s+");
		for (int i = arrayWithStr.length - 1; i > 0; i--) {
			String value = arrayWithStr[i];
			if (value.toUpperCase().equals("ONLY")) {
				lastWithValue = arrayWithStr[i - 1];
				break;
			}
			if (compareFuncMap.containsKey(value)) {
				break;
			} else {
				lastWithValue = value;
			}
		}

		return lastWithValue;
	}

	public void disconnect() {
		if (jconn == null) {
			_isConnected = false;
			return;
		}
		try {
			jconn.close();
			jconn = null;
		} catch (JRemoteException e) {
			e.printStackTrace();
		} finally {
			_isConnected = false;
		}

	}

	public static void disconnectAll() {
		if (singletonT24 == null)
			return;
		singletonT24.disconnect();
	}

	/**
	 * Query and get List<Map<String, String>> result
	 * 
	 * @author anhptn14
	 * @param jql: full jql
	 * @return
	 */
	public List<Map<String, String>> queryT24(String jql, String... title) {
		List<Map<String, String>> result = null;
		RemoteCommand t24remote = RemoteCommand.getDefaultConnect();
		try {
			result = t24remote.jqlList(jql, Constants.SEP_VALUE);
			System.out.println("JQL: " + jql);
			if (title.length > 0) {
				Serenity.recordReportData().withTitle(title[0]).andContents(jql);
				String stringResult = "";
				for (Map<String, String> map : result) {
					stringResult = stringResult + map.keySet().toString() + ":" + map.values() + "; ";
				}
				Serenity.recordReportData().withTitle("Result").andContents(stringResult);
				System.out.println(stringResult);
			}
		} catch (JRemoteException e) {
			e.printStackTrace();
		}
		t24remote.disconnect();
		return result;

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (singletonT24 == null)
			return;
		if (singletonT24.isConnected()) {
			singletonT24.disconnect();
		}
	}
}
