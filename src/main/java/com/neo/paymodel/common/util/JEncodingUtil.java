package com.neo.paymodel.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class JEncodingUtil {
  //
  public final static String ENCODING_ISO8859_1 = "iso8859-1"; // iso8859-1
  //
  public final static String ENCODING_UTF8 = "utf-8"; // utf-8
  //
  public final static String ENCODING_GBK = "gbk"; // gbk
  //
  public final static String ENCODING_GB2312 = "gb2312"; // gb2312
  //
  public static String SERVLET_CONTAINER_URI_ENCODING = ENCODING_ISO8859_1; // tomcat容器的默认URIEncoding：iso8859-1
  //
  public static String APP_DEFAULT_ENCODING = ENCODING_UTF8; //程序的默认编码
  //
  public static String urlDecode(String paramValue) throws UnsupportedEncodingException{
    //
    return urlDecode(paramValue,APP_DEFAULT_ENCODING,SERVLET_CONTAINER_URI_ENCODING);
  }
  //
  public static String urlDecode(String paramValue,String requestParamValueEncoding) throws UnsupportedEncodingException{
    //
    return urlDecode(paramValue,requestParamValueEncoding,SERVLET_CONTAINER_URI_ENCODING);
  }
  //
  public static String urlDecode(String paramValue,String requestParamValueEncoding,String servletContainerURIEncoding) throws UnsupportedEncodingException {
    //
    return URLDecoder.decode(new String(paramValue.getBytes(servletContainerURIEncoding),requestParamValueEncoding),
                             requestParamValueEncoding);
//    return URLDecoder.decode(URLDecoder.decode(URLEncoder.encode(paramValue,servletContainerURIEncoding),requestParamValueEncoding),
//                             requestParamValueEncoding);
    //
  }
  //=================================test
  public static void main(String[] args) throws UnsupportedEncodingException {
    System.out.println(urlDecode("%BF%A8%B2%BB%B4%E6%D4%DA%BB%F2%D3%E0%B6%EE%CE%AA0",ENCODING_GBK));
    System.out.println(urlDecode("您好"));
    System.out.println(URLEncoder.encode(JEncodingUtil.urlDecode("http%3a%2f%2fpayback.7pmi.com%2fcz591payBackground.jhtml"),"utf-8"));
  }
}
  

