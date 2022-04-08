package com.tcb.auto.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

import com.gargoylesoftware.htmlunit.javascript.host.intl.DateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class TimeController {

	public static final String ADD_DATE_TYPE_YEAR = "year";
	public static final String ADD_DATE_TYPE_MONTH = "month";
	public static final String ADD_DATE_TYPE_DAY = "day";
	public static final String ADD_DATE_TYPE_MIN = "min";
	public static int WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR = 2;
	public static int WAITING_TIME_WHEN_EXPECT_OBJECT_NOT_APPEAR_MILLIS = 200;
	/**
	 * Number of <b>seconds</b> the script waiting for a window present
	 */
	public static final int WAIT_FOR_WINDOWS_PRESENT = 60;
	/**
	 * SYSTEM_DELAY in millisecond. <br>
	 * Ex: after click on a button, then <i>SYSTEM_DELAY</i> <b>millisecond</b>
	 * after that the system start loading
	 */
	public static final int SYSTEM_DELAY = 500;

	/**
	 * Hàm lấy ngày cách value ngày/tháng/năm so với ngày hiện tại value > 0 lấy
	 * thời điểm trong tương lại, value < 0 lấy thời điểm trong quá khứ
	 * 
	 * @param type: theo ngày, tháng, năm
	 * @param value: số ngày/tháng/năm cách thời điểm hiện tại muốn lấy.
	 * @param format: định dạng hiển thị của ngày trả về
	 * @return
	 */
	public static String getDay(String type, int value, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		Date date2 = null;
		switch (type) {
		case ADD_DATE_TYPE_MIN:
			cal.add(Calendar.MINUTE, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_DAY:
			cal.add(Calendar.DATE, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_MONTH:
			cal.add(Calendar.MONTH, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_YEAR:
			cal.add(Calendar.YEAR, value);
			date2 = cal.getTime();
			break;
		default:
			break;
		}
		return dateFormat.format(date2);
	}

	/**
	 * tính ngày theo ngày có sẵn
	 *
	 * @param type:ngày, tháng, năm
	 * @param oldDate
	 * @param value
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String addDay(String type, String oldDate, int value, String format) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(oldDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date date2 = null;
		switch (type) {
		case ADD_DATE_TYPE_MIN:
			cal.add(Calendar.MINUTE, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_DAY:
			cal.add(Calendar.DATE, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_MONTH:
			cal.add(Calendar.MONTH, value);
			date2 = cal.getTime();
			break;
		case ADD_DATE_TYPE_YEAR:
			cal.add(Calendar.YEAR, value);
			date2 = cal.getTime();
			break;
		default:
			break;
		}
		return dateFormat.format(date2);
	}

	/**
	 * return today
	 */
	public static String getToday(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}


	/**
	 * Format string date to other format
	 * 
	 * @param date
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static String funcFormatDate(String date, String inFormat, String outFormat) {
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(outFormat);
		LocalDateTime jdate = funcGetLocalDateTime(date, inFormat);
		return formatterOut.format(jdate);
	}

	public static String funcFormatDate(LocalDateTime date, String outFormat) {
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(outFormat);
		return formatterOut.format(date);
	}

	public static LocalDateTime funcGetLocalDateTime(String date, String inFormat) {
		DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern(inFormat);
		TemporalAccessor parsed = formatterIn.parseBest(date, LocalDateTime::from, LocalDate::from);
		LocalDateTime jdate;
		if (parsed instanceof LocalDateTime) {
			jdate = (LocalDateTime) parsed;	// it's a LocalDateTime
		} else {
			jdate = ((LocalDate) parsed).atTime(LocalTime.MIDNIGHT); // it's a LocalDate
		}
		return jdate;
	}

	/**
	 * Get current data in format Get date today with format
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static LocalDateTime funcGetToday(String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		LocalDateTime jdate = LocalDateTime.now();
		return jdate;
	}

	/**
	 *
	 * @param date1    ex: 2019-07-04
	 * @param date2    ex: 2019-07-15
	 * @param diffType ex: day, month, year or d, m, y
	 * @return => 11
	 */
	public static long funcGetDiffDates(LocalDateTime date1, LocalDateTime date2, String diffType) {

		if (diffType.toLowerCase().equals("day") || diffType.toLowerCase().equals("d")) {
			long days = ChronoUnit.DAYS.between(date1, date2);
			return days;
		}
		if (diffType.toLowerCase().equals("month") || diffType.toLowerCase().equals("m")) {
			long months = ChronoUnit.MONTHS.between(date1, date2);
			return months;
		}
		if (diffType.toLowerCase().equals("year") || diffType.toLowerCase().equals("y")) {
			long years = ChronoUnit.MONTHS.between(date1, date2);
			return years;
		}
		return 0;
	}

	/**
	 * get different date
	 * @param dt1 ex: 2019-07-04
	 * @param dt2 ex: 2019-07-15
	 * @param diffType ex: day, month, year or d, m, y
	 * @param dateFormat ex: yyyy-MM-dd
	 * @return => 11
	 */
	public static long funcGetDiffDates(String dt1, String dt2, String diffType, String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		LocalDateTime date1 = LocalDateTime.parse(dt1, formatter);
		LocalDateTime date2 = LocalDateTime.parse(dt2, formatter);

		if(diffType.toLowerCase().equals("day") || diffType.toLowerCase().equals("d")){
			long days = ChronoUnit.DAYS.between(date1, date2);
			return days;
		}
		if(diffType.toLowerCase().equals("month") || diffType.toLowerCase().equals("m")){
			long days = ChronoUnit.MONTHS.between(date1, date2);
			return days;
		}
		if(diffType.toLowerCase().equals("year") || diffType.toLowerCase().equals("y")){
			long days = ChronoUnit.MONTHS.between(date1, date2);
			return days;
		}
		throw new DateTimeException("Invalid different type");
	}

	public static LocalDateTime funcPlusDate(LocalDateTime date, int iPlusAmt, String plusType) {
		LocalDateTime jdate = date;
		if (plusType.toLowerCase().equals("day") || plusType.toLowerCase().equals("d")) {
			jdate = date.plusDays(iPlusAmt);
		}
		if (plusType.toLowerCase().equals("month") || plusType.toLowerCase().equals("m")) {
			jdate = date.plusMonths(iPlusAmt);
		}
		if (plusType.toLowerCase().equals("year") || plusType.toLowerCase().equals("y")) {
			jdate = date.plusYears(iPlusAmt);
		}
		return jdate;
	}

	public static String funcPlusDate(String date, String plusAmt, String plusType, String dateFormat){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

		int iPlusAmt = Integer.parseInt(plusAmt);
		LocalDateTime jdate;
		if(plusType.toLowerCase().equals("day") || plusType.toLowerCase().equals("d")){
			jdate = LocalDateTime.parse(date, formatter).plusDays(iPlusAmt);
			return formatter.format(jdate);
		}
		if(plusType.toLowerCase().equals("month") || plusType.toLowerCase().equals("m")){
			jdate = LocalDateTime.parse(date, formatter).plusMonths(iPlusAmt);
			return formatter.format(jdate);
		}
		return date;
	}

	/**
	 * Get last day of month
	 * 
	 * @param date
	 * @param dayofmt : day in month
	 * @return
	 */
	public static LocalDateTime funcGetDayOfMonth(LocalDateTime date, String dayofmt) {

		LocalDate dateTmp = date.toLocalDate();
		if (dayofmt.toLowerCase().equals("eom")) {
			LocalDateTime lastDay = date.withDayOfMonth(date.getMonth().length(dateTmp.isLeapYear()));
			return lastDay;
		}
		if (dayofmt.matches("\\d+")) {
			int dayVal = Integer.parseInt(dayofmt);
			LocalDateTime dayOfMonth = date.withDayOfMonth(dayVal);
			return dayOfMonth;
		}
		// invalid dayofmt => return input date
		return date;
	}

	/**
	 * return curernt date or time in a defined format
	 * 
	 * @author anhptn14
	 * @param format
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
	/*public static String getCurrentDateTime(String currenDate,String inFomat,String outFormat) {
		DateFormat inDateFormat = new SimpleDateFormat(inFomat);
		DateFormat outDateFormat = new SimpleDateFormat(outFormat);
		Date date = inDateFormat.parse(currenDate);
		Date date = new Date();
		return dateFormat.format(date);
	}*/


	public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";

	public static boolean is_weekend(String currentDate, String fomart) {
		LocalDateTime jdate = funcGetLocalDateTime(currentDate, fomart);
        if (jdate.getDayOfWeek() == DayOfWeek.SATURDAY || jdate.getDayOfWeek() == DayOfWeek.SUNDAY)
            return true;
        return false;
    }
    public static int dayOfWeek(String currentDate,String fomart){
		LocalDateTime jdate = funcGetLocalDateTime(currentDate, fomart);
		int value = jdate.getDayOfWeek().getValue()+1;
		if(value==8) value =1;
		return value;
	}
    
    public static String convertValueToDate(String iVal, String fornat) {
    	 double value = Double.parseDouble(iVal);
    	 Date date = new Date(0);
    	 Calendar cal = new GregorianCalendar();
    	 cal.setTime(date);
    	 cal.add(Calendar.DATE, (int) value - 25569);
    	 return new SimpleDateFormat(fornat).format(cal.getTime());
    }

    @Test
    public void TestDateTime() throws Exception{
        String initDate = "26/11/2019 08:56:12";

       /* String date1 = TimeController.funcFormatDate(initDate, "dd/MM/yyyy hh:mm:ss", "yyyy/MM/dd");
        String date2 = TimeController.funcPlusDate(date1, "10", "d", "yyyy/MM/dd");
        date2 = TimeController.funcFormatDate(date2, "yyyy/MM/dd", "MM/dd/yyyy");
        String date3 = TimeController.funcFormatDate(date2, "MM/dd/yyyy", "yyyy/MM/dd");
        long diffDay = TimeController.funcGetDiffDates(date1, date3, "d", "yyyy/MM/dd");

        System.out.println("date1: " + date1);
        System.out.println("date2: " + date2);
        System.out.println("date3: " + date3);
        System.out.println("diffDay: " + diffDay);
        Assert.assertEquals("2019/10/19", date1);
        Assert.assertEquals("10/29/2019", date2);
        Assert.assertEquals("2019/10/29", date3);
        Assert.assertEquals(10, diffDay);*/
//       String date2 = TimeController.addDay("day",initDate,30,"YYYY-MM-DD hh:mm:ss");
//       long value = TimeController.getDiffDates(initDate,date2,"dd/MM/yyyy hh:mm:ss");
//		long diffDay = TimeController.funcGetDiffDates(initDate, date2, "d", "dd/MM/yyyy hh:mm:ss");
//       System.out.println("date 2: "+ date2 +" value:"+diffDay);
		System.out.println(funcFormatDate(initDate,"dd/MM/yyyy HH:mm:ss","ddMMyyy HH:mm"));
		System.out.println(funcFormatDate("26/11/2019","dd/MM/yyyy","ddMMyyy"));

		System.out.println(dayOfWeek("01/12/2019","dd/MM/yyyy"));

//		System.out.println(""+getCurrentDateTime("hhmm"));
    }

	/**
	 * Compare two date
	 *
	 * @param beforeStr
	 * @param afterStr
	 * @param formatDate
	 * @return negative if beforeStr greater than afterStr, positive if beforeStr less than afterStr and 0 if beforeStr equal afterStr
	 * @author TuyenNV
	 */
    public static int compareDate(String beforeStr, String afterStr, String formatDate) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(formatDate);
		LocalDate before = LocalDate.parse(beforeStr, format);
		LocalDate after = LocalDate.parse(afterStr, format);
		return after.compareTo(before);
	}
}
