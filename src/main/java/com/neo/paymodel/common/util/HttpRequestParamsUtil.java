package com.neo.paymodel.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class HttpRequestParamsUtil {
	/**
	 * request 参数封装 obj
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getRequestParamMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}
}