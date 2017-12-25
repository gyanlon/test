/**
 * 
 */
package com.dse.cas.web.action;

import java.util.List;

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
import org.springframework.web.servlet.view.RedirectView;

import com.dse.ca.service.CaAuthenticationService;

/**
 * 
 * @ClassName: noFlowLoginAction
 * @Description: TODO
 * @author xutt
 * @date 2015年12月11日 下午4:19:58
 * 
 */
public class NoFlowLoginAction extends AbstractController {
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
		String uName = request.getParameter("username");
		String password = request.getParameter("password");
		if (uName == null && password == null) {

		}
		UsernamePasswordCredential credential = new UsernamePasswordCredential();
		credential.setUsername(uName);
		credential.setPassword(password);
		if (!this.initialFlowSetupAction.pathPopulated) {
			final String contextPath = request.getContextPath();
			final String cookiePath = StringUtils.hasText(contextPath) ? contextPath
					+ "/"
					: "/";
			logger.info("Setting path for cookies to: " + cookiePath);
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

		} catch (TicketException e) {
			e.printStackTrace();
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String redirectUrl = null;
		String serviceURL = request.getParameter("service");
		if (serviceURL.indexOf("?") != -1) {
			redirectUrl = serviceURL + "&ticket=" + serviceTicket;
		} else {
			redirectUrl = serviceURL + "?ticket=" + serviceTicket;
		}
		return new ModelAndView(new RedirectView(redirectUrl));
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
