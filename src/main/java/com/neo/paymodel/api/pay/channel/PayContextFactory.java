package com.neo.paymodel.api.pay.channel;

import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.entity.PayChannelMethod;
import com.neo.paymodel.api.pay.entity.PayMerchant;
import org.apache.commons.lang.StringUtils;

public class PayContextFactory {

	public static IPayContext getPayContext( PayMerchant payMerchant){
		return new PayContext(payMerchant);
	}
	
	public static IPayContext getPayContext(PayChannelMethod payChannelMethod, PayMerchant payMerchant){
		return new PayContext(payChannelMethod, payMerchant);
	}
	
	
	static class PayContext implements IPayContext {

		private PayMerchant payMerchant;
		
		private JSONObject extendConfig;
		
		PayContext(PayMerchant payMerchant) {
			this.payMerchant = payMerchant;
			
			try {
				String extend = payMerchant.getExtend();
				//???
				//String extend2 = payChannel.getExtend();
				if(!StringUtils.isEmpty(extend)){
					JSONObject obj = JSONObject.parseObject(extend);
					//需要增加约束???会覆盖， 拷贝层次？？
					extendConfig=obj;
				}
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		PayContext(PayChannelMethod payChannelMethod,PayMerchant payMerchant) {
			this(payMerchant);
			
			try {
				String extend = payChannelMethod.getExtend();
				if(!StringUtils.isEmpty(extend)){
					JSONObject obj = JSONObject.parseObject(extend);
					extendConfig.putAll(obj);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		

		@Override
		public int getPayMerchantId() {
			return payMerchant.getId();
		}

		@Override
		public String getPayMerchantCode() {
			return payMerchant.getMerchantCode();
		}

		
		
		@Override
		public int getWhichOne() {
			return payMerchant.getWhichOne();
		}

		@Override
		public String getApiUrl() {
			return payMerchant.getApiUrl();
		}

		@Override
		public String getNotifyUrl() {
			return payMerchant.getNotifyUrl();
		}

		@Override
		public String getAccount() {
			return payMerchant.getAccount();
		}

		@Override
		public String getSignKey() {
			return payMerchant.getSignKey();
		}

		@Override
		public String getWhiteIps() {
			return payMerchant.getWhiteIps();
		}

		@Override
		public Object getConfig(String key) {
			if(extendConfig!=null){
				return extendConfig.get(key);
			}
			return null;
		}
	}
	
}
