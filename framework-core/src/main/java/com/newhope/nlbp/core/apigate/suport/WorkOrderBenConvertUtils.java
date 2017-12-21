package com.newhope.nlbp.core.apigate.suport;

import java.util.Date;

import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderModel;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.bean.NlbpWsChangeWorkOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsCreateOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsTransOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsUpdateBatinfoupderpInputBean;
import com.newhope.nlbp.core.tools.DateUtil;

public class WorkOrderBenConvertUtils {
	
	public static final String PIG_ORDER_TYPE_CONVERSION = "16000018";

	public static NlbpSysOrderProcessModel convertOrderBeanToOrderProcessModel(NlbpWsCreateOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrgCode(inputBean.getEbsOrgId());
		entity.setBatchNumber(inputBean.getBatch_num());
		entity.setInventoryItemId(inputBean.getItme_code());
		entity.setPrimaryUomQty(inputBean.getQty());
		entity.setFarmCode(inputBean.getFarm());
		entity.setRoomCode(inputBean.getDS_NUMBER());
		entity.setPbCode(inputBean.getPB_CODE());
		entity.setOrderType(inputBean.getSTAGE_TYPE());
		entity.setEarNumber(inputBean.getEP_NUMBER());
		entity.setBizDate(inputBean.getTRX_DATE());
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setHandlerStatus("W");
		return entity;
	}

	public static NlbpSysOrderModel convertOrderBeanToOrderModel(NlbpWsCreateOrderInputBean inputBean) {
		NlbpSysOrderModel entity = new NlbpSysOrderModel();
		entity.setBatchNumber(inputBean.getBatch_num());
		entity.setEarNumber(inputBean.getEP_NUMBER());
		entity.setBizDate(DateUtil.parseDate(inputBean.getTRX_DATE(), "yyyy-MM-dd"));
		entity.setOrderType(inputBean.getSTAGE_TYPE());
		entity.setInventoryItemId(inputBean.getItme_code());
		if(inputBean.getSTAGE_TYPE().equals(PIG_ORDER_TYPE_CONVERSION)){
			entity.setNlbpOrderNo(inputBean.getDocNum()+"-"+inputBean.getEventLineId()+"-zh");//转换工单专用
		}else{
			entity.setNlbpOrderNo(inputBean.getDocNum()+"-"+inputBean.getEventLineId());
		}
		
		entity.setCreatedBy(1L);
		entity.setOrderStatus("2");
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setCreationDate(new Date());
		return entity;
	}
	
	public static NlbpSysOrderProcessModel convertTranOrderBeanToOrderProcessModel(NlbpWsTransOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrgCode(inputBean.getOu_code());
		entity.setBatchNumber(inputBean.getBatch_num());
		entity.setInventoryItemId(inputBean.getItem_code());
		entity.setPrimaryUomQty(inputBean.getPrimary_qty());
		if("".equals(inputBean.getSecond_qty())){
			inputBean.setSecond_qty(null);
		}
		entity.setSecondUomQty(inputBean.getSecond_qty());
		entity.setPbCode(inputBean.getPb_status());
		entity.setBirdCheck(inputBean.getCheck());
		entity.setSubInventroyCode(inputBean.getSub_code());
		entity.setUperstage(inputBean.getUperstage());
		entity.setBizDate(inputBean.getTrx_date());
		entity.setBreedDate(inputBean.getBreedDate());
		entity.setNlbpOrderNo(inputBean.getWork_order());
		//TODO entity.setEbsBatchNo(ebsBatchNo);
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setTransactionType(inputBean.getTrx_type());
		entity.setHandlerStatus("W");
		return entity;
	}
	
	public static NlbpSysOrderProcessModel convertChangeOrderBeanToOrderProcessModel(
			NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrgCode(inputBean.getOu_code());
		entity.setPbCode(inputBean.getPb_status());
		entity.setBizDate(inputBean.getDate());
		entity.setNlbpOrderNo(inputBean.getWork_order());
		//TODO entity.setEbsBatchNo(ebsBatchNo);
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setUperstage(inputBean.getStage());
		entity.setHandlerStatus("W");
		return entity;
	}
	
	/**
	 * 2017-06-05
	 * @param inputBean
	 * @return
	 */
	public static NlbpSysOrderProcessModel convertUpdateBatinfoupderpProcessModel(
			NlbpWsUpdateBatinfoupderpInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		
		entity.setEbsOrgCode(inputBean.getOrganization_code());
		entity.setConceiveStatus(inputBean.getHybridization_status());//配怀状态
		entity.setBreedDate(inputBean.getBreeding_date());//养殖日期
		entity.setRoomCode(inputBean.getBarn());//栋舍信息
		entity.setNlbpOrderNo(inputBean.getBatch_no()); 
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setHandlerStatus("W");
		return entity;
	}

}
