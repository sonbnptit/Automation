package com.tcb.auto.utils;

public class Constants {

    public final static String SCRIPT_BINDING_MAP = "BINDING_MAP";

    public final static String SYMB_COMMA = "&com";
	public final static String SYMB_QUOTATION = "&quot";
	public final static String SYMB_DOLLAR = "&dol";
	public final static String SYMB_LEFT_PARENTHESIS = "&lparen";
	public final static String SYMB_RIGHT_PARENTHESIS = "&rparen";
	
	public final static String SYMB_COMMA_CAP = "&COM";
	public final static String SYMB_QUOTATION_CAP = "&QUOT";
	public final static String SYMB_DOLLAR_CAP = "&DOL";
	public final static String SYMB_LEFT_PARENTHESIS_CAP = "&LPAREN";
	public final static String SYMB_RIGHT_PARENTHESIS_CAP = "&RPAREN";
    
	public final static String REGEX_PROP_INJECTION = "\\{\\{([^\\{\\}]*)\\}\\}";
	public final static String REGEX_JS_INJECTION = "(?s)(func\\w+)\\(([^\\(\\)]*)\\)";
	public final static String REGEX_JS_CONTAIN = "(?s).*func\\w+\\([^\\(\\)]*\\).*";
	public final static String REGEX_JS_PARAMS_SEP = "(?s)\\s*,\\s*";
	/**
	 * Common Const
	 */
	public final static String NULL_TEXT_INDICATOR = "<<null>>";
	public final static String SEP_1 = ";";
	public final static String SEP_2 = ",";
	public final static String SEP_ROW = "<==>";
	public final static String SEP_COL = "<>";
	public final static String SEP_ATTR = "#";
	public final static String SEP_VALUE = "*";
	public final static String SEP_SUB_VALUE = "%";
	public final static String SEP_ASSIGN = ":=";

	/**
	 * T24 Const
	 */
	public static final String T24_JQL_SERVER = "T24.SERVER";
	public static final String T24_JQL_AUTH_UID = "T24.AUTH.UID";
	public static final String T24_JQL_AUTH_PWD = "T24.AUTH.PWD";
	public static final String T24_JQL_REMOTE_PORT = "T24.REMOTE.PORT";

	public final static String ENQ_SELECT_SUBRT = "AUTO.ENQUIRY.SELECT.SUB";
	public final static String ENQ_SELECT_PROGRAM = "AUTO.ENQUIRY.SELECT.PROC";
	public final static String T24_OFS_SOURCE = "GENERIC.OFS.PROCESS";
	public final static String JQL_TSS_COMMAND = "tSS %s";
	public final static String ENQ_NO_RECORD_MESSAGE = "No records were found";
	public final static String T24_SET_COMPANY_DATE_SUBRT = "SET.DATE.COMPANY.SUB";

	public final static String T24_SUBROUTINE_DEFAULT = "AUTO.TEST.SUB.TCB";
	public final static String JQL_SEP_ROW = "<>";
	public final static String JQL_SEP_COL = "<|>";
	public final static String JQL_SEP_VALUE = "<#>";

	public final static int DEFAULT_T24_JSH_REMOTE_PORT = 8039;
	public final static String DEFAULT_T24_JSH_COL_FORMAT = "COL_%03d";

	/* jQL Enquiry */
	public final static String REGEX_ENQ_RESPONSE = "RESPONSE:",
			REGEX_ENQ_KEY_LIST = "\\,([A-Za-z0-9:_\\.\\/\\s]+)\\s*,", REGEX_ENQ_KEY = "\\/?([A-Za-z0-9\\._\\s]+)::",
			REGEX_ENQ_SEP_ROW = "\\\",\\\"", REGEX_ENQ_VALUE = "\\\"([^\\\"]*)\\\"";

