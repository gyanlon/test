package com.newhope.nlbp.core.apigate.suport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsMaterialPlanOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderModel;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.bean.NlbpWSListNlbpWsStockInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsChangeWorkOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsChangeWorkOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsCreateOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsCreateOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsStockInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsStrockOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsTransOrderInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsTransOrderOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsUpdateBatinfoupderpInputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpWsUpdateBatinfoupderpOutputBean;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderDao;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderProcessDao;
import com.newhope.nlbp.core.exception.NlbpBizException;

@Service("ApiGateWorkOrderService")
public class ApiGateWorkOrderService {

	@Autowired
	private NlbpSysOrderDao orderDao;
	@Autowired
	private NlbpSysOrderProcessDao processDao;
	
	/*
	 *  创建领料计划
	 */
	public NlbpWsMaterialPlanOutputBean createMaterialPlan(NlbpWsMaterialPlanInputBean apiInputBean){
		NlbpWsMaterialPlanOutputBean outputBean = new NlbpWsMaterialPlanOutputBean();
		
		// 转换数据
		NlbpSysOrderProcessModel entity = MaterialPlanConverUtils.convertA2P(apiInputBean);
		//插入数据
		processDao.insertSelective(entity);
		
		//返回数据
		outputBean.setStatus(true);
		//设置NLBP虚拟工单
		outputBean.setNlbpOrderNo(entity.getNlbpOrderNo());
		
		return outputBean;
	}
	
	/*
	 *  取消领料计划
	 */
	public NlbpWsCancelMaterialPlanOutputBean cancelMaterialPlan(NlbpWsCancelMaterialPlanInputBean apiInputBean){
		NlbpWsCancelMaterialPlanOutputBean outputBean = new NlbpWsCancelMaterialPlanOutputBean();
		
		// 转换数据
		NlbpSysOrderProcessModel entity = MaterialPlanConverUtils.convertCancelA2P(apiInputBean);
		
/*		// 获取计划创建记录
		NlbpSysOrderProcessModel applyProcessQueryModel = new NlbpSysOrderProcessModel();
		applyProcessQueryModel.setNlbpOrderNo(entity.getNlbpOrderNo());
		applyProcessQueryModel.setInterfaceType(WorkOrderInterfaceType.MATERIAL_PLAN.getCode());
		List<NlbpSysOrderProcessModel> applyProcessModels = processDao.findList(applyProcessQueryModel);
		
		//设置doc_type
		if(applyProcessModels != null && applyProcessModels.size()>0){
			applyProcessModels.get(0).getNlbpOrderNo();
			entity.setExpand8(applyProcessModels.get(0).getExpand8());
		}*/		
		
		//插入数据
		processDao.insertSelective(entity);
		
		//返回数据
		outputBean.setStatus(true);
		
		return outputBean;
	}

//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsCreateOrderOutputBean createWorkOrder(NlbpWsCreateOrderInputBean inputBean) {
		NlbpWsCreateOrderOutputBean outputBean = new NlbpWsCreateOrderOutputBean();
		String workOrderNum = createNlbpWorkOrder(inputBean);
		NlbpSysOrderProcessModel entity = WorkOrderBenConvertUtils.convertOrderBeanToOrderProcessModel(inputBean);

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		entity.setNlbpOrderNo(workOrderNum);
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_NEW.getCode());
		entity.setCreationDate(new Date());
		processDao.insertSelective(entity);
		outputBean.setStatus(true);
		outputBean.setWorkorder_num(workOrderNum);
		return outputBean;
	}
	
	private String createNlbpWorkOrder(NlbpWsCreateOrderInputBean inputBean){
		NlbpSysOrderModel entity = WorkOrderBenConvertUtils.convertOrderBeanToOrderModel(inputBean);
		orderDao.insertSelective(entity);
		

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		return entity.getNlbpOrderNo();
	}

