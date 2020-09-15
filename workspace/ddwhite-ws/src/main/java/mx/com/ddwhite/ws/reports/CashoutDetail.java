package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class CashoutDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7780996971282316163L;

	private String group;
	private BigDecimal total;

	public CashoutDetail(String group, BigDecimal total) {
		this.group = group;
		this.total = total;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CashoutDetail other = (CashoutDetail) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}
	
}
