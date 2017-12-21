package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWSListNlbpWsStockInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsStockInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsStrockOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 其他出入库阶段，批量处理
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoStockInOutStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoStockInOutStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsSIO = getWaitDealRecordList(taskContext);

		if (CollectionUtils.isEmpty(recordsSIO)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		Map<String, NlbpWSListNlbpWsStockInputBean> inputBeanGrp = new HashMap<String, NlbpWSListNlbpWsStockInputBean>();
		Map<String, List<Long>> processIdsGrp = new HashMap<String, List<Long>>();
		for (NlbpSysOrderProcessModel orderProcessModel : recordsSIO) {
			String docNum = orderProcessModel.getDocNum();
			if (!inputBeanGrp.containsKey(docNum)) {
				NlbpWSListNlbpWsStockInputBean inputBean = new NlbpWSListNlbpWsStockInputBean();
				List<NlbpWsStockInputBean> inputBeanList = new ArrayList<NlbpWsStockInputBean>();
				inputBean.setInputListBean(inputBeanList);
				inputBean.setType(orderProcessModel.getExpand4());
				inputBeanGrp.put(docNum+"_"+orderProcessModel.getExpand4(), inputBean);
				List<Long> processIds = new ArrayList<Long>();
				processIdsGrp.put(docNum+"_"+orderProcessModel.getExpand4(), processIds);
			}

			long processId = orderProcessModel.getProcessId() == null ? 0 : orderProcessModel.getProcessId();

			NlbpWsStockInputBean nwsInputBean = new NlbpWsStockInputBean();

			nwsInputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));

			nwsInputBean.setBatch_num(orderProcessModel.getBatchNumber());
			nwsInputBean.setBusiness_date(orderProcessModel.getBizDate());
			nwsInputBean.setDocNum(orderProcessModel.getDocNum());
			nwsInputBean.setDs_code(orderProcessModel.getRoomCode());
			nwsInputBean.setEp_num(orderProcessModel.getEarNumber());
			nwsInputBean.setEventLineId(orderProcessModel.getEventId());
			nwsInputBean.setFarm_code(orderProcessModel.getFarmCode());
			nwsInputBean.setItem_code(orderProcessModel.getInventoryItemId());
			nwsInputBean.setPty(orderProcessModel.getPrimaryUomQty());
			nwsInputBean.setPurpose(orderProcessModel.getTransactionType());
			nwsInputBean.setSecond_qty(orderProcessModel.getSecondUomQty());
			nwsInputBean.setSub_code(orderProcessModel.getSubInventroyCode());
			nwsInputBean.setPerson_id(orderProcessModel.getExpand2());
			nwsInputBean.setFlag(orderProcessModel.getExpand3());
			nwsInputBean.setType(orderProcessModel.getExpand4());
			nwsInputBean.setTypeCheck(orderProcessModel.getBirdCheck());
			nwsInputBean.setWork_order(StringUtils.trimToEmpty(orderProcessModel.getEbsOrderNo()));
			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getOrg_id()=" + nwsInputBean.getOrg_id());
			LOGGER.info("inputBean.getWork_order()=" + nwsInputBean.getWork_order());
			LOGGER.info("inputBean.getDate()=" + nwsInputBean.getBusiness_date());
			inputBeanGrp.get(docNum+"_"+orderProcessModel.getExpand4()).getInputListBean().add(nwsInputBean);
			processIdsGrp.get(docNum+"_"+orderProcessModel.getExpand4()).add(processId);
		}

		for (Map.Entry<String, NlbpWSListNlbpWsStockInputBean> entry : inputBeanGrp.entrySet()) {
			NlbpWSListNlbpWsStockInputBean inputBean = entry.getValue();
			String docNum = entry.getKey();
			List<Long> processIds = processIdsGrp.get(docNum);
			NlbpWsStrockOutputBean outBean = workOrderService.stockBizService(inputBean);

			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

			List<NlbpSysOrderProcessModel> orderProcessModelList = this.getSelectedOrderProcessModelList(processIds,
					recordsSIO);
			if (null != outBean && outBean.isStatus()) {
				String issueNum = outBean.getIssue_num();
				for (NlbpSysOrderProcessModel orderProcessMode : orderProcessModelList) {
					orderProcessMode.setExpand6(issueNum);
				}
				updateOrderProcessRecordList2Sucess(orderProcessModelList, resutlMsg);
			} else {
				LOGGER.info(outBean.getMessage());
				updateOrderProcessRecordList2Error(orderProcessModelList, resutlMsg);
				b = false;
			}
		}
		return b;
	}

	@Override
	WorkOrderInterfaceType getInterfaceType() {
		return WorkOrderInterfaceType.WORK_ORDER_STOCK_IN_OUT;
	}
}
