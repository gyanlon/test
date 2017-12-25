/**
 * 
 */
package com.dse.cas.web.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
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

import com.dse.ca.filter.CaResult;
import com.dse.ca.service.CaAuthenticationService;
import com.dse.cas.exception.CaException;
import com.dse.cas.handler.CACredential;

/**
 * 
 * @ClassName: NoFlowLoginByOnlyCaAction
 * @Description: 只带有CA认证,且只有一套单点登录
 * @author xutt
 * @date 2015年12月11日 下午4:19:58
 * 
 */
public class NoFlowLoginByOnlyCaAction extends AbstractController {
	@NotNull
	private CaAuthenticationService caAuthenticationService;

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

		CaResult caResult = authenticationByCa(request, response);
		if (caResult.isSuccess()) {// CA认证成功
			String uName = getUserByCa(caResult);
			CACredential credential = new CACredential(uName);
			if (!this.initialFlowSetupAction.pathPopulated) {
				final String contextPath = request.getContextPath();
				final String cookiePath = StringUtils.hasText(contextPath) ? contextPath
						+ "/"
						: "/";
				this.warnCookieGenerator.setCookiePath(cookiePath);
				this.ticketGrantingTicketCookieGenerator
						.setCookiePath(cookiePath);
				this.initialFlowSetupAction.pathPopulated = true;
			}
			final Service service = WebUtils.getService(
					this.argumentExtractors, request);
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
				System.out.println(ae+"<<<<<"+ae.getMessage());
				ae.printStackTrace();
				result.setMsg("CA认证失败,请检查证书信息");
			} catch (Exception e) {
				e.printStackTrace();
				result.setMsg("CA登录失败,请检查证书信息");
			}
		} else {// CA认证失败
			result.setMsg("CA认证失败,请检查证书信息");
		}
		map.put("result", result);
		return new ModelAndView(new MappingJacksonJsonView(), map);
	}

	public CaResult authenticationByCa(HttpServletRequest request,
			HttpServletResponse response) throws CaException {
		CaResult result = null;
		String auth_data = (String) request.getParameter("auth_data");
		String original_data = (String) request.getSession().getAttribute(
				"original_data");
		if (null != original_data && !original_data.equals(auth_data)) {
			result = new CaResult();
			result.setErrCode("500");
			result.setErrDesc("客户端提供的认证原文与服务端的不一致");
			return result;
		}
		// 获取证书认证请求包
		String signed_data = (String) request.getParameter("signed_data");
		try {
			result = caAuthenticationService.authentication(auth_data,
					signed_data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getUserByCa(CaResult result) {
		String userName = null;
		String key = "X509Certificate.SubjectDN";
		Map params = result.getAttrs();
		Iterator<Map.Entry> items = params.entrySet().iterator();
		while (items.hasNext()) {
			Map.Entry item = items.next();
			String[] keys = (String[]) item.getKey();
			if (null != keys || keys.length >= 1) {
				if (key.equals(keys[0])) {
					userName = (String) item.getValue();
					break;
				}
			}
			System.out.println("CA>>>>>>" + Arrays.asList(keys) + ">>>>>>>"
					+ item.getValue());
		}
		if (null != userName) {
			String attrs[] = userName.split(",");
			for (String attr : attrs) {
				if (attr.indexOf("CN") != -1) {
					String[] temps = attr.split("=");
					if (null != temps && temps.length == 2) {
						userName = temps[1];
						break;
					}
				}
			}
		}
		userName = convert(userName);
		return userName;
	}

	public String convert(String userName) {
		String loginName = "system";
		if (null != userName && !"".equals(userName.trim())) {
			if (userName.equals("省水利厅")) {
				loginName = "sslt";
			} else if (userName.equals("宋立荣")) {
				loginName = "slr";
			} else if (userName.equals("雷洪成")) {
				loginName = "leihongcheng";
			} else if (userName.equals("东江流域管理局")) {
				loginName = "dj";
			} else if (userName.equals("北江流域管理局")) {
				loginName = "bj";
			} else if (userName.equals("西江流域管理局")) {
				loginName = "xj";
			} else if (userName.equals("韩江流域管理局")) {
				loginName = "hj";
			} else if (userName.equals("广州市水务局")) {
				loginName = "gzswj";
			} else if (userName.equals("广州市旺隆热电有限公司")) {
				loginName = "gzwlrd";
			} else if (userName.equals("system")) {
				loginName = "system";
			} else {// 其他全部是system
				loginName = "system";
			}
		}
		return loginName;
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

	public CaAuthenticationService getCaAuthenticationService() {
		return caAuthenticationService;
	}

	public void setCaAuthenticationService(
			CaAuthenticationService caAuthenticationService) {
		this.caAuthenticationService = caAuthenticationService;
	}
}
