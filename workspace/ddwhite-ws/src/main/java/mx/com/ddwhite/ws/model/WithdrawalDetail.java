package mx.com.ddwhite.ws.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "retiro_detalle")
public class WithdrawalDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3716472801126730719L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_retiro")
	private Long withdrawalId;
	@Column(name = "denominacion")
	private Long denominationId;
	@Column(name = "cantidad")
	private Integer quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWithdrawalId() {
		return withdrawalId;
	}

	public void setWithdrawalId(Long withdrawalId) {
		this.withdrawalId = withdrawalId;
	}

	public Long getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Long denominationId) {
		this.denominationId = denominationId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
