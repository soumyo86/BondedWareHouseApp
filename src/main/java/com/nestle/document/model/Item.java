package com.nestle.document.model;

public class Item {

	private String itemId;
	private String name;
	private String quantity;
	private String description;
	private String weightvolume;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeightvolume() {
		return weightvolume;
	}

	public void setWeightvolume(String weightvolume) {
		this.weightvolume = weightvolume;
	}

}