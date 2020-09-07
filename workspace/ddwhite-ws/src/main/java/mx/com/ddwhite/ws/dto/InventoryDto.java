package mx.com.ddwhite.ws.dto;

import java.math.BigDecimal;

public class InventoryDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543703129956091814L;

	private Long productId;
	private Integer quantity;
	private BigDecimal averageCost;
//	private BigDecimal price;
	private Integer price;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		if(quantity == null) return 0;
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPrice() {
		if(price == null) return 0;
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public BigDecimal getAverageCost() {
		if(averageCost == null) return BigDecimal.ZERO;
		return averageCost;
	}

	public void setAverageCost(BigDecimal averageCost) {
		this.averageCost = averageCost;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryDto [productId=");
		builder.append(productId);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", averageCost=");
		builder.append(averageCost);
		builder.append(", price=");
		builder.append(price);
		builder.append("]");
		return builder.toString();
	}
	
	

}
