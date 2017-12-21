package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsChangeWorkOrderInputBean {
	
	private long org_id;
	private String work_order="";
	private String date="";//biz_date
	private String ou_code="";
	private String pb_status="";
	private String stage="";
	private Long createdBy;
	private String docNum;
	private String eventLineId;
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getDocNum() {
		return docNum;
	}
	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	public String getEventLineId() {
		return eventLineId;
	}
	public void setEventLineId(String eventLineId) {
		this.eventLineId = eventLineId;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getWork_order() {
		return work_order;
	}
	public void setWork_order(String work_order) {
		this.work_order = work_order;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPb_status() {
		return pb_status;
	}
	public void setPb_status(String pb_status) {
		this.pb_status = pb_status;
	}
	public String getOu_code() {
		return ou_code;
	}
	public void setOu_code(String ou_code) {
		this.ou_code = ou_code;
	}
	

}
