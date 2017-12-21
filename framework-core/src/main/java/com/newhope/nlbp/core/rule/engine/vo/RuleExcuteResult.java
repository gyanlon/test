package com.newhope.nlbp.core.rule.engine.vo;

import java.util.Map;

public class RuleExcuteResult {
	private Boolean resultFlag;

	private Map<String, Object> resultContext;

	public RuleExcuteResult() {
		super();
	}

	public RuleExcuteResult(Boolean resultFlag, Map<String, Object> resultContext) {
		super();
		this.resultFlag = resultFlag;
		this.resultContext = resultContext;
	}

	/**
	 * @return the resultFlag
	 */
	public Boolean getResultFlag() {
		return resultFlag;
	}

	/**
	 * @param resultFlag
	 *            the resultFlag to set
	 */
	public void setResultFlag(Boolean resultFlag) {
		this.resultFlag = resultFlag;
	}

	/**
	 * @return the resultContext
	 */
	public Map<String, Object> getResultContext() {
		return resultContext;
	}

	/**
	 * @param resultContext
	 *            the resultContext to set
	 */
	public void setResultContext(Map<String, Object> resultContext) {
		this.resultContext = resultContext;
	}
}
