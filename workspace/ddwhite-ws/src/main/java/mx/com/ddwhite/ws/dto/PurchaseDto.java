package mx.com.ddwhite.ws.dto;

import java.math.BigDecimal;

public class PurchaseDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8736913074809182382L;

	private Long id;
	private Long userId;
	private Long providerId;
	private Long productId;
	private Double quantity;
	private BigDecimal cost;
	private Long unity;
	private String unityDesc;
	private Integer numPiece;
	private String dateCreated;
	private ProductDto product;

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

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public ProductDto getProduct() {
		if(product == null)
			product = new ProductDto();
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

}
