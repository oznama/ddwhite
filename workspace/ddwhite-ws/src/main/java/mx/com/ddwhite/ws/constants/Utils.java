package mx.com.ddwhite.ws.constants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	
	public static <T> String ExportListToCSV(boolean withHeaders, List<T> source, Class<T> clazz){		
		if(source.isEmpty()) return null;
		
		final StringBuilder builder = new StringBuilder();
		
		final Field[] headers = clazz.getDeclaredFields();
		
		if(withHeaders) {
			for(Field f : headers) {
				if( f.getName().contains("UID") ) continue;
				builder.append(f.getName()).append(GeneralConstants.CSV_SEPARATOR);
			}
			builder.append(GeneralConstants.CSV_EOF);
		}
		
		source.forEach( e -> {
			Method[] methods = e.getClass().getMethods();
			for( Field f: headers ) {
				for(Method m: methods) {
					if( m.getName().contains("UID") ) break;
					if( m.getName().startsWith("get") && (m.getName().length() == (f.getName().length()+3))) {
						if(m.getName().toLowerCase().endsWith(f.getName().toLowerCase())) {
							try {
								Object v = m.invoke(e);
								builder.append(v).append(GeneralConstants.CSV_SEPARATOR);
								break;
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
			builder.append(GeneralConstants.CSV_EOF);
		});
		
		return builder.toString();
	}

}
