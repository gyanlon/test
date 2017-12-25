/**
 * 
 */
package com.dse.cas.web.action;

/**
 * 
 * @ClassName: Result
 * @Description: TODO
 * @author xutt
 * @date 2015年12月25日 下午3:24:33
 * 
 */
public class Result {
	private String code = "500";// 200--成功 500--失败
	private String msg;
	private Object other;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getOther() {
		return other;
	}

	public void setOther(Object other) {
		this.other = other;
	}

}
