package com.newhope.nlbp.core.pipeline;

/**
 * 流水线模式：流水线总阶段
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public interface Pipeline extends Stage {

	 public void addStage (Stage stage);	 
}
