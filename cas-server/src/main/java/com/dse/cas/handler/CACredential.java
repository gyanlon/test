/**
 * 
 */
package com.dse.cas.handler;

import java.io.Serializable;

import org.jasig.cas.authentication.Credential;

/**
 * 
 * @ClassName: CACredential
 * @Description: TODO
 * @author xutt
 * @date 2016年3月8日 下午3:11:26
 * 
 */
public class CACredential implements Credential, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2853124699507943136L;
	private String userName;

	public CACredential(String userName) {
		this.userName = userName;
	}

	@Override
	public String getId() {
		return this.userName;
	}

}
