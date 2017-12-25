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
 * @ClassName: CAResolver
 * @Description: TODO
 * @author xutt
 * @date 2016年3月8日 下午3:26:35
 * 
 */
public class CAResolver implements PrincipalResolver {

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
