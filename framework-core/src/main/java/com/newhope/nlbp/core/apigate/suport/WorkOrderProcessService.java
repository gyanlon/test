package com.newhope.nlbp.core.apigate.suport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newhope.nlbp.common.bean.webservice.item.NlbpWSListNlbpWsStockInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCancelMaterialPlanOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsChangeWorkOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsChangeWorkOrderOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCreateOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsCreateOrderOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsMaterialPlanInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsMaterialPlanOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsPoOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsSaleOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsStrockOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsTransOrderInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsTransOrderOutputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsUpdateBatinfoupderpInputBean;
import com.newhope.nlbp.common.bean.webservice.item.NlbpWsUpdateBatinfoupderpOutputBean;
import com.newhope.nlbp.core.apigate.bean.NlbpSysOrderProcessModel;
import com.newhope.nlbp.core.apigate.bean.NlbpSysTranstionManagementModel;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderDao;
import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderProcessDao;
import com.newhope.nlbp.webservice.sys.base.Output;
import com.newhope.nlbp.webservice.sys.business.CancelMaterialPlan;
import com.newhope.nlbp.webservice.sys.business.CancleWorkOrder;
import com.newhope.nlbp.webservice.sys.business.ChangeWorkOrderStatus;
import com.newhope.nlbp.webservice.sys.business.CreateMaterialPlan;
import com.newhope.nlbp.webservice.sys.business.CreatePO;
import com.newhope.nlbp.webservice.sys.business.CreateWorkOrder;
import com.newhope.nlbp.webservice.sys.business.SaleOrder;
import com.newhope.nlbp.webservice.sys.business.StockWebService;
import com.newhope.nlbp.webservice.sys.business.TranscationWorkOrder;
import com.newhope.nlbp.webservice.sys.business.UpdateBatinfoupderp;

@Service
public class WorkOrderProcessService {

	@Autowired
	private CreateWorkOrder createWorkOrder;

	@Autowired
	private TranscationWorkOrder transcationWorkOrder;

	@Autowired
	private ChangeWorkOrderStatus changeWorkOrderStatus;

	@Autowired
	private UpdateBatinfoupderp updateBatinfoupderp;// 种禽栋舍更新以及配怀状态更新
	
	
	@Autowired
	private StockWebService stockWebService;

	@Autowired
	private CancleWorkOrder cancleWorkOrder;

	@Autowired
	private NlbpSysOrderDao nlbpSysOrderDao;

	@Autowired
	private NlbpSysOrderProcessDao nlbpSysOrderProcessDao;
	
	@Autowired
	private SaleOrder saleOrder;
	
	@Autowired
	private CreatePO cp;
	
	@Autowired
	private CreateMaterialPlan cmplan;
	
