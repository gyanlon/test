package com.newhope.nlbp.core.pipeline;

/**
 * 流水线模式：阶段接口
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public interface Stage {
	
	public boolean execute(Context context);
	
}
