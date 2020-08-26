package mx.com.ddwhite.ws.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static String currentDateToString(String format) {
		return dateToString(new Date(), format);
	}
	
	public static String dateToString(Date date, String format) {
		if( date == null || format.isEmpty())
			return null;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

}
