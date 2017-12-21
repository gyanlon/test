package com.newhope.nlbp.core.apigate.suport.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCreateOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCreateOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 创建工单
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoNewStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoNewStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsN = this.getWaitDealRecordList(taskContext);
		if (CollectionUtils.isEmpty(recordsN)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		for (NlbpSysOrderProcessModel orderProcessModel : recordsN) {
			NlbpWsCreateOrderInputBean inputBean = new NlbpWsCreateOrderInputBean();
			inputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));
			inputBean.setItme_code(StringUtils.trimToEmpty(orderProcessModel.getInventoryItemId()));
			inputBean.setBatch_num(StringUtils.trimToEmpty(orderProcessModel.getBatchNumber()));
			inputBean.setQty(StringUtils.trimToEmpty(orderProcessModel.getPrimaryUomQty()));
			inputBean.setFarm(StringUtils.trimToEmpty(orderProcessModel.getFarmCode()));
			inputBean.setDS_NUMBER(StringUtils.trimToEmpty(orderProcessModel.getRoomCode()));
			inputBean.setPB_CODE(StringUtils.trimToEmpty(orderProcessModel.getPbCode()));// 20170627
																							// update
			inputBean.setEP_NUMBER(StringUtils.trimToEmpty(orderProcessModel.getEarNumber()));
			inputBean.setSTAGE_TYPE(StringUtils.trimToEmpty(orderProcessModel.getOrderType()));
			inputBean.setTRX_DATE(StringUtils.trimToEmpty(orderProcessModel.getBizDate()));

			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getOrg_id=" + inputBean.getOrg_id());
			LOGGER.info("inputBean.getItme_code=" + inputBean.getItme_code());
			LOGGER.info("inputBean.getBatch_num=" + inputBean.getBatch_num());
			LOGGER.info("inputBean.getQty=" + inputBean.getQty());
			LOGGER.info("inputBean.getFarm=" + inputBean.getFarm());
			LOGGER.info("inputBean.getDS_NUMBER=" + inputBean.getDS_NUMBER());
			LOGGER.info("inputBean.getPB_CODE=" + inputBean.getPB_CODE());
			LOGGER.info("inputBean.getEP_NUMBER=" + inputBean.getEP_NUMBER());
			LOGGER.info("inputBean.getSTAGE_TYPE=" + inputBean.getSTAGE_TYPE());
			LOGGER.info("inputBean.getTRX_DATE=" + inputBean.getTRX_DATE());

			NlbpWsCreateOrderOutputBean outBean = workOrderService.createWorkOrder(inputBean);

			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));
			String ebsOrderNo = StringUtils.trimToEmpty(outBean.getWorkorder_num());
			orderProcessModel.setEbsOrderNo(ebsOrderNo);

			if (null != outBean && outBean.isStatus()) {
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
		return WorkOrderInterfaceType.WORK_ORDER_NEW;
	}
}
