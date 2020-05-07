package com.neo.paymodel.api.pay.channel.flashpay;

import com.neo.paymodel.api.pay.channel.Channel;
import com.neo.paymodel.api.pay.channel.IPayContext;
import com.neo.paymodel.api.pay.channel.PayChannelTemplateApi;
import com.neo.paymodel.api.pay.dao.IPayDao;
import com.neo.paymodel.api.pay.entity.PayMerchant;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.util.RequestUtil;
import com.neo.paymodel.api.pay.util.WebUtil;
import com.neo.paymodel.api.pay.web.model.RetModel;
import com.neo.paymodel.api.pay.web.vo.BankInfo;
import com.neo.paymodel.api.pay.web.vo.PaySubmitRequest;
import com.neo.paymodel.common.util.DateUtil;
import com.neo.paymodel.common.util.RSAUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Channel(id=12, name="flashpay支付")
public class FlashPayApi extends PayChannelTemplateApi {
	static final Logger logger = LoggerFactory.getLogger(FlashPayApi.class);
	@Autowired
	private IPayDao payDao;
	
	
	public void submitOrder(PaySubmitRequest paySubmitReq, HttpServletResponse resp) {
		//验证支付参数
		PayOrder payOrder = paySubmitReq.getPayOrder();
		String errMsg = checkOrderParameter(paySubmitReq);
		if(!StringUtils.isBlank(errMsg)) {
			WebUtil.write(resp, errMsg);
			return;
		}	
		PayMerchant payMerchant =payDao. getPayMerchant(payOrder.getPayMerchantId());
		//提交支付请求
		//String  url = "testapi.flashpay.id/gateway.do";
        //Map<String,String> resultMap = new HashMap<String, String>();
		JSONObject merchantExp=JSONObject.fromObject(payMerchant.getExtend());
		String appId=payMerchant.getAccount();
		String method=merchantExp.getString("method");
		String sign="";
		String timestamp= DateUtil.formatDateTime("yyyy-MM-dd HH:mm:ss");
		String version=merchantExp.getString("version");
		//String version=merchantExp.getString("version");
		String notifyUrl=payMerchant.getNotifyUrl();
		String bizContent="{\"bankId\":\""+paySubmitReq.getBankCode()+"\","
				+ "\"repayAddress\":\""+paySubmitReq.getRepayAddress()+"\","
				+ "\"repayPhoneNumber\":\""+paySubmitReq.getRepayPhoneNumber()+"\","
				+ "\"repayAmount\":\""+payOrder.getPlanPay()+"\","
				+ "\"repayIdNumber\":\""+paySubmitReq.getRepayIdNumber()+"\","
				+ "\"repayName\":\""+paySubmitReq.getRepayName().trim()+"\","
				+ "\"channelCode\":\""+merchantExp.getString("channelCode")+"\","
				+ "\"uniqueCode\":\""+payOrder.getOrderNo()+"\""
				+ "}";			
		Map<String,String> map=new HashMap<String,String>();
		map.put("appId",appId);
		 map.put("bizContent", bizContent);
		 map.put("method", method);
		 map.put("notifyUrl", notifyUrl);
		 map.put("timestamp", timestamp);
		 map.put("version", version);
	    try {
			sign = RSAUtils.signByPrivateKeyForMap(map,payMerchant.getSignKey());
		} catch (Exception e1) {
			logger.info("签名异常："+e1);
			e1.printStackTrace();
		}
	    
		
		 map.put("sign",sign);
		 
		 String result="";
		 try {
			 result = WebUtil.POST_FORM(payMerchant.getApiUrl(),map,"utf-8");
			} catch (ParseException e) {
				logger.info("请求第三方异常1："+e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.info("请求第三方异常2："+e);
				e.printStackTrace();
			}
		//返回支付码
		 JSONObject resultObj= JSONObject.fromObject(result);
		 if("0000".equals(resultObj.get("code"))) {
			 JSONObject resultData= JSONObject.fromObject(resultObj.get("data"));
			 //记录支付码
			 String va = (String)resultData.get("repayCode");
			 String transactionNo = (String)resultData.get("orderId");//第三方订单号
			 payOrder.setVirtualAccount(va);
			 payOrder.setTransactionNo(transactionNo);
			 payDao.updateOrderVAAndTNo(payOrder);
			 WebUtil.write(resp,va);
		 }else {
			 WebUtil.write(resp,(String)resultObj.get("msg"));	
		 }

	}

	private String checkOrderParameter(PaySubmitRequest paySubmitReq) {
		 String repayAddress=paySubmitReq.getRepayAddress();     //	还款人地址
		 String repayPhoneNumber = paySubmitReq.getRepayPhoneNumber(); //还款人手机号
		 String repayIdNumber = paySubmitReq.getRepayIdNumber();    //还款人身份证号
		 String repayName = paySubmitReq.getRepayName();        //还款人姓名
		 if(StringUtils.isBlank(repayAddress)||StringUtils.isBlank(repayPhoneNumber)||
				 StringUtils.isBlank(repayName) || StringUtils.isBlank(repayIdNumber)) {
			 return "还款人信息为空";
		 }
		return null;
	}

	@Override
	public int checkOrder(PayOrder payOrder) {
		
		return 0;
	}

	@Override
	public void submitOrder(PaySubmitRequest paySubmitReq, PayOrder payOrder, HttpServletResponse resp,
			IPayContext context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * appId	String	商户ID	123456789
	* 	orderId	String	订单号	HK20190808080808000001
	* 	callType	number	回调类型 1-放款 2-还款	1
	* 	actualTime	String	实际成果时间(只有处理成功有值)	2019-10-17 17:40:14
	* 	operateState	number	支付结果 1-支付成功 2-支付失败 -1-校验失败	1
	* 	callMessage	String	回调描述	支付成功
	 */
	@Override
	protected boolean checkNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		Map<String, String> paramMap = RequestUtil.getNotifyParmas(notifyReq);
		if(paramMap.get("operateState").equals("1") || paramMap.get("operateState").equals("-1")
				|| paramMap.get("operateState").equals("2")){
			return true;
		}
		
		return false;
	}

	@Override
	protected ITransactionOrder parsingNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		Map<String, String> paramMap = RequestUtil.getNotifyParmas(notifyReq);
		boolean status = false;
		if(Integer.parseInt(paramMap.get("operateState")) == 1) {
			status = true;
		}
		
		 String UpOrderNum = paramMap.get("orderId");
		 PayOrder payOrder = payDao.getPayOrderByTNo(UpOrderNum);
		 String callMessage = paramMap.get("callMessage");
		// String UpOrderNum = paramMap.get("billno");
		// int pay_amount=Integer.parseInt(paramMap.get("amount")); 
		 String currency = "IDR";
		//return new SimpleTransactionOrder(status, orderNum,   new Timestamp(System.currentTimeMillis()), currency,callMessage);
		 String orderNum = payOrder.getOrderNo();
		 long pay_amount=payOrder.getPlanPay(); 	
		return new SimpleTransactionOrder(status, orderNum,  pay_amount, UpOrderNum, new Timestamp(System.currentTimeMillis()), currency, callMessage);

	}
	/*
	 * public void handleNotify2(HttpServletRequest notifyReq, HttpServletResponse
	 * notifyResp, IPayContext context) {
	 * 
	 * //验证回调合法性包含：调用ip， //获取商户配置的ip白名单列表 String whiteIps = context.getWhiteIps();
	 * String reqIp= HttpUtil.getIpAddr(notifyReq); if(whiteIps!=null &&
	 * !whiteIps.equals("") && !whiteIps.contains(reqIp)){ //ip不合法 //do...
	 * WebUtil.write(notifyResp, "invalid-request-0");
	 * logger.debug("#handleNotify:invalid-request-0"); return; }
	 * 
	 * 
	 * //参数签名验证， if(!checkNotifyReq(notifyReq, context)){ //请求参数不合法 //do...
	 * WebUtil.write(notifyResp, "invalid-request-1");
	 * logger.debug("#handleNotify:invalid-request-1"); return; }
	 * 
	 * 
	 * ITransactionOrder transactionOrder = parsingNotifyReq(notifyReq, context);
	 * 
	 * String orderNo=transactionOrder.getOrderNo(); String
	 * transactionNo=transactionOrder.getTransactionNo(); Timestamp transactionTime
	 * = transactionOrder.getTransactionTime(); long payActual =
	 * transactionOrder.getPayActual(); String currency =
	 * transactionOrder.getCurrency(); String responseResult =
	 * transactionOrder.getResponseResult();
	 * 
	 * PayOrder payOrder = payOrderService.getPayOrder(orderNo); if(payOrder==null){
	 * // 无效订单 //do... WebUtil.write(notifyResp, "invalid-request-2");
	 * logger.debug("#handleNotify:invalid-request-2"); return; }else { //
	 * if(payOrder.getStatus()!=PayOrder.PAY_WAITING){ //响应
	 * WebUtil.write(notifyResp, nofiyOk(responseResult)); return; } }
	 * 
	 * // int payActual = getParamPayActual(notifyReq, payOrder.getCurrency());
	 * 
	 * currency = currency == null || currency.equals("") ? payOrder.getCurrency() :
	 * currency;
	 * 
	 * boolean payStatus=false; //订单状态验证 if(transactionOrder.getStatus()){ //
	 * ITransactionOrder transactionOrderNew = checkOrderRemote(payOrder, context);
	 * if(transactionOrderNew==null || transactionOrderNew.getStatus()){ //支付成功
	 * payStatus=true; }else{ //远程验证失败 payStatus=false; WebUtil.write(notifyResp,
	 * "invalid-request-3"); logger.debug("#handleNotify:invalid-request-3");
	 * return; }
	 * 
	 * }
	 * 
	 * 
	 * boolean isNeedHandle= false;
	 * 
	 * //保存订单状态 if(payStatus){ if(payOrderService.updateOrderPaySuccess(orderNo,
	 * payActual, transactionNo, transactionTime, currency)){
	 * 
	 * isNeedHandle=true;
	 * 
	 * payOrder.setStatus(PayOrder.PAY_SUCCESS); payOrder.setActualPay(payActual);
	 * payOrder.setTransactionNo(transactionNo);
	 * payOrder.setTranscationTime(transactionTime); payOrder.setCurrency(currency);
	 * }
	 * 
	 * }else{ payOrderService.updateOrderPayFailure(orderNo, transactionNo); }
	 * 
	 * //响应 WebUtil.write(notifyResp, nofiyOk(responseResult));
	 * 
	 * //通知加金币 //do。。。。。 if(isNeedHandle){
	 * payOrderService.handlePayOrderAsync(payOrder); }
	 * 
	 * 
	 * }
	 */

