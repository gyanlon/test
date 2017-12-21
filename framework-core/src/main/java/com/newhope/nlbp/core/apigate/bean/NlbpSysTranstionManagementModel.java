package com.newhope.nlbp.core.apigate.bean;


import com.newhope.nlbp.core.base.BaseModel;

/**
 * sys_transtion_manageModel 实体类
 * @author hekun
 * @version 2017-05-24
 */
public class NlbpSysTranstionManagementModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private String subCode;		// 仓库/养殖场
	private Long orgId;		// 组织ID
	private String workOrder;		// 工单号
	private String batchNum;		// 批次号
	private String itemCode;		// 物料号
	private String trxType;		// 事务处理类型
	private String trxDate;		// 事物处理日期
	private String pbStatus;		// 配怀状态
	private String primaryQty;		// 主单位数量
	private String secondQty;		// 辅助单位数量
	private String ouCode;		// 组织CODE
	private String uperstage;		// 是否上一阶段
	private String breeddate;		// 养殖日期
	
	public NlbpSysTranstionManagementModel() {
		super();
	}
	
	public NlbpSysTranstionManagementModel(Long id){
		super(id);
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}
	
	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}
	
	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	
	public String getPbStatus() {
		return pbStatus;
	}

	public void setPbStatus(String pbStatus) {
		this.pbStatus = pbStatus;
	}
	
	public String getPrimaryQty() {
		return primaryQty;
	}

	public void setPrimaryQty(String primaryQty) {
		this.primaryQty = primaryQty;
	}
	
	public String getSecondQty() {
		return secondQty;
	}

	public void setSecondQty(String secondQty) {
		this.secondQty = secondQty;
	}
	
	public String getOuCode() {
		return ouCode;
	}

	public void setOuCode(String ouCode) {
		this.ouCode = ouCode;
	}
	
	public String getUperstage() {
		return uperstage;
	}

	public void setUperstage(String uperstage) {
		this.uperstage = uperstage;
	}
	
	public String getBreeddate() {
		return breeddate;
	}

	public void setBreeddate(String breeddate) {
		this.breeddate = breeddate;
	}
	
}