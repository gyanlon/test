package com.newhope.nlbp.core.tools;

import java.io.Serializable;

public class SystemInfoException extends Exception  implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SystemInfoException() {
		super();
	}
	
	public SystemInfoException(String msg) {
		super();
		this.msg = msg;
	}
	
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
