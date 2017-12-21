package com.newhope.nlbp.core.apigate.bean;

public class NlbpWsTransOrderInputBean {
	
	private String sub_code="";
	private long org_id;
	private String work_order="";
	private String batch_num="";
	private String item_code="";
	private String trx_type="";
	private String trx_date="";
	private String pb_status="";
	private String primary_qty="";
	private String second_qty="";
	private String ou_code="";
	private String check="";
	private String 	uperstage=""; // Y or N 是否上一阶段产品
	
	private String  breedDate=""; //养殖日期

	public static final String YES = "Y";
	
	public static final String NO = "N";
	
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
	public String getUperstage() {
		return uperstage;
	}
	public void setUperstage(String uperstage) {
		this.uperstage = uperstage;
	}
	public String getBreedDate() {
		return breedDate;
	}
	public void setBreedDate(String breedDate) {
		this.breedDate = breedDate;
	}


	
	
	
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getOu_code() {
		return ou_code;
	}
	public void setOu_code(String ou_code) {
		this.ou_code = ou_code;
	}
	public String getSub_code() {
		return sub_code;
	}
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
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
	public String getBatch_num() {
		return batch_num;
	}
	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getTrx_type() {
		return trx_type;
	}
	public void setTrx_type(String trx_type) {
		this.trx_type = trx_type;
	}
	public String getTrx_date() {
		return trx_date;
	}
	public void setTrx_date(String trx_date) {
		this.trx_date = trx_date;
	}
	public String getPb_status() {
		return pb_status;
	}
	public void setPb_status(String pb_status) {
		this.pb_status = pb_status;
	}
	public String getPrimary_qty() {
		return primary_qty;
	}
	public void setPrimary_qty(String primary_qty) {
		this.primary_qty = primary_qty;
	}
	public String getSecond_qty() {
		return second_qty;
	}
	public void setSecond_qty(String second_qty) {
		this.second_qty = second_qty;
	}
	@Override
	public String toString() {
		return "NlbpWsTransOrderInputBean [sub_code=" + sub_code + ", org_id=" + org_id + ", work_order=" + work_order
				+ ", batch_num=" + batch_num + ", item_code=" + item_code + ", trx_type=" + trx_type + ", trx_date="
				+ trx_date + ", pb_status=" + pb_status + ", primary_qty=" + primary_qty + ", second_qty=" + second_qty
				+ ", ou_code=" + ou_code + "]";
	}

	
	
	
	
}
