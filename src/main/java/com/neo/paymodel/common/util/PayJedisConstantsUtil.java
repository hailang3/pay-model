package com.neo.paymodel.common.util;

/**
 * redis key 定义类
 * 
 * @author Administrator
 */
public final class PayJedisConstantsUtil {
	public static final String DSGAME_PAY_AMOUNT_TOTAL_USER = "dsgame:pay:amount:total:user:%s";
	public static final String DSGAME_PAY_AMOUNT_LIST_CODE = "dsgame:pay:amount:list:code:%s";
	public static final String DSGAME_LIANPAY_AMOUNT_LIST_CODE = "dsgame:lianpay:amount:list:code:%s";
	public static final String DSGAME_TBPAY_AMOUNT_LIST_CODE = "dsgame:tbpay:amount:list:code:%s";
	public static final String DSGAME_ALIPAY_OPEN_AMOUNT_CODE = "dsgame:alipay:open:amount:code:%s";
	public static final String DSGAME_WXPAY_OPEN_AMOUNT_CODE = "dsgame:wxpay:open:amount:code:%s";
	public static final String DSGAME_BANKPAY_OPEN_AMOUNT_CODE = "dsgame:bankpay:open:amount:code:%s";
	public static final String DSGAME_BANKPAY_RATE_CODE = "dsgame:bankpay:rate:code:%s";
	public static final String DSGAME_CHATPAY_RATE_CODE = "dsgame:chatpay:rate:code:%s";
	public static final String DSGAME_UNIONPAY_OPEN_AMOUNT_CODE = "dsgame:unionpay:open:amount:code:%s";
	public static final String DSGAME_UNIONPAY_RATE_CODE = "dsgame:unionpay:rate:code:%s";
	public static final String DSGAME_AGENTPAY_RECOMMENDED__STATUS = "dsgame:agentpay:recommended:status";
	public static final String DSGAME_EXCLUSIVE_RECOMMENDED__AMOUNT = "dsgame:exclusive:recommended:amount";
	public static final String DSGAME_EXCLUSIVE_RECOMMENDED__STATUS = "dsgame:exclusive:recommended:status";

	/** third pay */
	public static final String DSGAME_THIRD_CONFIG = "dsgame:third:config";
	public static final String DSGAME_THIRD_CONFIG_TYPE = "dsgame:third:config:%s";

//	public static final String DSGAME_THIRD_CONFIG_TMP_SWITCH = "dsgame:third:config:tmp:switch";
    public static final String DSGAME_THIRD_CONFIG_TMP_ANTISWITCH = "dsgame:third:config:tmp:antiswitch";


}
