package com.dse.cas;

import java.util.HashMap;
import java.util.Map;

public class CONSTANT {

	/**************************************************************
	 *登录页面返回的提示,可自己扩展
	 *************************************************************/
	public static final Integer LOGIN_EQ_OLD_USER_NAME= -1;
	public static final Integer LOGIN_SUCCESS= 0;
	public static final Integer LOGIN_NO_USER_NAME= 1;
	public static final Integer LOGIN_WRONG_PWD= 2;
	public static final Integer LOGIN_NULL_USER_NAME= 3;
	public static final Integer LOGIN_NULL_PWD= 4;

	public static final Map<Integer, String> LOGIN_MSG = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -8164653884996984485L;
		{
			put(LOGIN_EQ_OLD_USER_NAME, "登录的账号与原账号相同");
			put(LOGIN_SUCCESS, "登录成功");
			put(LOGIN_NO_USER_NAME, "用户不存在");
			put(LOGIN_WRONG_PWD, "密码错误");
			put(LOGIN_NULL_USER_NAME, "请输入用户名");
			put(LOGIN_NULL_PWD, "请输入密码");
		}
	};


}