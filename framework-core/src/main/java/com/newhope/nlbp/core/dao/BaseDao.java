package com.newhope.nlbp.core.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * DAO支持类实现
 * @param <T>
 */
public interface BaseDao<T> {
	
	/**
	 * 根据主键删除
	 */
	public int deleteByPrimaryKey(Long id);

	/**
	 * 插入
	 */
    public int insert(T entity);

    /**
     * 选择性插入
     */
    public int insertSelective(T entity);

    /**
     * 批量插入
     */
    public int insertBatch(List<T> itemList);
    
    /**
     * 根据主键查询
     */
    public T selectByPrimaryKey(Long id);

    /**
     * 根据主键选择性更新
     */
    public int updateByPrimaryKeySelective(T entity);

    
    /**
     * 根据主键更新
     */
    public int updateByPrimaryKey(T entity);
    
    /**
     * 查询
     */
    public List<T> findList(T entity);
    
    /**
     * 分页查询
     */
    public PageList<T> findList(T entity,PageBounds pageBounds);
    
}