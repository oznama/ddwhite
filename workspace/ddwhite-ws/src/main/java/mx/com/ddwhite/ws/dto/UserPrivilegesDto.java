package mx.com.ddwhite.ws.dto;

import java.io.Serializable;

public class UserPrivilegesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -305377244938012011L;

	private String name;
	private String key;

	public UserPrivilegesDto(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
