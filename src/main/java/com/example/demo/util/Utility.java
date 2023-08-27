package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Utility {
	
	public static boolean validateAccType(final String accType) {
		return accType.matches("^[dwDW]+$");
	}

	public static boolean validateAmount(final String amount) {
		return amount.matches("^(?![.0]*$)\\d+(?:\\.\\d{2})?$");
	}
	
	public static boolean validateDate(final String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date formatedDate = sdf.parse(date);
		return sdf.format(formatedDate).equals(date) && new Date().after(formatedDate);
	}
	
	public static boolean validateInterestRate(final String interestRate) {
		return interestRate.matches("^(?!0*(\\.0+)?$)([0-9]|[1-9][0-9])(\\.[0-9]{1,2})?$");
	}
	
	
	public static boolean validateMonth(final String month) {
		return month.matches("(^0?[1-9]$)|(^1[0-2]$)");
	}
	
	
	public static int getLastDayOfMonth(int month) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, month);
	    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	
	public static int getCurrentYear() {
		Calendar instance = Calendar.getInstance();
        return instance.get(Calendar.YEAR);
	}
	
	
	public static int getLengthOfYear() {
		return LocalDate.now().lengthOfYear();
	}
	
	
	public static Date convertStringToDate(final String date) throws ParseException {
		return new SimpleDateFormat("yyyyMMdd").parse(date);
	}
}
