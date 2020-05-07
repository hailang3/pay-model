package com.neo.paymodel.api.pay.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.neo.paymodel.api.pay.entity.DiscountInfo;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class RetModel<T> {
	
	private boolean status;
	private Integer errcode;
	private String errmsg;

	private T data;
	
	private List<Map<String, Object>> bankInfos;
	
	private DiscountInfo discountInfo;
	
	public RetModel() {
		this.status = true;
	}
	
	public RetModel(Integer errcode, String errmsg) {
		this.status = false;
		this.errcode = errcode;
		this.errmsg = errmsg;
	}
	
	public RetModel(T data) {
		this.status = true;
		this.data = data;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<Map<String, Object>> getBankInfos() {
		return bankInfos;
	}

	public void setBankInfos(List<Map<String, Object>> bankInfos) {
		this.bankInfos = bankInfos;
	}

	public DiscountInfo getDiscountInfo() {
		return discountInfo;
	}

	public void setDiscountInfo(DiscountInfo discountInfo) {
		this.discountInfo = discountInfo;
	}

	
}
