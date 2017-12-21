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

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsTransOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsTransOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 事务处理阶段（包括：工单投料、完工入库，投料退回，完工退回）
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoTransactionStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoTransactionStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsT = getWaitDealRecordList(taskContext);
		if (CollectionUtils.isEmpty(recordsT)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		Map<String, List<NlbpWsTransOrderInputBean>> inputBeanGrp = new HashMap<String, List<NlbpWsTransOrderInputBean>>();
		Map<String, List<Long>> processIdsGrp = new HashMap<String, List<Long>>();
		for (NlbpSysOrderProcessModel orderProcessModel : recordsT) {
			String docNum = orderProcessModel.getDocNum();
			if (!inputBeanGrp.containsKey(docNum)) {
				List<NlbpWsTransOrderInputBean> inputBeanList = new ArrayList<NlbpWsTransOrderInputBean>();
				inputBeanGrp.put(docNum, inputBeanList);
				List<Long> processIds = new ArrayList<Long>();
				processIdsGrp.put(docNum, processIds);
			}
			long processId = orderProcessModel.getProcessId() == null ? 0 : orderProcessModel.getProcessId();

			NlbpWsTransOrderInputBean inputBean = new NlbpWsTransOrderInputBean();
			inputBean.setSub_code(StringUtils.trimToEmpty(orderProcessModel.getSubInventroyCode()));
			inputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));
			inputBean.setWork_order(orderProcessModel.getEbsOrderNo());
			inputBean.setBatch_num(StringUtils.trimToEmpty(orderProcessModel.getBatchNumber()));
			inputBean.setItem_code(orderProcessModel.getInventoryItemId());
			inputBean.setTrx_type(StringUtils.trimToEmpty(orderProcessModel.getTransactionType()));
			inputBean.setTrx_date(StringUtils.trimToEmpty(orderProcessModel.getBizDate()));
			inputBean.setPb_status(StringUtils.trimToEmpty(orderProcessModel.getConceiveStatus()));
			inputBean.setPrimary_qty(StringUtils.trimToEmpty(orderProcessModel.getPrimaryUomQty()));
			inputBean.setSecond_qty(StringUtils.trimToEmpty(orderProcessModel.getSecondUomQty()));
			inputBean.setUperstage(StringUtils.trimToEmpty(orderProcessModel.getUperstage()));
			inputBean.setBreedDate(StringUtils.trimToEmpty(orderProcessModel.getBreedDate()));

			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getSub_code=" + inputBean.getSub_code());
			LOGGER.info("inputBean.getOrg_id=" + inputBean.getOrg_id());
			LOGGER.info("inputBean.getWork_order=" + inputBean.getWork_order());
			LOGGER.info("inputBean.getBatch_num=" + inputBean.getBatch_num());
			LOGGER.info("inputBean.getItem_code=" + inputBean.getItem_code());
			LOGGER.info("inputBean.getTrx_type=" + inputBean.getTrx_type());
			LOGGER.info("inputBean.getTrx_date=" + inputBean.getTrx_date());
			LOGGER.info("inputBean.getPb_status=" + inputBean.getPb_status());
			LOGGER.info("inputBean.getPrimary_qty=" + inputBean.getPrimary_qty());
			LOGGER.info("inputBean.getSecond_qty=" + inputBean.getSecond_qty());
			LOGGER.info("inputBean.getUperstage=" + inputBean.getUperstage());
			LOGGER.info("inputBean.getBreedDate=" + inputBean.getBreedDate());
			inputBeanGrp.get(docNum).add(inputBean);
			processIdsGrp.get(docNum).add(processId);
		}

		for (Map.Entry<String, List<NlbpWsTransOrderInputBean>> entry : inputBeanGrp.entrySet()) {
			List<NlbpWsTransOrderInputBean> inputBeanList = entry.getValue();
			String docNum = entry.getKey();
			List<Long> processIds = processIdsGrp.get(docNum);
			NlbpWsTransOrderOutputBean outBean = workOrderService.tranWorkOrder(inputBeanList);

			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));
			List<NlbpSysOrderProcessModel> orderProcessModelList = this.getSelectedOrderProcessModelList(processIds,
					recordsT);

			if (null != outBean && outBean.isStatus()) {
				updateOrderProcessRecordList2Sucess(orderProcessModelList, resutlMsg);
			} else {
				LOGGER.info(outBean.getMessage());
				updateOrderProcessRecordList2Error(orderProcessModelList, resutlMsg);
				b = false;
				break;
			}

		}
		return b;
	}

	@Override
	WorkOrderInterfaceType getInterfaceType() {
		return WorkOrderInterfaceType.WORK_ORDER_TRANSACTION;
	}
}
