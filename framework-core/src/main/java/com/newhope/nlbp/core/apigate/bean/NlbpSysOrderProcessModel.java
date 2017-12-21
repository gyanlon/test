package com.newhope.nlbp.core.apigate.bean;

import com.newhope.nlbp.core.base.BaseModel;

/**
 * 订单处理Model 实体类
 * @author aihao
 * @version 2017-05-26
 */
public class NlbpSysOrderProcessModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private Long processId;		// 物理键
	private String interfaceType;		// N:创建工单 F:关闭工单 T:事务处理
	private String docNum;		// 业务单据号
	private String eventId;		// 业务单据行ID
	private String pbCode;		// pb_code
	private String ebsOrgCode;		// EBS库存组织代码
	private String farmCode;		// 车间-养殖场
	private String bizDate;		// 业务发生时间
	private String nlbpOrderNo;		// 养殖平台虚拟工单号
	private String transactionType;		// 事务处理类型
	private String primaryUomQty;		// 主数量
	private String secondUomQty;		// 辅助数量
	private String orderType;		// 工单类型
	private String ebsOrderNo;		// EBS工单号
	private String roomCode;		// 栋舍
	private String earNumber;		// 耳号
	private String subInventroyCode;		// 子库存
	private String inventoryItemId;		// 物料代码
	private String conceiveStatus;		// 配怀状态 dict_type_code=PIG_BREED_RESULT
	private String batchNumber;		// 批次号
	private String birdCheck;		// 禽用
	private String breedDate;		// 禽用 养殖日期
	private String uperStage;		// 禽用 是否上阶段产物
	private Integer totalRecordCount;		// 总记录数
	private String handlerStatus;		// S:成功 E:报错 W:待处理
	private Integer handlerCount = 0;		// 处理次数
	private String processCode;		// EBS处理返回码 S000A000:成功
	private String processMessage;		// EBS处理返回消息
	private String lastUpdateBy;		// last_update_by
	private String expand1;		// expand1
	private String expand2;		// expand2
	private String expand3;		// expand3
	private String expand4;		// expand4
	private String expand5;		// expand5
	//7-26新增
	private String expand6;		//expand6
	private String expand7;		//expand7
	private String expand8;		//expand8
	private String expand9;		//expand9
	private String expand10;		//expand10
	private String price;
	private String nlbpPurchOrder;  //养殖平台采购申请单号
	private String ebsPurchOrder;    //ebs采购申请单号
	private String orgCode;		//公司编码
	
	public NlbpSysOrderProcessModel() {
		super();
	}
	
	public NlbpSysOrderProcessModel(Long id){
		super(id);
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	
	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public String getPbCode() {
		return pbCode;
	}

	public void setPbCode(String pbCode) {
		this.pbCode = pbCode;
	}
	
	public String getEbsOrgCode() {
		return ebsOrgCode;
	}

	public void setEbsOrgCode(String ebsOrgCode) {
		this.ebsOrgCode = ebsOrgCode;
	}
	
	public String getFarmCode() {
		return farmCode;
	}

	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}
	
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	
	public String getNlbpOrderNo() {
		return nlbpOrderNo;
	}

	public void setNlbpOrderNo(String nlbpOrderNo) {
		this.nlbpOrderNo = nlbpOrderNo;
	}
	
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getPrimaryUomQty() {
		return primaryUomQty;
	}

	public void setPrimaryUomQty(String primaryUomQty) {
		this.primaryUomQty = primaryUomQty;
	}
	
	public String getSecondUomQty() {
		return secondUomQty;
	}

	public void setSecondUomQty(String secondUomQty) {
		this.secondUomQty = secondUomQty;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getEbsOrderNo() {
		return ebsOrderNo;
	}

	public void setEbsOrderNo(String ebsOrderNo) {
		this.ebsOrderNo = ebsOrderNo;
	}
	
	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	
	public String getEarNumber() {
		return earNumber;
	}

	public void setEarNumber(String earNumber) {
		this.earNumber = earNumber;
	}
	
	public String getSubInventroyCode() {
		return subInventroyCode;
	}

	public void setSubInventroyCode(String subInventroyCode) {
		this.subInventroyCode = subInventroyCode;
	}
	
	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}
	
	public String getConceiveStatus() {
		return conceiveStatus;
	}

	public void setConceiveStatus(String conceiveStatus) {
		this.conceiveStatus = conceiveStatus;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	public String getBirdCheck() {
		return birdCheck;
	}
	public void setBirdCheck(String birdCheck) {
		this.birdCheck = birdCheck;
	}
	
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getBreedDate() {
		return breedDate;
	}

	public void setBreedDate(String breedDate) {
		this.breedDate = breedDate;
	}
	
	public String getUperstage() {
		return uperStage;
	}
	
	public void setUperstage(String uperstage) {
		this.uperStage = uperstage;
	}
	
	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Integer totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	
	public String getHandlerStatus() {
		return handlerStatus;
	}

	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}
	
	public Integer getHandlerCount() {
		return handlerCount;
	}

	public void setHandlerCount(Integer handlerCount) {
		this.handlerCount = handlerCount;
	}
	
	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	
	public String getProcessMessage() {
		return processMessage;
	}

	public void setProcessMessage(String processMessage) {
		this.processMessage = processMessage;
	}
	
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	
	public String getExpand1() {
		return expand1;
	}

	public void setExpand1(String expand1) {
		this.expand1 = expand1;
	}
	
	public String getExpand2() {
		return expand2;
	}

	public void setExpand2(String expand2) {
		this.expand2 = expand2;
	}
	
	public String getExpand3() {
		return expand3;
	}

	public void setExpand3(String expand3) {
		this.expand3 = expand3;
	}
	
	public String getExpand4() {
		return expand4;
	}

	public void setExpand4(String expand4) {
		this.expand4 = expand4;
	}
	
	public String getExpand5() {
		return expand5;
	}

	public void setExpand5(String expand5) {
		this.expand5 = expand5;
	}

	public String getExpand6() {
		return expand6;
	}

	public void setExpand6(String expand6) {
		this.expand6 = expand6;
	}

	public String getExpand7() {
		return expand7;
	}

	public void setExpand7(String expand7) {
		this.expand7 = expand7;
	}

	public String getExpand8() {
		return expand8;
	}

	public void setExpand8(String expand8) {
		this.expand8 = expand8;
	}

	public String getExpand9() {
		return expand9;
	}

	public void setExpand9(String expand9) {
		this.expand9 = expand9;
	}

	public String getExpand10() {
		return expand10;
	}

	public void setExpand10(String expand10) {
		this.expand10 = expand10;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getNlbpPurchOrder() {
		return nlbpPurchOrder;
	}

	public void setNlbpPurchOrder(String nlbpPurchOrder) {
		this.nlbpPurchOrder = nlbpPurchOrder;
	}



	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getEbsPurchOrder() {
		return ebsPurchOrder;
	}

	public void setEbsPurchOrder(String ebsPurchOrder) {
		this.ebsPurchOrder = ebsPurchOrder;
	}
	
}