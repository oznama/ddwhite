package mx.com.ddwhite.ws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5248249623334388625L;

	private String table;
	private String column;
	private Object value;

	public ResourceNotFoundException(String table, String column, Object value) {
		super(String.format("%s inexists with %s : %s", table, column, value));
		this.table = table;
		this.column = column;
		this.value = value;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
