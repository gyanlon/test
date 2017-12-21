/**
 * @项目名称:nlbpWeb-common
 * @文件名称:NlbpWebException.java
 * @Date  :2016年12月19日
 * @Copyright: 2016 XX All rights reserved.
 *
 */
package com.newhope.nlbp.core.exception;

/**
 * @author dugang
 * @date: 2016年12月19日 下午5:00:53
 * @version 
 * @since JDK 1.8
 *
 */
public class NlbpBizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6096096290240133010L;

	
	public NlbpBizException() {
		super();
	}
	
	public NlbpBizException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public NlbpBizException(String message) {
		super(message);
	}
	
	public NlbpBizException(Throwable cause) {
		super(cause);
	}
	
	
}
