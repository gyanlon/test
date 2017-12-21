package com.newhope.nlbp.core.rule.engine.impl;

import java.io.Serializable;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.newhope.nlbp.core.rule.engine.IRuleAnalysisService;
import com.newhope.nlbp.core.rule.engine.RuleEngine;
import com.newhope.nlbp.core.rule.engine.vo.RuleExcuteResult;

public class RuleAnalysisServiceImpl implements IRuleAnalysisService {

	@SuppressWarnings("unchecked")
	public <T> T executeExpression(Map<String, Object> map, String condition, String action, String functionNames) {
		T tValue = null;
		try {
			// 检查是否满足校验条件
			if (RuleEngine.executeCondition(map, condition)) {

				// 初始化ParserContext对象
				ParserContext ctx = new ParserContext();
				ctx.addPackageImport(Map.class.getPackage().getName());
				ctx.addImport(RuleExcuteResult.class);

				// 是否含有自定义函数
/*				if (StringUtils.isNotBlank(StringUtils.trim(functionNames))) {
					// 自定义方法名
					String[] functionNamesArray = functionNames.split(",");
					// 循环加载自定义函数
					for (String functionName : functionNamesArray) {
						String methodName = DataDictHelper.getDataDictNameByCode(functionName, "functionForMVEL");
						// 获取全部方法
						Method[] methods = RuleFunctionDefinition.class.getMethods();
						for (int i = 0; i < methods.length; i++) {
							// 匹配方法名
							if (methodName.equals(methods[i].getName())) {
								Class[] params = methods[i].getParameterTypes();
								ctx.addImport(functionName, RuleFunctionDefinition.class.getMethod(methodName, params));
								break;
							}
						}
					}
				}*/
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

}
