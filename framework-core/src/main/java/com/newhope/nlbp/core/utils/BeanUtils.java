package com.newhope.nlbp.core.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtils {

    private static Method getDeclareMethod(Object obj, String methodName) {
        Method method = null;
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName);
                break;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return method;
    }

    private static Method getDeclareMethod(Object obj, String methodName, Class<?>[] types) {
        Method method = null;
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, types);
                break;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return method;
    }

    private static Field getDeclareField(Object obj, String fieldName) {
        Field field = null;
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return field;
    }

    private static Field[] getDeclareFields(Object obj) {
        Class<?> origClazz = obj.getClass();
        List<Field> fields = new ArrayList<Field>();
        for (; origClazz != Object.class; origClazz = origClazz.getSuperclass()) {
            try {
                Field[] _fields = origClazz.getDeclaredFields();
                for (Field field : _fields)
                    fields.add(field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Field[] fs = new Field[fields.size()];
        int i = 0;
        for (Field f : fields)
            fs[i++] = f;
        return fs;
    }

    /**
     * 从源对象拷贝属性值到目标对象
     *
     * @param dest 目标对象
     * @param orig 源对象
     */
    public static void copyProperties(Object dest, Object orig) {
        Field[] fields = getDeclareFields(orig);
        for (Field field : fields) {
            try {
                copyProperty(dest, orig, field);
            } catch (SecurityException e) {
                e.printStackTrace();
                continue;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 从源对象拷贝属性值到目标对象
     *
     * @param dest          目标对象
     * @param orig          源对象
     * @param strFieldNames 需要拷贝的属性集合,用逗号分开,eg: "name,age,birthday"
     */
    public static void copyProperties(Object dest, Object orig, String strFieldNames) {
        if (StringUtils.isEmpty(strFieldNames))
            return;
        String[] fieldNames = strFieldNames.split(",");
        for (String fieldName : fieldNames) {
            try {
                Field field = getDeclareField(orig, fieldName);
                ;
                copyProperty(dest, orig, field);
            } catch (SecurityException e) {
                e.printStackTrace();
                continue;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 从源对象拷贝属性值到目标对象
     *
     * @param dest          目标对象
     * @param orig          源对象
     * @param strFieldNames 需要排除的属性集合,用逗号分开,eg: "name,age,birthday"
     */
    public static void copyExcludeProperties(Object dest, Object orig, String strFieldNames) {
        if (StringUtils.isEmpty(strFieldNames)) {
            copyProperties(dest, orig);
            return;
        }

        String[] fieldNames = strFieldNames.split(",");

        Field[] fields = getDeclareFields(orig);
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                if (exclude(fieldNames, fieldName))
                    continue;
                copyProperty(dest, orig, field);
            } catch (SecurityException e) {
                e.printStackTrace();
                continue;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private static boolean exclude(String[] fieldNames, String fieldName) {
        for (String _fieldName : fieldNames) {
            if (_fieldName.equals(fieldName))
                return true;
        }
        return false;
    }

    private static void copyProperty(Object dest, Object orig, Field field) throws Exception {
        String fieldName = field.getName();
        String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String getMethodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (field.getType() == Boolean.class) {
            getMethodName = "is" + getMethodName;
        } else {
            getMethodName = "get" + getMethodName;
        }
        try {
            //Method setMethod = dest.getClass().getDeclaredMethod(setMethodName, new Class[]{field.getType()});
            Method setMethod = getDeclareMethod(dest, setMethodName, new Class[]{field.getType()});
            //Method getMethod = orig.getClass().getDeclaredMethod(getMethodName);
            Method getMethod = getDeclareMethod(orig, getMethodName);
            if (getMethod != null) {
                Object value = getMethod.invoke(orig);
                //if (value != null) {
                    if (setMethod != null) {
                        setMethod.invoke(dest, value);
                    }
                //}
            }

        } catch (Exception e) {
            e.printStackTrace();
            //throw e;
        }
    }


    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
    
	/**
	 * 动态获取对象属性
	 * @param srcObj
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(Object srcObj, String propertyName){
		try {
			PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(srcObj.getClass(), propertyName);
			Method readMethod = pd.getReadMethod();
			Object propertyValue = readMethod.invoke(srcObj, readMethod.getParameterTypes());
			return propertyValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * 动态设置对象属性
	 * @param srcObj
	 * @param propertyName
	 * @return
	 */
	public static void setPropertyValue(Object srcObj, String propertyName, Object propertyValue){
		try {
			PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(srcObj.getClass(), propertyName);
			Method writeMethod = pd.getWriteMethod();
			writeMethod.invoke(srcObj, propertyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
}
