package com.neo.paymodel.api.pay.web.vo;


import com.neo.paymodel.common.util.RequestParams;

@RequestParams
public class PayOrderRequest {

	private Integer userId;
	private String userName;
	private String machineSerial;
	private String channelId;
	private Integer vipGrade;
	private Integer viewId;
	private Integer methodId;
	private Integer deviceType;
	private Long orderAmount;
	private String bankCode;
	private Integer discountCoin;
	
	private int orderGameId;		//下单的游戏id {0:大厅, >0:对应游戏id}
	
	private int goodsId; //商品ID
	
	//flashPay新增参数//flashpay expand 还款就是支付；放款就是提现
	private String repayAddress;     //	还款人地址
	private String repayPhoneNumber; //还款人手机号
	private String repayIdNumber;    //还款人身份证号
	private String repayName;        //还款人姓名
	private String virtualAccount;   //虚拟账号
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMachineSerial() {
		return machineSerial;
	}
	public void setMachineSerial(String machineSerial) {
		this.machineSerial = machineSerial;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getVipGrade() {
		return vipGrade;
	}
	public void setVipGrade(Integer vipGrade) {
		this.vipGrade = vipGrade;
	}
	public Integer getMethodId() {
		return methodId;
	}
	public void setMethodId(Integer methodId) {
		this.methodId = methodId;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	
	public Long getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Integer getViewId() {
		return viewId;
	}
	public void setViewId(Integer viewId) {
		this.viewId = viewId;
	}
	public int getOrderGameId() {
		return orderGameId;
	}
	public void setOrderGameId(int orderGameId) {
		this.orderGameId = orderGameId;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Integer getDiscountCoin() {
		return discountCoin;
	}
	public void setDiscountCoin(Integer discountCoin) {
		this.discountCoin = discountCoin;
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
