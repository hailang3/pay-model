package com.neo.paymodel.api.pay.dao;


import com.neo.paymodel.api.pay.entity.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface IPayDao {
	
	PayOrder createOrder(String orderNo, int userId, String userName, long orderAmount,
						 int rebateCoin, int rebateRate, String channelId, String orderMachine, int orderGameId,
						 int payViewId, int payMethodId, int payTypeId, int payMerchantId,
						 int payChannelId,  String ipAddr, int giftCoin, int goodsId);

	PayOrder getPayOrder(String orderNo);

	void updateOrderTransactionNo(String orderNo, String transactionNo);

	boolean updateOrderPaySuccess(String orderNo, long payActual,
                                  String transactionNo, Timestamp transactionTime, String currency);

	void updateOrderPayFailure(String orderNo, String transactionNo);

	boolean updateOrderSettled(String orderNo, int rebateCoin, long addCoin);



	PayMerchant getPayMerchant(int id);

	PayMerchant getPayMerchant(String merchantCode);

	PayMerchant getPayMerchant(int payChannelId, int whichOne);

	PayChannel getPayChannel(int id);

	PayView getPayView(int payViewId);

	PayView getPayView(int payMethodId, int whichOne);

	PayMethod getPayMethod(int id);

	PayChannelMethod getPayChannelMethod(int payMethodId, int payChannelId);

	List<PayMethodInstance> getPayMethodInstanceList(int payMethodId);

	List<PayType> getPayTypeListSorted();

	List<PayView> getPayViewListSorted(int whichOne);
	
	List<PayViewBind> getPayViewBindList(int payViewId);

	List<PayOrder> getPayOrderUnsettled(int minuteBefore, int rowCount);

	int getIsFirstPay(int userId);

	int getGiftCoinByFirstPay(int goodsId);

	void addGiftCoinByFirstPay(int userId);

	Map<String, Object> getGooglePayInfo(String app_name);

	List<Map<String, Object>> getPayBankInfos();

	void updateOrderVAAndTNo(PayOrder payOrder);

	PayOrder getPayOrderByTNo(String upOrderNum);
	

}
