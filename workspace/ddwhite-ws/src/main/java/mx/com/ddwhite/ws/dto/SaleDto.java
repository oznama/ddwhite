package mx.com.ddwhite.ws.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6154734036682052090L;
	
	private Long id;
	private Long userId;
	private Long clientId;
	private String dateCreated;
	private List<SaleDetailDto> detail;
	
	public SaleDto() {
		this.detail = new ArrayList<>();
	}

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

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<SaleDetailDto> getDetail() {
		return detail;
	}

	public void setDetail(List<SaleDetailDto> detail) {
		this.detail = detail;
	}

}
