package com.neo.paymodel.api.pay.web.filter;

import com.neo.paymodel.common.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PayApiRequestValidationFilter extends OncePerRequestFilter {

	static final Logger logger = LoggerFactory.getLogger(PayApiRequestValidationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {
		
		logger.debug("{}", HttpUtil.buildOriginalURL(req));
		
		chain.doFilter(req, resp);
	}
}