package mx.com.ddwhite.ws.dto;

import java.io.Serializable;

public class PurchaseReasignDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3772737704588803838L;

	private Long purchasesOrigin;
	private Long purchaseDestity;
	private Integer quantity;

	public Long getPurchasesOrigin() {
		return purchasesOrigin;
	}

	public void setPurchasesOrigin(Long purchasesOrigin) {
		this.purchasesOrigin = purchasesOrigin;
	}

	public Long getPurchaseDestity() {
		return purchaseDestity;
	}

	public void setPurchaseDestity(Long purchaseDestity) {
		this.purchaseDestity = purchaseDestity;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
