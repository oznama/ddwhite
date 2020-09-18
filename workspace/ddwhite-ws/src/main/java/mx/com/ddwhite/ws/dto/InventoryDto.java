package mx.com.ddwhite.ws.dto;

import java.math.BigDecimal;

public class InventoryDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543703129956091814L;

	private Long productId;
	private Double quantity;
	private BigDecimal averageCost;
	private BigDecimal currentCost;
	private BigDecimal price;
	private Long unity;
	private String unityDesc;
	private Integer numPiece;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		if (quantity == null)
			return 0.0;
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAverageCost() {
		if (averageCost == null)
			return BigDecimal.ZERO;
		return averageCost;
	}

	public void setAverageCost(BigDecimal averageCost) {
		this.averageCost = averageCost;
	}

	public BigDecimal getCurrentCost() {
		if(currentCost == null)
			return BigDecimal.ZERO;
		return currentCost;
	}

	public void setCurrentCost(BigDecimal currentCost) {
		this.currentCost = currentCost;
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

}
