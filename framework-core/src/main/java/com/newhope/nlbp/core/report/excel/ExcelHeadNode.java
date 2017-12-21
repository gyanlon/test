package com.newhope.nlbp.core.report.excel;

import java.util.ArrayList;
import java.util.List;

public class ExcelHeadNode {
	public boolean startLevel;
	public boolean endLevel;
	
	public int cellColSpan = 1;
	public int cellStyleWidth = 18;
	public int level;
	
	public String key;
	
	public String value;
	
	public List<ExcelHeadNode> childNodes = new ArrayList<ExcelHeadNode>();

	public boolean isStartLevel() {
		return startLevel;
	}

	public void setStartLevel(boolean startLevel) {
		this.startLevel = startLevel;
	}

	public boolean isEndLevel() {
		return endLevel;
	}

	public void setEndLevel(boolean endLevel) {
		this.endLevel = endLevel;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<ExcelHeadNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<ExcelHeadNode> childNodes) {
		this.childNodes = childNodes;
	}

	public int getCellColSpan() {
		return cellColSpan;
	}

	public void setCellColSpan(int cellColSpan) {
		this.cellColSpan = cellColSpan;
	}

	public int getCellStyleWidth() {
		return cellStyleWidth;
	}

	public void setCellStyleWidth(int cellStyleWidth) {
		this.cellStyleWidth = cellStyleWidth;
	}
}
