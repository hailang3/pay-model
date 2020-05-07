package com.neo.paymodel.api.pay.entity;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

public class PayOrder {

	public static final int PAY_WAITING=0;
	public static final int PAY_FAIL=-1;
	public static final int PAY_SUCCESS=1;
	public static final int ORDER_SETTLED=2;

	private int id;						//
	private String orderNo;				//订单号
	private int userId;					//用户id
	private String userName;			//用户名
	
	private long planPay;				//计划充值金额,单位:分
	private Long actualPay;			//实际充值金额,单位:分
	
	
	private int rebateRate;			//返利比例
	private int rebateCoin;			//返利金额(为0，则根据rebateRate*actualPay计算)
	private Integer giveCoin;			//赠送金额
	
	
	private Integer addCoin;			//实际添加货币数量（actualPay+rebateCoin+giveCoin）
	
	private String transactionNo;		//交易订单号（充值渠道订单号）
	private int status=0;				//订单状态 {0:待付款, -1:付款失败, 1: 付款成功, 2:加金币成功}
	private Timestamp createTime;			//订单创建时间
	private Timestamp transcationTime;		//玩家付款时间
	private Timestamp notifyTime;			//订单状态通知时间
	private Timestamp handleTime;			//订单处理（加金币）时间
	
	
	private String remark;				//备注
	
	private String currency;				//添加价值道具类型RMB,美刀USD,印尼盾IDR，Ip 
	private String orderMachine;		//订单机器码
	private String orderIp;				//订单ip
	private String channelId;				//渠道id
	private int whichOne;				//团队id
	
	private int payViewId;				//支付方式视图id
	private int payMerchantId;			//支付商户id
	private int payChannelId;			//支付渠道id
	private int payMethodId;			//支付方式id
	private int payTypeId;				//支付类别id
	
	private int goodsId; // 商品Id
	private String bankCode; // 银行编码
	
	private String appMame;
	
	private JSONObject extend;
	//flashpay expand 还款就是支付；放款就是提现
	private String repayAddress;     //	还款人地址
	private String repayPhoneNumber; //还款人手机号
	private String repayIdNumber;    //还款人身份证号
	private String repayName;        //还款人姓名
	private String virtualAccount;   //虚拟账号
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public long getPlanPay() {
		return planPay;
	}
	public void setPlanPay(long planPay) {
		this.planPay = planPay;
	}
	public Long getActualPay() {
		return actualPay;
	}
	public void setActualPay(Long actualPay) {
		this.actualPay = actualPay;
	}
	public Integer getAddCoin() {
		return addCoin;
	}
	public void setAddCoin(Integer addCoin) {
		this.addCoin = addCoin;
	}
	
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getTranscationTime() {
		return transcationTime;
	}
	public void setTranscationTime(Timestamp transcationTime) {
		this.transcationTime = transcationTime;
	}
	public Timestamp getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(Timestamp notifyTime) {
		this.notifyTime = notifyTime;
	}
	public Timestamp getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Timestamp handleTime) {
		this.handleTime = handleTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderMachine() {
		return orderMachine;
	}
	public void setOrderMachine(String orderMachine) {
		this.orderMachine = orderMachine;
	}
	public String getOrderIp() {
		return orderIp;
	}
	public void setOrderIp(String orderIp) {
		this.orderIp = orderIp;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public int getWhichOne() {
		return whichOne;
	}
	public void setWhichOne(int whichOne) {
		this.whichOne = whichOne;
	}
	
	public int getPayViewId() {
		return payViewId;
	}
	public void setPayViewId(int payViewId) {
		this.payViewId = payViewId;
	}
	public int getPayMerchantId() {
		return payMerchantId;
	}
	public void setPayMerchantId(int payMerchantId) {
		this.payMerchantId = payMerchantId;
	}
	public int getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(int payChannelId) {
		this.payChannelId = payChannelId;
	}
	public int getPayMethodId() {
		return payMethodId;
	}
	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
	}
	public int getPayTypeId() {
		return payTypeId;
	}
	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}
	public int getRebateRate() {
		return rebateRate;
	}
	public void setRebateRate(int rebateRate) {
		this.rebateRate = rebateRate;
	}
	public int getRebateCoin() {
		return rebateCoin;
	}
	public void setRebateCoin(int rebateCoin) {
		this.rebateCoin = rebateCoin;
	}
	public Integer getGiveCoin() {
		return giveCoin;
	}
	public void setGiveCoin(Integer giveCoin) {
		this.giveCoin = giveCoin;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public JSONObject getExtend() {
		return extend;
	}
	public void setExtend(JSONObject extend) {
		this.extend = extend;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getAppMame() {
		return appMame;
	}
	public void setAppMame(String appMame) {
		this.appMame = appMame;
	}
	public String getRepayAddress() {
		return repayAddress;
	}
	public void setRepayAddress(String repayAddress) {
		this.repayAddress = repayAddress;
	}
	public String getRepayPhoneNumber() {
		return repayPhoneNumber;
	}
	public void setRepayPhoneNumber(String repayPhoneNumber) {
		this.repayPhoneNumber = repayPhoneNumber;
	}
	public String getRepayIdNumber() {
		return repayIdNumber;
	}
	public void setRepayIdNumber(String repayIdNumber) {
		this.repayIdNumber = repayIdNumber;
	}
	public String getRepayName() {
		return repayName;
	}
	public void setRepayName(String repayName) {
		this.repayName = repayName;
	}
	public String getVirtualAccount() {
		return virtualAccount;
	}
	public void setVirtualAccount(String virtualAccount) {
		this.virtualAccount = virtualAccount;
	}
	
	

}
