/**
 * 
 */
package com.dse.cas.exception;

/**
 * 
 * @ClassName: CaException
 * @Description: CA认证异常
 * @author xutt
 * @date 2015年12月19日 下午2:40:53
 * 
 */
public class CaException extends Exception {
	private static final long serialVersionUID = 1L;

	public CaException(String msg) {
		super(msg);
	}

	public CaException(String msg, Throwable e) {
		super(msg, e);
	}
}
