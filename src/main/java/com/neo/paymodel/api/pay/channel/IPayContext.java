package com.neo.paymodel.api.pay.channel;

public interface IPayContext {
	int getPayMerchantId();
	
	String getPayMerchantCode();
	
	int getWhichOne();
	
	String getApiUrl();
	
	String getNotifyUrl();
	
	String getAccount();
	
	 String getSignKey();
	
	 String getWhiteIps();
	
	 Object getConfig(String key);
	
}
