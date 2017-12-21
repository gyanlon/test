package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsUpdateBatinfoupderpInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsUpdateBatinfoupderpOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 工单配怀状态更新
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public class WoPhStatusStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoPhStatusStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsHS = getWaitDealRecordList(taskContext);
		if (CollectionUtils.isEmpty(recordsHS)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		List<NlbpWsUpdateBatinfoupderpInputBean> inputBeanList = new ArrayList<NlbpWsUpdateBatinfoupderpInputBean>();
		for (NlbpSysOrderProcessModel orderProcessModel : recordsHS) {
			NlbpWsUpdateBatinfoupderpInputBean inputBean = new NlbpWsUpdateBatinfoupderpInputBean();
			inputBean.setBatch_no(orderProcessModel.getEbsOrderNo());
			inputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));
			inputBean.setOrganization_code(StringUtils.trimToEmpty(orderProcessModel.getEbsOrgCode()));
			inputBean.setHybridization_status(StringUtils.trimToEmpty(orderProcessModel.getConceiveStatus()));
			inputBean.setBarn(StringUtils.trimToEmpty(orderProcessModel.getRoomCode()));
			inputBean.setBreeding_date(StringUtils.trimToEmpty(orderProcessModel.getBreedDate()));

			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getBatch_no()=" + inputBean.getBatch_no());
			LOGGER.info("inputBean.getOrg_id()=" + inputBean.getOrg_id());
			LOGGER.info("inputBean.getOrganization_code()=" + inputBean.getOrganization_code());
			LOGGER.info("inputBean.getHybridization_status()=" + inputBean.getHybridization_status());
			LOGGER.info("inputBean.getBarn()=" + inputBean.getBarn());
			LOGGER.info("inputBean.getBreeding_date()=" + inputBean.getBreeding_date());
			inputBeanList.add(inputBean);
		}
		NlbpWsUpdateBatinfoupderpOutputBean outBean = workOrderService.UpdateBatinfoupderp(inputBeanList);
		String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

		if (null != outBean && outBean.isStatus()) {
			this.updateOrderProcessRecordList2Sucess(recordsHS, resutlMsg);
		} else {
			LOGGER.info(outBean.getMessage());
			b = false;
		}
		return b;
	}

	@Override
	WorkOrderInterfaceType getInterfaceType() {
		return WorkOrderInterfaceType.WORK_ORDER_PH_STATUS;
	}
}
