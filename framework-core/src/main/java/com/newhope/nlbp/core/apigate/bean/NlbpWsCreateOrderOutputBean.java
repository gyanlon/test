package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsCreateOrderOutputBean {
	private boolean status=false;
	private String message="";
	private String workorder_num="";

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

	public String getWorkorder_num() {
		return workorder_num;
	}

	public void setWorkorder_num(String workorder_num) {
		this.workorder_num = workorder_num;
	}

}
