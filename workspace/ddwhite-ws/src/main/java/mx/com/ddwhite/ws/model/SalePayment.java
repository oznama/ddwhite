package mx.com.ddwhite.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "venta_pago")
public class SalePayment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8924431105504600813L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_venta")
	private Long saleId;
	@Column(name = "forma_pago")
	private Long payment;
	@Column(name = "monto")
	private BigDecimal amount;
	@Column(name = "folio_voucher")
	private String voucherFolio;

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

}
