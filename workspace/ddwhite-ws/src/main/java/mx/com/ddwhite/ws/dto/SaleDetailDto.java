package mx.com.ddwhite.ws.dto;

import java.io.Serializable;

public class SaleDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4368585854288030344L;
	private Long id;
	private Long saleId;
	private Long purchaseId;
	private Integer quantity;

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

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}