package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.math.BigDecimal;

public class Warehouse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sku;
	private String nameLarge;
	private String unityDesc;
	private Integer numPiece;
	private Integer quantity;
	private BigDecimal percentage;
	private BigDecimal averageCost;
	private BigDecimal currentCost;
	private BigDecimal price;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNameLarge() {
		return nameLarge;
	}

	public void setNameLarge(String nameLarge) {
		this.nameLarge = nameLarge;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(BigDecimal averageCost) {
		this.averageCost = averageCost;
	}

	public BigDecimal getCurrentCost() {
		return currentCost;
	}

	public void setCurrentCost(BigDecimal currentCost) {
		this.currentCost = currentCost;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUnityDesc() {
		return unityDesc;
	}

	public void setUnityDesc(String unityDesc) {
		this.unityDesc = unityDesc;
	}

	public Integer getNumPiece() {
		return numPiece;
	}

	public void setNumPiece(Integer numPiece) {
		this.numPiece = numPiece;
	}

}
