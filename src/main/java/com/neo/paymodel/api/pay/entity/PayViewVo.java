package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neo.paymodel.api.pay.entity.PayView;

public class PayViewVo {

	@JsonProperty("viewId")
	private int id;						//支付方式视图id
	private String name;
	@JsonProperty("methodId")
	private int payMethodId;
	@JsonIgnore
	private int whichOne;				//团队id
	private String goodsExpand;     //商品扩展信息
	private String amounts;
	private Integer minAmount;
	private Long maxAmount;
	private int recomFlag;				//是否推荐的标识 {0：未推荐，1：推荐}
	private int rebateRate;				//返利的比率，千分之几
	
	private boolean bolFixed;			//是否是固额

	private int isShowBank; //是否显示银行
	
	public PayViewVo(){}
	
	public PayViewVo(PayView payView) {
		this.id=payView.getId();
		this.name=payView.getName();
		this.payMethodId=payView.getPayMethodId();
		this.whichOne=payView.getWhichOne();
		this.amounts=payView.getAmounts();
		this.minAmount=payView.getMinAmount();
		this.maxAmount=payView.getMaxAmount();
		this.recomFlag=payView.getRecomFlag();
		this.rebateRate=payView.getRebateRate();
		this.bolFixed=payView.isBolFixed();
		this.isShowBank = payView.getIsShowBank();
		this.goodsExpand = payView.getGoodsExpand();
	}

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
