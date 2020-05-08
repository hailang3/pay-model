package com.neo.paymodel.api.pay.web.vo;


import com.neo.paymodel.api.pay.util.RequestParams;

@RequestParams
public class PayCheckRequest {

	private Integer userId;
	private String orderNo;
	private String transactionNo; //谷歌。或苹果订单号
	private String actualPay; //官方充值实际金额
	private String currency; // 货币类型
	private String packageName; // 谷歌包名
	private String productId; // 谷歌产品Id
	private String token; // token


	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getActualPay() {
		return actualPay;
	}

	public void setActualPay(String actualPay) {
		this.actualPay = actualPay;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
