package com.neo.paymodel.api.pay.util;

public class PayConstant {

	public final static String CACHE="PayCache";
	
	public final static String PREFIX_PAY_MERCHANT="PayCache:PayMerchant:";
	public final static String PREFIX_PAY_CHANNEL="PayCache:PayChannel:";
	public final static String PREFIX_PAY_CHANNEL_METHOD="PayCache:PayChannelMethod:";
	public final static String PREFIX_PAY_VIEW="PayCache:PayView:";
	public final static String PREFIX_PAY_METHOD="PayCache:PayMethod:";
	
	
	public final static String PREFIX_PAY_TYPE_LIST_SORTED="PayCache:PayTypeListSorted";
	public final static String PREFIX_PAY_VIEW_LIST_SORTED="PayCache:PayViewListSorted:";
	public final static String PREFIX_PAY_VIEW_BIND_LIST="PayCache:PayViewBindList:";
	
	
	public final static String PREFIX_PAY_METHOD_INSTANCE_MAP="PayCache:PayMethodInstanceMap:";
	/**
	 * 支付扩大倍数
	 */
	public final static Integer PAY_MULTIPLE=10000;

	/**
	 * 当前货币类型----美元
	 */
    public static final String USD = "USD";
}
