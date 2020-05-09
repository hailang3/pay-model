package com.neo.paymodel.api.pay.service;

import com.neo.paymodel.api.pay.entity.*;

import java.util.List;
import java.util.Map;

 interface IPayConfService {

	 List<PayType> getPayTypeListSorted();
	
	
	 PayMethod getPayMethod(int payMethodId);

	 PayView getPayView(int payViewId);
	
	 List<PayView> getPayViewListSorted(int whichOne);
	
	 PayView getPayView(int payMethodId, int whichOne);
	
	 PayMerchant getPayMerchant(String merchantCode);

	 PayMerchant getPayMerchant(int payMerchantId);
	
	 PayChannel getPayChannel(int id);
	
	 PayChannelMethod getPayChannelMethod(int payMethodId, int payChannelId);
	
	 PayMerchant getPayMerchant(int payChannelId, int whichOne);
	
	 Map<String, PayMethodInstance> getPayMethodInstanceMap(int payMethodId);
	
	 List<PayViewBind> getPayViewBindList(int payViewId);


}
