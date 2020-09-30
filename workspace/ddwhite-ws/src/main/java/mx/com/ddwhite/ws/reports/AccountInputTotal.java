package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountInputTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2407528164821172595L;

	private Double quantityTotal;
	private BigDecimal sbTotal;
	private BigDecimal ivaTotal;
	private BigDecimal tTotal;
	private BigDecimal ganancia;

	public AccountInputTotal() {
		this.quantityTotal = 0.0;
		this.sbTotal = BigDecimal.valueOf(0);
		this.ivaTotal = BigDecimal.valueOf(0);
		this.tTotal = BigDecimal.valueOf(0);
		this.ganancia = BigDecimal.valueOf(0);
	}

	public Double getQuantityTotal() {
		return quantityTotal;
	}

	public void setQuantityTotal(Double quantityTotal) {
		this.quantityTotal = quantityTotal;
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

	public BigDecimal getGanancia() {
		return ganancia;
	}

	public void setGanancia(BigDecimal ganancia) {
		this.ganancia = ganancia;
	}

}
