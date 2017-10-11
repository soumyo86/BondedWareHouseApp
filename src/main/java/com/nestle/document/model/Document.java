package com.nestle.document.model;

import java.util.List;

public class Document {

	private String documentId;
	private String source;
	private String destination;
	private String status;
	private String deliveryDate;
	private String deliveryNo;
	private String custPO;
	private String documenthash;
	private String reasonCode;
	private String updatedBy;
	private List<Item> items;
	private String truckId;
	private String customLocation;
	private String sourceLatLong;
	private String destnationLatLong;
	private String customLatLong;
	private String estimatedTime;

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public String getCustomLocation() {
		return customLocation;
	}

	public void setCustomLocation(String customLocation) {
		this.customLocation = customLocation;
	}

	public String getSourceLatLong() {
		return sourceLatLong;
	}

	public void setSourceLatLong(String sourceLatLong) {
		this.sourceLatLong = sourceLatLong;
	}

	public String getDestnationLatLong() {
		return destnationLatLong;
	}

	public void setDestnationLatLong(String destnationLatLong) {
		this.destnationLatLong = destnationLatLong;
	}

	public String getCustomLatLong() {
		return customLatLong;
	}

	public void setCustomLatLong(String customLatLong) {
		this.customLatLong = customLatLong;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getCustPO() {
		return custPO;
	}

	public void setCustPO(String custPO) {
		this.custPO = custPO;
	}

	public String getDocumenthash() {
		return documenthash;
	}

	public void setDocumenthash(String documenthash) {
		this.documenthash = documenthash;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDocHash() {
		return documenthash;
	}

	public void setDocHash(String docHash) {
		this.documenthash = docHash;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

}
