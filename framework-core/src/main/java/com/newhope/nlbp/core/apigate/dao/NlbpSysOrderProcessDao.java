package com.newhope.nlbp.core.apigate.dao;

import java.util.List;
import java.util.Map;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.bean.NlbpSysTranstionManagementModel;
import com.newhope.nlbp.core.dao.BaseDao;

/**
 * 工单处理DAO接口
 */
@SuppressWarnings("rawtypes")
public interface NlbpSysOrderProcessDao extends BaseDao<NlbpSysOrderProcessModel> {
	
	List<Map> getForemostWorkOrderNos(Map parametersMap);
    
	List<NlbpSysOrderProcessModel> getErrorRecords(Map parametersMap);
	
	List<NlbpSysOrderProcessModel> getProcessingStatusRecordsForPipeline(Map parametersMap);

	void updateOrderProcess2ProcessingStatusForStage(Map parametersMap);
	
	void batchUpdateAllRecordsStatusForPipeline(Map parametersMap);
	
	List<Map> selectTypeinfor(Integer ebs_org_id);
	
	Map SelectItemCreateUsed(String itemCode, int orgid);
	
	int insertSysTranstionManagement(NlbpSysTranstionManagementModel nlbpSysTranstionManagementModel);
	
} 