package com.dse.cas.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class RandomAction extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Result result = new Result();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String randNum = generateRandomNum();
		if (randNum == null || randNum.trim().equals("")) {
			result.setCode("500");
			result.setMsg("证书认证数据不完整！");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} else {
			result.setCode("200");
			// 设置认证原文到页面，给页面程序提供参数，用于产生认证请求数据包
			result.setMsg(randNum);
			// 设置认证原文到session，用于程序向后传递，通讯报文中使用
			request.getSession().setAttribute("original_data", randNum);

		}
		map.put("result", result);
		return new ModelAndView(new MappingJacksonJsonView(), map);
	}

	/**
	 * 产生认证原文
	 */
	private String generateRandomNum() {
		/**************************
		 * 第二步 服务端产生认证原文 *
		 **************************/
		String num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
		int size = 6;
		char[] charArray = num.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
		}
		return sb.toString();
	}
}
