package com.newhope.nlbp.core.tools;

import com.google.common.base.Strings;

public class ObjectConvertHelper {

	//转成对象
	public static <T> T changeToObj(Object o){
		return o == null ? null : (T)o;
	}

	public static <T> T changeStrToObj(String str) {
		return Strings.isNullOrEmpty(str) ? null : (T)str;
	}

	
}
