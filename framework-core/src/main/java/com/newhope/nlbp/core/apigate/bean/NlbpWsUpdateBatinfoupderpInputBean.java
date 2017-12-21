package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsUpdateBatinfoupderpInputBean {
	
	private	String	batch_no	=	"";	//EBS工单号
	private long    org_id;	
	private	String	organization_code	=	"";	//组织code 
	private	String	hybridization_status	=	"";	//怀状态
	private	String	barn	=	"";	//栋舍信息
	private	String	breeding_date	=	"";	//养殖日期
	private Long createdBy;
	private String docNum;
	private String eventLineId;
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
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getOrganization_code() {
		return organization_code;
	}
	public void setOrganization_code(String organization_code) {
		this.organization_code = organization_code;
	}
	public String getHybridization_status() {
		return hybridization_status;
	}
	public void setHybridization_status(String hybridization_status) {
		this.hybridization_status = hybridization_status;
	}
	public String getBarn() {
		return barn;
	}
	public void setBarn(String barn) {
		this.barn = barn;
	}
	public String getBreeding_date() {
		return breeding_date;
	}
	public void setBreeding_date(String breeding_date) {
		this.breeding_date = breeding_date;
	}
	
	



}
