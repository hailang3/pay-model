package com.neo.paymodel.api.pay.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class HttpUtil {

	private HttpUtil() {
	}

	/**
	 * 获取请求参数串
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String buildOriginalURLParamsString(HttpServletRequest request) {

		StringBuffer originalParams = new StringBuffer();
		String paramsString = "";

		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			originalParams.append("?");
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				String[] values = parameters.get(key);
				for (int i = 0; i < values.length; i++) {
					originalParams.append(key).append("=").append(values[i]).append("&");
				}
			}
			paramsString = originalParams.substring(0, originalParams.length() - 1); // 去掉最后一个&
		}

		return paramsString;
	}

	@SuppressWarnings("unchecked")
	public static String buildOriginalURL(HttpServletRequest request) {
		StringBuffer originalURL = request.getRequestURL();
		String url = originalURL.toString();
		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			originalURL.append("?");
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				String[] values = parameters.get(key);
				for (int i = 0; i < values.length; i++) {
					originalURL.append(key).append("=").append(values[i]).append("&");
				}
			}
			url = originalURL.substring(0, originalURL.length() - 1); // 去掉最后一个&
		}

		return url;
	}

	@SuppressWarnings("unchecked")
	public static String buildOriginalGETURL(HttpServletRequest request) {
		StringBuffer originalURL = request.getRequestURL();
		String url = null;
		if (request.getMethod().equalsIgnoreCase("POST")) {
			return originalURL.toString();
		}
		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			originalURL.append("?");
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				String[] values = parameters.get(key);
				for (int i = 0; i < values.length; i++) {
					originalURL.append(key).append("=").append(values[i]).append("&");
				}
			}

			url = originalURL.substring(0, originalURL.length() - 1); // 去掉最后一个&
		}
		return url;
	}

	/**
	 * 获取应用程序的上下文路径 比如：http://www.7pmi.com
	 * 
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName();
		if (request.getServerPort() != 80) {
			basePath += ":" + request.getServerPort();
		}
		basePath += contextPath + "/";
		return basePath;
	}

	// 判断应用程序来源路径是否是基于本应用程序，防止刷浏览器地址，只允许本页面跳转
	public static boolean isRefererRight(HttpServletRequest request) {
		if (request.getHeader("Referer") == null)
			return false;
		String referer = request.getHeader("Referer");
		String refererServerName = referer.split("//")[1].split("/")[0];
		String realServerName = request.getServerName();
		if (request.getServerPort() != 80) {
			realServerName += ":" + request.getServerPort();
		}
		if (!realServerName.equals(refererServerName))
			return false;
		return true;
	}

	/**
	 * 检测来源路径是否正确
	 * 
	 * @param request
	 * @param referer
	 * @param method
	 * @return
	 */
	public static boolean isRefererAndMethodRight(HttpServletRequest request, String referer, String method) {
		if (!method.equalsIgnoreCase(request.getMethod()) || request.getHeader("Referer") == null
				|| !(HttpUtil.getBasePath(request) + referer).equals(request.getHeader("Referer"))) {
			return false;
		}
		return true;
	}

	/**
	 * 检测来源路径是否正确
	 * 
	 * @param request
	 * @param referer
	 * @return
	 */
	public static boolean isRefererAndMethodRight(HttpServletRequest request, String referer) {
		if (request.getHeader("Referer") == null
				|| !(HttpUtil.getBasePath(request) + referer).equals(request.getHeader("Referer"))) {
			return false;
		}
		return true;
	}

	public static void printCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				System.out.print("cookie name = [" + cookies[i].getName() + "]\t\t");
				System.out.println("cookie value = [" + cookies[i].getValue());
			}
		}
	}

	/**
	 * 模拟表单发送POST形式
	 * 
	 * @param requestURL
	 *            请求路径
	 * @param params
	 *            参数
	 * @param paramValues
	 *            参数值
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String simulateUrlPostRequest(String requestURL, String[] params, String[] paramValues,
			String charset) {

		HttpClient httpClient = new HttpClient();
		setHttpProxy(httpClient);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		PostMethod postMethod = new PostMethod(requestURL);

		// 填入各个表单域的值
		int paramCount = params.length;
		if (paramCount > 0) {
			NameValuePair[] data = new NameValuePair[paramCount];
			for (int i = 0; i < paramCount; i++) {
				NameValuePair nvp = new NameValuePair(params[i], paramValues[i]);
				data[i] = nvp;
			}
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);
		}
		// 执行postMethod
		int statusCode = 0;
		String responseStr = "";
		InputStream is = null;
		try {

			statusCode = httpClient.executeMethod(postMethod);
			responseStr = postMethod.getResponseBodyAsString();
			// is = postMethod.getResponseBodyAsStream();
			// responseStr = readFileContent1(is);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (null != postMethod) {
				postMethod.releaseConnection();
			}
		}

		return responseStr;
	}

	/**
	 * 模拟表单发送POST形式
	 * 
	 * @param requestURL
	 *            请求路径
	 * @param params
	 *            参数
	 * @param paramValues
	 *            参数值
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String simulateUrlPostMapRequest(String requestURL, Map<String, Object> maps, String charset) {

		HttpClient httpClient = new HttpClient();
		setHttpProxy(httpClient);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		PostMethod postMethod = new PostMethod(requestURL);

		// 填入各个表单域的值
		int paramCount = maps.size();
		if (paramCount > 0) {
			NameValuePair[] data = new NameValuePair[paramCount];
			int index = 0;
			for (Map.Entry<String, Object> entry : maps.entrySet()) {
				NameValuePair nvp = new NameValuePair(entry.getKey(), entry.getValue().toString());
				data[index] = nvp;
				index++;
			}
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);
		}
		// 执行postMethod
		int statusCode = 0;
		String responseStr = "";
		InputStream is = null;
		try {

			statusCode = httpClient.executeMethod(postMethod);
			responseStr = postMethod.getResponseBodyAsString();
			// is = postMethod.getResponseBodyAsStream();
			// responseStr = readFileContent1(is);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (null != postMethod) {
				postMethod.releaseConnection();
			}
		}

		return responseStr;
	}

	/**
	 * 模拟表单发送POST形式，抛出异常形式
	 * 
	 * @param requestURL
	 *            请求路径
	 * @param params
	 *            参数
	 * @param paramValues
	 *            参数值
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String simulateUrlPostRequestThrowable(String requestURL, String[] params, String[] paramValues,
			String charset) throws Exception {

		HttpClient httpClient = new HttpClient();
		setHttpProxy(httpClient);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		PostMethod postMethod = new PostMethod(requestURL);
		// 填入各个表单域的值
		int paramCount = params.length;
		if (paramCount > 0) {
			NameValuePair[] data = new NameValuePair[paramCount];
			for (int i = 0; i < paramCount; i++) {
				NameValuePair nvp = new NameValuePair(params[i], paramValues[i]);
				data[i] = nvp;
			}
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);
		}
		// 执行postMethod
		int statusCode = 0;
		statusCode = httpClient.executeMethod(postMethod);
		String responseStr = postMethod.getResponseBodyAsString();// 返回结果
		postMethod.releaseConnection();
		return responseStr;
	}
	
	
	 /**    
     * POST请求，Map形式数据    
     * @param url 请求地址    
     * @param param 请求数据    
     * @param charset 编码方式    
     */  
    public static String sendPost(String url, Map<String, String> param,  
            String charset) {  
  
        StringBuffer buffer = new StringBuffer();  
        if (param != null && !param.isEmpty()) {  
            for (Map.Entry<String, String> entry : param.entrySet()) {  
                try {
					buffer.append(entry.getKey()).append("=").append(  
					        URLEncoder.encode(entry.getValue(), "utf-8")).append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}  
  
            }  
        }  
        buffer.deleteCharAt(buffer.length() - 1);  
  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接    
            URLConnection conn = realUrl.openConnection();  
            // 设置通用的请求属性    
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent",  
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            // 发送POST请求必须设置如下两行    
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            // 获取URLConnection对象对应的输出流    
            out = new PrintWriter(conn.getOutputStream());  
            // 发送请求参数    
            out.print(buffer);  
            // flush输出流的缓冲    
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应    
            in = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream(), charset));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送 POST 请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流    
        finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  

	/**
	 * 模拟URL路径请求 GET形式
	 * 
	 * @param requestURL
	 *            请求路径
	 * @param charset
	 *            字符编码
	 * @return
	 */
	public static String simulateUrlGetRequest(String requestURL, String charset) {

		HttpClient httpClient = new HttpClient();
		setHttpProxy(httpClient);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		GetMethod getMethod = new GetMethod(requestURL);
		// 执行getMethod
		int statusCode = 0;
		String responseStr = "";
		try {
			statusCode = httpClient.executeMethod(getMethod);
			responseStr = getMethod.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != getMethod) {
				getMethod.releaseConnection();
			}
		}

		return responseStr;
	}
	
	public static String httpPostWithJSON(String url, String jsonData) throws Exception {

	    HttpClient httpClient = new HttpClient();  
	    setHttpProxy(httpClient);
	    // 设置连接超时时间(单位毫秒)  
	    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60*1000);  
	    // 设置读取超时时间(单位毫秒)  
	    httpClient.getHttpConnectionManager().getParams().setSoTimeout(60*1000);  
	    PostMethod method = new PostMethod(url);  
	    String info = null;  
	    try {  
	          
	        RequestEntity entity = new StringRequestEntity(jsonData, "application/json", "UTF-8");  
	        method.setRequestEntity(entity);  
	        httpClient.executeMethod(method);  
	        int code = method.getStatusCode();  
	        if (code == HttpStatus.SC_OK) {  
	            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));  
	            StringBuffer stringBuffer = new StringBuffer();  
	            String str = "";  
	            while ((str = reader.readLine()) != null) {  
	                stringBuffer.append(str);  
	            }  
	            info = stringBuffer.toString();  
	            System.out.print("httpPostWithJSON 返回报文："+info);  
	        }else{  
	        	System.out.print("httpPostWithJSON 接口返回失败  httpStatusCode="+code);  
	        }  
	  
	    } catch (Exception ex) {  
	    	System.out.print("内部接口报文发送异常:" + ex.getMessage());  
	        ex.printStackTrace();  
	    } finally {  
	        if (method != null) {  
	            method.releaseConnection();  
	        }  
	    }  
	    return info;  
	}

	public static String readFileContent1(InputStream is) {

		// InputStream is = null;

		BufferedReader br = null;
		InputStreamReader isr = null;
		BufferedWriter bw = null;
		StringBuffer line = new StringBuffer();
		String str = null;
		// File file1 = new File("info2.txt");
		// OutputStreamWriter osw = null;
		try {
			// is = new FileInputStream(soureFile);
			isr = new InputStreamReader(is, "gb2312");
			FileOutputStream fos;
			// fos = new FileOutputStream(file1);
			// osw = new OutputStreamWriter(fos);
			br = new BufferedReader(isr);
			// bw = new BufferedWriter(osw);
			while ((str = br.readLine()) != null) {
				line.append(str);
				// System.out.println("str:" + str);
				line.append("\n");
			}
			// System.out.println("line:" + line);
			// System.out.println("line.toString():"+line.toString());
			// bw.write("我发沙敦府打算发是！");
			// bw.close();

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return line.toString();
	}

	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = null;
		//
		ip = request.getHeader("X-Forwarded-For");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		//
		ip = request.getHeader("Proxy-Client-IP");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		//
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		//
		ip = request.getHeader("HTTP_CLIENT_IP");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		//
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		//
		ip = request.getParameter("__fromReferIP");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		// NGINX用
		ip = request.getHeader("X-Real-IP");
		if (isRealIP(ip)) {
			return getRealIp(ip);
		}
		ip = request.getRemoteAddr();
		//
		return ip;
	}

	static String UNKNOWN_IP = "unknown";

	public static boolean isRealIP(String ip) {
		return StringUtils.isNotBlank(ip) && !UNKNOWN_IP.equalsIgnoreCase(ip);
	}

	public static String getRealIp(String ip) {
		//
		if (ip.indexOf(",") != -1) {
			return StringUtils.left(ip.split(",")[0], 15);

		}
		return ip;
	}

	public static String getBodyString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		return str;
	}

	static void setHttpProxy(HttpClient httpClient) {

		try {
			String proxyHost = UtilConfig.getInstance().getValue("http.proxyHost");
			String proxyPort = UtilConfig.getInstance().getValue("http.proxyPort");
			httpClient.getHostConfiguration().setProxy(proxyHost, Integer.parseInt(proxyPort));
			System.out.println("setHttpProxy[" + proxyHost + ":" + proxyPort + "]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("setHttpProxy[fail:" + e.getMessage() + "]");
		}

	}

	static class UtilConfig {

		private static Object lock = new Object();
		private static UtilConfig config = null;
		private static ResourceBundle rb = null;
		private static final String CONFIG_FILE = "util";

		private UtilConfig() {
			rb = ResourceBundle.getBundle(CONFIG_FILE);
		}

		public static UtilConfig getInstance() {
			synchronized (lock) {
				if (null == config) {
					config = new UtilConfig();
				}
			}
			return (config);
		}

		public String getValue(String key) {
			return (rb.getString(key));
		}
	}

	public static void main(String[] args) {
		String url = "http://192.168.3.32:8080/ext-mj-web/shareTask/share.do";
		String[] params = { "uid", "utkn" };
		String[] paramValues = { "45241", "COZ7OYRYBUO44377ULP782C8Q6XW25SW" };
		String str = simulateUrlPostRequest(url, params, paramValues, "utf-8");
	}

}

