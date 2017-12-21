package com.newhope.nlbp.core.rule.engine;

import java.util.Map;

public interface IRuleAnalysisService {

	public <T> T executeExpression(Map<String, Object> map, String condition, String action, String functionNames);

}
