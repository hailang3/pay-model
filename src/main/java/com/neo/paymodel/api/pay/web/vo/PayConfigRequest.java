package com.neo.paymodel.api.pay.web.vo;

import com.dsgame.pay.api.util.RequestParams;

@RequestParams
public class PayConfigRequest {

	private Integer userId;
	private String machineSerial;
	private String channelId;
	private Integer vipGrade=0;
	
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
	
}
