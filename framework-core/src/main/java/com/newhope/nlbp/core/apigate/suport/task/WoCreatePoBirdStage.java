package com.newhope.nlbp.core.apigate.suport.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 禽用创建采购申请单
 * 
 * @author ibmzhanghua
 * @date: Jul 27, 2017 3:25:51 PM
 */
public class WoCreatePoBirdStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoCreatePoBirdStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsAB = getWaitDealRecordList(taskContext);

		if (CollectionUtils.isEmpty(recordsAB)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		if (CollectionUtils.isEmpty(recordsAB)) {
			return true;
		}
		boolean b = true;
		// 调用接口及更新状态
		for (NlbpSysOrderProcessModel orderProcessModel : recordsAB) {
	
			NlbpWsPoInputBean nwsInputBean = new NlbpWsPoInputBean();
			nwsInputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));

			nwsInputBean.setBatch_num(orderProcessModel.getBatchNumber());
			nwsInputBean.setDocNum(orderProcessModel.getDocNum());
			nwsInputBean.setItem_code(orderProcessModel.getInventoryItemId());
			nwsInputBean.setPrice(orderProcessModel.getPrice());
			nwsInputBean.setQty(orderProcessModel.getPrimaryUomQty());
			nwsInputBean.setPerson_id(orderProcessModel.getExpand2());
			nwsInputBean.setDept(orderProcessModel.getFarmCode());
			nwsInputBean.setEventLineId(orderProcessModel.getEventId());
			nwsInputBean.setWork_oder(orderProcessModel.getEbsOrderNo());
			
			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getOrg_id()=" + nwsInputBean.getOrg_id());
			LOGGER.info("inputBean.getWork_order()=" + nwsInputBean.getWork_oder());
	
			NlbpWsPoOutputBean outBean = workOrderService.createPOforBird(nwsInputBean);
			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

			if (null != outBean && outBean.isStatus()) {
				orderProcessModel.setEbsPurchOrder(outBean.getPo_num());
				updateOrderProcessRecord2Sucess(orderProcessModel, resutlMsg);
			} else {
				LOGGER.info(outBean.getMessage());
				updateOrderProcessRecord2Error(orderProcessModel, resutlMsg);
				b = false;
				break;
			}
		}
		return b;
	}
	

	@Override
	WorkOrderInterfaceType getInterfaceType() {
		return WorkOrderInterfaceType.WORK_ORDER_APPLY_BIRD;
	}
}