//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsTransOrderOutputBean tranWorkOrder(List<NlbpWsTransOrderInputBean> inputBean) {
		NlbpWsTransOrderOutputBean nlbpWsTransOrderOutputBean = new NlbpWsTransOrderOutputBean();
		for (int i = 0; i < inputBean.size(); i++) {
			NlbpSysOrderProcessModel entity = WorkOrderBenConvertUtils.convertTranOrderBeanToOrderProcessModel(inputBean.get(i));

			List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));
			Map m = listMap.get(0);
			String bizType = m.get("attribute5").toString();//猪、禽
			entity.setExpand5(bizType);
			
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("orderNo", inputBean.get(i).getWork_order());
			map.put(WorkOrderProcessType.ORG_CODE.getCode(), entity.getExpand1());
			NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
			if(order == null){
				throw new NlbpBizException("NLBP工单不存在");
			}
			entity.setEbsOrderNo(order.getEbsOrderNo());//设置EBS工单到 entity
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_TRANSACTION.getCode());
			entity.setCreationDate(new Date());
			processDao.insertSelective(entity);
		}
		nlbpWsTransOrderOutputBean.setStatus(true);
		return nlbpWsTransOrderOutputBean;
	}

//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatus(NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();
		NlbpSysOrderProcessModel entity = WorkOrderBenConvertUtils.convertChangeOrderBeanToOrderProcessModel(inputBean);

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		Map map = new HashMap();
		map.put("orderNo", inputBean.getWork_order());
		map.put(WorkOrderProcessType.ORG_CODE.getCode(), entity.getExpand1());
		NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
		if(order == null){
			throw new NlbpBizException("NLBP工单不存在");
		}else{
			order.setOrderStatus("3");
			orderDao.updateByPrimaryKeySelective(order);
		}
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_FINISH.getCode());
		entity.setEbsOrderNo(order.getEbsOrderNo());//设置EBS工单到 entity
		entity.setCreationDate(new Date());
		processDao.insertSelective(entity);
		outputBean.setStatus(true);
		return outputBean;
	}
	
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatusForBird(
			List<NlbpWsChangeWorkOrderInputBean> inputBean) {
		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();
		for (int i = 0; i < inputBean.size(); i++) {
			NlbpSysOrderProcessModel entity = WorkOrderBenConvertUtils.convertChangeOrderBeanToOrderProcessModel(inputBean.get(i));

			List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));
			Map m = listMap.get(0);
			String bizType = m.get("attribute5").toString();//猪、禽
			entity.setExpand5(bizType);
			
			
			Map map = new HashMap();
			map.put("orderNo", inputBean.get(i).getWork_order());
			map.put(WorkOrderProcessType.ORG_CODE.getCode(), entity.getExpand1());
			NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
			
			if(order == null){
				throw new NlbpBizException("NLBP工单不存在");
			}else{
				order.setOrderStatus("3");
				orderDao.updateByPrimaryKeySelective(order);
			}
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_FINISH_BIRD.getCode());
			entity.setEbsOrderNo(order.getEbsOrderNo());//设置EBS工单到 entity
			entity.setCreationDate(new Date());
			processDao.insertSelective(entity);
		}
		outputBean.setStatus(true);
		return outputBean;
	}

	/**
	 * 
	 * @param inputBean
	 * @return
	 * 2017-06-05
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsUpdateBatinfoupderpOutputBean UpdateBatinfoupderp1(List<NlbpWsUpdateBatinfoupderpInputBean> inputBean) {
		try{
			for (int i = 0; i < inputBean.size(); i++) {
				
				NlbpSysOrderProcessModel entity = WorkOrderBenConvertUtils.convertUpdateBatinfoupderpProcessModel(inputBean.get(i));
				String nlbpOrderNo = entity.getNlbpOrderNo();//entity.getDocNum() + "-" + entity.getEventId();

				List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));
				Map m = listMap.get(0);
				String bizType = m.get("attribute5").toString();//猪、禽
				entity.setExpand5(bizType);
				
					
				Map map = new HashMap();
				map.put("orderNo", nlbpOrderNo);
				map.put(WorkOrderProcessType.ORG_CODE.getCode(), entity.getExpand1());
				NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
				if(order == null){
					throw new NlbpBizException("NLBP工单不存在");
				}
				entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_PH_STATUS.getCode());
				entity.setEbsOrderNo(order.getEbsOrderNo());//设置EBS工单到 entity
				entity.setCreationDate(new Date());
				processDao.insertSelective(entity);
			}
		}catch(Exception e){
			throw new NlbpBizException(e.getMessage());
		}
		NlbpWsUpdateBatinfoupderpOutputBean nlbpWsUpdateBatinfoupderpOutputBean = new NlbpWsUpdateBatinfoupderpOutputBean();
		nlbpWsUpdateBatinfoupderpOutputBean.setStatus(true);
		return nlbpWsUpdateBatinfoupderpOutputBean;

	}

	/**
	 * 
	 * @param inputBean
	 * @return
	 * 2017-06-05
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean CancleWorkOrderBird(NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		String nlbpOrderNo = inputBean.getWork_order();//inputBean.getDocNum() + "-" + inputBean.getEventLineId();
		Map map = new HashMap();
		map.put("orderNo", nlbpOrderNo);
		map.put(WorkOrderProcessType.ORG_CODE.getCode(), String.valueOf(inputBean.getOrg_id()));
		NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
		
		if(order == null){
			throw new NlbpBizException("NLBP工单不存在");
		}else{
			order.setOrderStatus("-1");
			orderDao.updateByPrimaryKeySelective(order);
		}
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrderNo(order.getEbsOrderNo());
		entity.setNlbpOrderNo(nlbpOrderNo);
		entity.setEbsOrgCode(inputBean.getOu_code());
		entity.setUperstage(inputBean.getStage());
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_CANCEL_BIRD.getCode());
		entity.setCreationDate(new Date());
		entity.setHandlerStatus("W");
		processDao.insertSelective(entity);
		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();
		output.setStatus(true);
		return output;

	}
	/**
	 * 
	 * @param inputBean
	 * @return
	 * 2017-06-05
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean CancleWorkOrderPig(NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		String nlbpOrderNo = inputBean.getWork_order();//inputBean.getDocNum() + "-" + inputBean.getEventLineId();
		Map map = new HashMap();
		map.put("orderNo", nlbpOrderNo);
		map.put(WorkOrderProcessType.ORG_CODE.getCode(), String.valueOf(inputBean.getOrg_id()));
		NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
		
		if(order == null){
			throw new NlbpBizException("NLBP工单不存在");
		}else{
			order.setOrderStatus("-1");
			orderDao.updateByPrimaryKeySelective(order);
		}
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrderNo(order.getEbsOrderNo());
		entity.setNlbpOrderNo(nlbpOrderNo);
		entity.setEbsOrgCode(inputBean.getOu_code());
		entity.setUperstage(inputBean.getStage());
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_CANCEL_PIG.getCode());
		entity.setCreationDate(new Date());
		entity.setHandlerStatus("W");
		processDao.insertSelective(entity);
		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();
		output.setStatus(true);
		return output;

	}

//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatustoWIP(NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();

		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map m = listMap.get(0);
		String bizType = m.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);
		
		String nlbpOrderNo = inputBean.getWork_order();//inputBean.getDocNum() + "-" + inputBean.getEventLineId();
		Map map = new HashMap();
		map.put("orderNo", nlbpOrderNo);
		map.put(WorkOrderProcessType.ORG_CODE.getCode(),String.valueOf(inputBean.getOrg_id()));
		NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
		
		if(order == null){
			throw new NlbpBizException("NLBP工单不存在");
		}else{
			order.setOrderStatus("2");
			orderDao.updateByPrimaryKeySelective(order);
		}
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setEbsOrderNo(order.getEbsOrderNo());
		entity.setNlbpOrderNo(nlbpOrderNo);
		entity.setEbsOrgCode(inputBean.getOu_code());
		entity.setCreatedBy(inputBean.getCreatedBy());
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_OPEN.getCode());
		entity.setCreationDate(new Date());
		entity.setHandlerStatus("W");
		processDao.insertSelective(entity);
		
		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();
		output.setStatus(true);
		return output;

	}
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatusForBirdtoWIP(
			List<NlbpWsChangeWorkOrderInputBean> inputBean) {
		
		for (int i = 0; i < inputBean.size(); i++) {
			NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
			NlbpWsChangeWorkOrderInputBean inBean = inputBean.get(0);
			
			List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inBean.getOrg_id() + ""));
			Map m = listMap.get(0);
			String bizType = m.get("attribute5").toString();//猪、禽
			entity.setExpand5(bizType);
			
			String nlbpOrderNo = inBean.getWork_order();//inBean.getDocNum() + "-" + inBean.getEventLineId();
			Map map = new HashMap();
			map.put("orderNo", nlbpOrderNo);
			map.put(WorkOrderProcessType.ORG_CODE.getCode(), String.valueOf(inBean.getOrg_id()));
			NlbpSysOrderModel order = orderDao.selectByNlbpOrderNo(map);
			
			if(order == null){
				throw new NlbpBizException("NLBP工单不存在");
			}else{
				order.setOrderStatus("2");
				orderDao.updateByPrimaryKeySelective(order);
			}
			entity.setDocNum(inBean.getDocNum());
			entity.setEventId(inBean.getEventLineId());
			entity.setExpand1(String.valueOf(inBean.getOrg_id()));
			entity.setEbsOrderNo(order.getEbsOrderNo());
			entity.setNlbpOrderNo(nlbpOrderNo);
			entity.setEbsOrgCode(inBean.getOu_code());
			entity.setCreatedBy(inBean.getCreatedBy());
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_OPEN_BIRD.getCode());
			entity.setCreationDate(new Date());
			entity.setHandlerStatus("W");
			processDao.insertSelective(entity);
		}
		
		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();
		outputBean.setStatus(true);
		return outputBean;

	}
	
	/**
	 * 其他出入库
	 * @param inputBean
	 * @return
	 */
	public NlbpWsStrockOutputBean syncStockBizService(
			NlbpWSListNlbpWsStockInputBean inputBean) {
		List<NlbpWsStockInputBean> listStockInputBean = new ArrayList<NlbpWsStockInputBean>();
		listStockInputBean = inputBean.getInputListBean();
		
		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getInputListBean().get(0).getOrg_id() + ""));
		Map map = listMap.get(0);
		String bizType = map.get("attribute5").toString();//猪、禽
		String nlbpPoNum =  "STOCK-"+inputBean.getType()+"-"+inputBean.getInputListBean().get(0).getDocNum()+"-"+inputBean.getInputListBean().get(0).getEventLineId();
		for (int i = 0; i < listStockInputBean.size(); i++) {
			NlbpWsStockInputBean stockInputBean = listStockInputBean.get(i);
			NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
			entity.setFarmCode(stockInputBean.getFarm_code());
			entity.setRoomCode(stockInputBean.getDs_code());//栋舍
			entity.setInventoryItemId(stockInputBean.getItem_code());
			entity.setBatchNumber(stockInputBean.getBatch_num());
			entity.setEarNumber(stockInputBean.getEp_num());
			if(!StringUtils.isEmpty(stockInputBean.getPty())){
				entity.setPrimaryUomQty(stockInputBean.getPty());
			}
			entity.setTransactionType(stockInputBean.getPurpose());
			entity.setSubInventroyCode(stockInputBean.getSub_code());//自库存
			if(!StringUtils.isEmpty(stockInputBean.getSecond_qty())){
				entity.setSecondUomQty(stockInputBean.getSecond_qty());//辅助数量
			}
			if(stockInputBean.getTypeCheck().equals("YZ")){
				entity.setBirdCheck("YZ_PIG");
			}else{
				entity.setBirdCheck(stockInputBean.getTypeCheck());
			}
			
			entity.setBizDate(stockInputBean.getBusiness_date());
			entity.setNlbpOrderNo(stockInputBean.getWork_order());
			entity.setDocNum(stockInputBean.getDocNum());
			entity.setEventId(stockInputBean.getEventLineId());
			entity.setExpand1(String.valueOf(stockInputBean.getOrg_id()));
			entity.setExpand2(stockInputBean.getPerson_id());//身份证
			entity.setExpand3(stockInputBean.getFlag());//工单标识(Y:要传工单；N:不传工单)
			entity.setExpand4(inputBean.getType());//出入库标识
			entity.setExpand5(bizType);//猪、禽
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_STOCK_IN_OUT.getCode());
			entity.setCreationDate(new Date());
			entity.setHandlerStatus("W");
			
			
			entity.setExpand7(nlbpPoNum);
			
			processDao.insertSelective(entity);
		}
		
		NlbpWsStrockOutputBean outputBean = new NlbpWsStrockOutputBean();
		outputBean.setStatus(true);
		outputBean.setIssue_num(nlbpPoNum);
		return outputBean;

	}
	
	/**
	 * 禽创建采购申请
	 * 
	 * @author ibmzhanghua
	 * @date: Jul 26, 2017 3:08:47 PM
	 * @param inputBean
	 * @return
	 */
	public NlbpWsPoOutputBean createPOforBird(NlbpWsPoInputBean inputBean,Map<String, Object> paramMap) {
		NlbpWsPoOutputBean outputBean = new NlbpWsPoOutputBean();
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
		entity.setOrgCode(paramMap.get("attribute4").toString());
		entity.setInventoryItemId(inputBean.getItem_code());
		entity.setBatchNumber(inputBean.getBatch_num());
		if(!StringUtils.isEmpty(inputBean.getQty())){
			entity.setPrimaryUomQty(inputBean.getQty());
		}
		entity.setExpand2(inputBean.getPerson_id());
		entity.setPrice(inputBean.getPrice());
		entity.setNlbpOrderNo(inputBean.getWork_oder());
		entity.setFarmCode(inputBean.getDept());
		String nlbpPoNum =  "CGSQ-"+inputBean.getEventLineId()+"-"+System.currentTimeMillis()+(new Random().nextInt(100));
		entity.setNlbpPurchOrder(nlbpPoNum);
		
		entity.setDocNum(inputBean.getDocNum());
		entity.setEventId(inputBean.getEventLineId());
		entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_APPLY_BIRD.getCode());
		entity.setCreationDate(new Date());
		entity.setHandlerStatus("W");
		
		List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map map = listMap.get(0);
		String bizType = map.get("attribute5").toString();//猪、禽
		entity.setExpand5(bizType);//猪、禽
		processDao.insertSelective(entity);
		
		outputBean.setStatus(true);
		outputBean.setPo_num(nlbpPoNum);
		return outputBean;
	}

	
	/**
	 * 禽创建销售订单
	 * 
	 * @author ibmzhanghua
	 * @date: Aug 16, 2017 11:26:01 AM
	 * @param inputBean
	 * @return
	 */
	public NlbpWsSaleOutputBean createSaleforBird(List<NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		String bizType = null;
		String orderType = null;
		for (NlbpWsSaleInputBean inputBean: inputBeanList) {
			entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
			entity.setExpand6(inputBean.getOrderNo());
			entity.setExpand7(inputBean.getSource());
			entity.setBizDate(inputBean.getTRX_DATE());
			entity.setExpand8(inputBean.getCusNo());
			entity.setExpand9(inputBean.getStates());
			entity.setExpand3(inputBean.getCurrency());
			entity.setExpand10(inputBean.getRefundReason());
			entity.setInventoryItemId(inputBean.getItme_code());
			entity.setPrimaryUomQty(inputBean.getQty());
            entity.setSecondUomQty(inputBean.getSecond_qty());
            entity.setEbsOrgCode(inputBean.getOu_code());
			
			entity.setPrice(inputBean.getPrice());
			entity.setExpand4(inputBean.getUnit());
			entity.setEarNumber(inputBean.getFzUnit());
			entity.setDocNum(inputBean.getDocNum());
			entity.setEventId(inputBean.getEventLineId());
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_CREATE_SALE.getCode());
			entity.setCreationDate(new Date());
			entity.setHandlerStatus("W");
			
			String nlbpOrderNo =  "XN-"+inputBean.getDocNum()+"-"+inputBean.getEventLineId()+"-"+System.currentTimeMillis();
			entity.setEbsOrderNo(nlbpOrderNo);
			entity.setNlbpOrderNo(nlbpOrderNo);
			entity.setBreedDate(inputBean.getSourceNo());
			entity.setExpand2(inputBean.getIdCard());
			if(bizType==null) {
				List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
				Map map = listMap.get(0);
				bizType = map.get("attribute5").toString();//猪、禽
				orderType = map.get("attribute1").toString();
			}
			entity.setOrgCode(orderType);
			entity.setOrderType(orderType+"_"+inputBean.getOrderType());
			entity.setExpand5(bizType);//猪、禽
			
			String nlbpSaleNo =  inputBean.getDocNum()+"-"+inputBean.getEventLineId()+"-"+System.currentTimeMillis();
			entity.setNlbpPurchOrder(nlbpSaleNo);
			
			processDao.insertSelective(entity);
			outputBean.setSale_num(nlbpSaleNo);
			outputBean.setNlbpOrderNo(nlbpOrderNo);
			outputBean.setEbsOrderNo(nlbpOrderNo);
		}
		outputBean.setStatus(true);
		return outputBean;
	}
	
	/**
	 * 禽销售订单出库接口
	 * 
	 * @author ibmzhanghua
	 * @date: Aug 16, 2017 11:26:01 AM
	 * @param inputBean
	 * @return
	 */
	public NlbpWsSaleOutputBean outSaleforBird(List<NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		String bizType = null;
		String orderType = null;
		String organization = null;
		for (NlbpWsSaleInputBean inputBean: inputBeanList) {
			entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
			entity.setEbsOrgCode(inputBean.getOu_code());
			
			
			entity.setInventoryItemId(inputBean.getItme_code());
			entity.setPrimaryUomQty(inputBean.getQty());
            entity.setSecondUomQty(inputBean.getSecond_qty());
            entity.setSubInventroyCode(inputBean.getSub_code());
            entity.setBizDate(inputBean.getTRX_DATE());
            entity.setBatchNumber(inputBean.getBatch_num());
            
            entity.setExpand7(inputBean.getSource());//来源类型
            entity.setDocNum(inputBean.getDocNum());
			entity.setEventId(inputBean.getEventLineId());
			
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_SALE_OUT.getCode());
			entity.setCreationDate(new Date());
			entity.setHandlerStatus("W");
			entity.setEbsOrderNo(inputBean.getEbsOrderNo());
			entity.setNlbpOrderNo(inputBean.getNlbpOrderNo());
			entity.setNlbpPurchOrder(inputBean.getNlbpSaleNo());
		
			
			if(bizType==null) {
				List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
				Map map = listMap.get(0);
				bizType = map.get("attribute5").toString();//猪、禽
				orderType = map.get("attribute1").toString();
				organization = map.get("attribute4").toString();
			}
			entity.setExpand6(organization);
			entity.setOrgCode(orderType);
			entity.setOrderType(orderType+"_"+inputBean.getOrderType());
			entity.setExpand5(bizType);//猪、禽
			processDao.insertSelective(entity);
			
		}
		outputBean.setStatus(true);
		return outputBean;
	}
	
	/**
	 * 禽销售订单入库接口
	 * 
	 * @author huachao
	 * @date: Aug 16, 2017 11:26:01 AM
	 * @param inputBean
	 * @return
	 */
	public NlbpWsSaleOutputBean inSaleforBird(List<NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		NlbpSysOrderProcessModel entity = new NlbpSysOrderProcessModel();
		String bizType = null;
		String orderType = null;
		String organization = null;
		for (NlbpWsSaleInputBean inputBean: inputBeanList) {
			entity.setExpand1(String.valueOf(inputBean.getOrg_id()));
			entity.setEbsOrgCode(inputBean.getOu_code());
			
			
			entity.setInventoryItemId(inputBean.getItme_code());
			entity.setPrimaryUomQty(inputBean.getQty());
            entity.setSecondUomQty(inputBean.getSecond_qty());
            entity.setSubInventroyCode(inputBean.getSub_code());
            entity.setBizDate(inputBean.getTRX_DATE());
            entity.setBatchNumber(inputBean.getBatch_num());
            
            entity.setExpand7(inputBean.getSource());//来源类型
            entity.setDocNum(inputBean.getDocNum());
			entity.setEventId(inputBean.getEventLineId());
			
			entity.setInterfaceType(WorkOrderInterfaceType.WORK_ORDER_SALE_IN.getCode());
			entity.setCreationDate(new Date());
			entity.setHandlerStatus("W");
			entity.setEbsOrderNo(inputBean.getEbsOrderNo());
			entity.setNlbpOrderNo(inputBean.getNlbpOrderNo());
			entity.setNlbpPurchOrder(inputBean.getNlbpSaleNo());
		
			
			if(bizType==null) {
				List<Map> listMap = processDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
				Map map = listMap.get(0);
				bizType = map.get("attribute5").toString();//猪、禽
				orderType = map.get("attribute1").toString();
				organization = map.get("attribute4").toString();
			}
			entity.setExpand6(organization);
			entity.setOrgCode(orderType);
			entity.setOrderType(orderType+"_"+inputBean.getOrderType());
			entity.setExpand5(bizType);//猪、禽
			processDao.insertSelective(entity);
			
		}
		outputBean.setStatus(true);
		return outputBean;
	}	
}
