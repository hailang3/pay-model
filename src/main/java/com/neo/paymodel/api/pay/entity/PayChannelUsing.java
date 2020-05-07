package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class PayChannelUsing {

	private int id;
	private int whichOne;
	private int payChannelId;
	private int status;
	
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
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
