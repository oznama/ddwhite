package mx.com.ddwhite.ws.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Provider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5904880887425250924L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "razon_social")
	private String bussinesName;
	@Column(name = "direccion")
	private String address;
	@Column(name = "telefono")
	private String phone;
	@Column(name = "pagina_web")
	private String website;
	@Column(name = "contacto")
	private String contactName;
	@Column(name = "id_usuario")
	private Long userId;
	@Column(name = "fecha_registro", insertable = false)
	private String dateCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBussinesName() {
		return bussinesName;
	}

	public void setBussinesName(String bussinesName) {
		this.bussinesName = bussinesName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

}
