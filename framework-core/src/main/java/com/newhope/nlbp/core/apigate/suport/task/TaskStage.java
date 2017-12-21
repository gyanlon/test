package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderDao;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessType;
import com.newhope.nlbp.core.pipeline.Context;
import com.newhope.nlbp.core.pipeline.Stage;

/**
 * 任务所有处理阶段的抽象父类
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public abstract class TaskStage implements Stage {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskStage.class);

	@Override
	public boolean execute(Context context) {
		// convert to task context
		TaskContext taskContext = (TaskContext) context;
		LOGGER.info("stage description:" + getStageName(taskContext));
		long startTime = System.currentTimeMillis();
		ApplicationContext appContext = (ApplicationContext) taskContext.getAttributes().get(TaskConstant.APP_CXT_KEY);
		boolean result = this.internal(appContext, taskContext);
		// update W status for unprocessed data
		long endTime = System.currentTimeMillis();
		LOGGER.info("stage deal result:" + result);
		LOGGER.info("stage deal cost time:" + (endTime - startTime) + "ms");
		return result;
	}

	private String getStageName(TaskContext taskContext) {
		return this.getInterfaceType().getName();
	}

	abstract boolean internal(ApplicationContext appContext, TaskContext taskContext);

	abstract WorkOrderInterfaceType getInterfaceType();

	/**
	 * 截取返回消息200个字符
	 */
	protected String getReturnedEBSMsg(String msg) {
		return msg.length() > 200 ? msg.substring(0, 200) : msg;
	}

	/**
	 * 根据工单号及相应状态，获取工单记录
	 */
	@SuppressWarnings("unchecked")
	protected List<NlbpSysOrderProcessModel> getWaitDealRecordList(TaskContext taskContext) {
		Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp = (Map<String, List<NlbpSysOrderProcessModel>>) taskContext
				.getAttributes().get(TaskConstant.ALL_PROCESSING_RECORDS_MAP_KEY);
		if (processingRecordsGrp.containsKey(this.getInterfaceType().getCode())) {
			List<NlbpSysOrderProcessModel> records = processingRecordsGrp.get(this.getInterfaceType().getCode());
			LOGGER.info(this.getInterfaceType().getCode() + " records size=" + records.size());
			return records;
		}
		return ListUtils.EMPTY_LIST;
	}

	/**
	 * 更新Order表EBS工单号
	 */
	protected void updateEBSOrderNo(NlbpSysOrderDao orderDao, TaskContext taskContext, String ebsOrderNo) {
		Map<String, Object> tempOrderMap = new HashMap<String, Object>(4);
		tempOrderMap.put("ebsOrderNo", ebsOrderNo);
		tempOrderMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), taskContext.getNlbpOrderNo());
		tempOrderMap.put(WorkOrderProcessType.ORG_CODE.getCode(), taskContext.getOrgCode());
		tempOrderMap.put("lastUpdateDate", new Date());
		orderDao.updateOrderEBSOrderNo(tempOrderMap);
	}

	protected void updateOrderProcessRecord2Sucess(NlbpSysOrderProcessModel orderProcessModel, String processMessage) {
		orderProcessModel.setProcessMessage(processMessage);
		orderProcessModel.setLastUpdateDate(new Date());
		Integer handlerCount = orderProcessModel.getHandlerCount() + 1;
		orderProcessModel.setHandlerCount(handlerCount);
		orderProcessModel.setProcessCode("S000A000");
		orderProcessModel.setHandlerStatus(WorkOrderProcessType.HANDLER_STATUS_S.getCode());
	}

	protected void updateOrderProcessRecord2Error(NlbpSysOrderProcessModel orderProcessModel, String processMessage) {
		orderProcessModel.setProcessMessage(processMessage);
		orderProcessModel.setLastUpdateDate(new Date());
		Integer handlerCount = orderProcessModel.getHandlerCount() + 1;
		orderProcessModel.setHandlerCount(handlerCount);
		orderProcessModel.setProcessCode("000");
		orderProcessModel.setHandlerStatus(WorkOrderProcessType.HANDLER_STATUS_E.getCode());
	}

	protected void updateOrderProcessRecordList2Sucess(List<NlbpSysOrderProcessModel> orderProcessModelList,
			String processMessage) {
		for (NlbpSysOrderProcessModel orderProcessModel : orderProcessModelList) {
			this.updateOrderProcessRecord2Sucess(orderProcessModel, processMessage);
		}
	}

	protected void updateOrderProcessRecordList2Error(List<NlbpSysOrderProcessModel> orderProcessModelList,
			String processMessage) {
		for (NlbpSysOrderProcessModel orderProcessModel : orderProcessModelList) {
			this.updateOrderProcessRecord2Error(orderProcessModel, processMessage);
		}
	}

	protected List<NlbpSysOrderProcessModel> getSelectedOrderProcessModelList(List<Long> processIds,
			List<NlbpSysOrderProcessModel> allOderProcessModelList) {
		List<NlbpSysOrderProcessModel> selectedList = new ArrayList<NlbpSysOrderProcessModel>(
				allOderProcessModelList.size());
		for (NlbpSysOrderProcessModel orderProcessModel : allOderProcessModelList) {
			Long processId = orderProcessModel.getProcessId();
			if (processIds.contains(processId)) {
				selectedList.add(orderProcessModel);
			}
		}
		return selectedList;
	}

}
