package com.newhope.nlbp.core.job.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.JobDataMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.newhope.nlbp.core.job.dao.NlbpSysBusinessJobInfoModel;
import com.newhope.nlbp.core.job.support.BusinessJob;
import com.newhope.nlbp.core.job.support.JobManager;
import com.newhope.nlbp.core.job.support.JobStatus;
import com.newhope.nlbp.core.job.util.IPUtils;

@Controller
@RequestMapping(value = "/job/manager")
public class JobManagerController implements ApplicationContextAware {

	@Autowired
	private JobManager jobManager;

	private static ApplicationContext applicationContext;

	@RequestMapping(value = "modifyJobExpression", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> modifyJobExpression(HttpServletRequest request, String jsonParam) {
		NlbpSysBusinessJobInfoModel businessJobInfo = JSONObject.parseObject(jsonParam,
				NlbpSysBusinessJobInfoModel.class);

		String jobName = BusinessJob.JOB_NAME_PREFIX + businessJobInfo.getServiceName() + "#"
				+ businessJobInfo.getMethodName();
		String jobGroupName = BusinessJob.BUSINESS_JOB_GROUP_NAME;
		if (jobManager.isJobExist(jobName, jobGroupName)) {
			System.out.println("job 存在： " + jobName + "  ||" + jobGroupName);
		}
		String cronExpression = businessJobInfo.getCronExpression();

		jobManager.modifyJobTime(jobName, cronExpression);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		return result;
	}

	@RequestMapping(value = "addJob", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addJob(HttpServletRequest request, String jsonParam) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", "success");
		result.put("msg", "启动成功");

		NlbpSysBusinessJobInfoModel businessJobInfo = JSONObject.parseObject(jsonParam,
				NlbpSysBusinessJobInfoModel.class);

		String jobName = BusinessJob.JOB_NAME_PREFIX + businessJobInfo.getServiceName() + "#"
				+ businessJobInfo.getMethodName();
		String jobGroupName = BusinessJob.BUSINESS_JOB_GROUP_NAME;

		if (JobStatus.DISABLE.getValue().equals(businessJobInfo.getStatus())
				|| !IPUtils.isRunable(businessJobInfo.getIps())) {
			result.put("status", "failed");
			result.put("msg", "job 状态不可运行： " + jobName + "  ||" + jobGroupName);
			return result;
		}

		if (jobManager.isJobExist(jobName, jobGroupName)) {
			result.put("status", "failed");
			result.put("msg", "job 已在运行： " + jobName + "  ||" + jobGroupName);
			return result;
		}

		String cronExpression = businessJobInfo.getCronExpression();
		BusinessJob mj = new BusinessJob(jobName, cronExpression);
		JobDataMap attributeMap = new JobDataMap();
		attributeMap.put("businessJobInfo", businessJobInfo);
		attributeMap.put("applicationContext", applicationContext);
		mj.setAttributeMap(attributeMap);
		jobManager.addJob(mj);

		return result;
	}

	@RequestMapping(value = "stopJob", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> stopJob(HttpServletRequest request, String jsonParam) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", true);
		result.put("msg", "success");

		NlbpSysBusinessJobInfoModel businessJobInfo = JSONObject.parseObject(jsonParam,
				NlbpSysBusinessJobInfoModel.class);

		String jobName = BusinessJob.JOB_NAME_PREFIX + businessJobInfo.getServiceName() + "#"
				+ businessJobInfo.getMethodName();
		String jobGroupName = BusinessJob.BUSINESS_JOB_GROUP_NAME;
		if (jobManager.isJobExist(jobName, jobGroupName)) {
			jobManager.removeJob(jobName, jobGroupName);
		}

		return result;
	}

	@RequestMapping(value = "listRunningJob", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> listRunningJob(HttpServletRequest request) {

		List<Object[]> result = jobManager.getAllRunningJob();

		return result;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (JobManagerController.applicationContext == null) {
			JobManagerController.applicationContext = applicationContext;
		}
	}
}
