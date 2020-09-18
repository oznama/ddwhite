package mx.com.ddwhite.ws.constants;

import java.math.BigDecimal;

public interface GeneralConstants {
	
	String FORMAT_DATE = "yyyy-MM-dd";
	String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss.S";
	
	int BIG_DECIMAL_ROUND = 1;
	BigDecimal TAX = BigDecimal.valueOf(1.16);
	BigDecimal FIXED_PRICE = BigDecimal.valueOf(0.01);
	BigDecimal ONE_HUNDER = BigDecimal.valueOf(100);
	String GROUP_EXPENSE = "GASTO";
	
	String CSV_SEPARATOR = ",";
	String CSV_EOF = "\n";
	
	String USERNAME_ADMIN = "root";
	String ROOT_ROLE = "root";
	
	String CATALOG_NAME_COMPANY = "COMPANY";
	String CATALOG_NAME_COMPANY_NAME = "NOMBRE";
	String CATALOG_NAME_COMPANY_ADDRESS = "DIRECCION";
	String CATALOG_NAME_COMPANY_PHONE = "TELEFONO";
	String CATALOG_NAME_COMPANY_MESSAGE = "MESSAGE_TICKET";
	String CATALOG_NAME_COMPANY_WEBSITE = "PAGINA";
	String CATALOG_NAME_COMPANY_MAIL = "EMAIL";
	String CATALOG_NAME_COMPANY_BUSSINES_NAME = "NOMBRE_FISCAL";
	String CATALOG_NAME_COMPANY_RFC = "RFC";

}
