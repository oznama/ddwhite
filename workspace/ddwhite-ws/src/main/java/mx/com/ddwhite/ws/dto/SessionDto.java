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
	private String inDate;
	private String outDate;
	private BigDecimal initialAmount;
	private String withdrawalDate;

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

	public String getWithdrawalDate() {
		return withdrawalDate;
	}

	public void setWithdrawalDate(String withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SessionDto [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", inDate=");
		builder.append(inDate);
		builder.append(", outDate=");
		builder.append(outDate);
		builder.append(", initialAmount=");
		builder.append(initialAmount);
		builder.append("]");
		return builder.toString();
	}
	
	

}
