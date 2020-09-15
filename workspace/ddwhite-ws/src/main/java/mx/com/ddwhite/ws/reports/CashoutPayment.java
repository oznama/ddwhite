package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class CashoutPayment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2516194877696178374L;

	private String payment;
	private BigDecimal amount;

	public CashoutPayment(String payment, BigDecimal amount) {
		this.payment = payment;
		this.amount = amount;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payment == null) ? 0 : payment.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CashoutPayment other = (CashoutPayment) obj;
		if (payment == null) {
			if (other.payment != null)
				return false;
		} else if (!payment.equals(other.payment))
			return false;
		return true;
	}

}
