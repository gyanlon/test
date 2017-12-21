package com.newhope.nlbp.core.apigate.suport.task;

import java.util.ArrayList;
import java.util.List;

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
 * 禽用创建销售订单
 * 
 * @author ibmzhanghua
 * @date: Aug 16, 2017 5:38:15 PM
 * @version 1.0
 * @since JDK 1.8
 *
 */
public class WoSaleOrderBirdStage extends TaskStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WoSaleOrderBirdStage.class);

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
			nwsInputBean.setOrderType(orderProcessModel.getOrderType());
			nwsInputBean.setOrderNo(orderProcessModel.getExpand6());
			nwsInputBean.setSource(orderProcessModel.getExpand7());
			nwsInputBean.setTRX_DATE(orderProcessModel.getBizDate());
			nwsInputBean.setCusNo(orderProcessModel.getExpand8());
			nwsInputBean.setStates(orderProcessModel.getExpand9());
			nwsInputBean.setCurrency(orderProcessModel.getExpand3());
			nwsInputBean.setRefundReason(orderProcessModel.getExpand10());
			nwsInputBean.setItme_code(orderProcessModel.getInventoryItemId());
			nwsInputBean.setQty(orderProcessModel.getPrimaryUomQty());
			nwsInputBean.setPrice(orderProcessModel.getPrice());
			nwsInputBean.setUnit(orderProcessModel.getExpand4());
			nwsInputBean.setDocNum(orderProcessModel.getDocNum());
			nwsInputBean.setEventLineId(orderProcessModel.getEventId());
			nwsInputBean.setSourceNo(orderProcessModel.getBreedDate());
			nwsInputBean.setSecond_qty(orderProcessModel.getSecondUomQty());
			nwsInputBean.setIdCard(orderProcessModel.getExpand2());
			nwsInputBean.setFzUnit(orderProcessModel.getEarNumber());
			nwsInputBean.setOrg_code(orderProcessModel.getOrgCode());
			
			LOGGER.info("getDocNum=" + orderProcessModel.getDocNum());
			LOGGER.info("processId=" + orderProcessModel.getProcessId());
			LOGGER.info("nlbpOrderNo=" + taskContext.getNlbpOrderNo());
			LOGGER.info("orgCode=" + taskContext.getOrgCode());
			LOGGER.info("inputBean.getOrg_id()=" + nwsInputBean.getOrg_id());
			LOGGER.info("inputBean.setSource()=" + orderProcessModel.getPbCode());
	
			List <NlbpWsSaleInputBean> beanList = new ArrayList<NlbpWsSaleInputBean>();
			beanList.add(nwsInputBean);
			NlbpWsSaleOutputBean outBean = workOrderService.createSaleOrder(beanList);
			String resutlMsg = getReturnedEBSMsg(StringUtils.trimToEmpty(outBean.getMessage()));

			if (null != outBean && outBean.isStatus()) {
				orderProcessModel.setEbsPurchOrder(outBean.getSale_num());
				orderProcessModel.setRoomCode(outBean.getSale_line_num());
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
		return WorkOrderInterfaceType.WORK_ORDER_CREATE_SALE;
	}
}
