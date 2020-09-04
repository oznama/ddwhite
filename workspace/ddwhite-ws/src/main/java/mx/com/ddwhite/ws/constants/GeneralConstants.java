package mx.com.ddwhite.ws.constants;

import java.math.BigDecimal;

public interface GeneralConstants {
	
	String FORMAT_DATE = "yyyy-MM-dd";
	String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss.S";
	
	int BIG_DECIMAL_ROUND = 2;
	BigDecimal TAX = BigDecimal.valueOf(1.16);
	String GROUP_EXPENSE = "GASTO";
	
	String CSV_SEPARATOR = ",";
	String CSV_EOF = "\n";
	
	String USERNAME_ADMIN = "admin";
	String ROOT_ROLE = "root";

}
