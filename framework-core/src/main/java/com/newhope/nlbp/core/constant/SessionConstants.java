/**
 * @项目名称:nlbp-common
 * @文件名称:SessionConstant.java
 * @Date  :2017年3月1日
 * @Copyright: 2017 XX All rights reserved.
 *
 */
package com.newhope.nlbp.core.constant;

/**
 * @author dugang
 * @date: 2017年3月1日 下午4:52:20
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class SessionConstants {
	
	/**
	 * 登录用户session key.
	 */
	public static final String SESSION_USER_KEY = "nlbp_session_user";
	
	public static final String SESSION_USER_BEAN_KEY = "nlbp_session_user_bean";
	
	public static final String SESSION_USER_BUSSINESS_TYPE_KEY = "login_business_type";
	
	/**
	 * shiroUser session key.
	 */
	//public static final String SHIRO_USER_KEY = "shiro_session_user";
	/**
	 * session过期时间.
	 */
	public static final long sessionTimeOut = 3600000;
	

}
