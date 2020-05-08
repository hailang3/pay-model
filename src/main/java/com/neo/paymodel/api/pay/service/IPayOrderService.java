package com.neo.paymodel.api.pay.service;

import com.neo.paymodel.api.pay.entity.PayOrder;

import java.sql.Timestamp;

public interface IPayOrderService {

	PayOrder createOrder(int userId, String userName, long planAmount, int rebateRate, String channelId, String orderMachine, int orderGameId, int payViewId, int payMethodId, int payType,
						 int payMerchantId, int payChannelId,  String ipAddr, int giftCoin, int goodsId);

	PayOrder createOrder(String orderNo, int userId, String userName, long planAmount, int rebateCoin, int rebateRate, String channelId, String orderMachine, int orderGameId, int payViewId, int payMethodId, int payType,
                         int payMerchantId, int payChannelId, String ipAddr, int giftCoin, int goodsId);


	PayOrder getPayOrder(String orderNo);

	boolean updateOrderPaySuccess(String orderNo, long payActual,
                                  String transactionNo, Timestamp transactionTime, String currency);

	void updateOrderPayFailure(String orderNo, String transactionNo);

	void handlePayOrderAsync(PayOrder payOrder);
	
	void handlePayOrder(PayOrder payOrder);

	void updateOrderTransactionNo(String orderNo, String transactionNo);

	int getGiftCoinByFirstPay(int goodsId);

	int IsFirstPay(int userId);

}
