package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static com.example.demo.constant.Constants.*;

public class Utility {
	
	public static boolean validateAccType(final String accType) {
		return accType.matches(ACC_TYPE_REGEX);
	}

	public static boolean validateAmount(final String amount) {
		return amount.matches(AMOUNT_REGEX);
	}
	
	public static boolean validateDate(final String date) throws ParseException {
		boolean isDateValid = false;
		if(date.matches(NUMBER_REGEX) && date.length() == 8) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			Date formatedDate = sdf.parse(date);
			isDateValid = sdf.format(formatedDate).equals(date) && new Date().after(formatedDate);
		}
		
		return isDateValid;
	}
	
	public static boolean validateInterestRate(final String interestRate) {
		return interestRate.matches(INTEREST_RATE_REGEX);
	}
	
	
	public static boolean validateMonth(final String month) {
		return month.matches(MONTH_REGEX);
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
	
}
