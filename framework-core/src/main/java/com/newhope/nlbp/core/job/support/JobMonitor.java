package com.newhope.nlbp.core.job.support;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDataMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.newhope.nlbp.core.job.dao.NlbpSysBusinessJobInfoDao;
import com.newhope.nlbp.core.job.dao.NlbpSysBusinessJobInfoModel;
import com.newhope.nlbp.core.job.util.IPUtils;


@Component
public class JobMonitor implements ApplicationContextAware {

	@Autowired
	private NlbpSysBusinessJobInfoDao nlbpSysBusinessJobInfoDao;

	private ApplicationContext applicationContext;

	@Autowired
	private JobManager jobManager;

	private boolean active = false;

	public void start() {
		List<NlbpSysBusinessJobInfoModel> businessJobInfos = nlbpSysBusinessJobInfoDao.findList(new NlbpSysBusinessJobInfoModel());
		if (!active) {
			// if no managed jobs, ignore
			if (CollectionUtils.isEmpty(businessJobInfos)) {
				return;
			}
			// startup all managed jobs.
			for (NlbpSysBusinessJobInfoModel businessJobInfo : businessJobInfos) {
				if (JobStatus.DISABLE.getValue().equals(businessJobInfo.getStatus())
						|| !IPUtils.isRunable(businessJobInfo.getIps())) {
					continue;
				}
				String jobName = BusinessJob.JOB_NAME_PREFIX + businessJobInfo.getServiceName() + "#"
						+ businessJobInfo.getMethodName();
				String jobGroupName = BusinessJob.BUSINESS_JOB_GROUP_NAME;
				if (jobManager.isJobExist(jobName, jobGroupName)) {
					continue;
				}
				String cronExpression = businessJobInfo.getCronExpression();
				BusinessJob mj = new BusinessJob(jobName, cronExpression);
				JobDataMap attributeMap = new JobDataMap();
				attributeMap.put("businessJobInfo", businessJobInfo);
				attributeMap.put("applicationContext", applicationContext);
				mj.setAttributeMap(attributeMap);
				jobManager.addJob(mj);
			}
			active = true;
		}
	}

	public void stop() {
		if (active) {
			List<NlbpSysBusinessJobInfoModel> bjiList = this.getBusinessJobInfoList();
			for (NlbpSysBusinessJobInfoModel bji : bjiList) {
				String jobName = AbstractJob.JOB_NAME_PREFIX + bji.getServiceName() + "#" + bji.getMethodName();
				String jobGroupName = BusinessJob.BUSINESS_JOB_GROUP_NAME;
				jobManager.removeJob(jobName, jobGroupName);
			}
			active = false;
		}
	}

	public void restart() {
		stop();
		start();
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * @return the businessJobInfoList
	 */
	public List<NlbpSysBusinessJobInfoModel> getBusinessJobInfoList() {
		return nlbpSysBusinessJobInfoDao.findList(new NlbpSysBusinessJobInfoModel());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}
