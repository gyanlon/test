package com.newhope.nlbp.core.apigate.bean;

import com.newhope.nlbp.core.base.BaseModel;

/**
 * 
 * @author yang
 *
 */
public class NlbpWsStockInputBean extends BaseModel {

	private static final long serialVersionUID = -5811495065988331402L;

	private String type = ""; // IN 入库 OUT 出库

	private long org_id;
	private String farm_code = "";
	private String ds_code = "";
	private String person_id = "";
	private String item_code = "";
	private String batch_num = "";
	private String ep_num = "";
	private String pty = "";
	private String purpose = "";
	private String sub_code = "";
	private String second_qty = "";
	private String typeCheck = "";
	private String business_date = "";
	private String work_order = "";
	private String flag = "";// 工单标识
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public String getFarm_code() {
		return farm_code;
	}

	public void setFarm_code(String farm_code) {
		this.farm_code = farm_code;
	}

	public String getDs_code() {
		return ds_code;
	}

	public void setDs_code(String ds_code) {
		this.ds_code = ds_code;
	}

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getBatch_num() {
		return batch_num;
	}

	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}

	public String getEp_num() {
		return ep_num;
	}

	public void setEp_num(String ep_num) {
		this.ep_num = ep_num;
	}

	public String getPty() {
		return pty;
	}

	public void setPty(String pty) {
		this.pty = pty;
	}

	public String getBusiness_date() {
		return business_date;
	}

	public void setBusiness_date(String business_date) {
		this.business_date = business_date;
	}

	public String getTypeCheck() {
		return typeCheck;
	}

	public void setTypeCheck(String typeCheck) {
		this.typeCheck = typeCheck;
	}

	public String getWork_order() {
		return work_order;
	}

	public void setWork_order(String work_order) {
		this.work_order = work_order;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getSub_code() {
		return sub_code;
	}

	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}

	public String getSecond_qty() {
		return second_qty;
	}

	public void setSecond_qty(String second_qty) {
		this.second_qty = second_qty;
	}

}