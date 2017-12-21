package com.newhope.nlbp.core.rule.engine.example;

import java.util.HashMap;
import java.util.Map;

import com.newhope.nlbp.core.rule.engine.IRuleAnalysisService;
import com.newhope.nlbp.core.rule.engine.RuleEngine;
import com.newhope.nlbp.core.rule.engine.impl.RuleAnalysisServiceImpl;
import com.newhope.nlbp.core.rule.engine.vo.RuleExcuteResult;

public class Test {
	
	
/*	public static void main(String[] args){
		
		Map dataMap = new HashMap();
		Test t = new Test();
		A a = new A();
		a.setAttr1("attr1ashdfjk");
		a.setAttr2("attr2asdfasdf");
		
		dataMap.put("name", "111");
		dataMap.put("class", "class1");
		dataMap.put("A", a);
		RuleExcuteResult result = new RuleExcuteResult();
		result.setResultFlag(true);
		result.setResultContext(dataMap);
		String ruleExpress = "RuleExcuteResult result = new RuleExcuteResult();"
				+ "result.setResultFlag(true);"
				+ "Map resultContext = new HashMap();"
				+ "resultContext.put('name', '1111');"
				+ "result.setResultContext(resultContext);"
				+ "return result;";
		//"return '你好啊， 这里是中华人名共和国' + name + '哈哈哈哈' + class + '  A:' + A.attr1;"
		
		String message = RuleEngine.execute(dataMap, "return '你好啊， 这里是中华人名共和国' + name + '哈哈哈哈' + class + '  A:' + A.attr1;");
		
		System.out.println(message);
		
		IRuleAnalysisService ruleAnalysisService = new RuleAnalysisServiceImpl();
//		ruleAnalysisService.executeExpression(map, condition, action, functionNames);
		
		RuleExcuteResult oo = ruleAnalysisService.executeExpression(dataMap, "return true;", ruleExpress, null);
		System.out.println(oo.getResultFlag());
		
		String ruleExpress2 = "java.util.Map resultMap = new java.util.HashMap();"
				+ "com.newhope.nlbp.core.rule.engine.vo.RuleExcuteResult mm = new com.newhope.nlbp.core.rule.engine.vo.RuleExcuteResult();"
				+ "mm.setResultFlag(true);"
				+ "int a = 3;"
				+ "int b = 5;"
				+ "int c = a*b;"
				+ "resultMap.put('name', 'name1');"
				+ "resultMap.put('a', a);"
				+ "resultMap.put('c', c);"
				+ "resultMap.put('mm', mm);"
				+ "return resultMap;";
		
		Map mm = RuleEngine.execute(dataMap, ruleExpress2);
		
		System.out.println(((RuleExcuteResult)mm.get("mm")).getResultFlag());
	}*/
	

}

class A{
	String attr1;
	String attr2;
	
	public void A(){
		
	};
	
	public void A(String attr1, String attr2){
		this.attr1 = attr1;
		this.attr2 = attr2;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
}
