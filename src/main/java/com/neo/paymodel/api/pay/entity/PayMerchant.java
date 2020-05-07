package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


/**
 * 
 * 支付渠道商户配置
 * @author m
 *
 */
public class PayMerchant {

	private int id;					// id
	private String merchantCode;	// 商户代号（唯一标识商户）
	
	private String account;			//商户号
	private String signKey;			//签名key
	
	private String apiUrl;		//支付渠道接口地址
	
	private String notifyUrl;		//通知（回调）地址
	private String whiteIps;		//通知（回调）ip白名单
	
	private String extend;			//扩展配置
	
	private int status;				// 商户状态 {0：关闭，1：开启}
	private String remark;			// 备注
	private int whichOne;			// 属于的团队
	private int payChannelId;		// 支付渠道id

	@JsonIgnore
	private Timestamp createTime;
	@JsonIgnore
	private Timestamp updateTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSignKey() {
		return signKey;
	}
	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getWhiteIps() {
		return whiteIps;
	}
	public void setWhiteIps(String whiteIps) {
		this.whiteIps = whiteIps;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getWhichOne() {
		return whichOne;
	}
	public void setWhichOne(int whichOne) {
		this.whichOne = whichOne;
	}
	public int getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(int payChannelId) {
		this.payChannelId = payChannelId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
}
