package com.newhope.nlbp.core.warning.service;

import java.util.Map;

public interface IWarningAnalysisService {
	public <T> T executeExpression(Map<String, Object> map, String condition, String action);
	public <T> T executeExpression(Map<String, Object> map, String action);
}
