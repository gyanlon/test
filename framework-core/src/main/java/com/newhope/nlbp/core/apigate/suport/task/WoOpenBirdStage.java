package com.newhope.nlbp.core.apigate.suport.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsChangeWorkOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsChangeWorkOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 禽类工单打开
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoOpenBirdStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoOpenBirdStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsOB = getWaitDealRecordList(taskContext);

		if (CollectionUtils.isEmpty(recordsOB)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		for (NlbpSysOrderProcessModel orderProcessModel : recordsOB) {
			NlbpWsChangeWorkOrderInputBean inputBean = new NlbpWsChangeWorkOrderInputBean();
			inputBean.setStage(orderProcessModel.getUperstage());
			inputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));
			inputBean.setWork_order(orderProcessModel.getEbsOrderNo());

			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getStage=" + inputBean.getStage());
			LOGGER.info("inputBean.getOrg_id=" + inputBean.getOrg_id());
			LOGGER.info("inputBean.getWork_order=" + inputBean.getWork_order());

			NlbpWsChangeWorkOrderOutputBean outBean = workOrderService.changeWorkOrderStatustoWIP(inputBean);
			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

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
		return WorkOrderInterfaceType.WORK_ORDER_OPEN_BIRD;
	}
}
