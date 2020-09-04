package mx.com.ddwhite.ws.service.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.reports.AccountInput;
import mx.com.ddwhite.ws.reports.AccountOutput;
import mx.com.ddwhite.ws.reports.AccountTotal;

public class ReportUtils {
	
	/**
	 * Get Total Out from List Account Outs
	 * @param outs to totalizing
	 * @return AccountTotal outs
	 */
	public static AccountTotal getTotalOut(List<AccountOutput> outs) {
		final AccountTotal at = new AccountTotal();
		outs.forEach( o -> {
			at.setQuantityTotal( at.getQuantityTotal() + o.getQuantity() );
			at.setAmountTotal( at.getAmountTotal().add(o.getCost()) );
			at.settTotal(at.gettTotal().add(o.getTotal()));
			at.setSbTotal(at.getSbTotal().add(o.getSubTotal()));
			at.setIvaTotal(at.getIvaTotal().add(o.getIva()));
		});
		return at;
	}
	
	/**
	 * Get Total In from List Account Ins
	 * @param ins to totalizing
	 * @return AccountTotal ins
	 */
	public static AccountTotal getTotalIn(List<AccountInput> ins) {
		final AccountTotal at = new AccountTotal();
		ins.forEach( i -> {
			at.setQuantityTotal( at.getQuantityTotal() + i.getQuantity() );
			at.setAmountTotal( at.getAmountTotal().add(i.getPrice()) );
			at.settTotal(at.gettTotal().add(i.getTotal()));
			at.setSbTotal(at.getSbTotal().add(i.getSubTotal()));
			at.setIvaTotal(at.getIvaTotal().add(i.getIva()));
		});
		return at;
	}
	
	/* Constants for reflection */
	private final static String UID = "UID";
	private final static String GET_METHOD = "get";
	
	/**
	 * 
	 * @param <T> Generic class to handling
	 * @param withHeaders boolean
	 * @param e object
	 * @param clazz
	 * @return object to csv string
	 */
	public static <T> String ExportObjectToCSV(boolean withHeaders, T e, Class<T> clazz){
		if(e == null) return "";
		final StringBuilder builder = new StringBuilder();
		final Field[] headers = clazz.getDeclaredFields();
		builderHeaders(withHeaders, builder, headers);
		builderContent(e, UID, GET_METHOD, builder, headers);
		return builder.toString();
	}
	
	/**
	 * 
	 * @param <T> Generic class to handling
	 * @param withHeaders boolean
	 * @param source list of objects
	 * @param clazz
	 * @return list to csv string
	 */
	public static <T> String ExportListToCSV(boolean withHeaders, List<T> source, Class<T> clazz){
		if(source.isEmpty()) return "";
		final StringBuilder builder = new StringBuilder();
		final Field[] headers = clazz.getDeclaredFields();
		builderHeaders(withHeaders, builder, headers);
		source.forEach( e -> builderContent(e, UID, GET_METHOD, builder, headers));
		return builder.toString();
	}
	
	private static void builderHeaders(boolean withHeaders, final StringBuilder builder, final Field[] headers) {
		if(withHeaders) {
			for(Field f : headers) {
				if( f.getName().contains(UID) ) continue;
				builder.append(f.getName().toUpperCase()).append(GeneralConstants.CSV_SEPARATOR);
			}
			builder.append(GeneralConstants.CSV_EOF);
		}
	}

	private static <T> void builderContent(T e, final String UID, final String GET_METHOD,
			final StringBuilder builder, final Field[] headers) {
		Method[] methods = e.getClass().getMethods();
		for( Field f: headers ) {
			for(Method m: methods) {
				if( m.getName().contains(UID) ) break;
				if( m.getName().startsWith(GET_METHOD) && (m.getName().length() == (f.getName().length()+3))) {
					if(m.getName().toLowerCase().endsWith(f.getName().toLowerCase())) {
						builder.append(getValue(e, m)).append(GeneralConstants.CSV_SEPARATOR);
						break;
					}
				}
			}
		}
		builder.append(GeneralConstants.CSV_EOF);
	}
	
	private static <T> Object getValue(T e, Method m) {
		try {
			Object v = m.invoke(e);
			if( v == null) v = "";
			return v instanceof String ? v.toString().toUpperCase() : v;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return "";
	}

}
