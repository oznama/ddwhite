package mx.com.ddwhite.ws.dto;

import java.math.BigDecimal;

public class InventoryDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543703129956091814L;

	private Long productId;
	private Integer quantity;
	private BigDecimal cost;
	private BigDecimal price;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
