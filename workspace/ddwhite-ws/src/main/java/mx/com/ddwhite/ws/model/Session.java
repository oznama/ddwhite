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
@Table(name = "sesion")
public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2920194592845268176L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_usuario")
	private Long userId;
	@Column(name = "entrada", insertable = false, updatable = false)
	private String inDate;
	@Column(name = "salida")
	private String outDate;
	@Column(name = "monto_inicial")
	private BigDecimal initialAmount;
	@Column(name = "retiro", insertable = false)
	private String withdrawalDate;

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

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}

	public String getWithdrawalDate() {
		return withdrawalDate;
	}

	public void setWithdrawalDate(String withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

}