	/* XML CONST for jQL */
	public final static String XML_DATA_TAG = "data", XML_ROW_TAG = "row", XML_ATTRIBUTES_TAG = "attributes";
	public final static String XML_ATTR_MULTIVALUED = "multivalued", XML_ATTR_FILTERED = "filtered";

	public final static String EXCEL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String TEST_PASS = "PASS", TEST_FAILED = "FAILED";
    public static final String ERR_NOT_CONNECT_T_24 = "Not connect T24";

	/**
	 * variable for srt model
	 */
	public static final String TEST_RESPONE_FIRST="(?s)\\sPAGE\\s+\\d+\\s\\d{2}:\\d{2}:\\d{2}\\s+\\d+\\s+\\w+\\s+\\d{4}\\s*\n";

	public static final String TEST_RESPONE_CONTROL="[2J[H";
	public static final String JSH_RESPONE_FIRST="(?s)\\sPAGE\\s+\\d+\\s\\d{2}:\\d{2}:\\d{2}\\s+\\d+\\s+\\w+\\s+\\d{4}\\s*\n";
	public static final String JSH_RESPONE_LIST_CHARATER="L[0K[D[CI[0K[D[CS[0K[D[CT[0K[D[C [0K[D[CF[0K[D[CB[0K[D[CN[0K[D[CK[0K[D[C.[0K[D[CT[0K[D[CO[0K[D[CW[0K[D[CN[0K[D[C.[0K[D[CT[0K[D[CC[0K[D[CB[0K[D[C [0K[D[CW[0K[D[CI[0K[D[CT[0K[D[CH[0K[D[C [0K[D[CP[0K[D[CR[0K[D[CO[0K[D[CV[0K[D[CI[0K[D[CN[0K[D[CC[0K[D[CE[0K[D[C.[0K[D[CI[0K[D[CD[0K[D[C [0K[D[CE[0K[D[CQ[0K[D[C [0K[D[CH[0K[D[CA[0K[D[C-[0K[D[CN[0K[D[CO[0K[D[CI[0K[D[C [0K[D[C*[0K[D[CT[0K[D[CI[0K[D[CN[0K[D[CH[0K[D[C-[0K[D[CK[0K[D[CH[0K[D[CA[0K[D[CC[0K[D[C*[0K[D[C [0K[D[CA[0K[D[CN[0K[D[CD[0K[D[C [0K[D"+
			"L[0K[D[CE[0K[D[CV[0K[D[CE[0K[D[CL[0K[D[C [0K[D[CE[0K[D[CQ[0K[D[C [0K[D[C2[0K[D[C [0K[D[CN[0K[D[CA[0K[D[CM[0K[D[CE[0K[D[C [0K[D[CB[0K[D[CY[0K[D[C [0K[D[C@[0K[D[CI[0K[D[CD[0K[D[C [0K[D[C([0K[D[CN[0K[D[C"+
			"[2J[H";

	public static final String DB_CASE_PASS = "P", DB_CASE_FAILED = "F", DB_CASE_UNEXECUTE = "U";
	public static final String DB_LISTCASEID = "LISTCASEID";
	public static final String DB_BASEFLOWID = "BASEFLOWID";

	public static final String MAVEN_TEST_DATA_FILE = "testDataFile", MAVEN_TEST_DATA_SHEET = "testDataSheet";
	public static final String MAVEN_TEST_DATA_CONNECTION = "testDataConnection", MAVEN_TEST_DATA_TABLE = "testDataTable", MAVEN_TEST_DATA_QUERY = "testDataQuery";
	public static final String FILE_UPLOAD_AUTOIT="%cd%\\lib\\UploadFile.exe";

	public static final String BOOTSTRAP_SCRIPT = "Bootstrap";
	public static final String IS_RENAME_REPORT = "RenameReport";
	public static final String IS_CREATE_ONE_PAGE_REPORT = "OnePageReport";
	public static final String IS_CREATE_PDF_REPORT = "PDFReport";
	public static final String IS_CREATE_JSON_REPORT = "JSonReport";
}
