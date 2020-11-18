package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SessionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6918716934824072988L;

	private Long id;
	private Long userId;
	private String userFullname;
	private String inDate;
	private String outDate;
	private BigDecimal initialAmount;
	private BigDecimal finalAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserFullname() {
		return userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SessionDto [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userFullname=");
		builder.append(userFullname);
		builder.append(", inDate=");
		builder.append(inDate);
		builder.append(", outDate=");
		builder.append(outDate);
		builder.append(", initialAmount=");
		builder.append(initialAmount);
		builder.append(", finalAmount=");
		builder.append(finalAmount);
		builder.append("]");
		return builder.toString();
	}

}
