package com.nestle.document.model;

public class Status {

	private String approved;
	private String rejected;
	private String processed;
	private String verified;
	private String initiated;
	private String received;

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getRejected() {
		return rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getInitiated() {
		return initiated;
	}

	public void setInitiated(String initiated) {
		this.initiated = initiated;
	}

}
