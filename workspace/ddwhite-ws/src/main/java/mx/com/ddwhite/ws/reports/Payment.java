package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class Payment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5828519586419438702L;

	private Long saleId;
	private String payment;
	private BigDecimal amount;
	private String voucherFolio;
	private BigDecimal comision;

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
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

	public String getVoucherFolio() {
		return voucherFolio;
	}

	public void setVoucherFolio(String voucherFolio) {
		this.voucherFolio = voucherFolio;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

}
