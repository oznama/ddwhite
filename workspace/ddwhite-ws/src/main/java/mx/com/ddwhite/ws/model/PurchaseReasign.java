package mx.com.ddwhite.ws.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "compras_reasingadas")
public class PurchaseReasign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_compra_origen")
	private Long purchasesOrigin;
	@Column(name = "id_compra_destino")
	private Long purchaseDestity;
	@Column(name = "cantidad")
	private Double quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
