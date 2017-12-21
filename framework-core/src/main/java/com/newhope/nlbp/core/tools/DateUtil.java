package com.newhope.nlbp.core.tools;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    
    public static final long DAYMILLI = 24 * 60 * 60 * 1000;

    public static final long HOURMILLI = 60 * 60 * 1000;

    public static final long MINUTEMILLI = 60 * 1000;

    public static final long SECONDMILLI = 1000;

    public static final String DB_TIME_PATTERN = "yyyyMMddHHmmssSSS";

    public static final String DB_TIME_PATTERN_1 = "yyyy/MM/dd HH:mm:ss.SSS";

    public static final String DB_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DB_TIME_PATTERN_3 = "yyyy-MM-dd HH:mm:ss";

    public static final String DB_TIME_PATTERN_4 = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    public static final String DATE_FMT_0 = "yyyyMMdd";

    public static final String DATE_FMT_1 = "yyyy/MM/dd";

    public static final String DATE_FMT_2 = "yyyy-MM-dd";
    
    public static final int DECEMBER = 12;
    
    public static final String[] MONTHS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    
    public static java.util.Date toDate(String sDate)
    {
        return toDate(sDate, DB_TIME_PATTERN_3);
    }

    public static java.util.Date toDate(String sDate, String sFmt)
    {
        SimpleDateFormat sdfFrom = null;
        java.util.Date dt = null;
        
        try
        {
            sdfFrom = new SimpleDateFormat(sFmt);
            dt = sdfFrom.parse(sDate);
        } catch (Exception e) {
            //e.printStackTrace();
        	logger.error("toDate",e);
            return null;
        } finally
        {
            sdfFrom = null;
        }
        return dt;
    }

    public static java.sql.Date toSqlDate(String sDate) {
        return toSqlDate(sDate, DATE_FMT_1);
    }

    public static java.sql.Date toSqlDate(String sDate, String sFmt) {
        if (sDate == null) {
            return null;
        }
        return java.sql.Date.valueOf(formatDate(sDate, sFmt, DATE_FMT_2));
    }

    public static java.sql.Timestamp toSqlTimestamp(String sDate)
    {
        return toSqlTimestamp(sDate, DATE_FMT_1);
    }

    public static java.sql.Timestamp toSqlTimestamp(String sDate,String sFmt)
    {
        if (sDate == null)
        {
            return null;
        }
        return java.sql.Timestamp.valueOf(formatDate(sDate, sFmt, DB_TIME_PATTERN_2));
    }

    public static String toString(java.util.Date dt)
    {
        return toString(dt, DB_TIME_PATTERN);
    }
    
    public static String toStringForThree(java.util.Date dt)
    {
        return toString(dt, DB_TIME_PATTERN_3);
    }

    public static String toString(java.util.Date dt, String sFmt)
    {
        SimpleDateFormat sdfFrom = null;
        String sRet = null;

        try
        {
            sdfFrom = new SimpleDateFormat(sFmt);
            sRet = sdfFrom.format(dt).toString();
        } catch (Exception e)
        {
            //e.printStackTrace();
        	logger.error("toString",e);
            return null;
        } finally
        {
            sdfFrom = null;
        }

        return sRet;
    }

    public static String formatDate(String sDate,String sFmtFrom,String sFmtTo)
    {
        SimpleDateFormat sdfFrom = null;
        SimpleDateFormat sdfTo = null;
        java.util.Date dt = null;

        if (sDate == null)
        {
            return sDate;
        }

        try
        {
            sdfFrom = new SimpleDateFormat(sFmtFrom);
            dt = sdfFrom.parse(sDate);
            sdfTo = new SimpleDateFormat(sFmtTo);
            return sdfTo.format(dt);

        } catch (Exception e)
        {
            //e.printStackTrace();
        	logger.error("formatDate",e);
            return sDate;
        }
    }

    /**
	 * 将带时间格式字符转换成时间类 
	 * 
	 * @param dateStr
	 *            带时间格式的字符 
	 * @param pattern
	 *            格式化字符串
	 * @return 转换后时间类型数 
	 */
	public static Date parseDate(String dateStr, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			//e.printStackTrace();
			logger.error("parseDate",e);
		}
		return date;
	}

	/**
	 * 自定义格式化时间
	 * 
	 * @param date
	 *            时间
	 * @param formatStr
	 *            格式化字符串，默MM/dd/yyyy
	 * @return 格式化的时间字符
	 */
	public static String formartDate(Date date, String formatStr) {
		if (date == null)
			date = new Date();
		if (StringUtils.isEmpty(formatStr)) {
			formatStr = "MM/dd/yyyy";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}
	
    public static String getDateFromDispDate(String strDispDate)
    {
        String strY = "";
        String strM = "";
        String strD = "";
        
        if (!strDispDate.equals(""))
        {
            if (strDispDate.length() > 7)
            {
                strY = strDispDate.substring(0, 4);
                strM = strDispDate.substring(5, 7);
                strD = strDispDate.substring(8, 10);
                strDispDate = strY + strM + strD;
            } else {
                strY = strDispDate.substring(0, 4);
                strM = strDispDate.substring(5, 7);
                strDispDate = strY + strM;
            }
        }
        return strDispDate;
    }
    
   
    public static String getDispDateFromDate(String strDate)
    {
        if (!strDate.equals(""))
        {
            if (strDate.length() > 6)
            {
                strDate = strDate.substring(0, 4) + "-" + strDate.substring(4, 6) + "-" + strDate.substring(6, 8);
            } else
            {
                strDate = strDate.substring(0, 4) + "-" + strDate.substring(4, 6);
            }
        }
        return strDate;
    }

    public static String addYears(String date, int year)
    {

        if (date == null || "".equals(date))
        {
            return date;
        }

        if (year == 0)
        {
            return date;
        }

        boolean longFlg = false;

        try
        {
            if (date.length() == 10)
            {
                longFlg = true;
                date = getDateFromDispDate(date);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_0);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR) + year);
            date = dateFormat.format(calendar.getTime());

            if (longFlg)
            {
                date = getDispDateFromDate(date);
            }
            return date;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    public static String addDays(String date, int day)
    {

        if (date == null || "".equals(date))
        {
            return date;
        }

        if (day == 0)
        {
            return date;
        }

        boolean longFlg = false;

        try {
            if (date.length() == 10)
            {
                longFlg = true;
                date = getDateFromDispDate(date);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_0);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) + day);

            date = dateFormat.format(calendar.getTime());

            if (longFlg)
            {
                date = getDispDateFromDate(date);
            }

            return date;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    public static String addDays(Date date, int day,String fmt)
    {
        if (date == null ) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) + day);

            return dateFormat.format(calendar.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String subDays(Date date, int day,String fmt)
    {
        if (date == null ) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) - day);

            return dateFormat.format(calendar.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String subDays(Date date, int day)
    {

        if (date == null)
        {
            return toString(date,DATE_FMT_2);
        }

        if (day == 0)
        {
            return toString(date,DATE_FMT_2);
        }
        try {
           

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_0);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) - day);
            return toString(calendar.getTime(),DATE_FMT_2);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    
    public static String addDaysRtnFmt(Date date, int day,String fmt)
    {
 
    	if(fmt==null) {
    		fmt = DATE_FMT_2;
    	}
        if (date == null)
        {
            return toString(date,fmt);
        }

        if (day == 0)
        {
            return toString(date,fmt);
        }
        try {
           

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_0);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) + day);
            return toString(calendar.getTime(),fmt);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }
    
    public static String addMonths(String date, int months)
    {
        if (date == null || date.equals("") || date.length() != 6 ){
            return date; 
        }
        if (months == 0){
            return date; 
        }
        String day = date + "01";
        String newdate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_0);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(day));
            calendar.set(Calendar.MONTH,
                         calendar.get(Calendar.MONTH) + months);
            newdate = dateFormat.format(calendar.getTime());
            return newdate.substring(0,6);
        } catch (Exception ex) {
            return "";
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getSpeMonth(String yearMonFrom, String yearMonTo) {
        List monList = new ArrayList();
        
        if (yearMonFrom.compareTo(yearMonTo) <= 0) {

            monList.add(yearMonFrom.substring(0, 4)
                        + "-"
                        + yearMonFrom.substring(4, 6));
            
            String nextMon = addMonths(yearMonFrom, 1);
            
            while (nextMon.compareTo(yearMonTo) <= 0) {

                monList.add(nextMon.subSequence(0, 4) 
                            + "-"
                            + nextMon.substring(4, 6));

                nextMon = addMonths(nextMon, 1);
            }

        }
        
        return monList;
    }
    
    public static String addMonths(String date, String months) {
        try {
            return addMonths(date, Integer.parseInt(months));
        } catch (Exception ex) {
            return "";
        }

    }

    public static String addDays(String date, String day) {
        try {
            return addDays(date, Integer.parseInt(day));
        } catch (Exception ex) {
            return "";
        }

    }
   
    public static String fmtDate(String sYear, String sMonth, String sDay) {
        if (sMonth.length() == 1)
            sMonth = "0" + sMonth;
        if (sDay.length() == 1)
            sDay = "0" + sDay;
        return sYear + sMonth + sDay;
    }

    public static int getMonthLength(int year, int month) {
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
                return 29;
            else
                return 28;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11)
            return 30;
        else
            return 31;
    }
    
    public static int getDay(String date) {
        if (date == null) return -1;
        if (date.equals("")) return -1;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_1);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));
           
            return calendar.get(Calendar.DAY_OF_WEEK);
        } catch (Exception ex) {
            return -1;
        }
    }
    
    public static String changeFmt(String year,String month) {
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        String date = year + "-" + month;
        return date;
    }
    
    public static String changeToYearMon(String year,String month) {
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        String date = year + month;
        return date;
    }
    
    public static String getLastMonEndDay(String yearMon) {
        String beginDate = 
            getDispDateFromDate(addDays(yearMon + "01",-1));
        return beginDate;
    }

    public static String getCurMonEndDay(String date) {
        int year = Integer.parseInt(date.substring(0 ,4));
        int month = Integer.parseInt(date.substring(4 ,6));
        int lastDay = getMonthLength(year, month);
        String endDate = getDispDateFromDate(date + lastDay);
        return endDate;
    }
    
    public static String getNextMonBeginDay(String yearMon) {
        String endDate = 
            getDispDateFromDate(Integer
            .toString(Integer.parseInt(yearMon) + 1) + "01");  
        return endDate;
    }
    
    public static long countDays(String fromDate,String endDate) {
        Timestamp t1 = Timestamp.valueOf(formatDate(fromDate, DATE_FMT_1, DB_TIME_PATTERN_2));
        Timestamp t2 = Timestamp.valueOf(formatDate(endDate, DATE_FMT_1, DB_TIME_PATTERN_2));
        return (t2.getTime() - t1.getTime())/DAYMILLI + 1;
    }

    public static int[] convertSecond(int second) {
        int[] time = new int[2];
        time[0] = second / 60;
        time[1] = second - time[0] * 60;
        if (second < 0) {
            time[1] = -time[1];
        }
        return time;
    }

    public static int convertHour(int second, int hour) {
        if (hour < 0) {
            second = hour * 60 - second;
        } else {
            second = hour * 60 + second;
        }
        return second;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getYearMonthList() {

        List monthList = new ArrayList();

        for (int i = 0; i < DECEMBER; i++) {
            monthList.add(MONTHS[i]);
        }

        return monthList;
    }

    /**
     * 获得date的星期名称,如星期一, 星期天等
     * @param date
     * @return
     */
    public static String getWeekName(Date date)
    {
        int firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
        //logger.debug("firstDayOfWeek:" + firstDayOfWeek);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //logger.debug("dayOfWeek:" + dayOfWeek);
        final String[] weekNames = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        return weekNames[dayOfWeek - firstDayOfWeek];
    }
    
    public static java.sql.Date getCurDate()
    {
        return new java.sql.Date(System.currentTimeMillis());
    }

    public static String addDayHourMinute(String day, String hour, String minute) {
        Calendar cal = Calendar.getInstance();
        if (!day.trim().equals("")) {
            logger.debug("not blank" + day);
            logger.debug("day is " + day);
        cal.add(Calendar.DATE, Integer.parseInt(day));
        }
        if (!hour.trim().equals("")) {
        cal.add(Calendar.HOUR, Integer.parseInt(hour));
        }
        if (!minute.trim().equals("")) {
        cal.add(Calendar.MINUTE, Integer.parseInt(minute));
        }
        return toString(new java.util.Date(cal.getTimeInMillis()), DB_TIME_PATTERN_3);
    }

    
    public static boolean isValidDate(String dateString, String dateFormatPattern) {
        Date validDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        try {
            dateFormat.applyPattern(dateFormatPattern);
            dateFormat.setLenient(false);
            validDate = dateFormat.parse(dateString);
        }
        catch (Exception e) {
            // Ignore and return null
        }
        return validDate != null;
    }   
    
    //
    public static int getCurrentYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    //
    public static int getCurrentMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
    
    //
    public static String getCurrentDate()
    {
        return toString(new java.util.Date(),DATE_FMT_2);
    }   
    
    //
    public static String getFirstDateOfThisMonth()
    {
        String curYear = String.valueOf(getCurrentYear());
        String curMonth;
        if (getCurrentMonth() < 10)
        {
            curMonth = "0" + String.valueOf(getCurrentMonth());
        }
        else
        {
            curMonth = String.valueOf(getCurrentMonth());
        }
        return  curYear + "-" + curMonth + "-01";
    }

    /**
     * 
     * @param c
     * @param monthendBeforeDays
     * @param adjustWeekend
     * @return
     */
    public static boolean isInMonthendBefore(Calendar c, int monthendBeforeDays, boolean adjustWeekend)
    {
         int month = c.get(Calendar.MONTH);
         int year = c.get(Calendar.YEAR);
         int date = c.get(Calendar.DATE);
         //
         int feb = year % 400 != 0 && year % 4 == 0 ? 29 : 28;
         int enddate = new int[]{31,feb,31, 30,31,30, 31,31,30, 31,30,31}[month];
         int s0 = enddate - monthendBeforeDays + 1;
         Calendar _c = Calendar.getInstance();
        if(adjustWeekend)
        {
            _c.set(Calendar.DATE, s0);
            int w = _c.get(Calendar.DAY_OF_WEEK); //0-周日,6-周六
            s0 = s0 - (w == 0 ? 2 : (w== 6 ? 1 : 0)); //对周末进行调整
        }
        //
        if(date >= s0)
        {
            return true;
        }
        //
        return false;
    }
    
    /**
     * 计算日期间隔的天数
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     * @pre beginDate != null
     */
    public static int daysBetweenDates(Date beginDate, Date endDate)
    {
        int days = 0;
        Calendar calo = Calendar.getInstance();
        Calendar caln = Calendar.getInstance();
        calo.setTime(beginDate);
        caln.setTime(endDate);
        int oday = calo.get(6);
        int nyear = caln.get(1);
        for (int oyear = calo.get(1); nyear > oyear;)
        {
            calo.set(2, 11);
            calo.set(5, 31);
            days += calo.get(6);
            oyear++;
            calo.set(1, oyear);
        }

        int nday = caln.get(6);
        days = (days + nday) - oday;
        return days;
    }
    
    /**
     * 将一个日期值只保留年月日的值，去掉时分秒
     * @author zhaohai
     * @date: 2017年1月10日 下午8:51:50
     * @param oldDate
     * @return
     */
    public static Date reserveDate(Date oldDate){
    	if(oldDate == null){
    		return null;
    	}
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(oldDate);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.SECOND,0);
    	calendar.set(Calendar.MINUTE,0);
    	return calendar.getTime();
    }
    
    public static Date getSpecifiedDayBefore(Date specifiedDay) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
          
        return c.getTime();  
    }
    
    /**
     * 得到前几天的日期
     * @author dugang
     * @date: 2017年3月14日 下午5:50:12
     * @param oldDate
     * @param days
     * @return
     */
    public static Date getPreDate(Date oldDate,int days) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(oldDate);  
    	cal.add(Calendar.DATE,   0-days);
    	return cal.getTime();
    }
    
    /**
     * 判断某个日期是否在某个日期前
     * @author dugang
     * @date: 2017年3月24日 下午5:32:32
     * @param targetDate
     * @param originalDate
     * @return
     */
    public static int checkBeforeDate(Date targetDate,Date originalDate) {
    	if(targetDate == null || originalDate == null) {
    		return 0;
    	}else {
    		long times = targetDate.getTime() - originalDate.getTime();
    		if(times >0) {
    			return -1;
    		}else if(times == 0) {
    			return 0;
    		}else if(times <0) {
    			return 1;
    		}
    		
    		return -1;
    	}
    }
    
    public static Date addDays(Date targetDate,int days) {
    	Calendar calendar = new GregorianCalendar(); 
        calendar.setTime(targetDate); 
        calendar.add(calendar.DATE,days);//把日期往后增加一天.整数往后推,负数往前移动 
		return calendar.getTime();
    }
    
	public static void main(String[] args) {
		//System.out.println(addDaysRtnFmt(new Date(), 1,null));
		//System.out.println(toString(DateUtil.getPreDate(new Date(), -60)));
		System.out.println(DateUtil.addDays(new Date(), 1,DATE_FMT_2));
	}
}