	@Autowired
	private CancelMaterialPlan cancelMplan;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 物料计划
	 * @param inputBean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsMaterialPlanOutputBean createMaterialPlan(NlbpWsMaterialPlanInputBean inputBean) {
		NlbpWsMaterialPlanOutputBean outputBean = new NlbpWsMaterialPlanOutputBean();
		
		//TODO 调用webservice
		Output outputXML = cmplan.createPlan(inputBean);
		
		// 解析webservice返回结果
		if (outputXML.code.equals("S000A000")) {
			Document document;

			try {
				document = DocumentHelper.parseText(outputXML.data);

				Element root = document.getRootElement();
				
				if(root.asXML().contains("HEADER")==false){
					outputBean.setStatus(false);
					return outputBean;
				}
					

				List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					Element tepE = (Element) tepList.get(m);
					//EBS单据号
					// 设置组织DOC_TYPE
					String docType = tepE.elementTextTrim("DOC_TYPE");
					String docNo = tepE.elementTextTrim("DOC_NUM");
					outputBean.setDocType(docType);
					outputBean.setEbsOrderNo(docNo);
					outputBean.setStatus(true);
				}


			} catch (DocumentException e) {
				logger.error("createWorkOrder", e);
				outputBean.setMessage("解析XML报错");
				outputBean.setStatus(false);
				return outputBean;
			}

			return outputBean;
		} else {
			outputBean.setMessage(outputXML.message);
			outputBean.setStatus(false);
			return outputBean;

		}
	}
	
	/**
	 * 物料计划
	 * @param inputBean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsCancelMaterialPlanOutputBean cancelMaterialPlan(NlbpWsCancelMaterialPlanInputBean inputBean) {
		
		// 获取计划创建记录
		NlbpSysOrderProcessModel applyProcessQueryModel = new NlbpSysOrderProcessModel();
		applyProcessQueryModel.setNlbpOrderNo(inputBean.getNlbpOrderNo());
		applyProcessQueryModel.setInterfaceType(WorkOrderInterfaceType.MATERIAL_PLAN.getCode());
		List<NlbpSysOrderProcessModel> applyProcessModels = nlbpSysOrderProcessDao.findList(applyProcessQueryModel);
		
		//设置doc_type
		if(applyProcessModels != null && applyProcessModels.size()>0){
			inputBean.setDOC_TYPE(applyProcessModels.get(0).getExpand8());
			//传入正向申请领饲料时返回至云端放养单据类型为INTER对应的DOC_NUM字段值，最后EBS执行内部交易申请界面的单据关闭功能。
			inputBean.setDOC_NUM(applyProcessModels.get(0).getEbsOrderNo());
		}
		
		NlbpWsCancelMaterialPlanOutputBean outputBean = new NlbpWsCancelMaterialPlanOutputBean();
		
		//TODO 调用webservice
		Output outputXML = cancelMplan.cancelPlan(inputBean);
		
		//TODO 解析webservice返回结果
		if (outputXML.code.equals("S000A000")) {
			Document document;

			try {
				document = DocumentHelper.parseText(outputXML.data);

				Element root = document.getRootElement();
				
				if(root.asXML().contains("HEADER")==false){
					outputBean.setStatus(false);
					return outputBean;
				}
					

				List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					Element tepE = (Element) tepList.get(m);
					//EBS单据号
					// 设置组织DOC_TYPE
					String docType = tepE.elementTextTrim("DOC_TYPE");
					String docNo = tepE.elementTextTrim("DOC_NUM");
					outputBean.setDocType(docType);
					outputBean.setEbsOrderNo(docNo);
				}				
				outputBean.setNlbpOrderNo(inputBean.getNlbpOrderNo());
				outputBean.setStatus(true);

			} catch (DocumentException e) {
				logger.error("createWorkOrder", e);
				outputBean.setMessage("解析XML报错");
				outputBean.setStatus(false);
				return outputBean;
			}

			return outputBean;
		} else {
			outputBean.setMessage(outputXML.message);
			outputBean.setStatus(false);
			return outputBean;

		}
	}	
	
	/**
	 * 物料计划
	 * @param inputBean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsCancelMaterialPlanOutputBean cancelMaterialPlanSync(NlbpWsCancelMaterialPlanInputBean inputBean) {

		NlbpWsCancelMaterialPlanOutputBean outputBean = new NlbpWsCancelMaterialPlanOutputBean();
		
		// 调用webservice
		Output outputXML = cancelMplan.cancelPlan(inputBean);
		
		// 解析webservice返回结果
		if (outputXML.code.equals("S000A000")) {
			Document document;

			try {
				document = DocumentHelper.parseText(outputXML.data);

				Element root = document.getRootElement();
				
				if(root.asXML().contains("HEADER")==false){
					outputBean.setStatus(false);
					return outputBean;
				}
					

				List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					Element tepE = (Element) tepList.get(m);
					//EBS单据号
					// 设置组织DOC_TYPE
					String docType = tepE.elementTextTrim("DOC_TYPE");
					String docNo = tepE.elementTextTrim("DOC_NUM");
					outputBean.setDocType(docType);
					outputBean.setEbsOrderNo(docNo);
				}				
				outputBean.setNlbpOrderNo(inputBean.getNlbpOrderNo());
				outputBean.setStatus(true);

			} catch (DocumentException e) {
				logger.error("createWorkOrder", e);
				outputBean.setMessage("解析XML报错");
				outputBean.setStatus(false);
				return outputBean;
			}

			return outputBean;
		} else {
			outputBean.setMessage(outputXML.message);
			outputBean.setStatus(false);
			return outputBean;

		}
	}	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsCreateOrderOutputBean createWorkOrder(NlbpWsCreateOrderInputBean inputBean) {

		NlbpWsCreateOrderOutputBean outputBean = new NlbpWsCreateOrderOutputBean();

		long org_id = inputBean.getOrg_id();
		String itemCode = inputBean.getItme_code();

		String itemCode1 = itemCode.substring(0, 10);

		Map<String, Object> map = nlbpSysOrderProcessDao.SelectItemCreateUsed(itemCode1, (new Long(org_id).intValue()));
		if (map == null) {
			outputBean.setMessage("传入参数错误，公司名和物料不匹配");
			outputBean.setStatus(false);
			return outputBean;

		}
		String str = org_id + "";
		Integer org = Integer.valueOf(str);
		List<Map> m = nlbpSysOrderProcessDao.selectTypeinfor(org);

		String type = m.get(0).get("attribute5").toString();

		if ((null != inputBean.getBatch_num() && !inputBean.getBatch_num().equals(""))
				|| (null != inputBean.getEP_NUMBER() && !inputBean.getEP_NUMBER().equals(""))) {
			Map input = new HashMap();
			input.put("batch_code", inputBean.getBatch_num());
			input.put("org_id", org_id);
			input.put("item_code", inputBean.getItme_code());
			input.put("ear_num", inputBean.getEP_NUMBER());
			// input.put("trx_d ate", inputBean.getTRX_DATE());

			List<Map> workorderMapList = new LinkedList();
			if (!inputBean.getBatch_num().equals("")) {
				workorderMapList = nlbpSysOrderDao.getEBSworkOrderNum(input);
			} else if (!inputBean.getEP_NUMBER().equals("")) {
				workorderMapList = nlbpSysOrderDao.getEBSworkOrderNumUsingEP(input);

			}
			// 取消校验重复性问题
			// if (workorderMapList == null || workorderMapList.size() == 0) {
			//
			// } else if (type.equals("YZ_PIG")) {
			// String tempOrder =
			// workorderMapList.get(0).get("ebs_order_no")==null?"":workorderMapList.get(0).get("ebs_order_no").toString();
			// String work_order = tempOrder;
			// outputBean.setStatus(true);
			// outputBean.setWorkorder_num(work_order);
			// outputBean.setMessage("工单号已经生成了 没有同步EBS");
			// return outputBean;
			//
			// }

		}

		Output outputXML = createWorkOrder.CreateOrder(map.get("ebs_org_code").toString(), itemCode, inputBean.getQty(),
				inputBean.getBatch_num(), inputBean.getFarm(), inputBean.getDS_NUMBER(), inputBean.getPB_CODE(),
				inputBean.getEP_NUMBER(), inputBean.getSTAGE_TYPE(), inputBean.getTRX_DATE(),
				inputBean.getWorkOrderType());

		if (outputXML.code.equals("S000A000")) {
			Document document;

			try {
				document = DocumentHelper.parseText(outputXML.data);

				Element root = document.getRootElement();

				String workoder = root.elementTextTrim("BATCH_NO");
				String status = root.elementTextTrim("BATCH_STATUS");
				if (workoder.equals("") || null == workoder) {

					outputBean.setStatus(false);
					outputBean.setMessage("返回工单号为空");

					return outputBean;

				}

				outputBean.setStatus(true);
				outputBean.setWorkorder_num(workoder);
				System.out.println("*****************workoder*************************=" + workoder);
				// update on June 9:不需要插入记录
				/*
				 * NlbpSysOrderModel ommrecord = new NlbpSysOrderModel();
				 * ommrecord.setBatchNumber(inputBean.getBatch_num());
				 * ommrecord.setOrderStatus(status);
				 * ommrecord.setOrganizationId(org_id);
				 * ommrecord.setEbsOrderNo(workoder==null?"":workoder);
				 * ommrecord.setNlbpOrderNo(inputBean.getEventLineId());//
				 * 用来处理Nlbp_Order_No
				 * ommrecord.setEarNumber(inputBean.getEP_NUMBER());
				 * ommrecord.setAttribute1(inputBean.getItme_code());
				 * ommrecord.setAttribute2(inputBean.getTRX_DATE());
				 * nod.insert(ommrecord);
				 */

			} catch (DocumentException e) {
				logger.error("createWorkOrder", e);
				outputBean.setMessage("解析XML报错");
				outputBean.setStatus(false);
				return outputBean;
			}

			return outputBean;
		} else {
			outputBean.setMessage(outputXML.message);
			outputBean.setStatus(false);
			return outputBean;

		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsTransOrderOutputBean tranWorkOrder(List<NlbpWsTransOrderInputBean> inputBean) {

		NlbpWsTransOrderOutputBean nlbpWsTransOrderOutputBean = new NlbpWsTransOrderOutputBean();

		try {
			for (int i = 0; i < inputBean.size(); i++) {
				long org_id = inputBean.get(i).getOrg_id();
				String itemCode = inputBean.get(i).getItem_code();
				if (itemCode.length() == 15) {
					itemCode = itemCode.substring(0, 10);

				}
				Map<String, Object> map = nlbpSysOrderProcessDao.SelectItemCreateUsed(itemCode,
						(new Long(org_id).intValue()));

				inputBean.get(i).setOu_code(map.get("ebs_org_code").toString());
			}

			Output outputXML = transcationWorkOrder.TranscationOrder(inputBean);

			if (outputXML.code.equals("S000A000")) {

				nlbpWsTransOrderOutputBean.setStatus(true);

				for (int i = 0; i < inputBean.size(); i++) {
					NlbpSysTranstionManagementModel ntm = new NlbpSysTranstionManagementModel();
					NlbpWsTransOrderInputBean tepBean = inputBean.get(i);
					ntm.setBatchNum(tepBean.getBatch_num());
					ntm.setWorkOrder(tepBean.getWork_order());
					ntm.setItemCode(tepBean.getItem_code());
					ntm.setOrgId(tepBean.getOrg_id());
					ntm.setOuCode(tepBean.getOu_code());
					ntm.setPrimaryQty(tepBean.getPrimary_qty());
					ntm.setPbStatus(tepBean.getPb_status());
					ntm.setTrxType(tepBean.getTrx_type());
					ntm.setBreeddate(tepBean.getBreedDate());
					ntm.setTrxDate(tepBean.getTrx_date());
					ntm.setSubCode(tepBean.getSub_code());
					ntm.setSecondQty(tepBean.getSecond_qty());
					ntm.setUperstage(tepBean.getUperstage());
					nlbpSysOrderProcessDao.insertSysTranstionManagement(ntm);
				}

				return nlbpWsTransOrderOutputBean;
			} else {
				nlbpWsTransOrderOutputBean.setStatus(false);
				nlbpWsTransOrderOutputBean.setMessage(outputXML.message);
				return nlbpWsTransOrderOutputBean;

			}
		} catch (Exception e) {
			nlbpWsTransOrderOutputBean.setStatus(false);
			nlbpWsTransOrderOutputBean.setMessage("物料与组织ID， 找不到对应关系");

			// e.printStackTrace();
			logger.error("tranWorkOrder", e);

		} finally {

			return nlbpWsTransOrderOutputBean;

		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatus(NlbpWsChangeWorkOrderInputBean inputBean) {

		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();

		List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		Map map = listMap.get(0);
		String ou_code = map.get("ebs_org_code").toString();

		Output xmloutput = changeWorkOrderStatus.changeWorkOrder(ou_code, inputBean.getWork_order(),
				inputBean.getDate(), WorkOrderProcessType.HANDLER_STATUS_S.getCode(), inputBean.getPb_status(), null);
		if (xmloutput.code.equals("S000A000")) {
			long tempOrgid = inputBean.getOrg_id();
			String workOrder = inputBean.getWork_order();

			// nod.updateWorkOrderStatusFromOrgId(String.valueOf(tempOrgid),
			// workOrder);
			outputBean.setStatus(true);

		} else {

			outputBean.setStatus(false);
			outputBean.setMessage(xmloutput.message);
		}
		return outputBean;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatusForBird(
			List<NlbpWsChangeWorkOrderInputBean> inputBean) {

		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();
		for (int i = 0; i < inputBean.size(); i++) {
			List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));

			Map map = listMap.get(0);
			String ou_code = map.get("ebs_org_code").toString();
			inputBean.get(i).setOu_code(ou_code);
		}
		Output xmloutput = changeWorkOrderStatus.changeWorkOrder(null, null, null, "M", null, inputBean);
		if (xmloutput.code.equals("S000A000")) {

			for (int i = 0; i < inputBean.size(); i++) {
				long tempOrgid = inputBean.get(i).getOrg_id();

				String workOrder = inputBean.get(i).getWork_order();

				// nod.updateWorkOrderStatusFromOrgId(String.valueOf(tempOrgid),
				// workOrder);
			}
			outputBean.setStatus(true);

		} else {

			outputBean.setStatus(false);
			outputBean.setMessage(xmloutput.message);
		}
		return outputBean;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsStrockOutputBean stockBizService(NlbpWSListNlbpWsStockInputBean inputBean) {
		NlbpWsStrockOutputBean outputBean = new NlbpWsStrockOutputBean();
		Output outputXML = new Output();
		if (inputBean.getType().toUpperCase().equals("OUT")) {
			outputXML = stockWebService.StockOut(inputBean.getInputListBean());
		} else {
			outputXML = stockWebService.StockIn(inputBean.getInputListBean());
		}
		if (outputXML.code.equals("S000A000")) {
			Document document;
			try {
				document = DocumentHelper.parseText(outputXML.data);
				Element root = document.getRootElement();
				if (root.asXML().contains("HEADER") == false) {

					outputBean.setStatus(false);
					outputBean.setMessage("EBS 返回格式错误");

				}
				List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					Map objecMap = new HashMap();

					Element tepE = (Element) tepList.get(m);

					String tt = tepE.elementTextTrim("ISSUE_NO");
					outputBean.setIssue_num(tt);
					outputBean.setStatus(true);

				}

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				outputBean.setStatus(false);
				outputBean.setMessage(e.getMessage());
			}

		} else {
			outputBean.setStatus(false);
			outputBean.setMessage(outputXML.message);

		}

		return outputBean;

	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsUpdateBatinfoupderpOutputBean UpdateBatinfoupderp(List<NlbpWsUpdateBatinfoupderpInputBean> inputBean) {

		NlbpWsUpdateBatinfoupderpOutputBean nlbpWsUpdateBatinfoupderpOutputBean = new NlbpWsUpdateBatinfoupderpOutputBean();

		try {
			for (int i = 0; i < inputBean.size(); i++) {
				List<Map> listMap = nlbpSysOrderProcessDao
						.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));
				Map map = listMap.get(0);
				inputBean.get(i).setOrganization_code(map.get("ebs_org_code").toString());
			}

			Output outputXML = updateBatinfoupderp.UpdateBatinfoupderp1(inputBean);

			if (outputXML.code.equals("S000A000")) {
				nlbpWsUpdateBatinfoupderpOutputBean.setStatus(true);
				return nlbpWsUpdateBatinfoupderpOutputBean;
			} else {
				nlbpWsUpdateBatinfoupderpOutputBean.setStatus(false);
				nlbpWsUpdateBatinfoupderpOutputBean.setMessage(outputXML.message);
				return nlbpWsUpdateBatinfoupderpOutputBean;

			}
		} catch (Exception e) {
			nlbpWsUpdateBatinfoupderpOutputBean.setStatus(false);
			nlbpWsUpdateBatinfoupderpOutputBean.setMessage("种禽栋舍更新以及配怀状态更新失败");

			// e.printStackTrace();
			logger.error("UpdateBatinfoupderp", e);

		} finally {
			return nlbpWsUpdateBatinfoupderpOutputBean;

		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean CancleWorkOrderBird(NlbpWsChangeWorkOrderInputBean inputBean) {

		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();

		List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		String ou_code = listMap.get(0).get("ebs_org_code").toString();

		Output outputXML = cancleWorkOrder.CancelWorderOrderService(ou_code, inputBean.getWork_order(),
				inputBean.getStage());

		if (outputXML.code.equals("S000A000")) {
			// nod.updateWorkOrderCancleStatusFromOrgId(String.valueOf(inputBean.getOrg_id()),
			// inputBean.getWork_order());
			output.setStatus(true);

		} else {
			output.setStatus(false);
			output.setMessage(outputXML.message);
		}

		return output;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean CancleWorkOrderPig(NlbpWsChangeWorkOrderInputBean inputBean) {

		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();

		List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		String ou_code = listMap.get(0).get("ebs_org_code").toString();

		Output outputXML = cancleWorkOrder.CancelWorderOrderServicePIG(ou_code, inputBean.getWork_order(),
				inputBean.getStage());

		if (outputXML.code.equals("S000A000")) {
			// nod.updateWorkOrderCancleStatusFromOrgId(String.valueOf(inputBean.getOrg_id()),
			// inputBean.getWork_order());
			output.setStatus(true);

		} else {
			output.setStatus(false);
			output.setMessage(outputXML.message);
		}

		return output;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatustoWIP(NlbpWsChangeWorkOrderInputBean inputBean) {
		NlbpWsChangeWorkOrderOutputBean output = new NlbpWsChangeWorkOrderOutputBean();

		List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.getOrg_id() + ""));
		String ou_code = listMap.get(0).get("ebs_org_code").toString();
		Output outputXML = changeWorkOrderStatus.changeWorkOrderTOWIP(ou_code, inputBean.getWork_order());
		if (outputXML.code.equals("S000A000")) {
			// nod.updateWorkOrderStatusFromOrgIdtoWIP(String.valueOf(inputBean.getOrg_id()),
			// inputBean.getWork_order());
			output.setStatus(true);

		} else {
			output.setStatus(false);
			output.setMessage(outputXML.message);
		}

		return output;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsChangeWorkOrderOutputBean changeWorkOrderStatusForBirdtoWIP(
			List<NlbpWsChangeWorkOrderInputBean> inputBean) {

		NlbpWsChangeWorkOrderOutputBean outputBean = new NlbpWsChangeWorkOrderOutputBean();
		for (int i = 0; i < inputBean.size(); i++) {
			List<Map> listMap = nlbpSysOrderProcessDao.selectTypeinfor(Integer.valueOf(inputBean.get(i).getOrg_id() + ""));

			Map map = listMap.get(0);
			String ou_code = map.get("ebs_org_code").toString();
			inputBean.get(i).setOu_code(ou_code);
		}
		Output outputXML = changeWorkOrderStatus.changeWorkOrderTOWIPforBird(inputBean);
		if (outputXML.code.equals("S000A000")) {

			// for (int i = 0; i < inputBean.size(); i++) {
			//
			// nod.updateWorkOrderStatusFromOrgIdtoWIP(String.valueOf(inputBean.get(i).getOrg_id()),
			// inputBean.get(i).getWork_order());
			//
			// }
			outputBean.setStatus(true);

		} else {
			outputBean.setStatus(false);
			outputBean.setMessage(outputXML.message);
		}

		return outputBean;

	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsPoOutputBean createPOforBird(NlbpWsPoInputBean inputBean) {
		NlbpWsPoOutputBean outputBean = new NlbpWsPoOutputBean();
		String itemCode = "";
		if (inputBean.getItem_code().length() == 15)
			itemCode = inputBean.getItem_code().substring(0, 10);
		else
			itemCode = inputBean.getItem_code();
		Map<String, Object> map = nlbpSysOrderProcessDao.SelectItemCreateUsed(itemCode, (new Long(inputBean.getOrg_id()).intValue()));
		if(map==null)
		{
			outputBean.setMessage("传入参数错误，公司名和物料不匹配");
			outputBean.setStatus(false);
			return outputBean;
			
		}
		
		Output xmloutput = cp.CreatePOforBird(inputBean.getOrg_id() + "", map.get("attribute4").toString(),
				inputBean.getItem_code().toString(), inputBean.getBatch_num().toString(), inputBean.getQty().toString(),
				inputBean.getPerson_id().toString(),inputBean.getPrice(),inputBean.getWork_oder(),inputBean.getDept());

		if (xmloutput.code.equals("S000A000")) {

			Document document;

			try {
				document = DocumentHelper.parseText(xmloutput.data);

				Element root = document.getRootElement();

				Element header = root.element("HEADER");
				String po_num = header.elementTextTrim("REQUISITIONS_NUM");

				if (po_num.equals(null) || po_num.equals("")) {
					outputBean.setStatus(false);
					outputBean.setMessage("申请单号返回为空");
					return outputBean;

				}

				outputBean.setStatus(true);
				outputBean.setPo_num(po_num);

				return outputBean;

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				logger.error("createPOforBird",e);
				outputBean.setMessage("XML解析错误");
				outputBean.setStatus(false);
				return outputBean;
			}

		}

		else {
			outputBean.setMessage(xmloutput.message);
			outputBean.setStatus(false);

		}

		return outputBean;

	}
	
	/**
	 * 创建销售订单
	 * 
	 * @author ibmzhanghua
	 * @date: Aug 16, 2017 5:34:26 PM
	 * @param inputBeanList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsSaleOutputBean createSaleOrder(List <NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		Output outputXML = new Output();
		outputXML = saleOrder.createSaleOrder(inputBeanList);
		if (outputXML.code.equals("S000A000")) {
			Document document;
			try {
				document = DocumentHelper.parseText(outputXML.data);
				Element root = document.getRootElement();
				if (root.asXML().contains("ORDER_NUMBER") == false) {

					outputBean.setStatus(false);
					outputBean.setMessage("EBS 返回格式错误");

				}
				
				String tt = root.elementTextTrim("ORDER_NUMBER");
				String lineNum = root.elementTextTrim("ORDER_LINE_NUMBER");
				outputBean.setSale_num(tt);
				outputBean.setSale_line_num(lineNum);
				outputBean.setStatus(true);
				/*List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					
					Element tepE = (Element) tepList.get(m);

					String tt = tepE.elementTextTrim("ORDER_NUMBER");
					outputBean.setSale_num(tt);
					outputBean.setStatus(true);

				}*/

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			outputBean.setStatus(false);
			outputBean.setMessage(outputXML.message);

		}

		return outputBean;

	}
	
	/**
	 * 创建销售订单
	 * 
	 * @author ibmzhanghua
	 * @date: Aug 16, 2017 5:34:26 PM
	 * @param inputBeanList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsSaleOutputBean outSaleOrder(List <NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		Output outputXML = new Output();
		/*for(NlbpWsSaleInputBean inputBean :inputBeanList) {
			NlbpSysOrderProcessModel model = new NlbpSysOrderProcessModel();
			model.setNlbpPurchOrder(inputBean.getNlbpSaleNo());
			List<NlbpSysOrderProcessModel> process = nlbpSysOrderProcessDao.findList(model);
			if(process!=null && process.size()>0) {
				inputBean.setEbsSaleNo(process.get(0).getEbsPurchOrder());
			}
		}*/
		outputXML = saleOrder.outSaleOrder(inputBeanList);
		if (outputXML.code.equals("S000A000")) {
		//	Document document;
		//	try {
				//document = DocumentHelper.parseText(outputXML.data);
			
				outputBean.setStatus(true);
				/*List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					
					Element tepE = (Element) tepList.get(m);

					String tt = tepE.elementTextTrim("ORDER_NUMBER");
					outputBean.setSale_num(tt);
					outputBean.setStatus(true);

				}*/

		/*	} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		} else {
			outputBean.setStatus(false);
			outputBean.setMessage(outputXML.message);

		}

		return outputBean;

	}
	
	/**
	 * 创建销售退货入库
	 * 
	 * @author ibmzhanghua
	 * @date: Aug 16, 2017 5:34:26 PM
	 * @param inputBeanList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NlbpWsSaleOutputBean inSaleOrder(List <NlbpWsSaleInputBean> inputBeanList) {
		NlbpWsSaleOutputBean outputBean = new NlbpWsSaleOutputBean();
		Output outputXML = new Output();
		outputXML = saleOrder.inSaleOrder(inputBeanList);
		if (outputXML.code.equals("S000A000")) {
		//	Document document;
		//	try {
				//document = DocumentHelper.parseText(outputXML.data);
			
				outputBean.setStatus(true);
				/*List tepList = root.elements("HEADER");

				for (int m = 0; m < tepList.size(); m++) {
					
					Element tepE = (Element) tepList.get(m);

					String tt = tepE.elementTextTrim("ORDER_NUMBER");
					outputBean.setSale_num(tt);
					outputBean.setStatus(true);

				}*/

		/*	} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		} else {
			outputBean.setStatus(false);
			outputBean.setMessage(outputXML.message);

		}

		return outputBean;

	}	
}
