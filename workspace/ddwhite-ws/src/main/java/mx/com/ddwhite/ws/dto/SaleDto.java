package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6154734036682052090L;

	private Long id;
	private Long userId;
	private Long clientId;
	private String clientRfc;
	private String clientName;
	private BigDecimal subTotal;
	private BigDecimal tax;
	private BigDecimal total;
	private BigDecimal change;
	private String invoice;
	private String dateCreated;
	private List<SaleDetailDto> detail;
	private List<SalePaymentDto> payments;

	public SaleDto() {
		this.detail = new ArrayList<>();
		this.payments = new ArrayList<>();
	}

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

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getClientRfc() {
		return clientRfc;
	}

	public void setClientRfc(String clientRfc) {
		this.clientRfc = clientRfc;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<SaleDetailDto> getDetail() {
		return detail;
	}

	public void setDetail(List<SaleDetailDto> detail) {
		this.detail = detail;
	}

	public List<SalePaymentDto> getPayments() {
		return payments;
	}

	public void setPayments(List<SalePaymentDto> payments) {
		this.payments = payments;
	}
	
}
