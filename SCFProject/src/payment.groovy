import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


String unescapseParameter(String value){
    return value.replace("&com",",").replace("&lparen","(").replace("&rparen",")");
}

String funcGetGroupRegex(String str, String regex, String groupIdxStr, String asNull = "<<null>>"){
    str = unescapseParameter(str);
    regex = unescapseParameter(regex);
    //System.out.println(str);
    //System.out.println(regex);
    Pattern ptm = Pattern.compile(regex);
    Matcher match = ptm.matcher(str);
    int groupIdx = Integer.parseInt(groupIdxStr);
    if(match.find()) return match.group(groupIdx);
    return asNull;
}

/**
 *
 * @param dt1 ex: 2019-07-04
 * @param dt2 ex: 2019-07-15
 * @param diffType ex: day, month, year or d, m, y
 * @param dateFormat ex: yyyy-MM-dd
 * @return => 11
 */
String funcGetDiffDates(String dt1, String dt2, String diffType="day", String dateFormat="yyyyMMdd") {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    LocalDate date1 = LocalDate.parse(dt1, formatter);
    LocalDate date2 = LocalDate.parse(dt2, formatter);

    if(diffType.toLowerCase().equals("day") || diffType.toLowerCase().equals("d")){
        long days = ChronoUnit.DAYS.between(date1, date2);
        return days.toString();
    }
    if(diffType.toLowerCase().equals('month') || diffType.toLowerCase().equals("m")){
        long days = ChronoUnit.MONTHS.between(date1, date2);
        return days.toString();
    }
    if(diffType.toLowerCase().equals("year") || diffType.toLowerCase().equals("y")){
        long days = ChronoUnit.MONTHS.between(date1, date2);
        return days.toString();
    }
}

/**
 *
 * @param date ex: 2019-07-04 07:00:00
 * @param inFormat ex: yyyyMMdd hh:mm:ss
 * @param outFormat ex: dd-MM-yyy
 * @return => 04-07-2019
 */
String funcJFormatDate(String date, String inFormat = "yyyyMMdd", String outFormat = 'yyyyMMdd'){
    DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern(inFormat);
    DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(outFormat);
    LocalDate jdate = LocalDate.parse(date, formatterIn);
    return formatterOut.format(jdate);
}

/**
 * Calc plus day
 * @param date
 * @param days
 * @param dateFormat
 * @return
 */
String funcPlusDays(String date, String days, String dateFormat = "yyyyMMdd"){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    int pDay = Integer.parseInt(days);
    LocalDate jdate = LocalDate.parse(date, formatter).plusDays(pDay);
    return formatter.format(jdate);
}


String funcPlusDate(String date, String plusAmt, String plusType="day", String dateFormat = "yyyyMMdd"){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

    int iPlusAmt = Integer.parseInt(plusAmt);
    LocalDate jdate = date.isEmpty() ? LocalDate.now() : LocalDate.parse(date, formatter);
    if(plusType.toLowerCase().equals("day") || plusType.toLowerCase().equals("d")){
        jdate = jdate.plusDays(iPlusAmt);
    }
    if(plusType.toLowerCase().equals("month") || plusType.toLowerCase().equals("m")){
        jdate = jdate.plusMonths(iPlusAmt);
    }
    return formatter.format(jdate);
}

/**
 * Get last day of month
 * @param date
 * @param dateFormat
 * @return
 */
String funcGetDayOfMonth(String date, String dayofmt, String dateFormat = "yyyyMMdd"){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    LocalDate jdate = LocalDate.parse(date, formatter);
    if(dayofmt.toLowerCase().equals("eom")){
        LocalDate lastDay = jdate.withDayOfMonth(jdate.getMonth().length(jdate.isLeapYear()));
        return formatter.format(lastDay);
    }
    if(dayofmt.matches("\\d+")){
        int dayVal = Integer.parseInt(dayofmt);
        LocalDate dayOfMonth = jdate.withDayOfMonth(dayVal);
        return formatter.format(dayOfMonth);
    }
    //invalid dayofmt => return input date
    return date;
}

