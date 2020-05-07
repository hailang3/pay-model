package com.neo.paymodel.api.pay.service;

import com.neo.paymodel.api.pay.entity.*;

import java.util.List;
import java.util.Map;

public interface IPayConfService {

	public List<PayType> getPayTypeListSorted();
	
	
	public PayMethod getPayMethod(int payMethodId);

	public PayView getPayView(int payViewId);
	
	public List<PayView> getPayViewListSorted(int whichOne);
	
	public PayView getPayView(int payMethodId, int whichOne);
	
	public PayMerchant getPayMerchant(String merchantCode);

	public PayMerchant getPayMerchant(int payMerchantId);
	
	public PayChannel getPayChannel(int id);
	
	public PayChannelMethod getPayChannelMethod(int payMethodId, int payChannelId);
	
	public PayMerchant getPayMerchant(int payChannelId, int whichOne);
	
	public Map<String, PayMethodInstance> getPayMethodInstanceMap(int payMethodId, int whichOne);
	
	public List<PayViewBind> getPayViewBindList(int payViewId);


}
