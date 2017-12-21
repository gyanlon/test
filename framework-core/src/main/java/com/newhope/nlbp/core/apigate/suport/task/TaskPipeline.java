package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderModel;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderDao;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderProcessDao;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessType;
import com.newhope.nlbp.core.pipeline.Context;
import com.newhope.nlbp.core.pipeline.Pipeline;
import com.newhope.nlbp.core.pipeline.Stage;
import com.newhope.nlbp.core.utils.TransactionUtil;

/**
 * 养殖平台推送数据到EBS流水线模式具体实现类
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class TaskPipeline implements Pipeline {

	private List<Stage> stages;

	public TaskPipeline(int size) {
		stages = new ArrayList<Stage>(size);
	}

	public void addStage(Stage stage) {
		stages.add(stage);
	}

	public boolean execute(Context context) {
		ApplicationContext appContext = (ApplicationContext) context.getAttributes().get(TaskConstant.APP_CXT_KEY);
		// get transactionUtil from task context
		TransactionUtil transactionUtil = appContext.getBean(TransactionUtil.class);
		return transactionUtil.transact(s -> internal(context, appContext));
	}

	private void internal(Context context, ApplicationContext appContext) {
		// convert to task context
		TaskContext taskContext = (TaskContext) context;
		NlbpSysOrderProcessDao processDao = appContext.getBean(NlbpSysOrderProcessDao.class);
		NlbpSysOrderDao orderDao = appContext.getBean(NlbpSysOrderDao.class);
		updateOrderProcess2ProcessingStatusForStage(processDao, taskContext);
		Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp = this
				.getProcessingStatusRecordsMapForPipeline(taskContext, processDao);
		taskContext.getAttributes().put(TaskConstant.ALL_PROCESSING_RECORDS_MAP_KEY, processingRecordsGrp);
		/* execute the stages */
		for (Stage stage : stages) {
			boolean result = stage.execute(context);
			TaskStage taskStage = (TaskStage) stage;
			if (WorkOrderInterfaceType.WORK_ORDER_CHECK == taskStage.getInterfaceType() && !result) {
				break;
			}
			// update EBS_Order_No for all stage
			if (WorkOrderInterfaceType.WORK_ORDER_NEW == taskStage.getInterfaceType()) {
				updateEBSOrderNoForAllStage(taskContext, processingRecordsGrp, orderDao);
			}
		}
		// if exist P status record, then change to E status
		updateUnprocessed2ErrorStatusForStage(taskContext, processingRecordsGrp);
		// update all processed records to OrderProcess table
		batchUpdateAllRecordsStatusForPipeline(processDao, processingRecordsGrp);
	}

	private void batchUpdateAllRecordsStatusForPipeline(NlbpSysOrderProcessDao processDao,
			Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp) {
		List<NlbpSysOrderProcessModel> allRecords = new ArrayList<NlbpSysOrderProcessModel>();
		for (Map.Entry<String, List<NlbpSysOrderProcessModel>> entry : processingRecordsGrp.entrySet()) {
			List<NlbpSysOrderProcessModel> opModelList = entry.getValue();
			allRecords.addAll(opModelList);
		}
		if (CollectionUtils.isEmpty(allRecords)) {
			return;
		}
		Map<String, Object> parametersMap = new HashMap<String, Object>(1);
		parametersMap.put("orderProcessList", allRecords);
		processDao.batchUpdateAllRecordsStatusForPipeline(parametersMap);
	}

	private void updateOrderProcess2ProcessingStatusForStage(NlbpSysOrderProcessDao processDao,
			TaskContext taskContext) {
		Map<String, Object> parametersMap = new HashMap<String, Object>(4);
		parametersMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), taskContext.getNlbpOrderNo());
		parametersMap.put(WorkOrderProcessType.ORG_CODE.getCode(), taskContext.getOrgCode());
		parametersMap.put("lastUpdateDate", new Date());
		parametersMap.put(taskContext.getTryFlag(), Boolean.TRUE);
		processDao.updateOrderProcess2ProcessingStatusForStage(parametersMap);
	}

	private void updateUnprocessed2ErrorStatusForStage(TaskContext taskContext,
			Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp) {
		for (Map.Entry<String, List<NlbpSysOrderProcessModel>> entry : processingRecordsGrp.entrySet()) {
			List<NlbpSysOrderProcessModel> opModelList = entry.getValue();
			for (NlbpSysOrderProcessModel opModel : opModelList) {
				String handlerStatus = opModel.getHandlerStatus();
				if (WorkOrderProcessType.HANDLER_STATUS_P.getCode().equals(handlerStatus)) {
					opModel.setHandlerStatus(WorkOrderProcessType.HANDLER_STATUS_E.getCode());
					if (WorkOrderProcessType.WORK_ORDER_TRY.getCode().equals(taskContext.getTryFlag())) {
						opModel.setHandlerCount(4);// if try stage error, then
													// modify to 4.
					} else {
						opModel.setHandlerCount(opModel.getHandlerCount() + 1);
					}
					opModel.setProcessMessage("因ESB无法处理，被接口平台修改为E状态，请通过接口管理界面重置为W状态，尝试再次处理！");
					opModel.setLastUpdateBy("APIGate");
					opModel.setLastUpdateDate(new Date());
					opModel.setAttribute2("由接口平台修改");
				}
			}
		}
	}

	private Map<String, List<NlbpSysOrderProcessModel>> getProcessingStatusRecordsMapForPipeline(
			TaskContext taskContext, NlbpSysOrderProcessDao processDao) {
		// 从ID最小的记录开始获取
		Map<String, Object> parametersMap = new HashMap<String, Object>(4);
		parametersMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), taskContext.getNlbpOrderNo());
		parametersMap.put(WorkOrderProcessType.ORG_CODE.getCode(), taskContext.getOrgCode());
		parametersMap.put(taskContext.getTryFlag(), Boolean.TRUE);
		List<NlbpSysOrderProcessModel> allProcessingRecords = processDao
				.getProcessingStatusRecordsForPipeline(parametersMap);
		if (CollectionUtils.isEmpty(allProcessingRecords)) {
			return new HashMap<String, List<NlbpSysOrderProcessModel>>(0);
		}
		Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp = new HashMap<String, List<NlbpSysOrderProcessModel>>(
				WorkOrderInterfaceType.values().length);
		for (NlbpSysOrderProcessModel woProcess : allProcessingRecords) {
			String workOrderInterfaceType = woProcess.getInterfaceType();
			if (!processingRecordsGrp.containsKey(workOrderInterfaceType)) {
				List<NlbpSysOrderProcessModel> aProcessingRecords = new ArrayList<NlbpSysOrderProcessModel>(
						allProcessingRecords.size());
				processingRecordsGrp.put(workOrderInterfaceType, aProcessingRecords);
			}
			processingRecordsGrp.get(workOrderInterfaceType).add(woProcess);
		}
		return processingRecordsGrp;
	}

	private void updateEBSOrderNoForAllStage(TaskContext taskContext,
			Map<String, List<NlbpSysOrderProcessModel>> processingRecordsGrp, NlbpSysOrderDao orderDao) {
		List<NlbpSysOrderProcessModel> newStageRecords = null;
		if (processingRecordsGrp.containsKey(WorkOrderInterfaceType.WORK_ORDER_NEW.getCode())) {
			newStageRecords = processingRecordsGrp.get(WorkOrderInterfaceType.WORK_ORDER_NEW.getCode());
		}
		Map<String, String> orderNoMapping = new HashMap<String, String>();
		if (!CollectionUtils.isEmpty(newStageRecords)) {
			for (NlbpSysOrderProcessModel record : newStageRecords) {
				if (StringUtils.equals(record.getHandlerStatus(), WorkOrderProcessType.HANDLER_STATUS_S.getCode())) {
					Map<String, Object> parametersMap = new HashMap<String, Object>(4);
					String ebsOrderNo = record.getEbsOrderNo();
					String nlbpOrderNo = record.getNlbpOrderNo();
					String orgCode = record.getExpand1();
					parametersMap.put("ebsOrderNo", ebsOrderNo);
					parametersMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), nlbpOrderNo);
					parametersMap.put(WorkOrderProcessType.ORG_CODE.getCode(), orgCode);
					parametersMap.put("lastUpdateDate", new Date());
					orderDao.updateOrderEBSOrderNo(parametersMap);
					orderNoMapping.put(nlbpOrderNo, ebsOrderNo);
				}
			}
		} else {
			Map<String, String> parametersMap = new HashMap<String, String>(2);
			parametersMap.put(WorkOrderProcessType.NLBP_ORDER_NO.getCode(), taskContext.getNlbpOrderNo());
			parametersMap.put(WorkOrderProcessType.ORG_CODE.getCode(), taskContext.getOrgCode());
			NlbpSysOrderModel order = orderDao.selectOrderFromCreateSucess(parametersMap);
			if (order != null && StringUtils.isNotBlank(order.getEbsOrderNo())) {
				orderNoMapping.put(taskContext.getNlbpOrderNo(), order.getEbsOrderNo());
			}
		}
		if (MapUtils.isEmpty(orderNoMapping)) {
			return;
		}
		for (Map.Entry<String, List<NlbpSysOrderProcessModel>> entry : processingRecordsGrp.entrySet()) {
			String interfaceType = entry.getKey();
			if (StringUtils.equals(WorkOrderInterfaceType.WORK_ORDER_NEW.getCode(), interfaceType)) {
				continue;
			}
			List<NlbpSysOrderProcessModel> opModelList = entry.getValue();
			for (NlbpSysOrderProcessModel opModel : opModelList) {
				String nlbpOrderNo = opModel.getNlbpOrderNo();
				String ebsOrderNo = orderNoMapping.get(nlbpOrderNo);
				opModel.setEbsOrderNo(ebsOrderNo);
			}
		}
	}

}
