package com.newhope.nlbp.core.warning.service.impl;

import java.io.Serializable;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.springframework.stereotype.Service;

import com.newhope.nlbp.core.rule.engine.RuleEngine;
import com.newhope.nlbp.core.warning.service.IWarningAnalysisService;

@Service
public class WarningAnalysisServiceImpl implements IWarningAnalysisService {
	@SuppressWarnings("unchecked")
	public <T> T executeExpression(Map<String, Object> map, String condition, String action) {
		T tValue = null;
		try {
			// 检查是否满足校验条件
			if (RuleEngine.executeCondition(map, condition)) {

				// 初始化ParserContext对象
				ParserContext ctx = new ParserContext();
				//导入map包
				ctx.addPackageImport(Map.class.getPackage().getName());
				//导入报警封装bean
//				ctx.addImport(WarningResultBean.class);
				Serializable s = MVEL.compileExpression(action, ctx);
				tValue = (T) MVEL.executeExpression(s, map);
			}
			return tValue;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(String.format("规则引擎执行异常,执行条件[%s]表达式[%s]:", condition, action), e);
		}
	}
	
	public <T> T executeExpression(Map<String, Object> map, String action) {
		String condition = "return true;";
		return executeExpression(map, condition, action);
	}
}
