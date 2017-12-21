package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderProcessDao;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessType;

/**
 * 错误阶段处理
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoCheckStage extends TaskStage {

	private WorkOrderInterfaceType[] needCheckWorkOrderInterfaceTypes;

	public WoCheckStage(WorkOrderInterfaceType[] needCheckWorkOrderInterfaceTypes) {
		this.needCheckWorkOrderInterfaceTypes = needCheckWorkOrderInterfaceTypes;
	}

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		if (ArrayUtils.isEmpty(needCheckWorkOrderInterfaceTypes)) {
			return true;
		}
		List<String> needCheckWorkOrderInterfaceTypeCodes = new ArrayList<String>(
				needCheckWorkOrderInterfaceTypes.length);
		for (WorkOrderInterfaceType woit : needCheckWorkOrderInterfaceTypes) {
			needCheckWorkOrderInterfaceTypeCodes.add(woit.getCode());
		}
		if (existErrorRecords(appContext, taskContext, needCheckWorkOrderInterfaceTypeCodes)) {
			return false;
		}
		return true;
	}

	@Override
	WorkOrderInterfaceType getInterfaceType() {
		return WorkOrderInterfaceType.WORK_ORDER_CHECK;
	}

	@SuppressWarnings("unchecked")
	private boolean existErrorRecords(ApplicationContext appContext, TaskContext taskContext,
			List<String> needCheckWorkOrderInterfaceTypeCodes) {
		// check context first
		Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp = (Map<String, List<NlbpSysOrderProcessModel>>) taskContext
				.getAttributes().get(TaskConstant.ALL_PROCESSING_RECORDS_MAP_KEY);
		for (String needCheckWorkOrderInterfaceTypeCode : needCheckWorkOrderInterfaceTypeCodes) {
			if (!processingRecordsGrp.containsKey(needCheckWorkOrderInterfaceTypeCode)) {
				continue;
			}
			List<NlbpSysOrderProcessModel> opModelList = processingRecordsGrp.get(needCheckWorkOrderInterfaceTypeCode);
			for (NlbpSysOrderProcessModel opModel : opModelList) {
				String handlerStatus = opModel.getHandlerStatus();
				if (WorkOrderProcessType.HANDLER_STATUS_E.getCode().equals(handlerStatus)) {
					return true;
				}
			}
		}
		// then check database
		NlbpSysOrderProcessDao processDao = appContext.getBean(NlbpSysOrderProcessDao.class);
		Map<String, Object> parametersMap = new HashMap<String, Object>(4);
		parametersMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), taskContext.getNlbpOrderNo());
		parametersMap.put(WorkOrderProcessType.ORG_CODE.getCode(), taskContext.getOrgCode());
		parametersMap.put(WorkOrderProcessType.NEED_CHECK_INTERFACE_TYPE_CODES.getCode(),
				needCheckWorkOrderInterfaceTypeCodes);
		parametersMap.put(taskContext.getTryFlag(), Boolean.TRUE);
		List<NlbpSysOrderProcessModel> errorRecords = processDao.getErrorRecords(parametersMap);
		if (CollectionUtils.isEmpty(errorRecords)) {
			return false;
		}
		return true;
	}

}
