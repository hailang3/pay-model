package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


/**
 * 支付渠道
 */
public class PayChannel {

	private int id;					//id
	private String name;				//支付渠道名称
	private String extendProperties;	//扩展属性配置( 用来约束 PayMerchant的extend扩展配置+ 约束 PayChannelMethod的extend 扩展配置)
	
	private int status;				//渠道状态 {0:关闭，1：开启}
	
	private String channelDesc;			//支付渠道描述
	
	@JsonIgnore
	private Timestamp createTime;	//创建时间
	@JsonIgnore
	private Timestamp updateTime;	//最近更新时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExtendProperties() {
		return extendProperties;
	}
	public void setExtendProperties(String extendProperties) {
		this.extendProperties = extendProperties;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getChannelDesc() {
		return channelDesc;
	}
	public void setChannelDesc(String channelDesc) {
		this.channelDesc = channelDesc;
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
