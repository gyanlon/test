package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsCreateOrderInputBean {
	
	private long org_id;
	private String ebsOrgId;
	private String itme_code;
	private String batch_num="";
	private String Qty="";
	private String farm="";
	private String DS_NUMBER="";
	private String PB_CODE="";
	private String EP_NUMBER="";
	private String STAGE_TYPE="";
	private String TRX_DATE="";
	private String workOrderType="";
	private String docNum;
	private String eventLineId;
	private Long createdBy;
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getEbsOrgId() {
		return ebsOrgId;
	}
	public void setEbsOrgId(String ebsOrgId) {
		this.ebsOrgId = ebsOrgId;
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
	public String getWorkOrderType() {
		return workOrderType;
	}
	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getItme_code() {
		return itme_code;
	}
	public void setItme_code(String itme_code) {
		this.itme_code = itme_code;
	}
	public String getBatch_num() {
		return batch_num;
	}
	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}
	public String getQty() {
		return Qty;
	}
	public void setQty(String qty) {
		Qty = qty;
	}
	public String getFarm() {
		return farm;
	}
	public void setFarm(String farm) {
		this.farm = farm;
	}
	public String getDS_NUMBER() {
		return DS_NUMBER;
	}
	public void setDS_NUMBER(String dS_NUMBER) {
		DS_NUMBER = dS_NUMBER;
	}
	public String getPB_CODE() {
		return PB_CODE;
	}
	public void setPB_CODE(String pB_CODE) {
		PB_CODE = pB_CODE;
	}
	public String getEP_NUMBER() {
		return EP_NUMBER;
	}
	public void setEP_NUMBER(String eP_NUMBER) {
		EP_NUMBER = eP_NUMBER;
	}
	public String getSTAGE_TYPE() {
		return STAGE_TYPE;
	}
	public void setSTAGE_TYPE(String sTAGE_TYPE) {
		STAGE_TYPE = sTAGE_TYPE;
	}
	public String getTRX_DATE() {
		return TRX_DATE;
	}
	public void setTRX_DATE(String tRX_DATE) {
		TRX_DATE = tRX_DATE;
	}
	

}
