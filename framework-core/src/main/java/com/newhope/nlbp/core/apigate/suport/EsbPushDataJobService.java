package com.newhope.nlbp.core.apigate.suport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.newhope.nlbp.core.apigate.dao.NlbpSysOrderProcessDao;
import com.newhope.nlbp.core.apigate.suport.task.EsbPushDataTask;
import com.newhope.nlbp.core.apigate.suport.task.TaskConstant;
import com.newhope.nlbp.core.apigate.suport.task.TaskContext;

/**
 * EBS数据推送定时服务类
 * 
 * @author JackDou
 * @since 2017-07-10
 */
@Service
public class EsbPushDataJobService implements ApplicationContextAware, EnvironmentAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsbPushDataJobService.class);
	
	public static final String PREFIX = "nlbp.asyncInvokeEBS.";

	private ExecutorService pool;

	private int threadPoolSize;
	
	private CopyOnWriteArrayList<Future<Boolean>> futureList = new CopyOnWriteArrayList<Future<Boolean>>();

	@Autowired
	private NlbpSysOrderProcessDao processDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("rawtypes")
	private List<Map> getForemostWorkOrderNos(String tryFlag) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put(tryFlag, Boolean.TRUE);
		return processDao.getForemostWorkOrderNos(parametersMap);
	}

	/**
	 * 5秒钟定时检查线程池队列中是否为空，如果为空，则加入新100个的任务到队里
	 */
	@SuppressWarnings("rawtypes")
	public void executeAsyncInvokeEBS() {
		// 延迟创建连接池
		if (pool == null) {
			pool = Executors.newFixedThreadPool(threadPoolSize);
		}
		// 打印pool中的：活跃线程数/线程池总数
		this.printActiveThreadsCount();
		// 判断连接池队列中是否为空,线程池是否有活跃线程是否为空，以及所有任务是否已经处理完毕，否则不向队列中放入待处理的任务
		if (!this.isEmptyInQueue() && !this.isNoActiveThreadInPool() && this.isAllTaskDealCompleted()) {
			return;
		}
		// 以工单维度获取最先的process order需要处理的100条W和E状态的记录，
		WorkOrderProcessType op = WorkOrderProcessType.WORK_ORDER_TRY;
		List<Map> resultList = getForemostWorkOrderNos(op.getCode());
		if (CollectionUtils.isEmpty(resultList)) {
			// 在没有需要处理的W状态的情况下，工单维度获取最先的process
			// 且重试次数超过3次的E状态需要处理的100条记录，重试次数超过10次，不再处理
			op = WorkOrderProcessType.WORK_ORDER_RETRY;
			resultList = getForemostWorkOrderNos(op.getCode());
		}
		if (CollectionUtils.isEmpty(resultList)) {
			LOGGER.info("============ task process[no record need deal!!!] ============");
			return;
		}
		for (Iterator iter = resultList.iterator(); iter.hasNext();) {
			Map recordMap = (Map) iter.next();
			Object nlbpOrderNo = recordMap.get(WorkOrderProcessType.NLBP_ORDER_NO.getCode());
			Object orgCode = recordMap.get(WorkOrderProcessType.ORG_CODE.getCode());
			if (null == nlbpOrderNo || null == orgCode) {
				continue;
			}
			String orderNo = String.valueOf(nlbpOrderNo);
			String expand1 = String.valueOf(orgCode);
			TaskContext taskContext = new TaskContext(orderNo, expand1, op.getCode());
			// push applicationContext link into task context
			taskContext.getAttributes().put(TaskConstant.APP_CXT_KEY, applicationContext);
			EsbPushDataTask task = new EsbPushDataTask(taskContext);
			Future<Boolean> future = this.getPool().submit(task);
			futureList.add(future);
		}
	}

	public ExecutorService getPool() {
		return pool;
	}

	private boolean isEmptyInQueue() {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor) pool;
		BlockingQueue<Runnable> queue = tpe.getQueue();
		return queue.isEmpty();
	}

	private boolean isNoActiveThreadInPool() {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor) pool;
		int activeCount = tpe.getActiveCount();
		return 0 == activeCount;
	}
	
	private boolean isAllTaskDealCompleted() {
		if (CollectionUtils.isEmpty(futureList)) {
			return true;
		}
		for (Future<Boolean> future : futureList) {
			if (!future.isDone()) {
				return false;
			}
		}
		futureList.clear();
		return true;
	}

	private void printActiveThreadsCount() {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor) pool;
		int activeCount = tpe.getActiveCount();
		int poolSize = tpe.getMaximumPoolSize();
		LOGGER.info("============ activeCount/poolSize: " + activeCount + "/" + poolSize + "============");
	}

	@Override
	public void setEnvironment(Environment environment) {
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver( environment, PREFIX );
		Integer threadPoolSize = propertyResolver.getProperty("thread.pool.size", Integer.class);
		this.threadPoolSize = threadPoolSize;
	}
}
