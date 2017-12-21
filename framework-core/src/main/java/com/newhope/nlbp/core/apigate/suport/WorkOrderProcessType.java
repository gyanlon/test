package com.newhope.nlbp.core.apigate.suport;


public enum WorkOrderProcessType {
	
	NLBP_ORDER_NO("nlbpOrderNo","养殖平台工单号"),
	ORG_CODE("expand1","扩展字段存放组织CODE"),
	DOC_NUM("docNum","业务单据号"),
	INTERFACE_TYPE("interfaceType","工单处理类型"),
	HANDLER_STATUS("handlerStatus","存放处理状态"),
	NEED_CHECK_INTERFACE_TYPE_CODES("checkCodes","需要检查的工单处理类型"),
	HANDLER_STATUS_S("S","处理成功"),
	HANDLER_STATUS_P("P","正在处理"),
	HANDLER_STATUS_E("E","处理失败"),
	
	WORK_ORDER_TRY("try","处理工单"),
	WORK_ORDER_RETRY("reTry","空闲重试处理工单");
	
	public static final String aa = "";

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
	 * the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	private WorkOrderProcessType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static WorkOrderProcessType getEnumByCode(String code) {
		WorkOrderProcessType defaultCode = null;
		for (WorkOrderProcessType codeObj : WorkOrderProcessType.values()) {
			if (codeObj.getCode().equals(code)) {
				defaultCode = codeObj;
				break;
			}
		}
		return defaultCode;
	}
}
