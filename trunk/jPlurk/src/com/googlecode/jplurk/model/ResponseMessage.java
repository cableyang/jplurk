package com.googlecode.jplurk.model;

public class ResponseMessage extends Message {

	String plurkOwnerId;
	String plurkId;

	public String getPlurkOwnerId() {
		return plurkOwnerId;
	}

	public void setPlurkOwnerId(String plurkOwnerId) {
		this.plurkOwnerId = plurkOwnerId;
	}

	public String getPlurkId() {
		return plurkId;
	}

	public void setPlurkId(String plurkId) {
		this.plurkId = plurkId;
	}

	public boolean isValidResponseMessage() {
		return (plurkOwnerId != null && plurkId != null);
	}

}
