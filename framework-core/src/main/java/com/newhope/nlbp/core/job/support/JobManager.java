package com.newhope.nlbp.core.job.support;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Queue job manager class.
 * 
 * @author JackDou
 * @version 2017-4-17
 */
@Component
public class JobManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

	private static Scheduler scheduler = SchedulerManager.getInstanse();

	public static final String TRIGGER_GROUP_NAME = "oppein_trigger";

	public static final String TRIGGER_NAME_PERFIX = "trigger_name";

	public static final String QUEUE_OBJECT_KEY = "queue_object_key";

	/**
	 * Private constructor
	 */
	private JobManager() {
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param job
	 *            任务对象
	 */
	public void addJob(AbstractJob job) {
		String jobName = job.getName();
		String triggerName = TRIGGER_NAME_PERFIX + " at " + job.getName();
		LOGGER.info("Add a job[" + jobName + "] start...");
		try {
			JobDetail jobDetail = newJob(job.getClass()).withIdentity(jobName, job.getGroupName())
					.usingJobData(job.getAttributeMap()).build();
			Trigger trigger = newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
					.withSchedule(cronSchedule(job.getCronExpression())).build();
			scheduler.scheduleJob(jobDetail, trigger);
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

		} catch (Exception e) {
			LOGGER.error("Add a time in [" + job.getCronExpression() + "] triggered of the job [" + job.getName()
					+ "] is error!", e);
		}
		LOGGER.info("Add a job[" + jobName + "] end.");
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param jobName
	 *            任务名称
	 * @param cronExpress
	 *            定时器表达式
	 */
	public void modifyJobTime(String jobName, String cronExpression) {
		LOGGER.info("Modify a job[" + jobName + "] start...");
		try {
			String triggerName = TRIGGER_NAME_PERFIX + " at " + jobName;
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (trigger != null) {
				CronTrigger ct = (CronTrigger) trigger;
				// 修改时间
				ct.getTriggerBuilder().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).startNow()
						.build();
				// 重启触发器
				scheduler.resumeTrigger(triggerKey);
			}
		} catch (Exception e) {
			LOGGER.error("Add a time in [" + cronExpression + "] triggered of the job [" + jobName + "] is error!", e);
		}
		LOGGER.info("Modify a job[" + jobName + "] end.");
	}

	/**
	 * 移除一个任务
	 * 
	 * @param queueId
	 *            队列ID
	 */
	public boolean removeJob(String jobName, String jobGroupName) {
		LOGGER.info("Remove a job[" + jobName + "] start...");
		boolean b = false;
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			b = scheduler.deleteJob(jobKey); // 删除任务
		} catch (Exception e) {
			LOGGER.error("Delete a job [" + jobName + "] is error!", e);
		}
		LOGGER.info("Remove a job[" + jobName + "] end.");
		return b;
	}

	/**
	 * 查询是否某任务是否已经存在
	 * 
	 * @param queueId
	 *            队列ID
	 * @return 是否存在这个任务
	 */
	public boolean isJobExist(String jobName, String jobGroupName) {
		LOGGER.info("Find a job[" + jobName + "] start...");
		try {
			GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupContains(jobGroupName);
			Set<JobKey> jobKeySet = scheduler.getJobKeys(groupMatcher);
			for (JobKey jobKey : jobKeySet) {
				if (jobKey.getName().equals(jobName)) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Find a job[" + jobName + "] error.", e);
		}
		LOGGER.info("Find a job[" + jobName + "] end.");
		return false;
	}

	/**
	 * 查询所有执行中的任务
	 * 
	 * @return 所有执行中的任务
	 */
	public List<Object[]> getAllRunningJob() {
		List<Object[]> list = null;
		try {
			List<JobExecutionContext> runningJobs = scheduler.getCurrentlyExecutingJobs();
			if (CollectionUtils.isEmpty(runningJobs)) {
				return null;
			}
			list = new ArrayList<Object[]>(runningJobs.size());
			for (JobExecutionContext jeContext : runningJobs) {
				JobDetail jobDetail = jeContext.getJobDetail();
				Trigger jobTrigger = jeContext.getTrigger();
				JobDataMap jobDataMap = jobDetail.getJobDataMap();
				list.add(new Object[] { jobDetail, jobTrigger, jobDataMap });
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 当前任务管理器依赖的调度器
	 * 
	 * @return 当前任务管理器依赖的调度器
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}
}
