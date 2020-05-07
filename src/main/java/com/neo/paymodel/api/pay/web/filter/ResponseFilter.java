package com.neo.paymodel.api.pay.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseFilter implements Filter {

	static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resq = (HttpServletResponse) response;
		resq.setHeader("Access-Control-Allow-Origin", "*");
		resq.setHeader("Cache-Control","no-cache");
		logger.info("设置跨域");
		chain.doFilter(request, resq);
	}

	@Override
	public void destroy() {
		
		
	}




	

}