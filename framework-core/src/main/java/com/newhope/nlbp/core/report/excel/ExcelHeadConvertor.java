package com.newhope.nlbp.core.report.excel;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelHeadConvertor {

	/**
	 * 传入easyUI 配置列json数据格式
	 * @param sheetName
	 * @param headColumnsJsonArray
	 * @return
	 */
	public static ExcelHeadNode convertEasyUISimpleHead(String sheetName, JSONArray headColumnsJsonArray){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		excelHeadNode.setValue(sheetName);
		for(int i = 0; i<headColumnsJsonArray.size(); i++){
			JSONObject jsonColumn = headColumnsJsonArray.getJSONObject(i);
			
			ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
			if(jsonColumn.get("hidden")!=null
					&& jsonColumn.get("hidden").equals(true)){
				continue;
			}
			
			tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
			tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
			
			if(jsonColumn.get("width") != null){
				String withString = (String) jsonColumn.get("width");
				tmpExcelHeadNode.setCellStyleWidth(Integer.valueOf(withString.replaceAll("%", ""))*2);
			}
			
			excelHeadNode.getChildNodes().add(tmpExcelHeadNode);
		}
		return excelHeadNode;
	}
	
	/**
	 * 传入easyUI 配置列字符串数据格式
	 * @param sheetName
	 * @param headColumnsJsonString
	 * @return
	 */
	public static ExcelHeadNode convertEasyUISimpleHead(String sheetName, String headColumnsJsonString){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		headColumnsJsonString = headColumnsJsonString.replaceAll("&quot;", "\\\"");
		JSONArray headColumnsJsonArray = JSONObject.parseArray(headColumnsJsonString);
		excelHeadNode = convertEasyUISimpleHead(sheetName, headColumnsJsonArray);
		return excelHeadNode;
	}	
	
	public static ExcelHeadNode convertEasyUIAnalysisHead(String sheetName, String headColumnsJsonString){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		headColumnsJsonString = headColumnsJsonString.replaceAll("&quot;", "\\\"");
		JSONArray headColumnsJsonArray = JSONObject.parseArray(headColumnsJsonString);
		
		Map<String, ExcelHeadNode> batchMap = new HashMap<String, ExcelHeadNode>();
		
		excelHeadNode.setValue(sheetName);
		
		for(int i=0; i<headColumnsJsonArray.size(); i++){
			JSONArray levelColumns = headColumnsJsonArray.getJSONArray(i);
			
			//设置第一列
			if(i==0){
					JSONObject jsonColumn = levelColumns.getJSONObject(0);
					
					ExcelHeadNode dateWeek = new ExcelHeadNode();
					
					ExcelHeadNode endDateWeek = dateWeek;
					
					for(int j=2; j<headColumnsJsonArray.size(); j++){// j=2 第一层上面已经处理 + 实际比行数少了一层
						ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
						if(j == headColumnsJsonArray.size()-1){
							tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
							tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
						}
						dateWeek.getChildNodes().add(tmpExcelHeadNode);
						dateWeek = tmpExcelHeadNode;
					}
					excelHeadNode.getChildNodes().add(endDateWeek);
			}
			
			if(i==1){
				for(int j=0; j<levelColumns.size(); j++){
					JSONObject jsonColumn = levelColumns.getJSONObject(j);
					ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
					tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
					tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
					String batchId = tmpExcelHeadNode.getKey().split("_")[0];
					batchMap.put(batchId, tmpExcelHeadNode);
					
					excelHeadNode.getChildNodes().add(tmpExcelHeadNode);
				}
			}
			
			if(i==2){
				for(int j=0; j<levelColumns.size(); j++){
					JSONObject jsonColumn = levelColumns.getJSONObject(j);
					ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
					tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
					tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
					
					//设置特殊列
					String batchId = tmpExcelHeadNode.getKey().split("_")[0];
					
					ExcelHeadNode tmp = batchMap.get(batchId);
					tmp.getChildNodes().add(tmpExcelHeadNode);
				}
			}
			
		}
		
		return excelHeadNode;
	}
	/**
	 * 生产分析报表
	 * */
	public static ExcelHeadNode convertEasyUIAnalysisHeadByBreedProduct(String sheetName, String headColumnsJsonString){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		headColumnsJsonString = headColumnsJsonString.replaceAll("&quot;", "\\\"");
		JSONArray headColumnsJsonArray = JSONObject.parseArray(headColumnsJsonString);
		
		Map<String, ExcelHeadNode> batchMap = new HashMap<String, ExcelHeadNode>();
		
		excelHeadNode.setValue(sheetName);
		
		for(int i=0; i<headColumnsJsonArray.size(); i++){
			JSONArray levelColumns = headColumnsJsonArray.getJSONArray(i);
			
			//设置第一列
			if(i==0){
					JSONObject jsonColumn = levelColumns.getJSONObject(0);
					
					ExcelHeadNode dateWeek = new ExcelHeadNode();
					
					ExcelHeadNode endDateWeek = dateWeek;
					
					for(int j=2; j<headColumnsJsonArray.size(); j++){// j=2 第一层上面已经处理 + 实际比行数少了一层
						ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
						if(j == headColumnsJsonArray.size()-1){
							tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
							tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
						}
						dateWeek.getChildNodes().add(tmpExcelHeadNode);
						dateWeek = tmpExcelHeadNode;
					}
					excelHeadNode.getChildNodes().add(endDateWeek);
			}
			
			if(i==1){
				for(int j=0; j<levelColumns.size(); j++){
					JSONObject jsonColumn = levelColumns.getJSONObject(j);
					ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
					tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
					tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
					String batchId = tmpExcelHeadNode.getKey().split("_")[0];
					batchMap.put(batchId, tmpExcelHeadNode);
					
					excelHeadNode.getChildNodes().add(tmpExcelHeadNode);
				}
			}
			
			if(i==2){
				for(int j=0; j<levelColumns.size(); j++){
					JSONObject jsonColumn = levelColumns.getJSONObject(j);
					ExcelHeadNode tmpExcelHeadNode = new ExcelHeadNode();
					tmpExcelHeadNode.setKey(String.valueOf(jsonColumn.get("field")));
					tmpExcelHeadNode.setValue(String.valueOf(jsonColumn.get("title")));
					
					//设置特殊列
					String batchId = tmpExcelHeadNode.getKey().split("_")[2];
					
					ExcelHeadNode tmp = batchMap.get(batchId);
					tmp.getChildNodes().add(tmpExcelHeadNode);
				}
			}
			
		}
		
		return excelHeadNode;
	}
}
