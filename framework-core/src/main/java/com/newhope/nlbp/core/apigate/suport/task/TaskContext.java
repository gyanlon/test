package com.newhope.nlbp.core.apigate.suport.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.newhope.nlbp.core.pipeline.Context;

/**
 * 任务处理上下文类
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public final class TaskContext implements Context {

	private final String nlbpOrderNo;

	private final String orgCode;

	private final String tryFlag;

	private final Map<Object, Object> attributes;

	public TaskContext(String nlbpOrderNo, String orgCode, String tryFlag) {
		this.nlbpOrderNo = nlbpOrderNo;
		this.orgCode = orgCode;
		this.tryFlag = tryFlag;
		this.attributes = new HashMap<Object, Object>();
	}

	public final String getNlbpOrderNo() {
		return nlbpOrderNo;
	}

	public final String getOrgCode() {
		return orgCode;
	}

	public final String getTryFlag() {
		return tryFlag;
	}

	public final Map<Object, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		String str = "TaskContext [nlbpOrderNo=" + nlbpOrderNo + ", orgCode=" + orgCode + ", tryFlag=" + tryFlag;
		StringBuilder sb = new StringBuilder(str);
		if (!MapUtils.isEmpty(this.getAttributes())) {
			int i = 0;
			for (Map.Entry<Object, Object> entry : this.getAttributes().entrySet()) {
				String key = String.valueOf(entry.getKey());
				String value = String.valueOf(entry.getValue());
				sb.append(key).append("=").append(value);
				if (++i < this.getAttributes().size()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
