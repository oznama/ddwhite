package mx.com.ddwhite.ws.dto;

public class CatalogItemReadDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6495491942755700911L;
	
	private Long id;
	private String name;
	private String description;

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

}
