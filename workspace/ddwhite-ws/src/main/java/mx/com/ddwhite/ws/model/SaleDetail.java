package mx.com.ddwhite.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "venta_detalle")
public class SaleDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3104687479860992112L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "cantidad")
	private Double quantity;
	@Column(name = "precio")
	private BigDecimal price;
	@Column(name = "unidad")
	private Long unity;
	@Column(name = "num_piezas")
	private Integer numPiece;
	@Column(name = "id_venta", updatable = false)
	private Long saleId;
	@Column(name = "id_producto", updatable = false)
	private Long productId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getQuantity() {
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

	public Long getUnity() {
		return unity;
	}

	public void setUnity(Long unity) {
		this.unity = unity;
	}

	public Integer getNumPiece() {
		return numPiece;
	}

	public void setNumPiece(Integer numPiece) {
		this.numPiece = numPiece;
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

}