	@Override
	public RetModel<List<BankInfo>> getBankInfo(int payChannelId) {
		//取redis
		PayMerchant payMerchant =payDao. getPayMerchant(7);
		//提交查询请求
		JSONObject merchantExp=JSONObject.fromObject(payMerchant.getExtend());
		String appId=payMerchant.getAccount();
		String method=merchantExp.getString("bankMethod");
		String sign="";
		String timestamp=DateUtil.formatDateTime("yyyy-MM-dd HH:mm:ss");
		String version=merchantExp.getString("version");
		//String version=merchantExp.getString("version");
		String notifyUrl=payMerchant.getNotifyUrl();
	//	String bizContent="";			
		Map<String,String> map=new HashMap<String,String>();
		map.put("appId",appId);
		// map.put("bizContent", bizContent);
		 map.put("method", method);
		 map.put("notifyUrl", notifyUrl);
		 map.put("timestamp", timestamp);
		 map.put("version", version);
	    try {
			sign = RSAUtils.signByPrivateKeyForMap(map,payMerchant.getSignKey());
		} catch (Exception e1) {
			logger.info("签名异常："+e1);
			e1.printStackTrace();
		}	    		
		 map.put("sign",sign);
		 
		 String result="";
		 try {
			 result = WebUtil.POST_FORM(payMerchant.getApiUrl(),map,"utf-8");
			} catch (ParseException e) {
				logger.info("请求第三方异常1："+e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.info("请求第三方异常2："+e);
				e.printStackTrace();
			}
		//返回银行数据
		 JSONObject resultObj= JSONObject.fromObject(result);
		 if("0000".equals(resultObj.get("code"))) {
			 List<BankInfo> list = com.alibaba.fastjson.JSONObject.parseArray(resultObj.get("data").toString(),BankInfo.class);
			 return new RetModel< List<BankInfo>>(list);	 			
		 }else {
			 return new RetModel< List<BankInfo>>(-1,"查询失败");
		 }
	}
}
