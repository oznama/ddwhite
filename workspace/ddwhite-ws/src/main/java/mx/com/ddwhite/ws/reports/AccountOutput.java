package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750044435434346584L;

	private String user;
	private String provider;
	private String group;
	private String product;
	private String unityDesc;
	private Integer numPiece;
	private Double quantity;
	private BigDecimal cost;
	private BigDecimal total;
	private BigDecimal subTotal;
	private BigDecimal iva;
	private String date;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getUnityDesc() {
		return unityDesc;
	}

	public void setUnityDesc(String unityDesc) {
		this.unityDesc = unityDesc;
	}

	public Integer getNumPiece() {
		return numPiece;
	}

	public void setNumPiece(Integer numPiece) {
		this.numPiece = numPiece;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
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
