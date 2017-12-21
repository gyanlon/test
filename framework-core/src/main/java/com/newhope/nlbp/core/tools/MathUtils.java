package com.newhope.nlbp.core.tools;

import java.math.BigDecimal;

/**
 * 
 * @author ibmzhanghua
 * @date: Jan 12, 2017 5:57:29 PM
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class MathUtils{
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    //这个类不能实例化
    private MathUtils(){
    }
 
    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    } 
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 提供精确的乘法运算 保留几位小数。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2,int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return round(b1.multiply(b2).doubleValue(),scale);
    }
 
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }
 
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
 
    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    //   --------简单除法兼容--------
    public static Double simpleDiv(Long v1, Long v2, int scale){
    	if(v1 == null 
    			|| v2 == null
    			|| v2 == 0L){
    		return 0.0;
    	}else{
    		return div(v1.doubleValue(), v2.doubleValue(), scale);
    	}
    }
    
    public static Double simpleDiv(Double v1, Double v2, int scale){
    	if(v1 == null 
    			|| v2 == null
    			|| v2 == 0L){
    		return 0.0;
    	}else{
    		return div(v1, v2, scale);
    	}
    }
    
    public static Double simpleDiv(Long v1, Double v2, int scale){
    	if(v1 == null 
    			|| v2 == null
    			|| v2 == 0L){
    		return 0.0;
    	}else{
    		return div(v1.doubleValue(), v2.doubleValue(), scale);
    	}
    }
    
    public static Double simpleDiv(Double v1, Long v2, int scale){
    	if(v1 == null 
    			|| v2 == null
    			|| v2 == 0L){
    		return 0.0;
    	}else{
    		return div(v1.doubleValue(), v2.doubleValue(), scale);
    	}
    }    
    
	public static Double calcPercent(Long v1, Long v2){
		if(v2==null || v2==0L
				|| v1==null
				|| v1==0L){
			return new Double(0);
		}
		Double fertilizationA = MathUtils.div(new Double(v1), new Double(v2));
		Double fertilizationRate = MathUtils.mul(fertilizationA, new Double(100), 2);
		return fertilizationRate;
	}
	
	public static Double calcPercent(Double v1, Double v2){
		if(v2 == 0.0){
			return v2;
		}
		Double fertilizationA = MathUtils.div(v1, v2);
		Double fertilizationRate = MathUtils.mul(fertilizationA, new Double(100), 2);
		return fertilizationRate;
	}
	/**
	 * 简单兼容加法
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> T simpleAdd(T a, T b){
		Object result = null;
		Class mainClass = a!=null ? a.getClass() : (b!=null ? b.getClass(): null);
		if(mainClass == null){
			return (T) result;
		}
		
		try {
			
			if(mainClass == Long.class){
				result = (a==null?0L:a);
				result = (Long)(b==null?0L:b) + (Long)result;
			}else if(mainClass == Double.class){
				result = (a==null?0.0:a);
				result =MathUtils.add((Double)(b==null?0.0:b), (Double)result);
			}else if(mainClass == Integer.class){
				result = (a==null?0:a);
				result = (Integer)(b==null?0:b) + (Integer)a;
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (T) result;
	}	
};