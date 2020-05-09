package com.neo.paymodel.api.pay.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class PayTypeVo {

	private int typeId;
	private Integer recomFlag;
	private Integer rebateFlag;

	private List<PayViewVo> views;
	private String tips;
	
	private JSONObject ext;
	
	public PayTypeVo() {}
	
	public PayTypeVo(int typeId, Integer recomFlag, Integer rebateFlag,
			List<PayViewVo> views) {
		this.typeId=typeId;
		this.recomFlag = recomFlag;
		this.rebateFlag = rebateFlag;
		this.views = views;
	}
	
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public Integer getRecomFlag() {
		return recomFlag;
	}
	public void setRecomFlag(Integer recomFlag) {
		this.recomFlag = recomFlag;
	}
	public Integer getRebateFlag() {
		return rebateFlag;
	}
	public void setRebateFlag(Integer rebateFlag) {
		this.rebateFlag = rebateFlag;
	}
	
	public List<PayViewVo> getViews() {
		return views;
	}

	public void setViews(List<PayViewVo> views) {
		this.views = views;
	}

	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public JSONObject getExt() {
		return ext;
	}
	public void setExt(JSONObject ext) {
		this.ext = ext;
	}
	
}
