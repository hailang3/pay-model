package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


/**
 * 充值渠道的充值方式配置
 * @author m
 *
 */
public class PayChannelMethod {

	private int id;				// id
	
//	private String amounts;		// 建议金额
//	private Integer minAmount;		// 最小金额
//	private Integer maxAmount;		// 最大金额
	
	private int submitWay;		// 订单提交标识{0:浏览器，1:webview}
	private String extend;		// 扩展配置
	private int status;			// 状态{0:关闭 1：开启}
	
	private int payChannelId;	// 支付渠道id
	private int payMethodId;	// 支付方式id
//	private int payTypeId;		// 支付类型id
	
	@JsonIgnore
	private Timestamp createTime;	// 创建时间
	@JsonIgnore
	private Timestamp updateTime;	// 最新更新时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public int getSubmitWay() {
		return submitWay;
	}
	public void setSubmitWay(int submitWay) {
		this.submitWay = submitWay;
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
}
