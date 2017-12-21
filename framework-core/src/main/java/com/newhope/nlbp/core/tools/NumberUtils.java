package com.newhope.nlbp.core.tools;


/**
 * Function: 数值类型处理. <br/>
 * @author dugang
 * @date: 2017年1月23日 下午2:00:49
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class NumberUtils {
	/**
	 * 验证Integer是否为空.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmptyInteger(Integer obj) {
		if (obj == null || obj.equals(0)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmptyLong(Long obj) {
		if (obj == null || obj.equals(0)) {
			return true;
		}
		return false;
	}
	
	public static Long getNotNullLong(Long data) {
		if(data == null) {
			return 0L;
		}else {
			return data;
		}
		
	}

	public static Double getNotNullDouble(Double data) {
		if(data == null) {
			return 0d;
		}else {
			return data;
		}
		
	}
	
}
