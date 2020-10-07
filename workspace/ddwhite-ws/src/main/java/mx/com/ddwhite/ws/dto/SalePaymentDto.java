package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SalePaymentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3115585154032132752L;

	private Long id;
	private Long saleId;
	private Long payment;
	private BigDecimal amount;
	private String voucherFolio;
	private BigDecimal comision;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Long getPayment() {
		return payment;
	}

	public void setPayment(Long payment) {
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
