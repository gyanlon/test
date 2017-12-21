package com.newhope.nlbp.core.apigate.suport.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newhope.nlbp.core.apigate.suport.WorkOrderInterfaceType;
import com.newhope.nlbp.core.apigate.suport.WorkOrderProcessType;
import com.newhope.nlbp.core.pipeline.Pipeline;

/**
 * 养殖平台推送数据到EBS任务类
 * 
 * @author JackDou
 * @since 2017-07-10
 */
public final class EsbPushDataTask implements Callable<Boolean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsbPushDataTask.class);

	private static final Pipeline TASK_PIPELINE = new TaskPipeline(20);

	private final TaskContext taskContext;

	public EsbPushDataTask(final TaskContext taskContext) {
		this.taskContext = taskContext;
	}
	

	@Override
	public Boolean call() throws Exception {
		LOGGER.info(">>>>>>>>>>>>> task pipeline start...");
		LOGGER.info("current thread name:" + Thread.currentThread().getName());
		LOGGER.info("task context:" + taskContext.toString());
		if (WorkOrderProcessType.WORK_ORDER_TRY.getCode().equals(taskContext.getTryFlag())) {
			LOGGER.info("task tryFlag:[status:W|E, try count:<4]");
		} else {
			LOGGER.info("task tryFlag:[status:E, retry count:>3&<11]");
		}
		long startTime = System.currentTimeMillis();
		boolean result = TASK_PIPELINE.execute(taskContext);
		long endTime = System.currentTimeMillis();
		LOGGER.info("task pipeline deal result:" + result);
		LOGGER.info("task pipeline deal cost time:" + (endTime - startTime) + "ms");
		LOGGER.info("<<<<<<<<<<<<< task pipeline end...");
		return Boolean.valueOf(result);
	}


	static {
		// 创建工单
		TASK_PIPELINE.addStage(new WoNewStage());

		// 猪打开工单
		TASK_PIPELINE
				.addStage(new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_NEW }));
		TASK_PIPELINE.addStage(new WoOpenStage());

		// 禽打开工单
		TASK_PIPELINE
				.addStage(new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_NEW }));
		TASK_PIPELINE.addStage(new WoOpenBirdStage());

		
		// 禽创建采购申请
		TASK_PIPELINE
				.addStage(new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_NEW }));
		TASK_PIPELINE.addStage(new WoCreatePoBirdStage());

		
		// 工单事务处理
		TASK_PIPELINE.addStage(new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_NEW,
				WorkOrderInterfaceType.WORK_ORDER_OPEN, WorkOrderInterfaceType.WORK_ORDER_OPEN_BIRD }));
		TASK_PIPELINE.addStage(new WoTransactionStage());

		// 配坏状态更新
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoPhStatusStage());

		// 猪工单完工
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoFinishStage());

		// 禽工单完工
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoFinishBirdStage());

		// 其他出入库
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoStockInOutStage());

		// 禽领料计划创建
		TASK_PIPELINE.addStage(new WoMaterialPlanStage());
		// 禽领料计划取消
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.MATERIAL_PLAN }));		
		TASK_PIPELINE.addStage(new WoCancelMaterialPlanStage());
		
		// 猪工单取消
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoCancelPigStage());
		
		// 禽工单取消
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_TRANSACTION }));
		TASK_PIPELINE.addStage(new WoCancelBirdStage());
		
		// 禽创建销售订单
		TASK_PIPELINE.addStage(new WoSaleOrderBirdStage());
		
		// 禽销售订单出库
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_CREATE_SALE }));
		TASK_PIPELINE.addStage(new WoSaleOrderOutBirdStage());
		
		// 禽销售订单入库
		TASK_PIPELINE.addStage(
				new WoCheckStage(new WorkOrderInterfaceType[] { WorkOrderInterfaceType.WORK_ORDER_CREATE_SALE }));
		TASK_PIPELINE.addStage(new WoSaleOrderInBirdStage());		
	}
}
