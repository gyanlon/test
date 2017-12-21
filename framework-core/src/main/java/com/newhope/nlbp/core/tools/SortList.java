package com.newhope.nlbp.core.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author ibmzhanghua
 * @date: Jan 13, 2017 2:01:29 PM
 * @version 1.0
 * @since JDK 1.8
 *
 * @param <E>
 */
public class SortList<E> {
    public  void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                String value1 = "";
                String value2 = "";
                Integer val1 = 0;
                Integer val2 = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method, null);
                    Method m2 = ((E) b).getClass().getMethod(method, null);
                    if (sort != null && "desc".equals(sort))// 倒序
                    {
                    	value1 = m1.invoke(((E) a), null).toString();
	                    value2 = m2.invoke(((E) b), null).toString();
	                    val1 = StringUtils.isEmpty(value1)?0:Integer.valueOf(value1);
	                    val2 = StringUtils.isEmpty(value2)?0:Integer.valueOf(value2);
                    
                        ret = val2.compareTo(val1);
                    }
                    	
                    else {
                    	// 正序
                    	value1 = m1.invoke(((E) a), null).toString();
                        value2 = m2.invoke(((E) b), null).toString();
                        val1 = StringUtils.isEmpty(value1)?0:Integer.valueOf(value1);
                        val2 = StringUtils.isEmpty(value2)?0:Integer.valueOf(value2);
                        ret = val1.compareTo(val2) ;
                    }
                } catch (NoSuchMethodException ne) {
                    System.out.println(ne);
                } catch (IllegalAccessException ie) {
                    System.out.println(ie);
                } catch (InvocationTargetException it) {
                    System.out.println(it);
                }
                return ret;
            }
        });
    }
}