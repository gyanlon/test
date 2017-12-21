/**
 * 
 */
package com.newhope.nlbp.core.rule.engine;

import java.util.Map;

import org.mvel2.MVEL;

/**
 * Description: //基于MVEL的规则引擎实现类 
 * @author lzx
 * @date 2016年3月3日 上午10:57:59
 */
public class RuleEngine {
    /**
     * Description: //执行规则
     * 
     * @param ctx
     *            上下文对象
     * @param condition执行条件
     * @param action执行动作或表达式
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    @SuppressWarnings("unchecked")
    public static <T> T execute(Object ctx, String condition, String action) {
        try {
            if (executeCondition(ctx, condition)) {
                // executeAction(ctx, action);
                return executeAction(ctx, action);
            } 
                return (T) "";
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,执行条件[%s]表达式[%s]:", condition, action), e);
        }
    }
 
    /**
     * Description: //执行规则
     * 
     * @param map
     *            上下文map
     * @param condition执行条件
     * @param action执行动作或表达式
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    @SuppressWarnings("unchecked")
    public static <T> T execute(Map<String, Object> map, String condition, String action) {
        try {
            if (executeCondition(map, condition)) {
                return executeAction(map, action);
            }  
               return (T)"";
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,执行条件[%s]表达式[%s]:", condition, action), e);
        }
    }
 
    /**
     * Description: //执行规则
     * 
     * @param map
     *            上下文map
     * @param action执行动作或表达式
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    public static <T> T execute(Map<String, Object> map, String action) {
        try {
            return executeAction(map, action);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,表达式[%s]:", action), e);
        }
    }
    
    /**
     * Description: //规则执行条件检查 
     * @param ctx 上下文对象
     * @param condition 执行条件
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    private static boolean executeCondition(Object ctx, String condition) {
        try {
            return (Boolean) MVEL.eval(condition, ctx);
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,执行条件[%s]:", condition), e);
        }
    }
    
    /**
     * Description: //规则执行条件检查 
     * @param ctx 上下文对象
     * @param condition 执行条件
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    public static boolean executeCondition(Map<String, Object> map, String condition) {
        try {
            return (Boolean) MVEL.eval(condition, map);
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,执行条件[%s]:", condition), e);
        }
    }

    /**
     * Description:规则执行动作   
     * @param ctx 上下文对象
     * @param condition 执行动作
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    @SuppressWarnings("unchecked")
    private static <T> T executeAction(Object ctx, String action) {
        try {
            
            return (T) MVEL.eval(action, ctx);
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,表达式[%s]:", action), e);
       }
    }

    /**
     * Description:规则执行动作   
     * @param map 上下文map
     * @param condition 执行动作
     * @author lzx
     * @date 2016年3月3日 上午10:57:59
     */
    @SuppressWarnings("unchecked")
    private static <T> T executeAction(Map<String, Object> map, String action) {
        try {
            return (T) MVEL.eval(action, map);
        } catch (Exception e) {
            throw new RuntimeException(String.format("规则引擎执行异常,表达式[%s]:", action), e);
       }
    }
}
