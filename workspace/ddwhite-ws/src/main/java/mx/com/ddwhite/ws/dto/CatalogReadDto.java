package mx.com.ddwhite.ws.dto;

import java.util.ArrayList;
import java.util.List;

public class CatalogReadDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1271213402269382431L;

	private Long id;
	private String name;
	private String description;
	private List<CatalogItemReadDto> items;
	
	public CatalogReadDto() {
		this.items = new ArrayList<>();
	}

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

	public List<CatalogItemReadDto> getItems() {
		return items;
	}

	public void setItems(List<CatalogItemReadDto> items) {
		this.items = items;
	}

}
