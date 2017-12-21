package com.newhope.nlbp.core.apigate.dao;

import java.util.List;
import java.util.Map;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderModel;
import com.newhope.nlbp.core.dao.BaseDao;

/**
 * 系统工单DAO接口
 * @author aihao
 * @version 2017-05-26
 */
public interface NlbpSysOrderDao extends BaseDao<NlbpSysOrderModel> {
	@SuppressWarnings("rawtypes")
	public NlbpSysOrderModel selectByNlbpOrderNo(Map map);

	@SuppressWarnings("rawtypes")
	public void updateOrderEBSOrderNo(Map parametersMap);
	
	@SuppressWarnings("rawtypes")
	public NlbpSysOrderModel selectOrderFromCreateSucess(Map parametersMap);
	
	@SuppressWarnings("rawtypes")
	public List<Map> getEBSworkOrderNum(Map input);
	
	@SuppressWarnings("rawtypes")
	public List<Map> getEBSworkOrderNumUsingEP(Map input);

}