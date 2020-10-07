package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cashout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 129639932407123290L;

	private BigDecimal total;
	private BigDecimal totalChange;
	private BigDecimal initialAmount;

	private List<CashoutDetail> detail;
	private List<CashoutPayment> payment;

	public Cashout() {
		this.total = new BigDecimal(0);
		this.totalChange = new BigDecimal(0);
		this.detail = new ArrayList<>();
		this.payment = new ArrayList<>();
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalChange() {
		return totalChange;
	}

	public void setTotalChange(BigDecimal totalChange) {
		this.totalChange = totalChange;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}

	public List<CashoutDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<CashoutDetail> detail) {
		this.detail = detail;
	}

	public List<CashoutPayment> getPayment() {
		return payment;
	}

	public void setPayment(List<CashoutPayment> payment) {
		this.payment = payment;
	}

}
