package com.vin3s.auto.utils;

public class Constants {

    public final static String SCRIPT_BINDING_MAP = "BINDING_MAP";

    public final static String NULL_TEXT_INDICATOR = "<<null>>";


    public static final String MAVEN_TEST_DATA_FILE = "testDataFile";
    public static final String MAVEN_TEST_DATA_SHEET = "testDataSheet";

    public final static String SEP_1 = ";";
    public final static String SEP_2 = ",";

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

    public static final String BOOTSTRAP_SCRIPT = "Bootstrap";
    public static final String IS_RENAME_REPORT = "RenameReport";
    public static final String IS_CREATE_ONE_PAGE_REPORT = "OnePageReport";
    public static final String IS_CREATE_PDF_REPORT = "PDFReport";
    public static final String IS_CREATE_JSON_REPORT = "JSonReport";

}
