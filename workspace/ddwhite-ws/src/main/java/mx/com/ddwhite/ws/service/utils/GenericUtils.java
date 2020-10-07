package mx.com.ddwhite.ws.service.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.com.ddwhite.ws.constants.GeneralConstants;


public class GenericUtils {
	
	/**
	 * Transform current date to string with specific format
	 * @param format to parsing date
	 * @return date string
	 */
	public static String currentDateToString(String format) {
		return dateToString(new Date(), format);
	}
	
	/**
	 * String date with format to date
	 * @param date to convert
	 * @param format to parse
	 * @return strint date
	 */
	public static Date stringToDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			return new Date();
		}  
	}
	
	/**
	 * Param date to string with specific format 
	 * @param date to format
	 * @param format to parsing date
	 * @return date string
	 */
	public static String dateToString(Date date, String format) {
		if( date == null || format.isEmpty())
			return null;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * Plus day number to param date
	 * @param date to add days
	 * @param n days number
	 * @return Date with days incremented
	 */
	public static Date plusDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return calendar.getTime();
	}
	
	public static BigDecimal getValueOfPercentage(Double value) {
		return getValueOfPercentage(BigDecimal.valueOf(value));
	}
	
	public static BigDecimal getValueOfPercentage(BigDecimal value) {
		return value.divide(GeneralConstants.ONE_HUNDER).add(BigDecimal.ONE);
	}
	
	

}
