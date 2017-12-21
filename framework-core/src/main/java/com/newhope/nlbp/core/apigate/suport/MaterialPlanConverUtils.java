package com.newhope.nlbp.core.apigate.suport;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.newhope.commons.lang.DateUtils;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsMaterialPlanInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.utils.BeanUtils;

public class MaterialPlanConverUtils {
	
	public static Map<String, String> planA2PConverMap = new HashMap<String, String>();
	
	public static Map<String, String> cancelPlanA2PConverMap = new HashMap<String, String>();
	
	static{
		//公司ID Value1
		planA2PConverMap.put("ORG_ID", "expand1");
		//库存组织ID Value2
		planA2PConverMap.put("ORGANIZATION_CODE", "ebsOrgCode");
		//物料编码 Value3
		planA2PConverMap.put("ITEM_NUMBER", "inventoryItemId");
		//申请数量 Value4
		planA2PConverMap.put("REQUEST_QTY", "primaryUomQty");
		//目标子库存 Value5
		planA2PConverMap.put("SUBINV_CODE", "subInventroyCode");
		//申请人 身份证号 Value6
		planA2PConverMap.put("NATIONAL_IDENTIFIER", "expand4");
		//需求日期Value7
		planA2PConverMap.put("NEED_DATE", "breedDate");
		// 部门 Value8 养殖场
		planA2PConverMap.put("DEPT_CODE", "farmCode");
		//栋舍 Value9
		planA2PConverMap.put("ASSEMBLING_UNIT", "roomCode");
		//合同号 非必填 Value10
		planA2PConverMap.put("CONTRACT_NO", "expand6");
		//工单号 非必填 Value11
		planA2PConverMap.put("BATCH_NO", "batchNumber");
		// 备注 非必填  Value12
		planA2PConverMap.put("DESCRIPTION", "expand7");
		
		//来源类型 养殖平台：养猪：YZ；种禽：ZQ  Value13
		planA2PConverMap.put("SOURCE_TYPE", "pbCode");
		//来源单据号 Value14
		planA2PConverMap.put("SOURCE_NUM", "docNum");
		//来源单据题头ID  Value15
		planA2PConverMap.put("SOURCE_HEADER_ID", "nlbpPurchOrder");
		//来源单据行ID  Value16
		planA2PConverMap.put("SOURCE_LINE_ID", "eventId");
		//养殖户 非必填  身份证号 Value17
		planA2PConverMap.put("VENDOR", "ebsPurchOrder");
		//饲料厂 value 18
		planA2PConverMap.put("FEED_NAME", "expand10");
		//extend8作为 组织内组织外 字段
		//orderProcessModel.setExpand8(docType);
		
		//------取消接口字段映射---------
		//公司ID Value1
		cancelPlanA2PConverMap.put("ORG_ID", "expand1");
		//库存组织ID Value2
		cancelPlanA2PConverMap.put("ORGANIZATION_CODE", "ebsOrgCode");
		//单据类型   内部INTER 外部ORG Value3
		cancelPlanA2PConverMap.put("DOC_TYPE", "expand8");
		//调拨申请单号 Value4
		cancelPlanA2PConverMap.put("DOC_NUM", "expand9");
		//来源类型 养殖平台：养猪：YZ；种禽：ZQ  Value5
		cancelPlanA2PConverMap.put("SOURCE_TYPE", "pbCode");
		//来源单据号 Value6
		cancelPlanA2PConverMap.put("SOURCE_NUM", "docNum");
		//来源单据题头ID  Value7
		cancelPlanA2PConverMap.put("SOURCE_HEADER_ID", "nlbpPurchOrder");
		//来源单据行ID  Value8
		cancelPlanA2PConverMap.put("SOURCE_LINE_ID", "eventId");		
		
	}
	
	public static NlbpSysOrderProcessModel convertA2P(NlbpWsMaterialPlanInputBean apiInputBean){
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		
		// 转换代码
		//设置接口类型
		entity.setInterfaceType(WorkOrderInterfaceType.MATERIAL_PLAN.getCode());
		//创建NLBP order no
		String nlbpOrderNo = "MP_" + apiInputBean.getSOURCE_NUM() + apiInputBean.getSOURCE_HEADER_ID() + apiInputBean.getSOURCE_LINE_ID() + DateUtils.getMillis();
		entity.setNlbpOrderNo(nlbpOrderNo);
		entity.setEbsOrderNo(nlbpOrderNo);
		
		Set<Entry<String, String>> keys = planA2PConverMap.entrySet();
		for(Entry<String, String> entry : keys){
			BeanUtils.setPropertyValue(entity, entry.getValue(), BeanUtils.getPropertyValue(apiInputBean, entry.getKey()));
		}
		
		return entity;
	}
	
	public static NlbpWsMaterialPlanInputBean convertP2W(NlbpSysOrderProcessModel entity){
		
		NlbpWsMaterialPlanInputBean inputBean = new NlbpWsMaterialPlanInputBean();
		// 转换代码
		
		Set<Entry<String, String>> keys = planA2PConverMap.entrySet();
		for(Entry<String, String> entry : keys){
			BeanUtils.setPropertyValue(inputBean, entry.getKey(), BeanUtils.getPropertyValue(entity, entry.getValue()));
		}
		
		return inputBean;
	}	
	
	/**
	 * 取消计划转换
	 * @param apiInputBean
	 * @return
	 */
	public static NlbpSysOrderProcessModel convertCancelA2P(NlbpWsCancelMaterialPlanInputBean apiInputBean){
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		
		// 转换代码
		//设置接口类型
		entity.setInterfaceType(WorkOrderInterfaceType.MATERIAL_PLAN_CANCEL.getCode());
		
		Set<Entry<String, String>> keys = cancelPlanA2PConverMap.entrySet();
		for(Entry<String, String> entry : keys){
			BeanUtils.setPropertyValue(entity, entry.getValue(), BeanUtils.getPropertyValue(apiInputBean, entry.getKey()));
		}
		
		return entity;
	}
	
	/**
	 * 取消计划转换
	 * @param apiInputBean
	 * @return
	 */
	public static NlbpWsCancelMaterialPlanInputBean convertCancelP2W(NlbpSysOrderProcessModel entity){
		
		NlbpWsCancelMaterialPlanInputBean inputBean = new NlbpWsCancelMaterialPlanInputBean();
		// 转换代码
		
		Set<Entry<String, String>> keys = cancelPlanA2PConverMap.entrySet();
		for(Entry<String, String> entry : keys){
			BeanUtils.setPropertyValue(inputBean, entry.getKey(), BeanUtils.getPropertyValue(entity, entry.getValue()));
		}
		
		return inputBean;
	}	
	
	public static void main(String[] args){
		NlbpWsMaterialPlanInputBean apiBean = new NlbpWsMaterialPlanInputBean();
		apiBean.setASSEMBLING_UNIT("1111");
		apiBean.setBATCH_NO("bbbbb");
		apiBean.setCONTRACT_NO("ccccc");
		
		NlbpSysOrderProcessModel entity = convertA2P(apiBean);
		
		NlbpWsMaterialPlanInputBean mBean = convertP2W(entity);

	}
}
