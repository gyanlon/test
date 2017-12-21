package com.newhope.nlbp.core.base;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BaseModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** 主键 */
    private Long id;

    /** 预留属性1 */
    private String attribute1;

    /** 预留属性2 */
    private String attribute2;

    /** 预留属性3 */
    private String attribute3;

    /** 预留属性4 */
    private String attribute4;

    /** 预留属性5 */
    private String attribute5;

    /** 创建者 */
    private Long createdBy;

    /** 最后更新者 */
    private Long lastUpdatedBy;

    /** 创建时间 */
    private Date creationDate;

    /** 最后更新时间 */
    private Date lastUpdateDate;

    /** 生效日期 */
    private Date startDate;

    /** 失效日期 */
    private Date endDate;
    
    private Long organizationId;		// 组织ID
    
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
    
    public BaseModel() {
    	
    }
    
    public BaseModel(Long id) {
    	this.id = id;
    }

    public void fillDate() {
    	this.creationDate=new Date();
    	this.lastUpdateDate=new Date();
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
