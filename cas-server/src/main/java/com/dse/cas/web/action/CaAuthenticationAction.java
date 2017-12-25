/**
 * 
 */
package com.dse.cas.web.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dse.ca.filter.CaResult;
import com.dse.ca.service.CaAuthenticationService;
import com.dse.cas.exception.CaException;

/**
 * 
 * @ClassName: CaAuthenticationFilter
 * @Description: CA认证过滤器
 * @author xutt
 * @date 2015年12月14日 下午2:16:57
 * 
 */
public class CaAuthenticationAction extends AbstractController {

	@NotNull
	private CaAuthenticationService caAuthenticationService;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CaResult result = authenticationByCa(request, response);
		String message = "";
		if (result.isSuccess()) {// CA认证成功
			message = "{'isSuccess':true,'code':'" + result.getErrCode()
					+ "','msg':'" + result.getErrDesc() + "'}";
		} else {// CA认证失败
			message = "{'isSuccess':false,'code':'" + result.getErrCode()
					+ "','msg':'" + result.getErrDesc() + "'}";
		}
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(message);
		pw.close();
		return null;
	}

	public CaAuthenticationService getCaAuthenticationService() {
		return caAuthenticationService;
	}

	public void setCaAuthenticationService(
			CaAuthenticationService caAuthenticationService) {
		this.caAuthenticationService = caAuthenticationService;
	}

	public CaResult authenticationByCa(HttpServletRequest request,
			HttpServletResponse response) throws CaException {
		CaResult result = null;
		String auth_data = (String) request.getParameter("auth_data");
		// 获取证书认证请求包
		String signed_data = (String) request.getParameter("signed_data");
		try {
			result = caAuthenticationService.authentication(auth_data,
					signed_data);
		} catch (Exception e) {

		}
		return result;
	}
}
