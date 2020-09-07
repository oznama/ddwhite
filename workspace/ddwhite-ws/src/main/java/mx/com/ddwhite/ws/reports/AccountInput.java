package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532319354652026683L;

	private String saleId;
	private String user;
	private String client;
	private String product;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal total;
	private BigDecimal subTotal;
	private BigDecimal iva;
	private String date;

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
