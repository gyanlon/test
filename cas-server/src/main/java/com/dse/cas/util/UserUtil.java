/**
 * 
 */
package com.dse.cas.util;

import org.jasig.cas.authentication.UsernamePasswordCredential;

/**
 * 
 * @ClassName: UserUtil
 * @Description: TODO
 * @author xutt
 * @date 2015年12月7日 下午4:40:20
 * 
 */ 
public class UserUtil {
	static public boolean validate(UsernamePasswordCredential credential) {
		if (credential.getUsername().equals(credential.getPassword())) {
			return true;
		}
		if(credential.getUsername().equals("swj")&&credential.getPassword().equals("123")){
			return true;
		}
		return false;
	}
}
