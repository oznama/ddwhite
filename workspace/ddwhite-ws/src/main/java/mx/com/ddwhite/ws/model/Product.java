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
@Table(name = "productos")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4805375951760731984L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nombre_largo")
	private String nameLarge;
	@Column(name = "nombre_corto")
	private String nameShort;
	private String sku;
	@Column(name = "descripcion")
	private String description;
	@Column(name = "porcentaje_ganancia")
	private BigDecimal percentage;
	@Column(name = "grupo")
	private Long group;
	@Column(name = "fecha_registro", insertable = false)
	private String dateCreated;
	@Column(name = "id_usuario")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameLarge() {
		return nameLarge;
	}

	public void setNameLarge(String nameLarge) {
		this.nameLarge = nameLarge;
	}

	public String getNameShort() {
		return nameShort;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [sku=");
		builder.append(sku);
		builder.append(", nameLarge=");
		builder.append(nameLarge);
		builder.append("]");
		return builder.toString();
	}

}
