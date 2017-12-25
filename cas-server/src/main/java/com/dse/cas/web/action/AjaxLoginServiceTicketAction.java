/**
 * 
 */
package com.dse.cas.web.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 
 * @ClassName: AjaxLoginServiceTicketAction
 * @Description: TODO
 * @author xutt
 * @date 2015年12月1日 下午3:59:39
 * 
 */
public class AjaxLoginServiceTicketAction extends AbstractAction {
	protected static final String J_CALLBACK = "feedBackUrlCallBack";

	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		Event event = context.getCurrentEvent();
		boolean isAjax = BooleanUtils.toBoolean(request.getParameter("isAjax"));
		if (!isAjax) {// 非 ajax/iframe 方式登录，返回当前 event
			return event;
		}
		boolean isLoginSuccess;
		if ("success".equals(event.getId())) {// 是否登录成功
			final Service service = WebUtils.getService(context);
			final String serviceTicket = WebUtils
					.getServiceTicketFromRequestScope(context);
			if (service != null) {
				request.setAttribute("service", service);
			}
			request.setAttribute("ticket", serviceTicket);
			isLoginSuccess = true;
		} else {
			isLoginSuccess = false;
		}
		boolean isFrame = BooleanUtils.toBoolean(request
				.getParameter("isFrame"));
		String callback = request.getParameter("callback");
		if (StringUtils.isEmpty(callback)) { // 如果未转入 callback 参数，则采用默认 callback
												// 函数名
			callback = J_CALLBACK;
		}
		if (isFrame) { // 如果采用了 iframe ，则 concat 其 parent 。
			callback = "parent.".concat(callback);
		}
		request.setAttribute("isFrame", isFrame);
		request.setAttribute("callback", callback);
		request.setAttribute("isLogin", isLoginSuccess);
		return new Event(this, "local"); // 转入 ajaxLogin.jsp 页面
	}
}
