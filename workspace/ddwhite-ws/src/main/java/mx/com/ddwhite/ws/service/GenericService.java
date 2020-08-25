package mx.com.ddwhite.ws.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericService<E> {
	
	protected String currentDateToString(String format) {
		return dateToString(new Date(), format);
	}
	
	protected String dateToString(Date date, String format) {
		if( date == null || format.isEmpty())
			return null;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

}
