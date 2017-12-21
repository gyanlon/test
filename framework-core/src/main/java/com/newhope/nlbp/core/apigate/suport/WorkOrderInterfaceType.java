package com.newhope.nlbp.core.apigate.suport;


public enum WorkOrderInterfaceType {
	
	WORK_ORDER_CHECK("CHECK","检查上一事件处理情况"),
	WORK_ORDER_NEW("N","创建工单"),
	WORK_ORDER_TRANSACTION("T","工单事务处理"),
	WORK_ORDER_FINISH("F","工单完工"),
	WORK_ORDER_FINISH_BIRD("FB","禽专用工单关闭"),
	WORK_ORDER_PH_STATUS("HS","配怀状态更新"),
	WORK_ORDER_CANCEL_BIRD("CB","禽专用取消工单"),
	WORK_ORDER_CANCEL_PIG("CP","猪专用取消工单"),
	WORK_ORDER_OPEN("O","重新打开工单"),
	WORK_ORDER_OPEN_BIRD("OB","禽专用重新打开工单"),
	WORK_ORDER_APPLY_PIG("AP","猪专用采购申请"),
	WORK_ORDER_APPLY_BIRD("AB","禽专用采购申请"),
	WORK_ORDER_APPLY_CANCEL("AC","采购申请取消"),
	WORK_ORDER_CREATE_SALE("CX","创建销售订单"),
	WORK_ORDER_SALE_OUT("XO","销售订单出库"),
	WORK_ORDER_SALE_IN("XI","销售订单入库"),
	MATERIAL_PLAN("LLP","领料计划"),
	MATERIAL_PLAN_CANCEL("LLPC","领料计划取消"),
	WORK_ORDER_STOCK_IN_OUT("SIO","其他出入库");


	private String code;
	private String name;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	private WorkOrderInterfaceType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static WorkOrderInterfaceType getEnumByCode(String code) {
		WorkOrderInterfaceType defaultCode = null;
		for (WorkOrderInterfaceType codeObj : WorkOrderInterfaceType.values()) {
			if (codeObj.getCode().equals(code)) {
				defaultCode = codeObj;
				break;
			}
		}
		return defaultCode;
	}
}
