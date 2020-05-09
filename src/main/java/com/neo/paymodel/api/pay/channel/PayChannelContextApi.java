package com.neo.paymodel.api.pay.channel;


import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.entity.PayChannelMethod;
import com.neo.paymodel.api.pay.entity.PayMerchant;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.service.IPayConfService;
import com.neo.paymodel.api.pay.entity.PaySubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class PayChannelContextApi implements IPayChannelApi, ApplicationContextAware {

	static final Logger logger = LoggerFactory.getLogger(PayChannelContextApi.class);
	
	private int id;
	
	private String name;
	
	private IPayConfService payConfService;
	
	protected ApplicationContext appContext;
	
	void setChannelId(int id) {
		this.id = id;
	}

	void setChannelName(String name) {
		this.name = name;
	}


	public final int getId() {
		return this.id;
	}

	public final String getName() {
		return this.name;
	}
	
	
	@Override
	public final void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
		this.payConfService = appContext.getBean(IPayConfService.class);
	}
	
	@Override
	public void submitOrder(PaySubmitRequest paySubmitReq,
							HttpServletResponse resp) {
		
		PayOrder payOrder = paySubmitReq.getPayOrder();
		int payChannelId = payOrder.getPayChannelId();
		
		PayChannelMethod payChannelMethod = payConfService.getPayChannelMethod(payOrder.getPayMethodId(), payChannelId);
		PayMerchant payMerchant = payConfService.getPayMerchant(payOrder.getPayMerchantId());
		
		submitOrder(paySubmitReq, payOrder, resp, PayContextFactory.getPayContext(payChannelMethod, payMerchant));
	}

	@Override
	public int checkOrder(PayOrder payOrder) {
		System.out.println(payOrder.getPayMerchantId());
		PayMerchant payMerchant = payConfService.getPayMerchant(payOrder.getPayMerchantId());
		if(payOrder.getExtend() != null) {
			if(payMerchant.getExtend() != null && !payMerchant.getExtend().equals("")) {
				JSONObject obj = JSONObject.parseObject(payMerchant.getExtend());
				obj.putAll(payOrder.getExtend());
				System.out.println(obj.toJSONString());
				payMerchant.setExtend(obj.toJSONString());
			}else {
				payMerchant.setExtend(payOrder.getExtend().toJSONString());
			}
		}
		return checkOrder(payOrder, PayContextFactory.getPayContext(payMerchant));
	}

	@Override
	public void handleNotify(HttpServletRequest notifyReq,
                             HttpServletResponse notifyResp, PayMerchant payMerchant) {
		handleNotify(notifyReq, notifyResp, PayContextFactory.getPayContext(payMerchant));
	}
	

	protected void init(){}
	
	protected abstract void submitOrder(PaySubmitRequest paySubmitReq, PayOrder payOrder, HttpServletResponse resp, IPayContext context);
	
	protected int checkOrder(PayOrder payOrder, IPayContext context){
		return PayOrder.PAY_WAITING;
	}
	
	protected abstract void handleNotify(HttpServletRequest notifyReq, HttpServletResponse notifyResp, IPayContext context);
	
}
