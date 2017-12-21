package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsChangeWorkOrderOutputBean {
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private boolean status=false;
	private String message="";

}
