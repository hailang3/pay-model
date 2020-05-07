package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


public class PayMethodInstance implements Cloneable{

	private int id;						//商户支付方法配置id
	private String amounts;				// 建议金额
	private Integer minAmount;			// 最小金额
	private Long maxAmount;			// 最大金额
	
	private int payMethodId;
	private int payTypeId;				//支付类型id
	private int status;					//支付方法状态 {0:关闭，1:开启}
	
	private int whichOne;				// 属于的团队
	private int payMerchantId;
	private int payChannelId;
	
	@JsonIgnore
	private Timestamp createTime;
	@JsonIgnore
	private Timestamp updateTime;
	
	@JsonIgnore
	private int weight=1;               //状态开启的支付方式实例都会有权重，默认为1, 权重， 用于计算 在当前支付类型下，符合金额限制，存在多个支付方法时被选中的概率
	@JsonIgnore
	private Integer submitWay;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
	public Integer getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}
	
	public Long getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getWhichOne() {
		return whichOne;
	}
	public void setWhichOne(int whichOne) {
		this.whichOne = whichOne;
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
	
	public Integer getSubmitWay() {
		return submitWay;
	}
	public void setSubmitWay(Integer submitWay) {
		this.submitWay = submitWay;
	}
	

	
}
