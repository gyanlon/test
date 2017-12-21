package com.newhope.nlbp.core.pipeline;

import java.util.Map;

/**
 * 流水线模式：数据上下文对象
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public interface Context {
	Map<Object, Object> getAttributes();
}
