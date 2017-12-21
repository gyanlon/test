/**
 * @项目名称:nlbp-common
 * @文件名称:WeekAgeConverter.java
 * @Date  :2017年1月7日
 * @Copyright: 2017 XX All rights reserved.
 *
 */
package com.newhope.nlbp.core.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author dugang
 * @date: 2017年1月7日 下午2:55:16
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class WeekAgeUtils {

	/**
	 * 根据起始时间获取到当前时间的周龄
	 * 
	 * @author dugang
	 * @date: 2017年1月7日 下午2:56:24
	 * @param startDate
	 * @return 周龄和日龄
	 */
	public static Map<String, String> getWeekAge(Date startDate,Date endDate) {
		if(startDate == null) {
			return null;
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(startDate);
		if(endDate == null) {
			endCal.setTime(new Date());
		}else {
			endCal.setTime(endDate);
		}

		long startTime = startCal.getTimeInMillis();
		long endTime = endCal.getTimeInMillis();
		long days = (endTime - startTime) / (1000 * 3600 * 24);

		returnMap.put("DAYS", (days+1) + "");

		Long week = days / 7;
		Long weekEndDay = days % 7;
		String weekStr = "";
		if (week == 0) {
			weekStr = "1-" + (weekEndDay.intValue()+1);// ex:1-5
		} else if (week >= 1) {
			if (weekEndDay == null || (weekEndDay == 0 && week==0)) {// 第7天
				weekStr = "1-7";
			} else {
				Long tempWeek = week + 1;
				weekStr = tempWeek + "-" + (weekEndDay+1);
			}
		} else if(week <0) {
			weekStr = "0-0" ;
		}
		returnMap.put("WEEKS", weekStr);
		
		String weekWeek = "0";
		String weekDay = "0";
		if(StringUtils.isNotEmpty(weekStr)) {
			String weekArr[]= weekStr.split("-");
			weekWeek = weekArr[0];
			weekDay = weekArr[1];
		}
		returnMap.put("WEEKS-WEEK", weekWeek);
		returnMap.put("WEEKS-DAY", weekDay);
		
		return returnMap;
	}

	
//	public static void main(String[] args) throws Exception {
//		String dateStr = "2017-02-09";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date d = sdf.parse(dateStr);
//		System.out.println(WeekAgeUtils.getWeekAge(d));
//	}
}
