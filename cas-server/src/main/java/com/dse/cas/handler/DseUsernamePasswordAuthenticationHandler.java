/**
 * 
 */
package com.dse.cas.handler;

import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;

import com.dse.cas.CONSTANT;
import com.dse.cas.service.LoginService;
import com.dse.cas.util.UserUtil;



/**
 * 
 * @ClassName: DseUsernamePasswordAuthenticationHandler
 * @Description: TODO
 * @author xutt
 * @date 2015年12月7日 下午4:37:44
 * 
 */
public class DseUsernamePasswordAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler {

	private LoginService loginService;

	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(
			UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		String userName = credential.getUsername();
		// String password = credential.getPassword();
		// password = getPasswordEncoder().encode(password);// 对密码进行加密
		// credential.setPassword(password);
		// boolean isValidate = UserUtil.validate(credential);
		boolean isValidate = validate(credential);
		if (!isValidate) {
			throw new FailedLoginException();
		}
		return createHandlerResult(credential, new SimplePrincipal(userName),
				null);
	}

	public boolean validate(UsernamePasswordCredential credential) {
		/*boolean isOk = false;
		UserDto user = new UserDto();
		user.setUserCode(credential.getUsername());
		user.setPasswd(credential.getPassword());
		try {
			isOk = supportService.login(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;*/
		boolean isOk = false;
		try {
			Integer flag = loginService.checkLogin(credential);
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

	
}
