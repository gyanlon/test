/**
 * 
 */
package com.dse.cas.resolver;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.SimplePrincipal;

/**
 * 
 * @ClassName: DseUsernameResolver
 * @Description: TODO
 * @author xutt
 * @date 2015年12月7日 下午4:55:26
 * 
 */
public class DseUsernameResolver implements PrincipalResolver {

	@Override
	public Principal resolve(Credential credential) {
		String principalId = credential.getId();
		Map<String, Object> attrs = new HashMap<String, Object>();
		return new SimplePrincipal(principalId, attrs);
	}

	@Override
	public boolean supports(Credential credential) {
		return true;
	}
}
