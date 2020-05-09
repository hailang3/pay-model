package com.neo.paymodel.api.pay.entity;


import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.common.util.RequestParams;
import com.neo.paymodel.common.util.RequestType;

@RequestParams
public class PaySubmitRequest {

	private Integer userId;
	private String machineSerial;
	private String channelId;
	private Integer vipGrade;
	private Integer methodId;
	private Integer deviceType;
	private Integer orderAmount;
	private String bankCode;

	private String orderNo;
	//flashpay expand 还款就是支付；放款就是提现
	private String repayAddress;     //	还款人地址
	private String repayPhoneNumber; //还款人手机号
	private String repayIdNumber;    //还款人身份证号
	private String repayName;        //还款人姓名
	private String virtualAccount;   //虚拟账号
			
	
	@RequestType(required=false)
	private PayOrder payOrder;
	
	
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
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public PayOrder getPayOrder() {
		return payOrder;
	}
	public void setPayOrder(PayOrder payOrder) {
		this.payOrder = payOrder;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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
