package com.newhope.nlbp.core.apigate.dao;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * 管理系统虚拟工单号DAO接口
 * @author sulei
 * @version 2017-06-22
 */
public interface NlbpSysOrderProcessInterfaceDao{

	public PageList<Map<String, String>> searchListPigByPage(Map<String, String> param, PageBounds pageBounds);
	
	public PageList<Map<String, String>> searchListBirdByPage(Map<String, String> param, PageBounds pageBounds);

	@SuppressWarnings("rawtypes")
	public int updateByProcessIdSelective(Map parameterMap);
	
	public int alterStatusToSuccess(Map parameterMap);//手动将报错记录状态修改为S，8-29日新增
	
	//public long getFirstPageMaxId();   //获取首页最大id
	
	//public long getOtherPageMaxId(long previousMaxId);   //获取其他页最大id
	
	//返回第一页50条记录
	public PageList<Map<String, String>> searchListPigFirstPage(Map<String, String> param, PageBounds pageBounds);
	
	
}