package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4368585854288030344L;
	private Long id;
	private Long saleId;
	private Long productId;
	private Integer quantity;
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
