package com.neo.paymodel.api.pay.util;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WebUtil {
	
	static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	public static void write(HttpServletResponse resp, String respContent){
		try {
			resp.getWriter().write(respContent);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("{}", e.getMessage());
		}
	}
	
	public static void redirect(HttpServletResponse resp, String url){
		try {
			resp.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("{}", e.getMessage());
		};
	}
	
	//链接第三方
    public static String POST_FORM(String url, Map<String,String> map,String encoding) throws ParseException, IOException{  
        String body = "";  
        //创建httpclient对象  
        CloseableHttpClient client = HttpClients.createDefault();  
        //创建post方式请求对象  
        HttpPost httpPost = new HttpPost(url);  
        
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();  
        if(map!=null){  
           for (Entry<String, String> entry : map.entrySet()) {  
               nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
           }
       }  
       //设置参数到请求对象中  
       httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
       
       //设置连接超时时间 为3秒 
       RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000).setSocketTimeout(5000).build();
       httpPost.setConfig(config);
       
   
       //执行请求操作，并拿到结果（同步阻塞）  
       CloseableHttpResponse response = client.execute(httpPost);  
       //获取结果实体  
       HttpEntity entity = response.getEntity();  
       if (entity != null) {
           //按指定编码转换结果实体为String类型  
          body = EntityUtils.toString(entity, encoding);  
       }  
      EntityUtils.consume(entity);  
       //释放链接  
       response.close();  
       return body;  
   }

}
