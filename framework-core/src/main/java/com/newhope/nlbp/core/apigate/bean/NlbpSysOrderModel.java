package com.newhope.nlbp.core.apigate.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newhope.nlbp.core.base.BaseModel;

/**
 * 系统工单Model 实体类
 * @author aihao
 * @version 2017-05-26
 */
public class NlbpSysOrderModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private Long orderId;		// 主键
	private String orderType;		// 工单类型
	private String nlbpOrderNo;		// 养殖平台虚拟工单号
	private String ebsOrderNo;		// EBS工单号
	private String inventoryItemId;		// 物料代码
	private String orderStatus;		// 工单状态
	private String batchNumber;		// 批次号
	private String earNumber;		// 耳号
	private Date bizDate;		// 工单创建日期
	private Date completeDate;		// 工单关闭日期
	private String lastUpdateBy;		// last_update_by
	private String expand1;		// expand1
	private String expand2;		// expand2
	private String expand3;		// expand3
	private String expand4;		// expand4
	private String expand5;		// expand5
	
	public NlbpSysOrderModel() {
		super();
	}
	
	public NlbpSysOrderModel(Long id){
		super(id);
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getNlbpOrderNo() {
		return nlbpOrderNo;
	}

	public void setNlbpOrderNo(String nlbpOrderNo) {
		this.nlbpOrderNo = nlbpOrderNo;
	}
	
	public String getEbsOrderNo() {
		return ebsOrderNo;
	}

	public void setEbsOrderNo(String ebsOrderNo) {
		this.ebsOrderNo = ebsOrderNo;
	}
	
	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	public String getEarNumber() {
		return earNumber;
	}

	public void setEarNumber(String earNumber) {
		this.earNumber = earNumber;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBizDate() {
		return bizDate;
	}

	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
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
	
}