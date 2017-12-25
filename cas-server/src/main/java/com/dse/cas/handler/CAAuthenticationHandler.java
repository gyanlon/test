/**
 * 
 */
package com.dse.cas.handler;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.Message;
import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;

import com.dse.cas.CONSTANT;
import com.dse.cas.service.LoginService;



/**
 * 
 * @ClassName: CAAuthenticationHandler
 * @Description: TODO
 * @author xutt
 * @date 2016年3月8日 下午3:08:42
 * 
 */
public class CAAuthenticationHandler extends AbstractAuthenticationHandler {
	private LoginService loginService;

	@Override
	public HandlerResult authenticate(Credential credential)
			throws GeneralSecurityException, PreventedException {
		CACredential temp = (CACredential) credential;
		boolean isValidate = validate(temp);
		if (!isValidate) {
			throw new FailedLoginException();
		}
		return createHandlerResult(credential,
				new SimplePrincipal(temp.getId()), null);
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof CACredential;
	}

	public boolean validate(CACredential credential) {
		/*boolean isOk = false;
		try {
			if (null != supportService.getUserByUserCode(credential.getId())) {
				isOk = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;*/
		boolean isOk = false;
		try {
			Integer flag = loginService.checkUsername(credential);
			if (null != flag && flag.equals(CONSTANT.LOGIN_SUCCESS)) {
				isOk = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
		
		
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	protected final HandlerResult createHandlerResult(
			final Credential credential, final Principal principal,
			final List<Message> warnings) {
		return new HandlerResult(this, new BasicCredentialMetaData(credential),
				principal, warnings);
	}
}
