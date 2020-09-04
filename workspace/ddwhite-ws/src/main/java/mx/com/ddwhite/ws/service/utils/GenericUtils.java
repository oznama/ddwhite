package mx.com.ddwhite.ws.service.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

}
