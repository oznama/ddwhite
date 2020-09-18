package mx.com.ddwhite.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "compras")
public class Purchase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4214832502983390633L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_usuario")
	private Long userId;
	@Column(name = "id_proveedor")
	private Long providerId;
	@Column(name = "cantidad")
	private Integer quantity;
	@Column(name = "costo")
	private BigDecimal cost;
	@Column(name = "unidad")
	private Long unity;
	@Column(name = "num_piezas")
	private Integer numPiece;
	@Column(name = "fecha_registro", insertable = false, updatable = false)
	private String dateCreated;
	@JoinColumn(name = "id_producto", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private Product product;

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

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Product getProduct() {
		if(product == null) product = new Product();
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
