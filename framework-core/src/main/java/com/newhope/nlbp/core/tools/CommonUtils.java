package com.newhope.nlbp.core.tools;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author ibmzhanghua
 * @date: Jan 5, 2017 7:35:01 PM
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class CommonUtils {
	
	public static int getWeekAgeNumber(int dayAge) {
		 int week = dayAge%7==0?dayAge/7:dayAge/7+1;
		 return week;
	}
	
	public static String getWeekAge(int dayAge) {
		int week = dayAge%7==0?dayAge/7:dayAge/7+1;
		int weekDay = dayAge%7==0?7:dayAge%7;
		return week+"-"+weekDay;
	}
	
	public static int getWeek(int dayAge) {
		int week = dayAge%7==0?dayAge/7:dayAge/7+1;
		return week;
	}
	
	/**
	 * 获取本机IP
	 * @return
	 */
	public static String getLocalIp() {
		
		String result = "";
		
        try {
        	
        	 NetworkInterface item = null;
        	
            for (Enumeration e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
            	
            	item = (NetworkInterface) e.nextElement();

                //非虚拟非回路并开启状态
                if((!item.isVirtual()) && item.isUp() && (!item.isLoopback())){
                	
                    for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    	
                        if (address.getAddress() instanceof Inet4Address) {
                        	
                            result = ((Inet4Address) address.getAddress()).getHostAddress().toString();
                            break;
                        }
                    }
                }
                
                if(StringUtils.isNotEmpty(result)) break;
            }
        } catch (IOException ex) {}
        
        return result;
    }
	
	public static void main(String[] args) {
		
		System.out.println(getWeekAge(9));
	}
	
	
}
