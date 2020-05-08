package com.neo.paymodel.api.pay.channel.payidr;


import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.channel.Channel;
import com.neo.paymodel.api.pay.channel.IPayContext;
import com.neo.paymodel.api.pay.channel.PayChannelTemplateApi;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.common.util.MD5Util;
import com.neo.paymodel.common.util.RequestUtil;
import com.neo.paymodel.common.util.WebUtil;
import com.neo.paymodel.api.pay.web.model.RetModel;
import com.neo.paymodel.api.pay.web.vo.BankInfo;
import com.neo.paymodel.api.pay.web.vo.PaySubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Channel(id=6, name="IDRPay")
public class IDRApi extends PayChannelTemplateApi {
	
	static final Logger logger = LoggerFactory.getLogger(IDRApi.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void submitOrder(PaySubmitRequest paySubmitReq, PayOrder payOrder, HttpServletResponse resp,
							IPayContext context) {
		JSONObject params = new JSONObject();
		Map<String, String> paraMap = new HashMap<String, String>();
		
		String postUrl = context.getApiUrl();	
		String key = context.getSignKey();
		String merchant_id = context.getAccount();
		
		
		String order_id = payOrder.getOrderNo();
		String business_email = context.getConfig("business_email").toString();
		String bank_id = payOrder.getBankCode();
		String deposit_method_id = context.getConfig("deposit_method_id").toString();
		if(bank_id.equals("VAPERMATA")) {
			deposit_method_id = "4";
		}

		long deposit_amount = payOrder.getPlanPay();
		String currency = context.getConfig("currency").toString();
		String customer_name = String.valueOf(payOrder.getUserId());
		String customer_email = context.getConfig("customer_email").toString();
		String customer_phone_no = context.getConfig("customer_phone_no").toString();
		String customer_address = context.getConfig("customer_address").toString();
		String note = context.getConfig("note").toString();
		String website_url = context.getConfig("website_url").toString();
		String request_time = String.valueOf(System.currentTimeMillis() / 1000);
		String success_url = context.getConfig("success_url").toString();
		String fail_url =  context.getConfig("fail_url").toString();
		String callback_noti_url = context.getNotifyUrl();
		String sign_data_str = key + merchant_id + business_email + order_id + deposit_method_id + bank_id + deposit_amount + currency +
				               customer_name + customer_email + customer_phone_no + customer_address  
				               + note + website_url + request_time + success_url + fail_url + callback_noti_url;
		String sign_data = MD5Util.encode(sign_data_str).toUpperCase();
		
		
		String API_USER_NAME = context.getConfig("API_USER_NAME").toString();
		String API_PASSWORD = context.getConfig("API_PASSWORD").toString();
		String userPwd = API_USER_NAME + ":" + API_PASSWORD;
		String encoding = new sun.misc.BASE64Encoder().encode(userPwd.getBytes());
		
		
		params.put("merchant_id", merchant_id);
		params.put("business_email", business_email);
		params.put("order_id", order_id);
		params.put("deposit_method_id", deposit_method_id);
		params.put("bank_id", bank_id);
		params.put("deposit_amount", deposit_amount);
		params.put("currency", currency);
		params.put("customer_name", customer_name);
		params.put("customer_email", customer_email);
		params.put("customer_phone_no", customer_phone_no);
		params.put("customer_address", customer_address);
		params.put("note", note);
		params.put("website_url", website_url);
		params.put("request_time", request_time);
		params.put("success_url", success_url);
		params.put("fail_url", fail_url);
		params.put("callback_noti_url", callback_noti_url);
		params.put("sign_data", sign_data);
		
		paraMap.put("requestParams", params.toJSONString());

		String resultJson = sendPost(postUrl, paraMap, "UTF-8", encoding);
		
		logger.info("IDRApi response[{}][{}]", params.toJSONString(), resultJson);
		
		String redirect_url = "";
		JSONObject responseStr = JSONObject.parseObject(resultJson);
		if(responseStr != null) {
			
			String status = responseStr.getString("status");
			if("success".equalsIgnoreCase(status)) {
				
				redirect_url = responseStr.getString("redirect_url");
			}
		}
		logger.info("IDRApi redirect_url[{}]", redirect_url);
		
		
		StringBuffer retHtml = new StringBuffer();
		
			retHtml.append("<!DOCTYPE html>");
			retHtml.append("<html>");
			retHtml.append("<head>");
			retHtml.append("<title>submit</title>");
			retHtml.append("<meta charset=\"utf-8\">");
			retHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
			retHtml.append("</head>");
			retHtml.append("<body>");
			if(redirect_url != null && !redirect_url.equals("")) {
			retHtml.append("<script language='javascript'>document.location = '" + redirect_url + "'</script>");

			}else {
				retHtml.append("<p>pay error</p>");
			}
			retHtml.append("</body>");
			retHtml.append("</html>");
		
		logger.debug("响应内容：{}", retHtml);
		
		WebUtil.write(resp, retHtml.toString());
		
	}

	@Override
	protected boolean checkNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		
//		[{requestParams={"merchant_id":"83070068","transaction_id":"D2118883BCAH8G1OEJJN","order_id":"202002181622538814944886",
//				"bank_id":"BCA","deposit_method_id":"1","deposit_amount":"100000","merchant_fee_percent":"3.0","net_amount":"97000",
//				"currency":"IDR","order_status":"Successful","sign_data":"022B34E9FC3F9F502B451E6543916981"}}]
		Map<String, String> paramMap = RequestUtil.getNotifyParmas(notifyReq);
		logger.info("IDRApi notify [{}]", paramMap);
		String resultMap = paramMap.get("requestParams");
		logger.info("IDRApi resultMap [{}]", resultMap);
		JSONObject finalResult = JSONObject.parseObject(resultMap);
		logger.info("IDRApi finalResult [{}]", finalResult);
		if(finalResult != null && !finalResult.isEmpty()) {
			String merchant_id = finalResult.getString("merchant_id");
			String transaction_id = finalResult.getString("transaction_id");
			String order_id = finalResult.getString("order_id");
			String bank_id = finalResult.getString("bank_id");
			String deposit_method_id = finalResult.getString("deposit_method_id");
			String deposit_amount = finalResult.getString("deposit_amount");
			String merchant_fee_percent = finalResult.getString("merchant_fee_percent");
			String net_amount = finalResult.getString("net_amount");
			String currency = finalResult.getString("currency");
			String order_status = finalResult.getString("order_status");
			String sign_data = finalResult.getString("sign_data");
			
			if(!"Successful".equalsIgnoreCase(order_status)) {
				return false;
			}
			
			String signStr = context.getSignKey() + merchant_id + order_id + deposit_method_id + currency;
			logger.info("IDRApi notify signstr [{}]", signStr);
			
			String checkSign = MD5Util.encode(signStr).toUpperCase();
			logger.info("IDRApi notify checkSign [{}]", checkSign);
			if(checkSign.equalsIgnoreCase(sign_data)) {
				return true;
			}
			
		}
		
		return false;
	}

