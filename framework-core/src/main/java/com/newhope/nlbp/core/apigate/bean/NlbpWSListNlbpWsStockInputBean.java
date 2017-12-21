package com.newhope.nlbp.core.apigate.bean;

import java.util.List;

import com.newhope.nlbp.core.base.BaseModel;

/**
 * 
 * @author yang
 *
 */
public class NlbpWSListNlbpWsStockInputBean extends BaseModel {

	private static final long serialVersionUID = -9154186780365786216L;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public List<com.newhope.nlbp.core.apigate.bean.NlbpWsStockInputBean> getInputListBean() {
		return inputListBean;
	}
	public void setInputListBean(List<com.newhope.nlbp.core.apigate.bean.NlbpWsStockInputBean> inputListBean) {
		this.inputListBean = inputListBean;
	}

	private List<com.newhope.nlbp.core.apigate.bean.NlbpWsStockInputBean> inputListBean;
	private String type="";
	
}