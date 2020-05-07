package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class PayView {

	private int id;						//支付方式视图id
	private String name;
	private int payMethodId;
	private int whichOne;				//团队id
	private String goodsExpand;     //商品扩展信息
	private String amounts;
	private Integer minAmount;
	private Long maxAmount;
	private int recomFlag;				//是否推荐的标识 {0：未推荐，1：推荐}
	private int rebateRate;				//返利的比率，千分之几
	
	private int openMinLimit;			//充值方式开放 最低充值金额限制
	private int status;					//支付方式状态 {0:关闭，1:开启}
	
	private int payTypeId;				//支付类型id
	private int sortNum;				// 排序号
	@JsonIgnore
	private Timestamp createTime;
	@JsonIgnore
	private Timestamp updateTime;
	
	private boolean bolFixed;			//是否是固额
	
	private int isShowBank; //是否显示银行
	
	
	public String getGoodsExpand() {
		return goodsExpand;
	}

	public void setGoodsExpand(String goodsExpand) {
		this.goodsExpand = goodsExpand;
	}

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

	public int getPayMethodId() {
		return payMethodId;
	}

	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
	}

	public int getWhichOne() {
		return whichOne;
	}

	public void setWhichOne(int whichOne) {
		this.whichOne = whichOne;
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

	public int getRecomFlag() {
		return recomFlag;
	}

	public void setRecomFlag(int recomFlag) {
		this.recomFlag = recomFlag;
	}

	public int getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(int rebateRate) {
		this.rebateRate = rebateRate;
	}

	public int getOpenMinLimit() {
		return openMinLimit;
	}

	public void setOpenMinLimit(int openMinLimit) {
		this.openMinLimit = openMinLimit;
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

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
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

	public boolean isBolFixed() {
		return bolFixed;
	}

	public void setBolFixed(boolean bolFixed) {
		this.bolFixed = bolFixed;
	}

	public int getIsShowBank() {
		return isShowBank;
	}

	public void setIsShowBank(int isShowBank) {
		this.isShowBank = isShowBank;
	}
	
	
	
}
