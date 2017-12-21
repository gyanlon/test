package com.newhope.nlbp.core.job.dao;

import java.util.Map;
import com.newhope.nlbp.core.dao.BaseDao;

/**
 * sys_business_job_infoDAO接口
 * @author sulei
 * @version 2017-05-26
 */
public interface NlbpSysBusinessJobInfoDao extends BaseDao<NlbpSysBusinessJobInfoModel> {
	
/**
 * 根据id（批量）修改定时任务状态
 * @param parameterMap
 * @return
 */
	
	 public int updateByIds(Map parameterMap);
	
}