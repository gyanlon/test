/**
 * 
 */
package com.dse.cas.web.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @ClassName: LoginFilter
 * @Description: TODO
 * @author xutt
 * @date 2015年12月11日 下午1:28:12
 * 
 */
public class LoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		System.out.println(">>>>>>>>>url:" + hRequest.getRequestURL() + "</br>>>>>>>>>qs:"
				+ hRequest.getQueryString());
		Enumeration<String> params = hRequest.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			System.out.println(key + ">>>>>>>>>>>>>>>>>>>" + hRequest.getParameter(key));
		}
		filterChain.doFilter(hRequest, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
