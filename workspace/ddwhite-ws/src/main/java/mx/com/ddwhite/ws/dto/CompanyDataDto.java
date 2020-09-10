package mx.com.ddwhite.ws.dto;

import java.io.Serializable;

public class CompanyDataDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6286907251388471108L;

	private String name;
	private String address;
	private String phone;
	private String website;
	private String email;
	private String bussinesName;
	private String rfc;
	private String messageTicket;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBussinesName() {
		return bussinesName;
	}

	public void setBussinesName(String bussinesName) {
		this.bussinesName = bussinesName;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getMessageTicket() {
		return messageTicket;
	}

	public void setMessageTicket(String messageTicket) {
		this.messageTicket = messageTicket;
	}

}
