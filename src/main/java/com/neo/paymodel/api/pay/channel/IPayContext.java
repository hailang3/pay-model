package com.neo.paymodel.api.pay.channel;

public interface IPayContext {

//	int getPayChannelId();
//	
//	String getPayChannelName();
	
	int getPayMerchantId();
	
	String getPayMerchantCode();
	
	int getWhichOne();
	
	String getApiUrl();
	
	String getNotifyUrl();
	
	String getAccount();
	
	public String getSignKey();
	
	public String getWhiteIps();
	
	public Object getConfig(String key);
	
}
