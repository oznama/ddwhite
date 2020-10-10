package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithdrawalDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4790075124365249067L;
	
	private String denomination;
	private BigDecimal denominationValue;
	private int quantity;

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getDenominationValue() {
		return denominationValue;
	}

	public void setDenominationValue(BigDecimal denominationValue) {
		this.denominationValue = denominationValue;
	}

}
