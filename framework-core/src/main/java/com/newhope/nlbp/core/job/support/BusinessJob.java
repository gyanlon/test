package com.newhope.nlbp.core.job.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.newhope.nlbp.core.job.dao.NlbpSysBusinessJobInfoModel;

public class BusinessJob extends AbstractJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessJob.class);

	public static final String BUSINESS_JOB_GROUP_NAME = JOB_GROUP_NAME + "business_jobs";

	private String jobName;

	private String cronExpression;

	public BusinessJob() {
	}

	public BusinessJob(String jobName, String cronExpression) {
		this.jobName = jobName;
		this.cronExpression = cronExpression;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		NlbpSysBusinessJobInfoModel businessJobInfo = (NlbpSysBusinessJobInfoModel) jobDataMap.get("businessJobInfo");
		ApplicationContext appContext = (ApplicationContext) jobDataMap.get("applicationContext");
		String serviceName = businessJobInfo.getServiceName();
		String methodName = businessJobInfo.getMethodName();
		Object bjs = appContext.getBean(serviceName);
		Class<?> clazz = bjs.getClass();
		Method m1 = null;
		try {
			m1 = clazz.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		//TODO 添加调度日志
		try {
			LOGGER.info("execute start:" + businessJobInfo);
			m1.invoke(bjs);
			LOGGER.info("execute end:" + businessJobInfo);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCronExpression() {
		return this.cronExpression;
	}

	@Override
	public String getName() {
		return this.jobName;
	}

	@Override
	public String getGroupName() {
		return BUSINESS_JOB_GROUP_NAME;
	}
}
