package com.neo.paymodel.api.pay.web.filter;

import com.neo.paymodel.api.pay.util.HttpUtil;
import com.neo.paymodel.common.util.AESUtils;
import com.neo.paymodel.common.util.Base64;
import com.neo.paymodel.common.util.MD5Util;
import com.neo.paymodel.common.util.SpringUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;



public class DecodeFilter implements Filter {
	static final Logger logger = LoggerFactory.getLogger(DecodeFilter.class);

	private Map<String, Object> requestRedisTask = null;
	public static boolean isOpenDecode = true;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		refresh();
		
	}
	
	
	public void refresh(){
	}

	@Override
	public void doFilter(ServletRequest request0, ServletResponse response0, FilterChain chain)
			throws IOException, ServletException {
		logger.info("DecodeFilter......doFilter");
		HttpServletRequest requestHttp = (HttpServletRequest) request0;
		HttpServletResponse response = (HttpServletResponse) response0;
		if (requestHttp.getRequestURI().contains("/pay/success")
		   || requestHttp.getRequestURI().contains("/pay/fail")
		   || requestHttp.getRequestURI().contains("/pay/thirdPayNotify/111111111")
		   || requestHttp.getRequestURI().contains("/pay/flashPayNotify")
		   ) {// 不需要加密接口
			chain.doFilter(requestHttp, response);
			return;
		}

		
		// 参数值url编码 3des编码 base64之后在url编码
		String data = requestHttp.getParameter("data"); 
		String sign = requestHttp.getParameter("sign"); 
		String timestamp = requestHttp.getParameter("timestamp"); // 时间戳

		
		// 是否频繁请求
		if (repeatRequest(requestHttp)) {
			logger.error("request is Requests are too frequent,{}", requestHttp.getRequestURL().toString());
			response.getWriter().write(aesEncryptMessage(
					(JSONObject.fromObject("{\"10001\":\"Requests are too frequent!\"}")).toString()));
			return;
		}
		
		if (requestRedisTask != null && Integer.parseInt(requestRedisTask.get("CONDITION_1").toString()) > 0) {
			// 禁止get请求
			if (requestHttp.getMethod().equalsIgnoreCase("GET") && !allowHttpGet(requestHttp)) {
				logger.error("request is nonsupport,{}", requestHttp.getRequestURL().toString());
				response.getWriter().write(aesEncryptMessage(
						(JSONObject.fromObject("{\"10001\":\"nonsupport get request!\"}").toString())));
				return;
			}
		}
		logger.info("==============" + data);
		// 不需要加密
		if (requestRedisTask != null && Integer.parseInt(requestRedisTask.get("CONDITION_2").toString()) <=0) {
			chain.doFilter(requestHttp, response);
			return;
		}
		if (StringUtils.isEmpty(data) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(timestamp)) {
			logger.error("request is illegal,{}", requestHttp.getRequestURL().toString());
			response.getWriter()
					.write(aesEncryptMessage((JSONObject.fromObject("{\"10001\":\"request is illegal!\"}")).toString()));
			return;
		}
		String requestUrl = requestHttp.getRequestURL().toString();// url
		logger.info("requestUrl:" + requestUrl);
		String realUrl = requestUrl;
		Map<String, Object> extendParams = new HashMap<String, Object>();
		//String signStr = Base64.encodeToString(exclusive(data)) + timestamp + generateStr + signKey;
		String signStr = null;
		try {
			signStr = "data=" + data + "&timestamp=" + timestamp;
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		
		try {
			String signData = MD5Util.md5(signStr);
			if (!signData.equalsIgnoreCase(sign)) {
				logger.error("Illegal Signature Verification", data + timestamp);
				response.getWriter()
						.write(aesEncryptMessage((JSONObject.fromObject("{\"10001\":\"Illegal Signature Verification!\"}")).toString()));
				return;
			}
			String queryData = decodeUrl(requestUrl, data);

			// 根据queryData 截取参数..
			logger.info("realUrl:" + realUrl + "?" + queryData);

			extendParams = urlQueryToMap(queryData);
			logger.info("extendParams:" + extendParams.toString());
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(aesEncryptMessage("{\"10001\":\"" + e.getMessage() + "\"}"));
			return;
		}
		logger.info("extendParams:" + extendParams.toString());
		chain.doFilter(request0, response);
	}

	public static Map<String, Object> urlQueryToMap(String urlparam) {
		if (StringUtils.isBlank(urlparam)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String[] param = urlparam.split("&");
		for (String keyvalue : param) {
			String[] pair = keyvalue.split("=");
			if (pair.length == 2) {
				try {
					map.put(pair[0], URLDecoder.decode(pair[1], "utf-8"));
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
				}
			}
			else {
				map.put(pair[0], "");
			}
		}
		
		
		return map;
	}

	/**
	 * 是否允许get请求
	 * 
	 * @return
	 */
	public boolean allowHttpGet(HttpServletRequest request) {
		if (request.getRequestURI().contains("centerCallback") || request.getRequestURI().contains("PayNotify")|| request.getRequestURI().contains("updateSignaTure")
				|| request.getRequestURI().contains("getShareTask") || request.getRequestURI().contains("refresh")|| request.getRequestURI().contains("getConfigApp")
				|| request.getRequestURI().contains("receiveShareTask")|| request.getRequestURI().contains("pay/pay")
				|| request.getRequestURI().contains("pay/order")|| request.getRequestURI().contains("pay/config")
				|| request.getRequestURI().contains("/pay/thirdPayNotify/111111111")) {
			return true;
		}
		return false;
	}

	/**
	 * 是否允许频繁请求
	 * 
	 * @return
	 */
	public boolean allowFrequentlyRequest(HttpServletRequest request) {
		if (request.getRequestURI().contains("centerCallback") || request.getRequestURI().contains("agentPayNotify")
				|| request.getRequestURI().contains("configPayNotify")
				|| request.getRequestURI().contains("/otherPay/getEeziePayOut")
				|| request.getRequestURI().contains("/otherPay/thirdEeziePayOutNotify")
				|| request.getRequestURI().contains("/pay/notify/{mechantCode}")
				|| request.getRequestURI().contains("/pay/thirdPayNotify/111111111")) {
			return true;
		}
		return false;
	}

	/**
	 * 防止重放
	 * 
	 * @param request
	 * @return
	 */
	public boolean repeatRequest(HttpServletRequest request) {
		return false;
	}
	
	
	public String aesEncryptMessage(String message){
		
		if(!isOpenDecode){
			return message;
		}
		if(StringUtils.isEmpty(message)){
			return "";
		}
		try {
			return Encrypt(message, "8w*gaesR^TIlab7a");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
    /**
 * 加密 * * @param src
  * @return
  * @throws Exception
 */  public static String Encrypt(String src, String key) throws Exception {
        return Operation(src, key, Cipher.ENCRYPT_MODE);
    }
 
//实际的加密解密操作
	private static String Operation(String src, String key, int mode) throws Exception {
		if (key==null) {
			return "Key不能为空";
		}
		if (key.length()!=16) {
			return "Key需要16位长度";
		}
		String result = "";
		byte[] raw = key.getBytes("utf-8");
		SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(mode, keySpec);
			byte[] encrypted = cipher.doFinal(src.getBytes("utf-8"));
			//将+替换为%2B
	  result = new BASE64Encoder().encode(encrypted).replace("+", "%2B");
		} else {
			cipher.init(mode, keySpec);
			//将%2B替换为+
	  src = src.replace("%2B", "+");
			byte[] encrypted = cipher.doFinal(new BASE64Decoder().decodeBuffer(src));
			result = new String(encrypted, "utf-8");
		}

		return result;
	}

	/**
	 * 是否统一验证加密
	 * 
	 * @return
	 */
	public boolean uniteEncryption(HttpServletRequest request) {
		if (request.getRequestURI().contains("centerCallback") || request.getRequestURI().contains("PayNotify")|| request.getRequestURI().contains("getAppConfig")
				|| request.getRequestURI().contains("payHtml")|| request.getRequestURI().contains("receiveShareTask")) {
			return false;
		}
		return true;
	}

	/**
	 * 解密url，base64
	 * @param url
	 * @param params
	 * @return
	 */
	private String decodeUrl(String url, String params)
	{
		if (!StringUtils.isEmpty(params))
		{
			try {
				params = AESUtils.decryptAES(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (params.endsWith("&"))
			{
				params = params.substring(0, params.length() - 1);
			}
			
			logger.info("请求真是url:"+url + "?" + params);
		}
		return params;
	}

	private String exclusive(String base64str)
	{
		base64str = base64str.replace(" ", "+");
		String info = Base64.decode(base64str, "utf-8");
		String key = "Iy54Zj1MQlV";
		byte[] result = new byte[info.length()];
		for (int i = 0, j = 0; i < info.length(); ++i)
		{
			result[i] = (byte) (info.charAt(i) ^ key.charAt(j));
			j = (++j) % (key.length());
		}
		String resultStr = new String(result);
		resultStr = resultStr.replace(" ", "+");
		String secretResult = Base64.decode(resultStr, "utf-8");
		return secretResult;
	}

	public static String inputStream2String(InputStream in) throws IOException
	{
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;)
		{
			out.append(new String(b, 0, n));
		}
		return out.toString();

	}

	@Override
	public void destroy() {

	}
	


}