	@Override
	protected ITransactionOrder parsingNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		Map<String, String> paramMap = RequestUtil.getNotifyParmas(notifyReq);

		String resultMap = paramMap.get("requestParams");

		JSONObject finalResult = JSONObject.parseObject(resultMap);
		boolean status = true;
		if(finalResult != null && !finalResult.isEmpty()) {
			//{"bank_id":"BCA","currency":"IDR","deposit_amount":"100000","deposit_method_id":"1","merchant_fee_percent":"3.0",
			//"merchant_id":"83070068","net_amount":"97000","order_id":"202002181622538814944886","order_status":"Successful",
			//"sign_data":"022B34E9FC3F9F502B451E6543916981","transaction_id":"D2118883BCAH8G1OEJJN"}
			String transaction_id = finalResult.getString("transaction_id");
			String order_id = finalResult.getString("order_id");
			Integer net_amount = finalResult.getInteger("net_amount");
			String currency = finalResult.getString("currency");
			String order_status = finalResult.getString("order_status");
			
			if(!"Successful".equalsIgnoreCase(order_status)) {
				status = false;
			}
			
			String responseResult = "";
			if(status) {
				responseResult = "{\"received\":\"Yes\"}";
			}
			
			return new SimpleTransactionOrder(status, order_id,  net_amount, transaction_id, new Timestamp(System.currentTimeMillis()), currency, responseResult);
			
		}
		
		return null;
	}



	
	 /**    
     * POST请求，Map形式数据    
     * @param url 请求地址    
     * @param param 请求数据    
     * @param charset 编码方式    
     */  
    private String sendPost(String url, Map<String, String> param,  
            String charset, String encoding) {  
  
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
            // 设置用户名和密码
            conn.setRequestProperty("Authorization", "Basic " + encoding);

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

	@Override
	public RetModel<List<BankInfo>> getBankInfo(int payChannelId) {
		// TODO Auto-generated method stub
		return null;
	}  




}