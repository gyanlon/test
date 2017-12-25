/**
 * 
 */
package com.dse.cas.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.flow.InitialFlowSetupAction;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * 
 * @ClassName: NoFlowLoginByUserNameAction
 * @Description: 通过用户名和密码的方式进行认证
 * @author xutt
 * @date 2016年3月8日 下午2:51:45
 * 
 */
public class NoFlowLoginByUserNameAction extends AbstractController {
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	@NotNull
	private CookieRetrievingCookieGenerator warnCookieGenerator;
	@NotNull
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	private InitialFlowSetupAction initialFlowSetupAction;

	/** Extractors for finding the service. */
	@NotEmpty
	private List<ArgumentExtractor> argumentExtractors;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Result result = new Result();
		String uName = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordCredential credential = new UsernamePasswordCredential();
		credential.setUsername(uName);
		credential.setPassword(password);
		if (!this.initialFlowSetupAction.pathPopulated) {
			final String contextPath = request.getContextPath();
			final String cookiePath = StringUtils.hasText(contextPath) ? contextPath
					+ "/"
					: "/";
			this.warnCookieGenerator.setCookiePath(cookiePath);
			this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
			this.initialFlowSetupAction.pathPopulated = true;
		}
		final Service service = WebUtils.getService(this.argumentExtractors,
				request);
		String ticketGrantingTicketId = "";
		String serviceTicket = "";
		try {
			ticketGrantingTicketId = this.centralAuthenticationService
					.createTicketGrantingTicket(credential);

			/***
			 * 产生新的票据，并将票据及服务记录在缓存中
			 */
			serviceTicket = this.centralAuthenticationService
					.grantServiceTicket(ticketGrantingTicketId, service);

			this.ticketGrantingTicketCookieGenerator.removeCookie(response);

			this.ticketGrantingTicketCookieGenerator.addCookie(request,
					response, ticketGrantingTicketId);

			this.warnCookieGenerator.addCookie(request, response, "true");
			result.setCode("200");
			String redirectUrl = null;
			String serviceURL = request.getParameter("service");
			if (serviceURL.indexOf("?") != -1) {
				redirectUrl = serviceURL + "&ticket=" + serviceTicket;
			} else {
				redirectUrl = serviceURL + "?ticket=" + serviceTicket;
			}
			// redirectUrl = request.getParameter("service")
			// + "?ticket=" + serviceTicket;
			result.setMsg(redirectUrl);
		} catch (TicketException e) {
			e.printStackTrace();
			result.setMsg("创建Ticket失败");
		} catch (AuthenticationException ae) {
			result.setMsg("登录失败,请检查输入的用户信息");
		} catch (Exception e) {
			result.setMsg("登录失败,请检查输入的用户信息");
		}
		map.put("result", result);
		return new ModelAndView(new MappingJacksonJsonView(), map);
	}

	public void setWarnCookieGenerator(
			final CookieRetrievingCookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}

	public void setArgumentExtractors(
			final List<ArgumentExtractor> argumentExtractors) {
		this.argumentExtractors = argumentExtractors;
	}

	public final void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public void setTicketGrantingTicketCookieGenerator(
			final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setInitialFlowSetupAction(
			InitialFlowSetupAction initialFlowSetupAction) {
		this.initialFlowSetupAction = initialFlowSetupAction;
	}
}
