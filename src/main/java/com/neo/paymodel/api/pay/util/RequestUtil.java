package com.neo.paymodel.api.pay.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RequestUtil
{
  private static final Log logger = LogFactory.getLog(RequestUtil.class);

  public static String getAppHost(HttpServletRequest request)
  {
    StringBuffer url = request.getRequestURL();
    return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
  }

  public static String buildRequestUrl(HttpServletRequest request)
  {
    StringBuffer originalURL = request.getRequestURL();
    String url = originalURL.toString();
    Map<String, String[]> parameters = request.getParameterMap();

    if ((parameters != null) && (parameters.size() > 0)) {
      originalURL.append("?");
      for (String key : parameters.keySet()) {
        String[] values = (String[])parameters.get(key);
        for (int i = 0; i < values.length; ++i) {
          originalURL.append(key).append("=").append(values[i]).append("&");
        }
      }

      url = originalURL.substring(0, originalURL.length() - 1);
    }

    return url;
  }

  

  public static String getIp(HttpServletRequest request)
  {
    String ip = null;

    ip = request.getHeader("X-Forwarded-For");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getHeader("Proxy-Client-IP");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getHeader("WL-Proxy-Client-IP");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getHeader("HTTP_CLIENT_IP");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getParameter("__fromReferIP");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }

    ip = request.getHeader("X-Real-IP");
    if (isRealIP(ip)) {
      return getRealIp(ip);
    }
    ip = request.getRemoteAddr();

    return ip;
  }

  private static boolean isRealIP(String ip)
  {
    return ((StringUtils.isNotBlank(ip)) && (!("unknown".equalsIgnoreCase(ip))));
  }

  private static String getRealIp(String ip)
  {
    if (ip.indexOf(",") != -1) {
      return StringUtils.left(ip.split(",")[0], 15);
    }

    return ip;
  }

  public static boolean isJsonAjaxRequest(HttpServletRequest request)
  {
    String accept = request.getHeader("Accept");

    return ((accept != null) && (accept.indexOf("application/json") != -1));
  }

  public static Map<String, String> getParameterMap(HttpServletRequest request)
  {
    Map<String, String[]> properties = request.getParameterMap();

    Map<String, String> returnMap = new HashMap<String, String>();

    String value = "";
    for (Entry<String,  String[]> entry : properties.entrySet()) {
      String name = (String)entry.getKey();
      String[] values = (String[])entry.getValue();
      if (values == null) {
        value = "";
      } else {
        for (int i = 0; i < values.length; ++i) {
          value = values[i] + ",";
        }
        value = value.substring(0, value.length() - 1);
      }
      returnMap.put(name, value);
    }

    return returnMap;
  }

  public static String getLocalRealIp()
    throws SocketException
  {
    String localip = null;
    String netip = null;

    Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();

    InetAddress ip = null;
    boolean finded = false;

    while ((netInterfaces.hasMoreElements()) && (!(finded))) {
      NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
      Enumeration address = ni.getInetAddresses();

      while (address.hasMoreElements()) {
        ip = (InetAddress)address.nextElement();
        if ((!(ip.isSiteLocalAddress())) && (!(ip.isLoopbackAddress())) && (ip.getHostAddress().indexOf(":") == -1))
        {
          netip = ip.getHostAddress();
          finded = true;
          break; }
        if ((!(ip.isSiteLocalAddress())) || (ip.isLoopbackAddress()) || (ip.getHostAddress().indexOf(":") != -1))
          continue;
        localip = ip.getHostAddress();
      }

    }

    if ((netip != null) && (!("".equals(netip)))) {
      return netip;
    }
    return localip;
  }

  public static String getRequestIgnoreCaseParameter(HttpServletRequest request, String parameter)
  {
    String paramValue = null;

    Enumeration pnameEnum = request.getParameterNames();

    while (pnameEnum.hasMoreElements())
    {
      String paramName = (String)pnameEnum.nextElement();

      if (paramName.equalsIgnoreCase(parameter)) {
        paramValue = request.getParameter(paramName);
        break;
      }
    }

    return paramValue;
  }
  
  
  /**
	 * 获取充值回调参数
   *
   * @param request
   * @return
   */
  public static Map<String, String> getNotifyParmas(HttpServletRequest request) {
      Map<String, String> notifyParamsMap = new HashMap<String, String>();
      Enumeration<?> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
          String paramName = paramNames.nextElement().toString();
          notifyParamsMap.put(paramName, request.getParameter(paramName));
      }
      return notifyParamsMap;
  }
  
}
