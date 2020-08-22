package mx.com.ddwhite.ws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalogos")
public class Catalog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9125039781492382428L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nombre")
	private String name;
	@Column(name = "descripcion")
	private String description;
	@Column(name = "catalogo_padre")
	private Long catalogParentId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCatalogParentId() {
		return catalogParentId;
	}

	public void setCatalogParentId(Long catalogParentId) {
		this.catalogParentId = catalogParentId;
	}

}
