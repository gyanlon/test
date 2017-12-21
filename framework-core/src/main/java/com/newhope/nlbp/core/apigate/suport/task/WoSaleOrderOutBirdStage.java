package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessService;

/**
 * 禽用销售订单出库
 * 
 * @author ibmzhanghua
 * @date: Aug 16, 2017 5:38:15 PM
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class WoSaleOrderOutBirdStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoSaleOrderOutBirdStage.class);

	@Override
	boolean internal(ApplicationContext appContext, TaskContext taskContext) {
		List<NlbpSysOrderProcessModel> recordsSale = getWaitDealRecordList(taskContext);

		if (CollectionUtils.isEmpty(recordsSale)) {
			return true;
		}
		WorkOrderProcessService workOrderService = appContext.getBean(WorkOrderProcessService.class);
		if (CollectionUtils.isEmpty(recordsSale)) {
			return true;
		}
		boolean b = true;
		// 调用接口及更新状态
		for (NlbpSysOrderProcessModel orderProcessModel : recordsSale) {
	
			NlbpWsSaleInputBean nwsInputBean = new NlbpWsSaleInputBean();
			
			nwsInputBean.setOrg_id(orderProcessModel.getExpand1() == null ? Long.getLong("0")
					: Long.parseLong(orderProcessModel.getExpand1()));
			nwsInputBean.setOu_code(orderProcessModel.getEbsOrgCode());
			nwsInputBean.setItme_code(orderProcessModel.getInventoryItemId());
			nwsInputBean.setQty(orderProcessModel.getPrimaryUomQty());
			nwsInputBean.setSecond_qty(orderProcessModel.getSecondUomQty());
			nwsInputBean.setSub_code(orderProcessModel.getSubInventroyCode());//子库存
			nwsInputBean.setTRX_DATE(orderProcessModel.getBizDate());
			nwsInputBean.setBatch_num(orderProcessModel.getBatchNumber());
			nwsInputBean.setSource(orderProcessModel.getExpand7());
			nwsInputBean.setDocNum(orderProcessModel.getDocNum());
			nwsInputBean.setEventLineId(orderProcessModel.getEventId());
			nwsInputBean.setSourceNo(orderProcessModel.getAttribute4());
			nwsInputBean.setNlbpSaleNo(orderProcessModel.getNlbpPurchOrder());
			nwsInputBean.setOrderType(orderProcessModel.getOrderType());
			nwsInputBean.setOrg_code(orderProcessModel.getOrgCode());
			nwsInputBean.setOrg_organization(orderProcessModel.getExpand6());
			LOGGER.info("getDocNum=" + orderProcessModel.getDocNum());
			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getOrg_id()=" + nwsInputBean.getOrg_id());
			LOGGER.info("inputBean.setSource()=" + orderProcessModel.getPbCode());
	
			List <NlbpWsSaleInputBean> beanList = new ArrayList<NlbpWsSaleInputBean>();
			beanList.add(nwsInputBean);
			NlbpWsSaleOutputBean outBean = workOrderService.outSaleOrder(beanList);
			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

			if (null != outBean && outBean.isStatus()) {
			/*	orderProcessModel.setEbsPurchOrder(outBean.getSale_num());
				orderProcessModel.setRoomCode(outBean.getSale_line_num());*/
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
		return WorkOrderInterfaceType.WORK_ORDER_SALE_OUT;
	}
}
