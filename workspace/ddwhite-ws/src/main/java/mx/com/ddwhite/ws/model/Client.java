package mx.com.ddwhite.ws.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientes")
public class Client implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7521515650720827088L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nombre")
	private String name;
	@Column(name = "ap_paterno")
	private String midleName;
	@Column(name = "ap_materno")
	private String lastName;
	@Column(name = "domicilio_particular")
	private String address;
	@Column(name = "telefono")
	private String phone;
	@Column(name = "email")
	private String email;
	private String rfc;
	@Column(name = "domicilio_fiscal")
	private String bussinessAddress;
	@Column(name = "telefono_fiscal")
	private String bussinessPhone;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMidleName() {
		return midleName;
	}

	public void setMidleName(String midleName) {
		this.midleName = midleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getBussinessAddress() {
		return bussinessAddress;
	}

	public void setBussinessAddress(String bussinessAddress) {
		this.bussinessAddress = bussinessAddress;
	}

	public String getBussinessPhone() {
		return bussinessPhone;
	}

	public void setBussinessPhone(String bussinessPhone) {
		this.bussinessPhone = bussinessPhone;
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
