package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PurchaseListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3171919940763715280L;

	private Long id;
	private String user;
	private String provider;
	private String product;
	private Double quantity;
	private BigDecimal cost;
	private Long unity;
	private String unityDesc;
	private Integer numPiece;
	private String dateCreated;
	private Boolean usedInSale;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getUnity() {
		return unity;
	}

	public void setUnity(Long unity) {
		this.unity = unity;
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

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Boolean getUsedInSale() {
		return usedInSale;
	}

	public void setUsedInSale(Boolean usedInSale) {
		this.usedInSale = usedInSale;
	}

}
