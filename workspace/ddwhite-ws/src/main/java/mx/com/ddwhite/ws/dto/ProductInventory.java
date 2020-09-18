package mx.com.ddwhite.ws.dto;

import java.math.BigDecimal;

public class ProductInventory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3302756330495223602L;

	private Long id;
	private String nameLarge;
	private String nameShort;
	private String sku;
	private String description;
	private BigDecimal percentage;
	private Long group;
	private String dateCreated;
	private Long userId;

	private InventoryDto inventory;

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

	public InventoryDto getInventory() {
		if(inventory == null) inventory = new InventoryDto();
		return inventory;
	}

	public void setInventory(InventoryDto inventory) {
		this.inventory = inventory;
	}

}
