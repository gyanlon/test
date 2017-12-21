package com.newhope.nlbp.core.apigate.bean;

import com.newhope.nlbp.core.base.BaseModel;

/**
 * @author yang
 */
public class NlbpWsStrockOutputBean extends BaseModel {

	private static final long serialVersionUID = 4701520602436782749L;
	
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
	public String getIssue_num() {
		return issue_num;
	}
	public void setIssue_num(String issue_num) {
		this.issue_num = issue_num;
	}
	private boolean status=false;
	private String message="";
	private String issue_num="";
	
}