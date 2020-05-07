package com.neo.paymodel.api.pay.channel;

import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.service.IPayOrderService;
import com.neo.paymodel.api.pay.util.HttpUtil;
import com.neo.paymodel.api.pay.util.WebUtil;
import com.neo.paymodel.api.pay.web.vo.PaySubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public abstract class PayChannelTemplateApi extends PayChannelContextApi {

	static final Logger logger = LoggerFactory.getLogger(PayChannelTemplateApi.class);
	
	protected IPayOrderService payOrderService;

	@Override
	protected final void init(){
		//
		payOrderService=appContext.getBean(IPayOrderService.class);
		doInit();
	}
	
	protected void doInit() {}

	public abstract void submitOrder(PaySubmitRequest paySubmitReq, PayOrder payOrder, HttpServletResponse resp, IPayContext context);

	@Override
	public final int checkOrder(PayOrder payOrder, IPayContext context) {
		//
		int orderStatus=PayOrder.PAY_WAITING;
		
		ITransactionOrder transactionOrder = checkOrderRemote(payOrder, context);
		if(transactionOrder==null){
			return orderStatus;
		}

		logger.info("orderStatus:" + transactionOrder.getStatus());
		if(!transactionOrder.getStatus()){
			//??
			return orderStatus;
		}
		
		String orderNo=transactionOrder.getOrderNo();
		String transactionNo=transactionOrder.getTransactionNo();
		Timestamp transactionTime = transactionOrder.getTransactionTime();
		long payActual = transactionOrder.getPayActual();
		String currency = transactionOrder.getCurrency();
		
		payOrder =payOrderService.getPayOrder(orderNo);
		
		orderStatus = payOrder.getStatus();
		//订单结算状态
		if(orderStatus==PayOrder.PAY_WAITING){
			
			if(payOrderService.updateOrderPaySuccess(orderNo, payActual, transactionNo, transactionTime, currency)){
				
				payOrder.setStatus(PayOrder.PAY_SUCCESS);
				payOrder.setActualPay(payActual);
				payOrder.setTransactionNo(transactionNo);
				payOrder.setTranscationTime(transactionTime);
				payOrder.setCurrency(currency);
				
				try {
					payOrderService.handlePayOrder(payOrder);
					orderStatus=PayOrder.ORDER_SETTLED;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return orderStatus;
	}

	@Override
	public final void handleNotify(HttpServletRequest notifyReq, HttpServletResponse notifyResp, IPayContext context) {
		
		//验证回调合法性包含：调用ip，
		//获取商户配置的ip白名单列表
		String whiteIps = context.getWhiteIps();
		String reqIp= HttpUtil.getIpAddr(notifyReq);
		if(whiteIps!=null && !whiteIps.equals("") && !whiteIps.contains(reqIp)){
			//ip不合法
			//do...
			WebUtil.write(notifyResp, "invalid-request-0");
			logger.debug("#handleNotify:invalid-request-0");
			return;
		}

		
		//参数签名验证，
		if(!checkNotifyReq(notifyReq, context)){
			//请求参数不合法
			//do...
			WebUtil.write(notifyResp, "invalid-request-1");
			logger.debug("#handleNotify:invalid-request-1");
			return;
		}
		

		ITransactionOrder transactionOrder = parsingNotifyReq(notifyReq, context);
		
		String orderNo=transactionOrder.getOrderNo();
		String transactionNo=transactionOrder.getTransactionNo();
		Timestamp transactionTime = transactionOrder.getTransactionTime();
		long payActual = transactionOrder.getPayActual();
		String currency = transactionOrder.getCurrency();
		String responseResult = transactionOrder.getResponseResult();
		
		PayOrder payOrder = payOrderService.getPayOrder(orderNo);
		if(payOrder==null){
			// 无效订单
			//do...
			WebUtil.write(notifyResp, "invalid-request-2");
			logger.debug("#handleNotify:invalid-request-2");
			return;
		}else {
			//
			if(payOrder.getStatus()!=PayOrder.PAY_WAITING){
				//响应
				WebUtil.write(notifyResp, nofiyOk(responseResult));
				return;
			}
		}
		
//		int payActual = getParamPayActual(notifyReq, payOrder.getCurrency());
		
		currency = currency == null || currency.equals("") ? payOrder.getCurrency() : currency;
		
		boolean payStatus=false;
		//订单状态验证
		if(transactionOrder.getStatus()){
			//
			ITransactionOrder transactionOrderNew = checkOrderRemote(payOrder, context);
			if(transactionOrderNew==null || transactionOrderNew.getStatus()){
				//支付成功
				payStatus=true;
			}else{
				//远程验证失败
				payStatus=false;
				WebUtil.write(notifyResp, "invalid-request-3");
				logger.debug("#handleNotify:invalid-request-3");
				return;
			}
			
		}
		
		
		boolean isNeedHandle= false;
		
		//保存订单状态
		if(payStatus){
			if(payOrderService.updateOrderPaySuccess(orderNo, payActual, transactionNo, transactionTime, currency)){
				
				isNeedHandle=true;
				
				payOrder.setStatus(PayOrder.PAY_SUCCESS);
				payOrder.setActualPay(payActual);
				payOrder.setTransactionNo(transactionNo);
				payOrder.setTranscationTime(transactionTime);
				payOrder.setCurrency(currency);
			}
			
		}else{
			payOrderService.updateOrderPayFailure(orderNo, transactionNo);
		}
		
		//响应
		WebUtil.write(notifyResp, nofiyOk(responseResult));
		
		//通知加金币
		//do。。。。。
		if(isNeedHandle){
			payOrderService.handlePayOrderAsync(payOrder);
		}
		
		
	}
	
	protected final void saveTransactionNo(String orderNo, String transactionNo){
		payOrderService.updateOrderTransactionNo(orderNo, transactionNo);
	}

	protected abstract boolean checkNotifyReq(HttpServletRequest notifyReq,
                                              IPayContext context);

	protected abstract ITransactionOrder parsingNotifyReq( final HttpServletRequest notifyReq,
			IPayContext context);
	

	public ITransactionOrder checkOrderRemote(PayOrder payOrder, IPayContext context) {
		return null;
	}
	
	protected String nofiyOk(String responseResult){
		if(responseResult != null && !responseResult.equals("")) {
			return responseResult;
		}
		return "SUCCESS";
	}
	
	
	protected static interface ITransactionOrder {
		
		public boolean getStatus();

		public String getOrderNo();
		
		public long getPayActual();
		
		public String getTransactionNo();
		
		public Timestamp getTransactionTime();
		
		public String getCurrency();
		
		public String getResponseResult();
		
	}

	protected static class SimpleTransactionOrder implements ITransactionOrder {

		private boolean status;
		
		private String orderNo;
		private long payActual;
		private String transactionNo;
		private Timestamp transactionTime;
		private String currency;
		private String responseResult;
		
		
		public SimpleTransactionOrder(boolean status, String orderNo,
				long payActual, String transactionNo, Timestamp transactionTime, String currency, String responseResult) {
			this.status = status;
			this.orderNo = orderNo;
			this.payActual = payActual;
			this.transactionNo = transactionNo;
			this.transactionTime = transactionTime;
			this.currency = currency;
			this.responseResult = responseResult;
		}

		
		public SimpleTransactionOrder(boolean status2, String orderNum, Timestamp timestamp, String currency2,String callMessage) {
			this.status = status2;
			this.transactionNo = orderNum;
			this.transactionTime = timestamp;
			this.currency = currency2;
			this.responseResult = callMessage;
		}


		@Override
		public boolean getStatus() {
			return status;
		}

		@Override
		public String getOrderNo() {
			return orderNo;
		}

		@Override
		public long getPayActual() {
			return payActual;
		}

		@Override
		public String getTransactionNo() {
			return transactionNo;
		}

		@Override
		public Timestamp getTransactionTime() {
			return transactionTime;
		}
		
		@Override
		public String getCurrency() {
			return currency;
		}

		@Override
		public String getResponseResult() {
			return responseResult;
		}
		
	}

	public String nofiyOk() {
		
		return "SUCCESS";
	}
	
}


