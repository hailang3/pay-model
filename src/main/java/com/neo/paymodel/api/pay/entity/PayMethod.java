package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


/**
 * 支付方式配置
 * config_pay_method
 * @author m
 *
 */
public class PayMethod {

	private int id;						//支付方式id
	private String name;
	private boolean bolFixed;			//是否是固额
	private int status;					//支付方式状态 {0:关闭，1:开启}
	private int payTypeId;				//支付类型id
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isBolFixed() {
		return bolFixed;
	}
	public void setBolFixed(boolean bolFixed) {
		this.bolFixed = bolFixed;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPayTypeId() {
		return payTypeId;
	}
	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
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