/**
 * Get date today with format
 * @param dateFormat
 * @return
 */
String funcJGetToday(String dateFormat = "yyyyMMdd"){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    LocalDate jdate = LocalDate.now();
    return formatter.format(jdate);
}

/**
 * Get current time with format
 * @param timeFromat
 * @return 
*/
String funcGetTimeToday(String timeFormat = "yyyyMMdd"){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
    LocalDateTime time = LocalDateTime.now();
    return formatter.format(time);
}

String funcFloor(String number){
    double fNo = Double.parseDouble(number);
    fNo = Math.floor(fNo);
    long lNo = (long) fNo;
    return String.valueOf(lNo);
}

String funcGetMoneyString(String moneyList, String comma = ",", String fix = "0"){
    moneyList = moneyList.replaceAll(",","");
    comma = unescapseParameter(comma);
    String format = "%" + comma + "." + fix + "f";

    String[] moneyArr = moneyList.split(";");
    StringBuilder results = new StringBuilder();
    for(String money: moneyArr){
        double fMoney = Double.parseDouble(money);
        if(results.length() > 0) { results.append(";"); }
        results.append(String.format(format, fMoney));
    }
    return results;
}

String funcGetFileName(String filePath){
    File f = new File(filePath);
    return f.getName();
}

String funcSplitList(String list, String start = "0", String end = "-1", String seperate = ";"){
    seperate = unescapseParameter(seperate);
    String[] arrList = list.split(seperate);
    int startIdx = Integer.parseInt(start) < 0 ? 0 : Integer.parseInt(start);
    int endIdx = Integer.parseInt(end) < 0 ? arrList.length : Math.min(arrList.length, Integer.parseInt(end));

    String[] copyArrList = new String[endIdx - startIdx];
    for(int i=startIdx;i<endIdx;i++){
        copyArrList[i-startIdx] = arrList[i];
    }
    return copyArrList.join(seperate);
}

String funcPrintBinding(abc){
    Map<String,String> _valueMap = getBinding().getProperty("BINDING_MAP");
    System.out.println(_valueMap);
    return abc;
}

String funcSubLeft(String input, String strCount){
    int count = Integer.parseInt(strCount);
    return input.substring(0, count);
}

String funcSubRight(String input, String strCount){
    int count = Integer.parseInt(strCount);
    return input.substring(input.length() - count);
}

String funcReplace(String input, String searchText, String replaceText){
    return input.replace(searchText, replaceText);
}

String funcReplaceAll(String input, String searchText, String replaceText){
    return input.replaceAll(searchText, replaceText);
}

String funcRandomNumber(String min, String max){
    int minNo = Integer.parseInt(min)
    int maxNo = Integer.parseInt(max)
    Random r = new Random()
    return String.valueOf(r.nextInt((maxNo - minNo) + 1) + minNo);
}

String funcWarpText(String input, String sCharPerLine, String sMaxLength){
    input=input.replace("&com", ",");
    int charPerLine = Integer.parseInt(sCharPerLine);
    int maxLength = Integer.parseInt(sMaxLength);
    input = input.substring(0, Math.min(maxLength, input.length()));
    StringBuilder output = new StringBuilder();
    int count = 0;
    while (count < input.length()){
        if(count > 0){
            output.append("\r\n");
        }
        output.append(input.substring(count, Math.min(count + charPerLine, input.length())));
        count+= charPerLine
    }
    return output.toString();
}

String funcTextDivide(String input, String sDivide, String sCharPerLine){
    input=input.replace("&com", ",");
    int divide = Integer.parseInt(sDivide);
    int charPerLine = Integer.parseInt(sCharPerLine);
    if(charPerLine >= input.length()) return input;
    float dSize = input.length() / divide;

    StringBuilder output = new StringBuilder();
    for (int count = 0; count < divide; count++){
        if(count > 0){
            output.append("\r\n");
        }
        output.append(input.substring((int)(count * dSize), Math.min((int)((count + 1 ) * dSize), input.length())));
    }
    return output.toString();
}