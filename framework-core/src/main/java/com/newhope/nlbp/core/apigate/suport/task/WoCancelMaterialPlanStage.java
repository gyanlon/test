package com.newhope.nlbp.core.apigate.suport.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.MaterialPlanConverUtils;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

public class WoCancelMaterialPlanStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoFinishStage.class);
	
	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsN = this.getWaitDealRecordList(taskContext);
		if (CollectionUtils.isEmpty(recordsN)) {
			return true;
		}
		
		//TODO 这里直接调用webservice 生成物料计划
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		boolean b = true;
		// 调用接口及更新状态
		for (NlbpSysOrderProcessModel orderProcessModel : recordsN) {
			//这里把 process 转换成webservice bean
			NlbpWsCancelMaterialPlanInputBean inputBean = MaterialPlanConverUtils.convertCancelP2W(orderProcessModel);
			
			NlbpWsCancelMaterialPlanOutputBean outBean = workOrderService.cancelMaterialPlan(inputBean);
			orderProcessModel.setEbsOrderNo(outBean.getEbsOrderNo());
			orderProcessModel.setExpand8(outBean.getDocType());
			orderProcessModel.setNlbpOrderNo(outBean.getNlbpOrderNo());

			//公共代码
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
		return WorkOrderInterfaceType.MATERIAL_PLAN_CANCEL;
	}

}
