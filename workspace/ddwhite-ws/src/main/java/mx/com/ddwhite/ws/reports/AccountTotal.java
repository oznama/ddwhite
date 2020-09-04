package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2407528164821172595L;

	private Integer quantityTotal;
	private BigDecimal amountTotal;
	private BigDecimal tTotal;
	private BigDecimal sbTotal;
	private BigDecimal ivaTotal;

	public AccountTotal() {
		this.quantityTotal = 0;
		this.amountTotal = BigDecimal.valueOf(0);
		this.tTotal = BigDecimal.valueOf(0);
		this.sbTotal = BigDecimal.valueOf(0);
		this.ivaTotal = BigDecimal.valueOf(0);
	}

	public Integer getQuantityTotal() {
		return quantityTotal;
	}

	public void setQuantityTotal(Integer quantityTotal) {
		this.quantityTotal = quantityTotal;
	}

	public BigDecimal getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(BigDecimal amountTotal) {
		this.amountTotal = amountTotal;
	}

	public BigDecimal gettTotal() {
		return tTotal;
	}

	public void settTotal(BigDecimal tTotal) {
		this.tTotal = tTotal;
	}

	public BigDecimal getSbTotal() {
		return sbTotal;
	}

	public void setSbTotal(BigDecimal sbTotal) {
		this.sbTotal = sbTotal;
	}

	public BigDecimal getIvaTotal() {
		return ivaTotal;
	}

	public void setIvaTotal(BigDecimal ivaTotal) {
		this.ivaTotal = ivaTotal;
	}

}
