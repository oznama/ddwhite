package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.util.List;

public class UserGrantDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -713452872799017588L;

	private Long id;
	private String username;
	private String fullName;
	private String role;
	private List<String> privileges;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}

}
